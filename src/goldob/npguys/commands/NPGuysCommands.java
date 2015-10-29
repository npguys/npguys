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

package goldob.npguys.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import goldob.npguys.DialogueManager;
import goldob.npguys.ElementManager;
import goldob.npguys.NPGuys;
import goldob.npguys.conversation.ConversationManager;

public class NPGuysCommands extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] arguments) {
		/*if(arguments.length == 0) {
			// TODO Command list
			return true;
		}*/
		if(assertArgsLengthEqual(sender, 1, arguments)) {
			switch(arguments[0].toLowerCase()) {
				case "reload":
					sendFeedback(sender, "Reloading NPGuys data from YML files...");
					ConversationManager.endAll();
					
					ElementManager.reload(NPGuys.getPlugin());
					DialogueManager.reload();
					break;
				case "save":
					sendFeedback(sender, "Saving NPGuys data to YML files...");
				
					DialogueManager.saveAll();
					break;
				case "requirements":
					sendFeedback(sender, ElementManager.generateRequirementsList());
					break;
			case "actions":
					sendFeedback(sender, ElementManager.generatActionsList());
					break;
				default:
				reportFailure(sender, COMMAND_NOT_RECOGNIZED);			
			}
		}
		return true;
	}
}
