package pl.ragecraft.npguys.action.quest;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.FailedToLoadException;
import pl.ragecraft.npguys.InvalidCommandException;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.quester.QuesterManager;


public class CompleteQuest extends Action {
	String quest;
	
	public CompleteQuest(String type) {
		super(type);
	}
	
	@Override
	public void perform(NPC npc, Player player) {
		QuesterManager.completeQuest(player, quest);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getData() {
		return quest;
	}

}
