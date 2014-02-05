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
