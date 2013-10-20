package pl.ragecraft.npguys;

import org.bukkit.plugin.RegisteredServiceProvider;

import pl.ragecraft.npguys.quester.NPGuysObjective;

import com.gmail.molnardad.quester.Quester;
import com.gmail.molnardad.quester.exceptions.ElementException;

import net.citizensnpcs.api.CitizensPlugin;
import net.milkbowl.vault.economy.Economy;

public class PluginManager {
	private static Economy economy = null;
	private static CitizensPlugin citizens = null;
	private static Quester quester = null;

	
	public static void init(NPGuys plugin) {
		setupCitizens(plugin);
		//TODO setupEconomy(plugin);
		setupQuester(plugin);
	}

	private static void setupQuester(NPGuys plugin) {
		if (!plugin.getServer().getPluginManager().isPluginEnabled("Quester")) {
			return;
		}
		quester = (Quester)plugin.getServer().getPluginManager().getPlugin("Quester");
		if (quester == null) {
			return;
		}
		try {
			quester.getElementManager().register(NPGuysObjective.class);
		} 
		catch (ElementException e) {
			e.printStackTrace();
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
	
	public static Economy getEconomy() {
		return economy;
	}
	
	public static CitizensPlugin getCitizens() {
		return citizens;
	}
	
	public static Quester getQuester() {
		return quester;
	}
}
