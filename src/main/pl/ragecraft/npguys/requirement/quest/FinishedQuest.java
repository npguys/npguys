package pl.ragecraft.npguys.requirement.quest;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.FailedToLoadException;
import pl.ragecraft.npguys.InvalidCommandException;
import pl.ragecraft.npguys.quester.QuesterManager;
import pl.ragecraft.npguys.requirement.Requirement;


public class FinishedQuest extends Requirement {
	String quest;
	
	public FinishedQuest(String type) {
		super(type);
	}

	@Override
	public boolean isMet(NPC npc, Player player) {
		return QuesterManager.hasCompleted(player, quest);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = (String) data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest UID missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest UID missing!");
		}
		if (data.length > 0) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		quest = data[0];
	}

	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("quest", quest);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		return quest;
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}	
}
