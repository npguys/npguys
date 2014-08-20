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

import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.molnardad.quester.PlayerProfile;
import com.gmail.molnardad.quester.Quest;
import com.gmail.molnardad.quester.QuestManager;
import com.gmail.molnardad.quester.Quester;
import com.gmail.molnardad.quester.exceptions.QuesterException;

import pl.ragecraft.npguys.quest.QuestHandler;

public class QuesterHandler implements QuestHandler {
	private Quester quester;
	
	public QuesterHandler(Quester quester) {
		this.quester = quester;
	}
	
	@Override
	public void beginQuest(Player player, String questName) {
		try {
			getQuestManager().startQuest(player, questName, false);
		} catch (QuesterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void completeObjectives(Player player, String questName,
			List<Integer> objectivesIDs) {
		if (isPerforming(player, questName)) {
			try {
				selectQuest(player, questName);
				Quest quest = getQuestManager().getQuest(questName);
				for (int i : objectivesIDs) {
					if (getQuestManager().isObjectiveActive(player, i)) {
						if (quest.getObjective(i).getType().equalsIgnoreCase("CUSTOM")) {
							getQuestManager().incProgress(player, i);
						}
					}
				}
			} catch (QuesterException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void completeQuest(Player player, String questName) {
		if (isPerforming(player, questName)) {
			try {
				selectQuest(player, questName);
				getQuestManager().completeQuest(player);
			} catch (QuesterException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void cancelQuest(Player player, String questName) {
		if(isPerforming(player, questName)) {
			try {
				selectQuest(player, questName);
				getQuestManager().cancelQuest(player, false);
			} catch (QuesterException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public boolean hasCompletedObjectives(Player player, String questName,
			List<Integer> objectivesIDs) {
		if (isPerforming(player, questName)) {
			try {
				selectQuest(player, questName);
				Quest quest = getQuestManager().getQuest(questName);
				List<Integer> objectivesProgress = getQuesterProfile(player).getProgress();
				for (int obj : objectivesIDs) {
					if (objectivesProgress.get(obj) < quest.getObjective(obj).getTargetAmount()) {
						return false;
					}
				}
				return true;
			} catch (QuesterException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public boolean hasActiveObjectives(Player player, String questName,
			List<Integer> objectivesIDs) {
		if (isPerforming(player, questName)) {
			try {
				selectQuest(player, questName);
			} catch (QuesterException e) {
				e.printStackTrace();
			}
			for (int obj : objectivesIDs) {
				if (!getQuestManager().isObjectiveActive(player, obj)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasCompleted(Player player, String questName) {
		return getQuesterProfile(player).isCompleted(questName);
	}

	@Override
	public boolean isPerforming(Player player, String questName) {
		return getQuesterProfile(player).hasQuest(questName);
	}
	
	private void selectQuest(Player player, String quest) throws QuesterException {
		getQuestManager().selectQuest(player.getName(), getQuestManager().getQuest(quest).getID());
	}
	
	private PlayerProfile getQuesterProfile(Player player) {
		return getQuestManager().getProfile(player.getName());
	}
	
	@SuppressWarnings("static-access")
	private QuestManager getQuestManager() {
		return quester.qMan;
	}
}
