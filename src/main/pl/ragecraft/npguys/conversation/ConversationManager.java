package pl.ragecraft.npguys.conversation;

import java.util.HashMap;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.Player;

import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.gui.GUIManager;



public class ConversationManager {
	private static HashMap<Player, Conversation> conversations = new HashMap<Player, Conversation>();

	public static void endConversation(Player caller, NPC npc) {
		Conversation conversation = conversations.get(caller);
		if(conversation.getNPC() != npc) {
			return;
		}
		endConversation(caller);
	}


	public static void endConversation(Player caller) {
		Conversation conversation = conversations.get(caller);
		if (conversation == null) {
			return;
		}
		conversation.end();
		GUIManager.closeGUI(caller);
		conversations.remove(caller);	
	}

	public static void beginConversation(Player caller, NPGuy npc){
		conversations.put(caller, new Conversation(caller, npc));
		conversations.get(caller).beginConversation();
	}

	public static Conversation getConversationByCaller(Player player) {
		if(conversations.containsKey(player)) {
			return conversations.get(player);
		}
		else {
			return null;
		}
	}


	public static void endConversations(NPGuy npguy) {
		for (Conversation conversation : conversations.values()) {
			if (conversation.getNPC() == npguy) {
				endConversation(conversation.getPlayer());
			}
		}
	}
}
