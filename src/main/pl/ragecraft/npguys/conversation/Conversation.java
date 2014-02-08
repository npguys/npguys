package pl.ragecraft.npguys.conversation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.DialogueManager;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.exception.UIMissingException;
import pl.ragecraft.npguys.ui.ConversationUI;

public class Conversation {
	private NPGuy npc;
	private Player player;
	private ConversationUI ui;
	private boolean finish = false;
	private PlayerMessage displayedMessage;
	private List<PlayerMessage> possibleResponses;
	private int choosenResponse;
	
	public Conversation(Player player, NPGuy npc) throws UIMissingException {
		this.player = player;
		this.npc = npc;
		this.ui = ElementManager.newUI(this);
	}
	
	public void beginConversation() {
		choosenResponse = 1;
		possibleResponses = new ArrayList<PlayerMessage>();
		possibleResponses.add(DialogueManager.getWelcomeMessage(npc.getUID()));
		continueConversation();
	}
	
	public void continueConversation() {	
		for(Action action : possibleResponses.get(choosenResponse-1).getActions()) {
			action.perform(npc.getNPC(), player);
		}
		
		displayedMessage = possibleResponses.get(choosenResponse-1);
		if (displayedMessage == null) {
			ConversationManager.endConversation(player);
			return;
		}
		
		choosenResponse = 1;
		possibleResponses = new ArrayList<PlayerMessage>();
		
		if(!finish) {
			possibleResponses.addAll(npc.getPossibleResponses(displayedMessage.getNPCMessage(), player));
			ui.openView();
		} else {
			ui.closeView();
		}
	}
	
	public void changeResponse(int index) {
		choosenResponse = Math.min(possibleResponses.size(), Math.max(1, index));
		ui.updateView();
	}
	
	public NPGuy getNPGuy() {
		return npc;
	}
	
	public PlayerMessage getDisplayedMessage() {
		return displayedMessage;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public List<PlayerMessage> getPossibleResponses() {
		return possibleResponses;
	}
	
	public int getChoosenResponse() {
		return choosenResponse;
	}
	
	public ConversationUI getUI() {
		return ui;
	}
	
	protected void end() {
		finish = true;
	}
}
