package pl.ragecraft.npguys.event;

import org.bukkit.event.HandlerList;

public class ConversationAbandonedEvent extends ConversationEvent {
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
