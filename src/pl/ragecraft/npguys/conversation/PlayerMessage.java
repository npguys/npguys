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

package pl.ragecraft.npguys.conversation;

import java.util.ArrayList;
import java.util.List;

import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.requirement.Requirement;


public class PlayerMessage {
	private String dialogueName;
	private String shortcut;
	private String message;
	private NPCMessage response;
	private List<Action> actions = new ArrayList<Action>();
	private List<Requirement> requirements = new ArrayList<Requirement>();
	
	public PlayerMessage(String dialogueName, String shortcut, String message, NPCMessage response, List<Requirement> requirements, List<Action> actions) {
		this.dialogueName = dialogueName;
		this.shortcut = shortcut;
		this.message = message;
		this.response = response;
		this.requirements = requirements;
		this.actions = actions;
	}
	
	public String getName() {
		return dialogueName;
	}
	
	public String getShortcut(){
		return shortcut;
	}
	
	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}
	
	public NPCMessage getNPCMessage() {
		return response;
	}
	
	public List<Requirement> getRequirements() {
		return requirements;
	}
	
	public List<Action> getActions() {
		return actions;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
