package pl.ragecraft.npguys;

import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.plugin.java.JavaPlugin;

import pl.ragecraft.npguys.action.AbandonConversation;
import pl.ragecraft.npguys.action.quest.BeginQuest;
import pl.ragecraft.npguys.action.quest.CompleteObjectives;
import pl.ragecraft.npguys.action.quest.CompleteQuest;
import pl.ragecraft.npguys.action.rpgitem.TakeRPGItem;
import pl.ragecraft.npguys.action.rpgitem.GiveRPGItem;
import pl.ragecraft.npguys.commands.NPGuyCommands;
import pl.ragecraft.npguys.requirement.quest.FinishedQuest;
import pl.ragecraft.npguys.requirement.quest.PerformedQuest;
import pl.ragecraft.npguys.requirement.rpgitem.RequiredRPGItem;

public class NPGuys extends JavaPlugin {
	private static NPGuys plugin = null;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		plugin = this;
		
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		ElementsManager.init(this);
		DialoguesManager.init(this);
		
		ElementsManager.registerAction("GIVE_RPGITEM", GiveRPGItem.class);
		ElementsManager.registerAction("TAKE_RPGITEM", TakeRPGItem.class);
		ElementsManager.registerAction("BEGIN_QUEST", BeginQuest.class);
		ElementsManager.registerAction("FINISH_QUEST", CompleteQuest.class);
		ElementsManager.registerAction("COMPLETE_OBJECTIVES", CompleteObjectives.class);
		ElementsManager.registerAction("ABANDON_CONVERSATION", AbandonConversation.class);
		
		ElementsManager.registerRequirement("REQUIRED_RPGITEM", RequiredRPGItem.class);
		ElementsManager.registerRequirement("PERFORMED_QUEST", PerformedQuest.class);
		ElementsManager.registerRequirement("FINISHED_QUEST", FinishedQuest.class);
		
		//TODO Unsafe code
		
		getCommand("npguy").setExecutor(new NPGuyCommands());
		
		ElementsManager.getCitizens().getTraitFactory().registerTrait(TraitInfo.create(NPGuy.class).withName("npguy"));
	}
	
	public static NPGuys getPlugin() {
		return plugin;
	}
	
	@Override
	public void onDisable() {
		DialoguesManager.saveAll();
	}
	
}
