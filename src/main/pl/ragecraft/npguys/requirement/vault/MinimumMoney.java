package pl.ragecraft.npguys.requirement.vault;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import pl.ragecraft.npguys.ElementManager;
import pl.ragecraft.npguys.exception.FailedToLoadException;
import pl.ragecraft.npguys.exception.InvalidCommandException;
import pl.ragecraft.npguys.requirement.Requirement;

public class MinimumMoney extends Requirement {
	private double money;
	
	public MinimumMoney(String type) {
		super(type);
	}

	@Override
	public boolean isMet(NPC npc, Player player) {
		return ElementManager.getEconomy().has(player, money);
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("money")) {
			if (data.get("money") instanceof Integer) {
				money = data.getInt("money");
				return;
			}
			if (data.get("money") instanceof Double) {
				money = data.getDouble("money");
				return;
			}
		}
		throw new FailedToLoadException("Minimum money missing!");
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("Minimum money missing!");
		}
		if (data.length > 1) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		try{
			money = Double.valueOf(data[0]);
		} catch (NumberFormatException e) {
			throw new InvalidCommandException("Invalid format!");
		}
	}

	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("money", money);
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
		return String.valueOf(money);
	}

}
