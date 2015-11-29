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

package goldob.npguys.requirement.quest;

import org.bukkit.configuration.ConfigurationSection;

import goldob.npguys.ElementManager;
import goldob.npguys.conversation.Conversation;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;
import goldob.npguys.requirement.Requirement;


public class FinishedQuest extends Requirement {
	String quest;
	
	public FinishedQuest(String type) {
		super(type);
	}

	@Override
	public boolean isMet(Conversation context) {
		return ElementManager.getQuestHandler().hasCompleted(context.getPlayer(), quest);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = (String) data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest name missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest name missing!");
		}
		if (data.length > 1) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		quest = data[0];
	}

	@Override
	public void save(ConfigurationSection data) {
		data.set("quest", quest);
	}

	@Override
	public String getDescription() {
		return "Checks if the player has finished certain quest, at least once.";
	}

	@Override
	public String getUsage() {
		return "[quest]";
	}

	@Override
	public String getData() {
		return quest;
	}	
}