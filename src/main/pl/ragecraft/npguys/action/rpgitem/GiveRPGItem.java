package pl.ragecraft.npguys.action.rpgitem;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.ragecraft.npguys.FailedToLoadException;
import pl.ragecraft.npguys.InvalidCommandException;
import pl.ragecraft.npguys.action.Action;

import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;


public class GiveRPGItem extends Action {
	RPGItem item;
	int amount = 1;
	
	public GiveRPGItem(String type) {
		super(type);
	}

	@Override
	public void perform(NPC npc, Player player) {
		ItemStack toGive = item.toItemStack("en_GB");
		toGive.setAmount(amount);
		player.getInventory().addItem(toGive);	
	}

	@Override
	public void load(ConfigurationSection data) throws FailedToLoadException {
		if (data.contains("item") && data.get("item") instanceof String) {
			item = ItemManager.getItemByName(data.getString("item"));
			if (item == null) {
				throw new FailedToLoadException("RPG Item not found!");
			}
		}
		else {
			throw new FailedToLoadException("RPG Item name missing!");
		}
		if (data.contains("amount")) {
			if (data.get("amount") instanceof Integer) {
				amount = data.getInt("amount");
			}
			else {
				throw new FailedToLoadException("Invalid RPG Item amount! Amount must be a valid integer!");
			}
		}
	}

	@Override
	public void fromCommand(String[] data) throws InvalidCommandException {
		if (data.length < 1) {
			throw new InvalidCommandException("RPG Item name missing!");
		}
		if (data.length > 2) {
			throw new InvalidCommandException("Too long command syntax!");
		}
		item = ItemManager.getItemByName(data[0]);
		if (item == null) {
			throw new InvalidCommandException("RPG Item not found!");
		}
		try {
			if (data.length == 2) amount = Integer.valueOf(data[1]);
		}
		catch(NumberFormatException e) {
			throw new InvalidCommandException("Invalid RPG Item amount! Amount must be a valid integer!");
		}
	}
	
	@Override
	public void save(ConfigurationSection data) {
		super.save(data);
		data.set("item", item.getName());
		data.set("amount", amount);
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
		return item+" x"+String.valueOf(amount);
	}

}
