package pl.ragecraft.npguys.requirement;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;


public abstract class Requirement {
	String type;
	boolean reversed = false;
	
	public Requirement(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public abstract boolean isMet(NPC npc, Player player);

	public abstract void load(ConfigurationSection data) throws FailedToLoadException;
	
	public abstract void fromCommand(String[] data) throws InvalidCommandException;
	
	public void save(ConfigurationSection data) {
		data.set("type", getType());
		data.set("reversed", reversed);
	}
	
	public abstract String getDescription();
	
	public abstract String getUsage();
	
	public abstract String getData();
	
	public final void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	public final boolean isReversed() {
		return reversed;
	}
}
