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

package pl.ragecraft.npguys.quest;

import java.util.List;

import org.bukkit.entity.Player;

public interface QuestHandler {
	public void beginQuest(Player player, String questName);
	
	public void completeObjectives(Player player, String questName, List<Integer> objectivesIDs);
	
	public void completeQuest(Player player, String questName);
	
	public void cancelQuest(Player player, String questName);
	
	public boolean hasActiveObjectives(Player player, String questName, List<Integer> objectivesIDs);
	
	public boolean hasCompleted(Player player, String questName);
	
	public boolean isPerforming(Player player, String questName);
}
