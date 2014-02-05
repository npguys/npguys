package pl.ragecraft.npguys.requirement.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.PluginsManager;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;
import pl.ragecraft.npguys.requirement.Requirement;


public class PerformedQuest extends Requirement {
	String quest;
	List<Integer> objectives = new ArrayList<Integer>();
	
	public PerformedQuest(String type) {
		super(type);
	}
	
	@Override
	public boolean isMet(NPC npc, Player player) {
		if(PluginsManager.getQuestHandler().isPerforming(player, quest)) {
			return PluginsManager.getQuestHandler().hasActiveObjectives(player, quest, objectives);
		}
		return false;
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest UID missing!");
		}
		if (data.contains("objectives")) {
			if (data.get("objectives") instanceof List<?>) {
				for (int objective : data.getIntegerList("objectives")) {
					objectives.add(objective);
				}
			}
			else {
				throw new FailedToLoadException("Invalid objectives! Objectives must be a valid integer list!");
			}
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest UID missing!");
		}
		quest = data[0];
		for (int i = 1; i < data.length; i++) {
			try {
				objectives.add(Integer.valueOf(data[i]));
			}
			catch(NumberFormatException e) {
				throw new InvalidCommandException("Invalid quest objective! Objective must be a valid integer!");
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
		String str = quest;
		if(!objectives.isEmpty()) {
			str=str+": ";
		}
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
