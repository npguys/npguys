package pl.ragecraft.npguys.quester;

import java.util.List;

import org.bukkit.entity.Player;

import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.PluginManager;

import com.gmail.molnardad.quester.ActionSource;
import com.gmail.molnardad.quester.lang.QuesterLang;
import com.gmail.molnardad.quester.profiles.PlayerProfile;
import com.gmail.molnardad.quester.profiles.ProfileManager;
import com.gmail.molnardad.quester.exceptions.QuesterException;
import com.gmail.molnardad.quester.quests.QuestManager;

public class QuesterManager {
	
	public static boolean hasCompleted(Player player, String quest) {
		return getQuesterProfile(player).isCompleted(quest);
	}
	
	public static boolean hasActiveObjs(Player player, String quest, List<Integer> objectives) {
		if (isPerforming(player, quest)) {
			try {
				selectQuest(player, quest);
			} catch (QuesterException e) {
				e.printStackTrace();
			}
			for (int obj : objectives) {
				if (!getProfileManager().isObjectiveActive(getQuesterProfile(player), obj)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean isPerforming(Player player, String quest) {
		return getQuesterProfile(player).hasQuest(quest);
	}
	
	public static void beginQuest(Player player, String quest) {
		try {
			getProfileManager().startQuest(player, quest, ActionSource.otherSource(NPGuys.getPlugin()), getLang(player));
		} catch (QuesterException e) {
			e.printStackTrace();
		}
	}

	public static void completeQuest(Player player, String quest) {
		if (isPerforming(player, quest)) {
			try {
				selectQuest(player, quest);
				getProfileManager().completeQuest(player, ActionSource.otherSource(NPGuys.getPlugin()), getLang(player));
			} catch (QuesterException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void completeObjs(Player player, String quest, List<Integer> objectives) {
		if (isPerforming(player, quest)) {
			try {
				selectQuest(player, quest);
			} catch (QuesterException e) {
				e.printStackTrace();
			}
			for (int i : objectives) {
				if (getProfileManager().isObjectiveActive(getQuesterProfile(player), i)) {
					if (getQuestManager().getQuest(quest).getObjective(i).getType().equalsIgnoreCase("CUSTOM")) {
						getProfileManager().incProgress(player, ActionSource.otherSource(NPGuys.getPlugin()), i);
					}
				}
			}
		}
	}
	
	private static void selectQuest(Player player, String quest) throws QuesterException {
		getProfileManager().selectQuest(getQuesterProfile(player), getQuestManager().getQuest(quest));
	}
	
	private static PlayerProfile getQuesterProfile(Player player) {
		return PluginManager.getQuester().getProfileManager().getProfile(player.getName());
	}
	
	private static QuestManager getQuestManager() {
		return PluginManager.getQuester().getQuestManager();
	}
	
	private static ProfileManager getProfileManager() {
		return PluginManager.getQuester().getProfileManager();
	}
	
	private static QuesterLang getLang(Player player) {
		return PluginManager.getQuester().getLanguageManager().getPlayerLang(player.getName());
	}
}
