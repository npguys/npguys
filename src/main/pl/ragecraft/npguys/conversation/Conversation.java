package pl.ragecraft.npguys.conversation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.DialoguesManager;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.gui.GUIManager;


public class Conversation {
	private NPGuy npc;
	private Player player;
	private boolean finish = false;
	private PlayerMessage displayedMessage;
	private List<PlayerMessage> possibleResponses;
	private int choosenResponse;
	
	public Conversation(Player player, NPGuy npc) {
		this.player = player;
		this.npc = npc;
	}
	
	public void beginConversation() {
		choosenResponse = 1;
		possibleResponses = new ArrayList<PlayerMessage>();
		possibleResponses.add(DialoguesManager.getWelcomeMessage(npc.getUID()));
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
		
		if(!finish) possibleResponses.addAll(npc.getPossibleResponses(displayedMessage.getNPCMessage(), player));
		
		GUIManager.showGUI(this);
	}
	
	public void changeResponse(boolean reversed) {
		if (!reversed) {
			if (choosenResponse < possibleResponses.size()) {
				choosenResponse++;
			}
			else {
				choosenResponse = 1;
			}
		}
		else {
			if (choosenResponse > 1) {
				choosenResponse--;
			}
			else {
				choosenResponse = possibleResponses.size();
			}
		}
		GUIManager.updateGUI(this);
	}
	
	public NPGuy getNPC() {
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
	
	public void end() {
		finish = true;
	}
}
