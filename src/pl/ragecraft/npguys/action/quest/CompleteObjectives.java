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

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;

public class CompleteObjectives extends Action {
	private String quest;
	private List<Integer> objectives = new ArrayList<Integer>();
	
	public CompleteObjectives(String type) {
		super(type);
	}
	
	@Override
	public void perform(NPC npc, Player player) {
		ElementManager.getQuestHandler().completeObjectives(player, quest, objectives);	
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest name missing!");
		}
		if (data.contains("objectives") && data.get("objectives") instanceof List<?>) {
			objectives.addAll(data.getIntegerList("objectives"));
			if (objectives.size() < 1) {
				throw new FailedToLoadException("Quest objectives missing!");
			}
		}
		else {
			throw new FailedToLoadException("Quest objectives missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest name missing!");
		}
		if (data.length < 2) {
			throw new InvalidCommandException("Quest objectives missing!");
		}
		quest = data[0];
		for (int i = 1; i < data.length; i++) {
			try {
				objectives.add(Integer.valueOf(data[i]));
			}
			catch(NumberFormatException e) {
				throw new InvalidCommandException("Invalid objectives format. Objectives must be a valid set of integers");
			}
		}
	}

	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("quest", quest);
		data.set("objectives", objectives);
	}

	@Override
	public String getDescription() {
		return "Completes quest objectives.";
	}

	@Override
	public String getUsage() {
		return "[quest] [obj1] (obj2, obj3,...)";
	}

	@Override
	public String getData() {
		String str = quest+": ";
		boolean isFirst = true;
		for (int objective : objectives) {
			if(!isFirst) {
				str = str + ", ";
			}
			str = str + String.valueOf(objective);
			isFirst = false;
		}
		return str;
	}
}
