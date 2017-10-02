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

package goldob.npguys.action;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import goldob.npguys.NPGuys;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;

public class RunCommand extends Action {
	private String command;
	
	public RunCommand(String type) {
		super(type);
	}

	@Override
	public void perform(NPC npc, Player player) {
		NPGuys.getPlugin().getServer().dispatchCommand(NPGuys.getPlugin().getServer().getConsoleSender(), command.replaceAll("%player", player.getName()));
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("command") && data.get("command") instanceof String) {
			command = data.getString("command");
		}
		else {
			throw new FailedToLoadException("Command missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if(data.length < 1) {
			throw new InvalidCommandException("Command missing!");
		} else {
			command = "";
			for(String str : data) {
				command += (str + " ");
			}
		}
	}
	
	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("command", command);
	}

	@Override
	public String getDescription() {
		return "Executes specified console command.";
	}

	@Override
	public String getUsage() {
		return "[cmd]";
	}

	@Override
	public String getData() {
		return command;
	}

}
