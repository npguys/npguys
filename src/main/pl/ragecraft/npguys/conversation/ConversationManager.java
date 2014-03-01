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


	public static void endAll() {
		for(Player player : conversations.keySet()) {
			endConversation(player);
		}
	}
}
