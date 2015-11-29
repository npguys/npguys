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

import org.bukkit.configuration.ConfigurationSection;

import goldob.npguys.conversation.Conversation;
import goldob.npguys.conversation.ConversationManager;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;


public class AbandonConversation extends Action {

	public AbandonConversation(String type) {
		super(type);
	}

	@Override
	public void perform(Conversation context) {
		ConversationManager.endConversation(context.getPlayer());
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {}

	@Override
	public String getDescription() {
		return "Immediately breaks conversation.";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String getData() {
		return "";
	}

	@Override
	public void save(ConfigurationSection data) {}
}
