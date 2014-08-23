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

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.exception.UIInitializationFailedException;

public abstract class ConversationUI implements Listener {
	private Conversation conversation;
	private static long npcDelay;
	private static String playerMessageFormat;
	private static String npcMessageFormat;
	
	// This will be called only once per each registered UI type.
	 // It should save any values it needs in a static way.
	public void init(ConfigurationSection config) throws UIInitializationFailedException {
		ConfigurationSection generalConfig = NPGuys.getPlugin().getConfig().getConfigurationSection("ui");
		npcDelay = generalConfig.getLong("npc.delay");
		playerMessageFormat = generalConfig.getString("player.message_format");
		npcMessageFormat = generalConfig.getString("npc.message_format");
	}
	
	// Be aware that conversation may be null if constructor is called only for UI type
	// initialization
	public ConversationUI(Conversation conversation) {
		this.conversation = conversation;
	}
	
	// Called at the conversation start and when player picks next dialogue line
	public final void displayMessages() {
		final Conversation conversation = getConversation();
		String playerMsg = playerMessageFormat;
		playerMsg = playerMsg.replaceAll("%msg", conversation.getDisplayedMessage().getMessage());
		playerMsg = playerMsg.replaceAll("%player", conversation.getPlayer().getName());
		playerMsg = playerMsg.replaceAll("%npc", conversation.getNPGuy().getNPC().getName());
		playerMsg = playerMsg.replace('&', '§');
		if(!conversation.getDisplayedMessage().getMessage().equals("")) {
			conversation.getPlayer().sendMessage(playerMsg);
		}
		
		String npcMsg = npcMessageFormat;

		npcMsg = npcMsg.replaceAll("%msg", conversation.getDisplayedMessage().getNPCMessage().getMessage());
		npcMsg = npcMsg.replaceAll("%player", conversation.getPlayer().getName());
		npcMsg = npcMsg.replaceAll("%npc", conversation.getNPGuy().getNPC().getName());
		npcMsg = npcMsg.replace('&', '§');
		final String final_npcMsg = npcMsg;
		
		Bukkit.getScheduler().runTaskLater(NPGuys.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(!conversation.getDisplayedMessage().getNPCMessage().equals(""))
					conversation.getPlayer().sendMessage(final_npcMsg);
			}
		}, npcDelay);
	}
	
	public final void scheduleChoiceViewOpening() {
		Bukkit.getScheduler().runTaskLater(NPGuys.getPlugin(), new Runnable() {
			@Override
			public void run() {
				openChoiceView();
			}
		}, npcDelay);
	}
	
	// After the method is called, the player should be able to choose his response
	public abstract void openChoiceView();
	
	// Called when the conversation ends
	public abstract void closeChoiceView();
	
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
