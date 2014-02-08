package pl.ragecraft.npguys.conversation;

import java.util.HashMap;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.exception.UIMissingException;

public class ConversationManager {
	private static HashMap<Player, Conversation> conversations = new HashMap<Player, Conversation>();

	public static void endConversation(Player caller, NPC npc) {
		Conversation conversation = conversations.get(caller);
		if(conversation.getNPGuy() != npc) {
			return;
		}
		endConversation(caller);
	}


	public static void endConversation(Player caller) {
		Conversation conversation = conversations.get(caller);
		if (conversation == null) 
			return;
		
		conversations.remove(caller);	
		conversation.end();
		HandlerList.unregisterAll(conversation.getUI());
	}

	public static void beginConversation(Player caller, NPGuy npc){
		endConversation(caller);
		try {
			Conversation conversation;
			conversation = new Conversation(caller, npc);
			
			conversations.put(caller, conversation);
			
			NPGuys.getPlugin().getServer().getPluginManager().registerEvents(conversation.getUI(), NPGuys.getPlugin());
			conversation.beginConversation();
		} catch (UIMissingException e) {
			e.printStackTrace();
		}
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
			if (conversation.getNPGuy() == npguy) {
				endConversation(conversation.getPlayer());
			}
		}
	}
}
