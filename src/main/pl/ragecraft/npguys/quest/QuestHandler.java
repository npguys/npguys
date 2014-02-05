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
