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

package pl.ragecraft.npguys.action;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.conversation.ConversationManager;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;


public class AbandonConversation extends Action {

	public AbandonConversation(String type) {
		super(type);
	}

	@Override
	public void perform(NPC npc, Player player) {
		ConversationManager.endConversation(player);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		//Do nothing
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		//Do nothing
	}

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
}
