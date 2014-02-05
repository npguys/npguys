package pl.ragecraft.npguys.action.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.PluginsManager;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;

public class CompleteObjectives extends Action {
	public CompleteObjectives(String type) {
		super(type);
	}

	String quest;
	List<Integer> objectives = new ArrayList<Integer>();
	
	@Override
	public void perform(NPC npc, Player player) {
		PluginsManager.getQuestHandler().completeObjectives(player, quest, objectives);	
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest UID missing!");
		}
		if (data.contains("objectives") && data.get("objectives") instanceof List<?>) {
			for (int objective : data.getIntegerList("objectives")) {
				objectives.add(objective);
			}
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
			throw new InvalidCommandException("Quest UID missing!");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
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
