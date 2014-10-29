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

package pl.ragecraft.npguys;

import java.util.HashMap;
import java.util.Map;

import pl.ragecraft.npguys.conversation.PlayerMessage;

public class NPGuyData {
	private String name;
	private boolean active;
	private String welcomeMessage = "default";
	private Map<String,PlayerMessage> dialogues = new HashMap<String,PlayerMessage>();
	
	public NPGuyData(String name, boolean active) {
		this.name = name;
		this.active = active;
	}

	public String getName() {
		return name;
	}
	
	protected void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
	
	public Map<String,PlayerMessage> getDialogues() {
		return dialogues;
	}
}
