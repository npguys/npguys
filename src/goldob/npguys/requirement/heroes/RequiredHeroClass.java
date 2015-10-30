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

package goldob.npguys.requirement.heroes;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import goldob.npguys.ElementManager;
import goldob.npguys.exception.FailedToLoadException;
import goldob.npguys.exception.InvalidCommandException;
import goldob.npguys.requirement.Requirement;

public class RequiredHeroClass extends Requirement {
	private String className;
	
	public RequiredHeroClass(String type) {
		super(type);
	}

	@Override
	public boolean isMet(Player player) {
		return ElementManager.getHeroesCharacterManager().getHero(player).getHeroClass()
				.getName().equalsIgnoreCase(className);
		}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("class") && data.get("class") instanceof String) {
			className = data.getString("class");
		}
		else {
			throw new FailedToLoadException("Class name missing!");
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Class name missing!");
		}
		if (data.length > 1) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		className = data[0];
	}
	
	@Override
	public void save(ConfigurationSection data) {
		data.set("class", className);
	}

	@Override
	public String getDescription() {
		return "Checks if player has the required Heroes class.";
	}

	@Override
	public String getUsage() {
		return "[class]";
	}

	@Override
	public String getData() {
		return className;
	}

}
