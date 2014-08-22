package pl.ragecraft.npguys.requirement.heroes;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;
import pl.ragecraft.npguys.requirement.Requirement;

public class RequiredHeroClass extends Requirement {
	private String className;
	
	public RequiredHeroClass(String type) {
		super(type);
	}

	@Override
	public boolean isMet(NPC npc, Player player) {
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
		super.save(data);
		data.set("class", className);
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
		return className;
	}

}
