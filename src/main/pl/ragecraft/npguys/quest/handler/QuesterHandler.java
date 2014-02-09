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

import com.gmail.molnardad.quester.ActionSource;
import com.gmail.molnardad.quester.Quester;
import com.gmail.molnardad.quester.exceptions.QuesterException;
import com.gmail.molnardad.quester.lang.QuesterLang;
import com.gmail.molnardad.quester.profiles.PlayerProfile;
import com.gmail.molnardad.quester.profiles.ProfileManager;
import com.gmail.molnardad.quester.quests.QuestManager;

import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.quest.QuestHandler;

public class QuesterHandler implements QuestHandler {
	private Quester quester;
	
	public QuesterHandler(Quester quester) {
		this.quester = quester;
	}
	
	@Override
	public void beginQuest(Player player, String questName) {
		try {
			getProfileManager().startQuest(player, questName, ActionSource.otherSource(NPGuys.getPlugin()), getLang(player));
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
			} catch (QuesterException e) {
				e.printStackTrace();
			}
			for (int i : objectivesIDs) {
				if (getProfileManager().isObjectiveActive(getQuesterProfile(player), i)) {
					if (getQuestManager().getQuest(questName).getObjective(i).getType().equalsIgnoreCase("CUSTOM")) {
						getProfileManager().incProgress(player, ActionSource.otherSource(NPGuys.getPlugin()), i);
					}
				}
			}
		}
	}

	@Override
	public void completeQuest(Player player, String questName) {
		if (isPerforming(player, questName)) {
			try {
				selectQuest(player, questName);
				getProfileManager().completeQuest(player, ActionSource.otherSource(NPGuys.getPlugin()), getLang(player));
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
				quester.getProfileManager().cancelQuest(player, ActionSource.otherSource(NPGuys.getPlugin()), getLang(player));
			} catch (QuesterException e) {
				e.printStackTrace();
			}
			
		}
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
				if (!getProfileManager().isObjectiveActive(getQuesterProfile(player), obj)) {
					return false;
				}
			}
		}
		return true;
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
		getProfileManager().selectQuest(getQuesterProfile(player), getQuestManager().getQuest(quest));
	}
	
	private PlayerProfile getQuesterProfile(Player player) {
		return quester.getProfileManager().getProfile(player.getName());
	}
	
	private QuestManager getQuestManager() {
		return quester.getQuestManager();
	}
	
	private ProfileManager getProfileManager() {
		return quester.getProfileManager();
	}
	
	private QuesterLang getLang(Player player) {
		return quester.getLanguageManager().getPlayerLang(player.getName());
	}
}
