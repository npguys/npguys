package goldob.npguys.action;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;

public class ForceCommand extends Action {
	private String command;
	
	public ForceCommand(String type) {
		super(type);
	}

	@Override
	public void perform(NPC npc, Player player) {
		Bukkit.getServer().dispatchCommand(player, command);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("command") && data.get("command") instanceof String) {
			command = data.getString("command");
		}
		else {
			throw new FailedToLoadException("Command missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if(data.length < 1) {
			throw new InvalidCommandException("Command missing!");
		} else {
			command = "";
			for(String str : data) {
				command += (str + " ");
			}
		}
	}
	
	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("command", command);
	}

	@Override
	public String getDescription() {
		return "Makes specified player to execute a command.";
	}

	@Override
	public String getUsage() {
		return "[cmd]";
	}

	@Override
	public String getData() {
		return command;
	}

}
