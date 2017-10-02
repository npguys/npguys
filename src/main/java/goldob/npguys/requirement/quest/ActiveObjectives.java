package goldob.npguys.requirement.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import goldob.npguys.ElementManager;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;
import goldob.npguys.requirement.Requirement;

public class ActiveObjectives extends Requirement {
	private String quest;
	private List<Integer> objectives = new ArrayList<Integer>();
	
	public ActiveObjectives(String type) {
		super(type);
	}
	
	@Override 
	public boolean isMet(NPC npc, Player player) {
		return ElementManager.getQuestHandler().hasActiveObjectives(player, quest, objectives);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest name missing!");
		}
		if (data.contains("objectives") && data.get("objectives") instanceof List<?>) {
			objectives.addAll(data.getIntegerList("objectives"));
			if (objectives.size() < 1) {
				throw new FailedToLoadException("Quest objectives missing!");
			}
		}
		else {
			throw new FailedToLoadException("Quest objectives missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest name missing!");
		}
		if (data.length < 2) {
			throw new InvalidCommandException("Quest objectives missing!");
		}
		quest = data[0];
		for (int i = 1; i < data.length; i++) {
			try {
				objectives.add(Integer.valueOf(data[i]));
			}
			catch(NumberFormatException e) {
				throw new InvalidCommandException("Invalid objectives format. Objectives must be a valid set of integers");
			}
		}
	}

	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("quest", quest);
		data.set("objectives", objectives);
	}

	@Override
	public String getDescription() {
		return "Checks if the player is performing certain quest and certain objectives are active for him.";
	}

	@Override
	public String getUsage() {
		return "[quest] [obj1] (obj2, obj3,...)";
	}

	@Override
	public String getData() {
		String str = quest+": ";
		boolean isFirst = true;
		for (int objective : objectives) {
			if(!isFirst) {
				str = str + ", ";
			}
			str = str + String.valueOf(objective);
			isFirst = false;
		}
		return str;
	}
}