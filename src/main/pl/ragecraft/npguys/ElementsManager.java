package pl.ragecraft.npguys;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.exception.ActionMissingException;
import pl.ragecraft.npguys.exception.RequirementMissingException;
import pl.ragecraft.npguys.quest.QuestHandler;
import pl.ragecraft.npguys.quest.handler.QuesterHandler;
import pl.ragecraft.npguys.requirement.Requirement;

import com.gmail.molnardad.quester.Quester;

import net.citizensnpcs.api.CitizensPlugin;
import net.milkbowl.vault.economy.Economy;

public class ElementsManager {
	private static Economy economy = null;
	private static CitizensPlugin citizens = null;
	private static QuestHandler questHandler = null;
	
	private static Map<String, Class<? extends Action>> actions;
	private static Map<String, Class<? extends Requirement>> requirements;
	
	public static void init(NPGuys plugin) {
		setupCitizens(plugin);
		//TODO setupEconomy(plugin);
		setupQuestHandler(plugin);
		
		actions = new HashMap<String, Class<? extends Action>>();
		requirements = new HashMap<String, Class<? extends Requirement>>();
	}

	private static void setupQuestHandler(NPGuys plugin) {
		if(plugin.getServer().getPluginManager().isPluginEnabled("Quester")) {
			Plugin questerPlugin = plugin.getServer().getPluginManager().getPlugin("Quester");
			if(questerPlugin instanceof Quester) {
				questHandler = new QuesterHandler((Quester)questerPlugin);
				return;
			}
		}
	}

	private static void setupCitizens(NPGuys plugin) {
		citizens = (CitizensPlugin)plugin.getServer().getPluginManager().getPlugin("Citizens");
	}

	private static void setupEconomy(NPGuys plugin)	{	
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
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
}
