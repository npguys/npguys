package pl.ragecraft.npguys.action.quest;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.FailedToLoadException;
import pl.ragecraft.npguys.InvalidCommandException;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.quester.QuesterManager;


public class BeginQuest extends Action {
	public BeginQuest(String type) {
		super(type);
	}

	String quest;

	@Override
	public void perform(NPC npc, Player player) {
		QuesterManager.beginQuest(player, quest);
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
		//TODO
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
