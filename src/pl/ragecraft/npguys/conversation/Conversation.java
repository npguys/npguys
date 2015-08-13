/*
* NPGuys - Bukkit plugin for better NPC interaction
* Copyright (C) 2014 Adam Gotlib <Goldob>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

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
	
	public Conversation(Player player, NPGuy npc) throws UIMissingException {
		this.player = player;
		this.npc = npc;
		this.ui = ElementManager.newUI(this);
	}
	
	public void beginConversation() {
		ui.conversationStart();
		continueConversation(DialogueManager.getWelcomeMessage(npc.getUID()));
	}
	
	public void continueConversation(PlayerMessage choosenResponse) {	
		if (choosenResponse == null) {
			ConversationManager.endConversation(player);
			return;
		}
		
		displayedMessage = choosenResponse;
		possibleResponses = new ArrayList<PlayerMessage>();
		possibleResponses.addAll(npc.getPossibleResponses(displayedMessage.getNPCMessage(), player));
		
		ui.displayMessages();
		
		for(Action action : choosenResponse.getActions()) {
			action.perform(npc.getNPC(), player);
		}
		
		if (!finish) {
			ui.scheduleChoiceViewOpening();
		}
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
	
	public ConversationUI getUI() {
		return ui;
	}
	
	protected void end() {
		ui.conversationEnd();
		finish = true;
	}
}
