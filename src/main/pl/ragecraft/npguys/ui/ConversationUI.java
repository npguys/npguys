package pl.ragecraft.npguys.ui;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import pl.ragecraft.npguys.conversation.Conversation;

public abstract class ConversationUI implements Listener {
	private Conversation conversation;
	
	/* This will be called only once per each registered UI type.
	 * It should save any values it needs in a static way.
	 */
	public abstract void init(ConfigurationSection config);
	
	// Be aware that conversation may be null if constructor is called only for UI type
	// initialization
	public ConversationUI(Conversation conversation) {
		this.conversation = conversation;
	}
	
	// Called at the conversation start and when player picks next dialogue line
	public abstract void openView();
	
	// Called every time the choosen response changes
	public abstract void updateView();
	
	// Called when the conversation ends
	public abstract void closeView();
	
	protected final Conversation getConversation() {
		return conversation;
	}
	
	protected final boolean checkPlayer(Player player) {
		return player == conversation.getPlayer();
	}
	
	protected final boolean checkNPC(NPC npc) {
		return npc == conversation.getNPGuy().getNPC();
	}
}
