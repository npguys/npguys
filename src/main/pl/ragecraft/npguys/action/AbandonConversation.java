package pl.ragecraft.npguys.action;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.FailedToLoadException;
import pl.ragecraft.npguys.InvalidCommandException;
import pl.ragecraft.npguys.conversation.ConversationManager;


public class AbandonConversation extends Action {

	public AbandonConversation(String type) {
		super(type);
	}

	@Override
	public void perform(NPC npc, Player player) {
		ConversationManager.endConversation(player);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		//Do nothing
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		//Do nothing
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
		return "";
	}
}
