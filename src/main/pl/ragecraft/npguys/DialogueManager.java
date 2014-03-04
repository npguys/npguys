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

package pl.ragecraft.npguys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.conversation.ConversationManager;
import pl.ragecraft.npguys.conversation.NPCMessage;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.exception.ActionMissingException;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.MessageNotFoundException;
import pl.ragecraft.npguys.exception.NPGuyAlreadyExistsException;
import pl.ragecraft.npguys.exception.NPGuyNotFoundException;
import pl.ragecraft.npguys.exception.RequirementMissingException;
import pl.ragecraft.npguys.requirement.Requirement;


public class DialogueManager {
	private static NPGuys plugin = null;
	private static File npcs;
	private static Map<String, NPGuyData> npguys;
	
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
		
		plugin.getServer().getPluginManager().registerEvents(new EventListener(), plugin);
	}
	
	public static void reload() {
		npguys = new HashMap<String, NPGuyData>();
		
		for (File npc : npcs.listFiles()) {
			load(npc.getName().replaceAll(".yml", ""), YamlConfiguration.loadConfiguration(npc));
		}
		plugin.getLogger().log(Level.INFO, "Dialogues loaded.");
	}
	
	private static void load(String npguy, YamlConfiguration data) {
		NPGuyData toLoad = new NPGuyData();
		//TODO Handle exceptions
		toLoad.name = npguy;
		toLoad.welcomeMessage = data.getString("welcome_message");
		for (String messageUid : data.getConfigurationSection("dialogues").getKeys(false)) {
			String shortcut = data.getString("dialogues."+messageUid+".shortcut");
			String message = data.getString("dialogues."+messageUid+".message");
			
			List<Requirement> loadedRequirements = new ArrayList<Requirement>();
			if (data.getConfigurationSection("dialogues."+messageUid+".requirements") == null) {
				data.getConfigurationSection("dialogues."+messageUid).createSection("requirements");
			}
			for (String key : data.getConfigurationSection("dialogues."+messageUid+".requirements").getKeys(false)) {
				ConfigurationSection requirement = data.getConfigurationSection("dialogues."+messageUid+".requirements."+key);
				
				String type = requirement.getString("type");
				try {
					Requirement loadedRequirement = ElementManager.newRequirement(type);
					loadedRequirement.load(requirement);
					if(requirement.contains("reversed")) {
						loadedRequirement.setReversed(requirement.getBoolean("reversed"));
					}
					loadedRequirements.add(loadedRequirement);
				} catch (FailedToLoadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RequirementMissingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<Action> loadedActions = new ArrayList<Action>();
			if (data.getConfigurationSection("dialogues."+messageUid+".actions") == null) {
				data.getConfigurationSection("dialogues."+messageUid).createSection("actions");
			}
			for (String key : data.getConfigurationSection("dialogues."+messageUid+".actions").getKeys(false)) {
				ConfigurationSection action = data.getConfigurationSection("dialogues."+messageUid+".actions."+key);
				
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
			
			String npcResponse_message = data.getString("dialogues."+messageUid+".npc_response.message");
			List<String> possibleResponses = data.getStringList("dialogues."+messageUid+".npc_response.possible_responses");
			NPCMessage npcResponse = new NPCMessage(npcResponse_message, possibleResponses);
			
			PlayerMessage loadedMessage = new PlayerMessage(shortcut, message, npcResponse, loadedRequirements, loadedActions);
			toLoad.dialogues.put(messageUid, loadedMessage);
		}
		npguys.put(npguy, toLoad);
	}
	
	private static void save(String npguy, YamlConfiguration data) {
		NPGuyData toSave = npguys.get(npguy);
		data.set("welcome_message", toSave.welcomeMessage);
		for (String uid : toSave.dialogues.keySet()) {
			ConfigurationSection savedMessage = data.createSection("dialogues."+uid);
			PlayerMessage messageToSave = toSave.dialogues.get(uid);
			
			savedMessage.set("shortcut", messageToSave.getShortcut());
			savedMessage.set("message", messageToSave.getMessage());
			
			savedMessage.createSection("requirements");
			int i = 0;
			for (Requirement requirement : messageToSave.getRequirements()) {
				ConfigurationSection savedRequirement = savedMessage.createSection("requirements."+String.valueOf(i));
				requirement.save(savedRequirement);
				i++;
			}
			
			savedMessage.createSection("actions");
			i = 0;
			for (Action action : messageToSave.getActions()) {
				ConfigurationSection savedAction = savedMessage.createSection("actions."+String.valueOf(i));
				action.save(savedAction);
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
	
	public static PlayerMessage getWelcomeMessage(String npguy) {
		try {
			return getPlayerMessage(npguy, npguys.get(npguy).welcomeMessage);
		} catch (MessageNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PlayerMessage getPlayerMessage(String npguy, String uid) throws MessageNotFoundException {
		return npguys.get(npguy).dialogues.get(uid);
	}
	
	public static PlayerMessage getDefaultMessage() {
		List<Action> actions = new ArrayList<Action>();
		List<Requirement> requirements = new ArrayList<Requirement>();
		List<String> possibleResponses = new ArrayList<String>();
		NPCMessage response = new NPCMessage(plugin.getConfig().getString("dialogues.default.npc_response.message"), possibleResponses);
		return new PlayerMessage(plugin.getConfig().getString("dialogues.default.shortcut"), plugin.getConfig().getString("dialogues.default.message"), response, requirements, actions);
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
		return new PlayerMessage(plugin.getConfig().getString("dialogues.exit.shortcut"), plugin.getConfig().getString("dialogues.exit.message"), response, requirements, actions);
	}
	
	private static class EventListener implements Listener {
		@SuppressWarnings("unused")
		@EventHandler
		public void onRightClick(NPCRightClickEvent event) {
			Player player = event.getClicker();
			NPC npc = event.getNPC();
			if (player.getLocation().distance(npc.getEntity().getLocation()) > plugin.getConfig().getDouble("conversation.distance"))
				return;
			
			Conversation conversation = ConversationManager.getConversationByCaller(player);
			if (conversation == null || conversation.getNPGuy().getNPC() != npc) {
				ConversationManager.beginConversation(player, npc.getTrait(NPGuy.class));
			}
		}
		
		@SuppressWarnings("unused")
		@EventHandler
		 public void onPlayerMove(PlayerMoveEvent event) {
			Conversation conversation = ConversationManager.getConversationByCaller(event.getPlayer());
			if (conversation != null) {
				if (event.getPlayer().getLocation().distance(conversation.getNPGuy().getNPC().getEntity().getLocation()) > plugin.getConfig().getDouble("conversation.distance")) {
					ConversationManager.endConversation(event.getPlayer());
				}
			}
		}
		
		@SuppressWarnings("unused")
		@EventHandler
		 public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
			ConversationManager.endConversation(event.getPlayer());
		}
	}
}
