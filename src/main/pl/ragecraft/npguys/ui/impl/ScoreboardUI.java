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

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.ui.ClassicControlsUI;

public class ScoreboardUI extends ClassicControlsUI {
	private static String headline;
	
	public ScoreboardUI(Conversation conversation) {
		super(conversation);
	}
	
	@Override
	public void init(ConfigurationSection config) {
		super.init(config);
		headline = config.getString("headline");
	}
	
	@Override
	public void openChoiceView() {
		super.openChoiceView();
		Scoreboard view = Bukkit.getScoreboardManager().getNewScoreboard();
		Conversation conversation = getConversation();
		view.registerNewObjective(ChatColor.UNDERLINE+headline, "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
		
		List<PlayerMessage> possibleResponses = conversation.getPossibleResponses();
		int id = possibleResponses.size();
		
		for(PlayerMessage response : possibleResponses) {
			StringBuilder line = new StringBuilder();
			if(possibleResponses.size()-id == getChoosenResponseIndex()) {
				line.append(" > ");
			}
			else {
				line.append("   ");
			}
			if (response.getShortcut().length() <= 13) {
				line.append(response.getShortcut());
			}
			else {
				line.append(response.getShortcut().substring(0, 13));
			}
			view.getObjective(DisplaySlot.SIDEBAR).getScore(line.toString()).setScore(id);
			id--;
		}
		conversation.getPlayer().setScoreboard(view);
	}

	@Override
	public void closeChoiceView() {
		getConversation().getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	}
}
