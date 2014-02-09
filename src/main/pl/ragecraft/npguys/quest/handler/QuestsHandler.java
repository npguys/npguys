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

package pl.ragecraft.npguys.quest.handler;

import java.util.Collections;
import java.util.List;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.blackvein.quests.Stage;

import org.bukkit.entity.Player;

import pl.ragecraft.npguys.quest.QuestHandler;

public class QuestsHandler implements QuestHandler {
	private Quests quests;
	
	public QuestsHandler(Quests quests) {
		this.quests = quests;
	}
	
	@Override
	public void beginQuest(Player player, String questName) {
		getQuester(player).takeQuest(getQuest(questName), true);
	}

	@Override
	public void completeObjectives(Player player, String questName,
			List<Integer> objectivesIDs) {
		Quester quester = getQuester(player);
		Quest currentQuest = getQuest(questName);
		if(currentQuest != null && currentQuest.name.equalsIgnoreCase(questName) && !objectivesIDs.isEmpty()) {
			Collections.sort(objectivesIDs);
			int lastObjectiveID = objectivesIDs.get(objectivesIDs.size()-1);
			
			while(quester.currentQuest == currentQuest && quester.currentStageIndex >= lastObjectiveID) {
				currentQuest.nextStage(quester);
			}
		}
	}

	@Override
	public void completeQuest(Player player, String questName) {
		Quester quester = getQuester(player);
		Quest currentQuest = quester.currentQuest;
		if(currentQuest != null && currentQuest.name.equalsIgnoreCase(questName)) {
			currentQuest.completeQuest(quester);
		} else {
			/* We have to restore previous state or the current quest will be cancelled.
			 * It would be the best to also restore objective progress, but
			 * the plugin doesn't provide a way to do it	*/
			Stage currentStage = quester.currentStage;
			int currentStageIndex = quester.currentStageIndex;

			getQuest(questName).completeQuest(quester);
			
			quester.currentQuest = currentQuest;
			quester.currentStage = currentStage;
			quester.currentStageIndex = currentStageIndex;
			quester.startStageTimer();
		}
	}

	@Override
	public void cancelQuest(Player player, String questName) {
		Quester quester = getQuester(player);
		Quest currentQuest = quester.currentQuest;
		if(currentQuest != null && currentQuest.name.equalsIgnoreCase(questName)) {
			currentQuest.failQuest(quester);
		}
	}

	@Override
	public boolean hasActiveObjectives(Player player, String questName,
			List<Integer> objectivesIDs) {
		Quester quester = getQuester(player);
		Quest currentQuest = quester.currentQuest;
		if(currentQuest != null && currentQuest.name.equalsIgnoreCase(questName)) {
			int currentStageIndex = quester.currentStageIndex;
			for(Integer objectiveID : objectivesIDs) {
				if(objectiveID.intValue() != currentStageIndex) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasCompleted(Player player, String questName) {
		// It looks hacky, but solves problem of case-sensitivity
		return getQuester(player).completedQuests.contains(getQuest(questName).name);
	}

	@Override
	public boolean isPerforming(Player player, String questName) {
		Quest currentQuest = getQuester(player).currentQuest;
		return (currentQuest != null && currentQuest.name.equalsIgnoreCase(questName));
	}
	
	private Quester getQuester(Player player) {
		return quests.getQuester(player.getName());
	}
	
	private Quest getQuest(String name) {
		return quests.findQuest(name);
	}
}
