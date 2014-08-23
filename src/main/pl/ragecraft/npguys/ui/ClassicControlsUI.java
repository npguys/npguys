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

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.exception.UIInitializationFailedException;

public abstract class ClassicControlsUI extends ConversationUI {
	private boolean ignoreEvents;
	private int choosenResponseIndex;
	
	private static boolean useScrollback;

	public ClassicControlsUI(Conversation conversation) {
		super(conversation);
		ignoreEvents = true;
		choosenResponseIndex = 0;
	}
	
	@Override
	public void init(ConfigurationSection config) throws UIInitializationFailedException {
		super.init(config);
		useScrollback = config.getBoolean("use-scrollback");
	}
	
	@Override
	public void openChoiceView() {
		ignoreEvents = false;
	}

	private void changeResponse(boolean increment) {
		Conversation conversation = getConversation();
		if (increment) {
			if (choosenResponseIndex < conversation.getPossibleResponses().size()-1) {
				choosenResponseIndex++;
			} else {
				choosenResponseIndex = 0;
			}
		}
		else {
			if (choosenResponseIndex > 0) {
				choosenResponseIndex--;
			} else {
				choosenResponseIndex = conversation.getPossibleResponses().size()-1;
			}
		}
		openChoiceView();
	}
	
	@EventHandler
	public void onSlotChange(PlayerItemHeldEvent event) {
		if(ignoreEvents || !checkPlayer(event.getPlayer())) 
			return;
		
		Conversation conversation = getConversation();
		if (useScrollback) {
			
			int oldSlot = event.getPreviousSlot();
			int newSlot = event.getNewSlot();
			
			Location playerLoc = conversation.getPlayer().getEyeLocation();
			Entity npcEntity = conversation.getNPGuy().getNPC().getEntity();
			Location npcLoc = (npcEntity instanceof LivingEntity ? ((LivingEntity)npcEntity).getEyeLocation() : npcEntity.getLocation());
			
			// Checks if player looks at npc face (we don't want to talk to dirt)
			Vector toCenter = new Vector(npcLoc.getX()-playerLoc.getX(), npcLoc.getY()-playerLoc.getY(), npcLoc.getZ()-playerLoc.getZ());
			Vector direction = playerLoc.getDirection();
			if (direction.angle(toCenter) < Math.atan(0.5/playerLoc.distance(npcLoc))) {
				if (newSlot == oldSlot+1 || (newSlot == 0 && oldSlot == 8)) {
					changeResponse(true);
				}
				if (newSlot == oldSlot-1 || (newSlot == 8 && oldSlot == 0)) {
					changeResponse(false);
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onRightClick(NPCRightClickEvent event) {
		if(ignoreEvents || !checkPlayer(event.getClicker()) || !checkNPC(event.getNPC())) 
			return;
		
		changeResponse(true);
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onLeftClick(NPCLeftClickEvent event) {
		if(ignoreEvents || !checkPlayer(event.getClicker()) || !checkNPC(event.getNPC()))
			return;
		
		getConversation().continueConversation(getConversation().getPossibleResponses()
				.get(choosenResponseIndex));
		event.setCancelled(true);
		
		ignoreEvents = true;
		choosenResponseIndex = 0;
	}
	
	protected int getChoosenResponseIndex() {
		return choosenResponseIndex;
	}
}
