package pl.ragecraft.npguys.event;

import org.bukkit.event.Event;

import pl.ragecraft.npguys.conversation.Conversation;

public abstract class ConversationEvent extends Event {
	private Conversation conversation;
	
	public Conversation getConversation() {
		return conversation;
	}
}
