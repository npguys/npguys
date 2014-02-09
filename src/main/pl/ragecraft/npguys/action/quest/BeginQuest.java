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

package pl.ragecraft.npguys.action.quest;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;


public class BeginQuest extends Action {
	public BeginQuest(String type) {
		super(type);
	}

	String quest;

	@Override
	public void perform(NPC npc, Player player) {
		ElementManager.getQuestHandler().beginQuest(player, quest);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest UID missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest UID missing!");
		}
		if (data.length > 1) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		quest = data[0];
	}

	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("quest", quest);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getData() {
		return quest;
	}
}
