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
