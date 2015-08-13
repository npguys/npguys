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

package pl.ragecraft.npguys.requirement;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.npc.NPC;
import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;

public class RequiredSkill extends Requirement {
	private String skill;
	
	public RequiredSkill(String type) {
		super(type);
	}

	@Override
	public boolean isMet(NPC npc, Player player) {
		return ElementManager.getSkillHandler().hasSkill(player, skill);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("skill") && data.get("skill") instanceof String) {
			skill = data.getString("skill");
		}
		else {
			throw new FailedToLoadException("Skill name missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Skill name missing!");
		}
		if (data.length > 1) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		skill = data[0];
	}
	
	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("skill", skill);
	}
	
	@Override
	public String getDescription() {
		return "Checks if player has access to the required SkillAPI or Heroes skill.";
	}

	@Override
	public String getUsage() {
		return "[skill]";
	}

	@Override
	public String getData() {
		return skill;
	}

}
