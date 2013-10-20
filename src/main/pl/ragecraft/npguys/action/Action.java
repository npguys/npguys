package pl.ragecraft.npguys.action;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.FailedToLoadException;
import pl.ragecraft.npguys.InvalidCommandException;


public abstract class Action {
	String type;
	
	public Action(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public abstract void perform(NPC npc, Player player);
	
	public abstract void load(ConfigurationSection data) throws FailedToLoadException;
	
	public abstract void fromCommand(String[] data) throws InvalidCommandException;
	
	public void save(ConfigurationSection data) {
		data.set("type", getType());
	}
	
	public abstract String getDescription();
	
	public abstract String getUsage();
	
	public abstract String getData();
}
