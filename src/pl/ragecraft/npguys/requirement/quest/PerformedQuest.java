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

package pl.ragecraft.npguys.requirement.quest;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;
import pl.ragecraft.npguys.requirement.Requirement;


public class PerformedQuest extends Requirement {
	String quest;
	List<Integer> objectives = new ArrayList<Integer>();
	
	public PerformedQuest(String type) {
		super(type);
	}
	
	@Override
	public boolean isMet(NPC npc, Player player) {
		if(ElementManager.getQuestHandler().isPerforming(player, quest)) {
			return ElementManager.getQuestHandler().hasActiveObjectives(player, quest, objectives);
		}
		return false;
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("quest") && data.get("quest") instanceof String) {
			quest = data.getString("quest");
		}
		else {
			throw new FailedToLoadException("Quest UID missing!");
		}
		if (data.contains("objectives")) {
			if (data.get("objectives") instanceof List<?>) {
				data.getIntegerList("objectives");
			}
			else {
				throw new FailedToLoadException("Invalid objectives! Objectives must be a valid integer list!");
			}
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Quest UID missing!");
		}
		quest = data[0];
		for (int i = 1; i < data.length; i++) {
			try {
				objectives.add(Integer.valueOf(data[i]));
			}
			catch(NumberFormatException e) {
				throw new InvalidCommandException("Invalid quest objective! Objective must be a valid integer!");
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
		String str = quest;
		if(!objectives.isEmpty()) {
			str=str+": ";
		}
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