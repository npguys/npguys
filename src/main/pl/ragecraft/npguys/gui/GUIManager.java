package pl.ragecraft.npguys.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.conversation.PlayerMessage;


public class GUIManager {
	private static Map<String, BukkitTask> players = new HashMap<String, BukkitTask>();
	
	public static void showGUI(final Conversation conversation) {
		String playerMsg = NPGuys.getPlugin().getConfig().getString("gui.player.message_format");
		playerMsg = playerMsg.replaceAll("%msg", conversation.getDisplayedMessage().getMessage());
		playerMsg = playerMsg.replaceAll("%player", conversation.getPlayer().getName());
		playerMsg = playerMsg.replaceAll("%npc", conversation.getNPC().getNPC().getName());
		playerMsg = playerMsg.replace('&', '§');
		conversation.getPlayer().sendMessage(playerMsg);
		
		String npcMsg = NPGuys.getPlugin().getConfig().getString("gui.npc.message_format");
		npcMsg = npcMsg.replaceAll("%msg", conversation.getDisplayedMessage().getNPCMessage().getMessage());
		npcMsg = npcMsg.replaceAll("%player", conversation.getPlayer().getName());
		npcMsg = npcMsg.replaceAll("%npc", conversation.getNPC().getNPC().getName());
		npcMsg = npcMsg.replace('&', '§');
		
		final String final_npcMsg = npcMsg;
		players.put(conversation.getPlayer().getName(),
		Bukkit.getScheduler().runTaskLater(NPGuys.getPlugin(), new Runnable() {

			@Override
			public void run() {
				conversation.getPlayer().sendMessage(final_npcMsg);
				updateGUI(conversation);
			}
		}, NPGuys.getPlugin().getConfig().getLong("gui.npc.delay")));
	}
	
	public static void updateGUI(Conversation conversation) {
		
		Scoreboard hud = Bukkit.getScoreboardManager().getNewScoreboard();
		hud.registerNewObjective(ChatColor.UNDERLINE+NPGuys.getPlugin().getConfig().getString("gui.headline"), "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
		
		List<PlayerMessage> possibleResponses = conversation.getPossibleResponses();
		int id = possibleResponses.size();
		
		for(PlayerMessage response : possibleResponses) {
			StringBuilder line = new StringBuilder();
			if(possibleResponses.size()-id+1 == conversation.getChoosenResponse()) {
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
			hud.getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getOfflinePlayer(line.toString())).setScore(id);
			id--;
		}
		conversation.getPlayer().setScoreboard(hud);
	}
	
	public static void closeGUI(Player player) {
		players.get(player.getName()).cancel();
		players.remove(player.getName());
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	}
}
