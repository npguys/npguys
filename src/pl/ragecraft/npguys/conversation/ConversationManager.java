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

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import pl.ragecraft.npguys.NPGuy;
import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.exception.UIMissingException;

public class ConversationManager {
	private static NPGuys plugin = null;
	private static HashMap<Player, Conversation> conversations = new HashMap<Player, Conversation>();
	
	public static void init(final NPGuys plugin) {
		ConversationManager.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(new EventListener(), plugin);
	}
	
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
		
		NPGuy npguy = conversation.getNPGuy();
		npguy.getHeldConversations().remove(conversation);
		if(npguy.getHeldConversations().isEmpty()) {
			npguy.getNPC().getDefaultGoalController().setPaused(false);
		}
		conversations.remove(caller);
		conversation.end();
		HandlerList.unregisterAll(conversation.getUI());
	}

	public static void beginConversation(Player caller, NPGuy npc){
		npc.getNPC().getDefaultGoalController().setPaused(true);
		npc.getNPC().getNavigator().cancelNavigation();
		
		endConversation(caller);
		try {
			Conversation conversation;
			conversation = new Conversation(caller, npc);
			conversations.put(caller, conversation);
			npc.getHeldConversations().add(conversation);
			
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
	
	private static class EventListener implements Listener {
		@EventHandler
		public void onRightClick(NPCRightClickEvent event) {
			Player player = event.getClicker();
			NPC npc = event.getNPC();
			if (player.getLocation().distance(npc.getEntity().getLocation()) > plugin.getConfig().getDouble("conversation.distance") 
					|| !npc.hasTrait(NPGuy.class))
				return;
			
			Conversation conversation = getConversationByCaller(player);
			if (conversation == null || conversation.getNPGuy().getNPC() != npc) {
				beginConversation(player, npc.getTrait(NPGuy.class));
			}
		}
		
		@EventHandler
		 public void onPlayerMove(PlayerMoveEvent event) {
			Conversation conversation = ConversationManager.getConversationByCaller(event.getPlayer());
			if (conversation != null) {
				if (event.getPlayer().getLocation().distance(conversation.getNPGuy()
						.getNPC().getEntity().getLocation()) > plugin.getConfig().getDouble("conversation.distance")) {
					endConversation(event.getPlayer());
				}
			}
		}
		
		@EventHandler
		 public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
			endConversation(event.getPlayer());
		}
		
		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent event) {
			endConversation(event.getEntity());
		}
	}
}
