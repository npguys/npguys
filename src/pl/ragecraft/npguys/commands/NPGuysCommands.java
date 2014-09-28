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

package pl.ragecraft.npguys.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import pl.ragecraft.npguys.DialogueManager;
import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.NPGuys;
import pl.ragecraft.npguys.conversation.ConversationManager;

public class NPGuysCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		switch(args.length) {
		case 0:
			return false;
		case 1:
			if(args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage(ChatColor.GREEN.toString()+"Reloading NPGuys data...");
				
				ConversationManager.endAll();
				
				ElementManager.reload(NPGuys.getPlugin());
				DialogueManager.reload();
				return true;
			}
			if(args[0].equalsIgnoreCase("save")) {
				sender.sendMessage(ChatColor.GREEN.toString()+"Saving NPGuys data...");
				
				DialogueManager.saveAll();
				return true;
			}
		default:
			return false;
		}
	}
	
}
