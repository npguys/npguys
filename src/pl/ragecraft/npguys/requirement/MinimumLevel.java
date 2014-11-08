package pl.ragecraft.npguys.requirement;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;

public class MinimumLevel extends Requirement {
	private int minLevel;
	
	public MinimumLevel(String type) {
		super(type);
	}

	@Override
	public boolean isMet(NPC npc, Player player) {
		return player.getLevel() >= minLevel;
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("level") && data.get("level") instanceof Integer) {
			minLevel = data.getInt("level");
		}
		else {
			throw new FailedToLoadException("Level missing!");
		}
		
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Level missing!");
		}
		if (data.length > 1) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		try {
			minLevel = Integer.valueOf(data[0]);
		} catch (NumberFormatException e) {
			throw new InvalidCommandException("Level must be a valid integer number!");
		}
	}

	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("level", minLevel);
	}

	@Override
	public String getDescription() {
		return "Checks if player has reached the required level.";
	}

	@Override
	public String getUsage() {
		return "[level]";
	}

	@Override
	public String getData() {
		return String.valueOf(minLevel);
	}

}
