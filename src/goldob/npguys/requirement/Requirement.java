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

package goldob.npguys.requirement;

import org.bukkit.configuration.ConfigurationSection;

import goldob.npguys.conversation.Conversation;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;


public abstract class Requirement {
	String type;
	boolean reversed = false;
	
	public Requirement(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public abstract boolean isMet(Conversation context);

	public abstract void load(ConfigurationSection data) throws FailedToLoadException;
	
	public abstract void fromCommand(String[] data) throws InvalidCommandException;
	
	public abstract void save(ConfigurationSection data);
	
	public final void saveIt(ConfigurationSection data) {
		data.set("type", getType());
		data.set("reversed", reversed);
		save(data);
	}
	
	public abstract String getDescription();
	
	public abstract String getUsage();
	
	public abstract String getData();
	
	public final void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	public final boolean isReversed() {
		return reversed;
	}
}
