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
import org.bukkit.command.CommandSender;

public abstract class CommandUtils {
	public static final String COMMAND_NOT_RECOGNIZED = "Command not recognized. Check spelling and try again!";
	public static final String TOO_MUCH_ARGUMENTS = "Too much arguments. No idea what to do with them... :(";
	public static final String NOT_ENOUGH_ARGUMENTS = "Not enough arguments.";
	
	public static final String PADDING = "   ";
	
	public static final void reportSuccess(CommandSender sender, String feedback) {
		sender.sendMessage(ChatColor.GREEN.toString()+feedback);
	}
	
	public static final void reportFailure(CommandSender sender, String feedback) {
		sender.sendMessage(ChatColor.RED.toString()+feedback);
	}
	
	public static final void sendFeedback(CommandSender sender, String feedback) {
		sender.sendMessage(feedback);
	}
	
	public static final boolean assertArgsLengthEqual(CommandSender sender, int length, String[] args) {
		return assertArgsLengthRange(sender, length, length, args);
	}
	
	public static final boolean assertArgsLengthRange(CommandSender sender, int minLength, int maxLength, String[] args) {
		return assertMinArgsLength(sender, minLength, args) && assertMaxArgsLength(sender, maxLength, args);
	}
	
	public static final boolean assertMinArgsLength(CommandSender sender, int minLength, String[] args) {
		if(args.length < minLength) {
			reportFailure(sender, NOT_ENOUGH_ARGUMENTS);
			return false;
		}
		return true;
	}
	
	public static final boolean assertMaxArgsLength(CommandSender sender, int maxLength, String[] args) {
		if(args.length > maxLength) {
			reportFailure(sender, TOO_MUCH_ARGUMENTS);
			return false;
		}
		return true;
	}
	
	public static final String joinStrings(String[] array, int firstIndex) {
		return joinStrings(array, firstIndex, array.length-1);
	}
	
	public static final String joinStrings(String[] array, int firstIndex, int lastIndex) {
		lastIndex = Math.min(array.length-1, lastIndex);
		StringBuilder sb = new StringBuilder();
		for(int i = firstIndex; i <= lastIndex; i++) {
			sb.append(" ").append(array[i]);
		}
		return sb.deleteCharAt(0).toString();
	}
}
