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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.exception.ActionMissingException;
import pl.ragecraft.npguys.exception.RequirementMissingException;
import pl.ragecraft.npguys.exception.UIInitializationFailedException;
import pl.ragecraft.npguys.exception.UIMissingException;
import pl.ragecraft.npguys.quest.QuestHandler;
import pl.ragecraft.npguys.quest.handler.QuesterHandler;
import pl.ragecraft.npguys.quest.handler.QuestsHandler;
import pl.ragecraft.npguys.requirement.Requirement;
import pl.ragecraft.npguys.ui.ConversationUI;

import me.ragan262.quester.Quester;
import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.CharacterManager;

import me.blackvein.quests.Quests;
import net.citizensnpcs.api.CitizensPlugin;
import net.milkbowl.vault.economy.Economy;

public class ElementManager {
	private static Economy economy = null;
	private static CitizensPlugin citizens = null;
	private static QuestHandler questHandler = null;
	private static CharacterManager heroesCharacterManager = null;
	
	private static Map<String, Class<? extends Action>> actions;
	private static Map<String, Class<? extends Requirement>> requirements;
	private static Map<String, Class<? extends ConversationUI>> uiTypes;
	
	private static String defaultUI;
	
	public static void init(NPGuys plugin) {
		reload(plugin);
		
		actions = new HashMap<String, Class<? extends Action>>();
		requirements = new HashMap<String, Class<? extends Requirement>>();
		uiTypes = new HashMap<String, Class<? extends ConversationUI>>();
	}
	
	public static void reload(NPGuys plugin) {
		setupCitizens(plugin);
		setupEconomy(plugin);
		setupQuestHandler(plugin);
		setupHeroes(plugin);
		
		defaultUI = NPGuys.getPlugin().getConfig().getString("ui.default").toUpperCase();
	}

	private static void setupQuestHandler(NPGuys plugin) {
		if(plugin.getServer().getPluginManager().isPluginEnabled("Quester")) {
			Plugin questerPlugin = plugin.getServer().getPluginManager().getPlugin("Quester");
			if(questerPlugin instanceof Quester) {
				questHandler = new QuesterHandler((Quester)questerPlugin);
				return;
			}
		}
		if (plugin.getServer().getPluginManager().isPluginEnabled("Quests")) {
			Plugin questsPlugin = plugin.getServer().getPluginManager().getPlugin("Quests");
			if(questsPlugin instanceof Quests) {
				questHandler = new QuestsHandler((Quests)questsPlugin);
				return;
			}
		}
	}

	private static void setupCitizens(NPGuys plugin) {
		citizens = (CitizensPlugin)plugin.getServer().getPluginManager().getPlugin("Citizens");
	}
	
	private static void setupEconomy(NPGuys plugin) {
		if(plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
			RegisteredServiceProvider<Economy> economyProvider = plugin.getServer()
					.getServicesManager().getRegistration(Economy.class);
	        if (economyProvider != null) {
	            economy = economyProvider.getProvider();
	        }
		}
	}
	
	private static void setupHeroes(NPGuys plugin) {
		if(plugin.getServer().getPluginManager().isPluginEnabled("Heroes")) {
			heroesCharacterManager = Heroes.getInstance().getCharacterManager();
		}
	}
	
	public static Action newAction(String name) throws ActionMissingException {
		try {
			if(actions.containsKey(name)) {
				return actions.get(name).getConstructor(String.class).newInstance(name);
			}
			else {
				throw new ActionMissingException(name);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void registerAction(String name, Class<? extends Action> clazz) {
		actions.put(name.toUpperCase(), clazz);
	}
	
	public static Requirement newRequirement(String name) throws RequirementMissingException {
		try {
			if (requirements.containsKey(name)) {
				return requirements.get(name).getConstructor(String.class).newInstance(name);
			}
			else {
				throw new RequirementMissingException(name);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void registerRequirement(String name, Class<? extends Requirement> clazz) {
		requirements.put(name.toUpperCase(), clazz);
	}
	
	public static Economy getEconomy() {
		return economy;
	}
	
	public static CitizensPlugin getCitizens() {
		return citizens;
	}
	
	public static QuestHandler getQuestHandler() {
		return questHandler;
	}
	
	public static CharacterManager getHeroesCharacterManager() {
		return heroesCharacterManager;
	}
	
	public static void setQuestHandler(QuestHandler questHandler) {
		ElementManager.questHandler = questHandler;
	}
	
	public static void registerUI(String name, Class<? extends ConversationUI> clazz) {
		uiTypes.put(name.toUpperCase(), clazz);
		ConfigurationSection uiConfig = NPGuys.getPlugin().getConfig().getConfigurationSection("ui.configs."+name.toLowerCase());
		try {
			newUI(name, null).init(uiConfig);
		} catch (UIMissingException e) {
			e.printStackTrace();
		} catch (UIInitializationFailedException e) {
			NPGuys.getPlugin().getLogger()
			.log(Level.WARNING, "Failed to initialize "+name+" UI: "+e.getMessage());
		}
	}
	
	public static ConversationUI newUI(Class<? extends ConversationUI> clazz, Conversation conversation) {
		try {
			return clazz.getConstructor(Conversation.class).newInstance(conversation);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		// TODO FailedToCreateUIException or similar
		return null;
	}
	
	public static ConversationUI newUI(String uiName, Conversation conversation) throws UIMissingException {
		if (uiTypes.containsKey(uiName)) {
			return newUI(uiTypes.get(uiName), conversation);
		} else {
			throw new UIMissingException(uiName);
		}
	}
	
	public static ConversationUI newUI(Conversation conversation) throws UIMissingException {
		return newUI(defaultUI, conversation);
	}
}
