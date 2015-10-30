/*
* NPGuys - Bukkit plugin for better NPC interaction
* Copyright (C) 2014 Adam Gotlib <Goldob>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package goldob.npguys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import goldob.npguys.action.Action;
import goldob.npguys.conversation.Conversation;
import goldob.npguys.conversation.ConversationManager;
import goldob.npguys.conversation.NPCMessage;
import goldob.npguys.conversation.PlayerMessage;
import goldob.npguys.exception.ActionMissingException;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.MessageNotFoundException;
import goldob.npguys.exception.NPGuyAlreadyExistsException;
import goldob.npguys.exception.NPGuyNotFoundException;
import goldob.npguys.exception.RequirementMissingException;
import goldob.npguys.requirement.Requirement;


public class DialogueManager {
	private static NPGuys plugin = null;
	private static File npcs;
	private static Map<String, NPGuyData> npguys;
	
	private DialogueManager() {}
	
	public static void init(final NPGuys plugin) {
		DialogueManager.plugin = plugin;
		
		npcs = new File(plugin.getDataFolder(), "npc");
		if (!npcs.exists()) {
			npcs.mkdir();
		}
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				reload();
			}
		}, 1);
	}
	
	public static void reload() {
		npguys = new HashMap<String, NPGuyData>();
		
		for (File npc : npcs.listFiles()) {
			load(npc.getName().replaceAll(".yml", ""), YamlConfiguration.loadConfiguration(npc));
		}
		plugin.getLogger().log(Level.INFO, "Dialogues loaded.");
	}
	
	private static void load(String npguy, YamlConfiguration data) {
		NPGuyData toLoad = new NPGuyData(npguy,
				(data.contains("active") ? data.getBoolean("active") : true));
		toLoad.setWelcomeMessage(data.getString("welcome_message"));
		for (String messageName : data.getConfigurationSection("dialogues").getKeys(false)) {
			String shortcut = data.getString("dialogues."+messageName+".shortcut");
			String message = data.getString("dialogues."+messageName+".message");
			
			List<Requirement> loadedRequirements = new ArrayList<Requirement>();
			if (data.getConfigurationSection("dialogues."+messageName+".requirements") == null) {
				data.getConfigurationSection("dialogues."+messageName).createSection("requirements");
			}
			for (String key : data.getConfigurationSection("dialogues."+messageName+".requirements").getKeys(false)) {
				ConfigurationSection requirement = data.getConfigurationSection("dialogues."+messageName+".requirements."+key);
				
				String type = requirement.getString("type");
				try {
					Requirement loadedRequirement = ElementManager.newRequirement(type);
					loadedRequirement.load(requirement);
					if(requirement.contains("reversed")) {
						loadedRequirement.setReversed(requirement.getBoolean("reversed"));
					}
					loadedRequirements.add(loadedRequirement);
				} catch (FailedToLoadException e) {
					e.printStackTrace();
				} catch (RequirementMissingException e) {
					e.printStackTrace();
				}
			}
			
			List<Action> loadedActions = new ArrayList<Action>();
			if (data.getConfigurationSection("dialogues."+messageName+".actions") == null) {
				data.getConfigurationSection("dialogues."+messageName).createSection("actions");
			}
			for (String key : data.getConfigurationSection("dialogues."+messageName+".actions").getKeys(false)) {
				ConfigurationSection action = data.getConfigurationSection("dialogues."+messageName+".actions."+key);
				
				String type = action.getString("type");
				try {
					Action loadedAction = ElementManager.newAction(type);
					loadedAction.load(action);
					loadedActions.add(loadedAction);
				} catch (FailedToLoadException e) {
					e.printStackTrace();
				} catch (ActionMissingException e) {
					e.printStackTrace();
				}
			}
			
			String npcResponse_message = data.getString("dialogues."+messageName+".npc_response.message");
			List<String> possibleResponses = data.getStringList("dialogues."+messageName+".npc_response.possible_responses");
			NPCMessage npcResponse = new NPCMessage(npcResponse_message, possibleResponses);
			
			PlayerMessage loadedMessage = new PlayerMessage(messageName, shortcut, message, npcResponse, loadedRequirements, loadedActions);
			toLoad.getDialogues().put(messageName, loadedMessage);
		}
		npguys.put(npguy, toLoad);
	}
	
	private static void save(String npguy, YamlConfiguration data) {
		NPGuyData toSave = npguys.get(npguy);
		data.set("active", toSave.isActive());
		data.set("welcome_message", toSave.getWelcomeMessage());
		for (String dialogue : toSave.getDialogues().keySet()) {
			ConfigurationSection savedMessage = data.createSection("dialogues."+dialogue);
			PlayerMessage messageToSave = toSave.getDialogues().get(dialogue);
			
			savedMessage.set("shortcut", messageToSave.getShortcut());
			savedMessage.set("message", messageToSave.getMessage());
			
			savedMessage.createSection("requirements");
			int i = 0;
			for (Requirement requirement : messageToSave.getRequirements()) {
				ConfigurationSection savedRequirement = savedMessage.createSection("requirements."+String.valueOf(i));
				requirement.saveIt(savedRequirement);
				i++;
			}
			
			savedMessage.createSection("actions");
			i = 0;
			for (Action action : messageToSave.getActions()) {
				ConfigurationSection savedAction = savedMessage.createSection("actions."+String.valueOf(i));
				action.saveIt(savedAction);
				i++;
			}
			
			ConfigurationSection savedResponse = savedMessage.createSection("npc_response");
			savedResponse.set("message", messageToSave.getNPCMessage().getMessage());
			savedResponse.set("possible_responses", messageToSave.getNPCMessage().getPossibleResponses());
		}
	}
	
	public static void saveAll() {
		for (File file : npcs.listFiles()) {
			file.delete();
		}
		for(String toSave : npguys.keySet()) {
			File output = new File(npcs, toSave+".yml");
			if(output.exists()) {
				output.delete();
			}
			try {
				output.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			YamlConfiguration data = YamlConfiguration.loadConfiguration(output);
			save(toSave, data);
			
			try {
				data.save(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		plugin.getServer().getLogger().log(Level.INFO, "Dialogues saved.");
	}
	
	public static Set<String> getNPGuysNames() {
		return npguys.keySet();
	}
	
	public static NPGuyData getData(String npguy) throws NPGuyNotFoundException {
		if (!(npguys.containsKey(npguy))) {
			throw new NPGuyNotFoundException(npguy);
		}
		else {
			return npguys.get(npguy);
		}
	}
	
	public static void removeData(String npguy) throws NPGuyNotFoundException {
		if (!(npguys.containsKey(npguy))) {
			throw new NPGuyNotFoundException(npguy);
		}
		else {
			npguys.remove(npguy);
		}
	}
	
	public static void putData(String npguy, NPGuyData data) throws NPGuyAlreadyExistsException {
		if ((npguys.containsKey(npguy))) {
			throw new NPGuyAlreadyExistsException(npguy);
		}
		else {
			npguys.put(npguy, data);
		}
	}
	
	public static void setActive(String npguy, boolean active) throws NPGuyNotFoundException {
		getData(npguy).setActive(active);
		if(active == false) {
			for(Conversation conversation : ConversationManager.getConversations().values()) {
				if(conversation.getNPGuy().getUID().equals(npguy)) {
					ConversationManager.endConversation(conversation.getPlayer(), conversation.getNPGuy().getNPC());
				}
			}
		}
	}
	
	public static boolean isActive(String npguy) throws NPGuyNotFoundException {
		return getData(npguy).isActive();
	}
	
	public static PlayerMessage getWelcomeMessage(String npguy) {
		try {
			return getPlayerMessage(npguy, npguys.get(npguy).getWelcomeMessage());
		} catch (MessageNotFoundException | NPGuyNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PlayerMessage getPlayerMessage(String npguy, String dialogue) throws MessageNotFoundException, NPGuyNotFoundException {
		PlayerMessage message = getData(npguy).getDialogues().get(dialogue);
		if(message == null) {
			throw new MessageNotFoundException(npguy, dialogue);
		}
		return npguys.get(npguy).getDialogues().get(dialogue);
	}
	
	public static PlayerMessage getDefaultMessage(String name) {
		List<Action> actions = new ArrayList<Action>();
		List<Requirement> requirements = new ArrayList<Requirement>();
		List<String> possibleResponses = new ArrayList<String>();
		NPCMessage response = new NPCMessage(plugin.getConfig().getString("dialogues.default.npc_response.message"), possibleResponses);
		return new PlayerMessage(name, plugin.getConfig().getString("dialogues.default.shortcut"), plugin.getConfig().getString("dialogues.default.message"), response, requirements, actions);
	}
	
	public static PlayerMessage getExitMessage() {
		List<Action> actions = new ArrayList<Action>();
		try {
			actions.add(ElementManager.newAction("ABANDON_CONVERSATION"));
		} catch (ActionMissingException e) {
			e.printStackTrace();
		}
		List<Requirement> requirements = new ArrayList<Requirement>();
		List<String> possibleResponses = new ArrayList<String>();
		NPCMessage response = new NPCMessage(plugin.getConfig().getString("dialogues.exit.npc_response.message"), possibleResponses);
		return new PlayerMessage("", plugin.getConfig().getString("dialogues.exit.shortcut"), plugin.getConfig().getString("dialogues.exit.message"), response, requirements, actions);
	}

	public static void createNPGuy(String name, boolean active) throws NPGuyAlreadyExistsException {
		NPGuyData npguy = new NPGuyData(name, active);
		npguy.setWelcomeMessage("default");
		npguy.getDialogues().put("default", getDefaultMessage("default"));
		putData(name, npguy);
	}
}
