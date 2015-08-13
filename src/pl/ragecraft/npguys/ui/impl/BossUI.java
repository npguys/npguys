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

package pl.ragecraft.npguys.ui.impl;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.exception.UIInitializationFailedException;
import pl.ragecraft.npguys.ui.ClassicControlsUI;

public class BossUI extends ClassicControlsUI {
	public void init(ConfigurationSection config) throws UIInitializationFailedException {
		super.init(config);
		if (!Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
			throw new UIInitializationFailedException("BarAPI plugin required");
		}
	}
	
	public BossUI(Conversation conversation) {
		super(conversation);
	}
	
	@Override
	public void conversationStart() {}
	
	@Override
	public void responseChoice() {
		super.responseChoice();
		PlayerMessage choosenResponse = getConversation().getPossibleResponses().get(getChoosenResponseIndex());
		float possibleResponsesNumber = (float)getConversation().getPossibleResponses().size();
		BarAPI.setMessage(getConversation().getPlayer(), choosenResponse.getShortcut(), 100.0F*((float)getChoosenResponseIndex()+1.0F)/possibleResponsesNumber);
	}

	@Override
	public void conversationEnd() {
		BarAPI.removeBar(getConversation().getPlayer());
	}
}
