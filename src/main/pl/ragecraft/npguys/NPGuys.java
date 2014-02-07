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
		
		ElementManager.init(this);
		DialogueManager.init(this);
		
		ElementManager.registerAction("GIVE_RPGITEM", GiveRPGItem.class);
		ElementManager.registerAction("TAKE_RPGITEM", TakeRPGItem.class);
		ElementManager.registerAction("BEGIN_QUEST", BeginQuest.class);
		ElementManager.registerAction("FINISH_QUEST", CompleteQuest.class);
		ElementManager.registerAction("COMPLETE_OBJECTIVES", CompleteObjectives.class);
		ElementManager.registerAction("ABANDON_CONVERSATION", AbandonConversation.class);
		
		ElementManager.registerRequirement("REQUIRED_RPGITEM", RequiredRPGItem.class);
		ElementManager.registerRequirement("PERFORMED_QUEST", PerformedQuest.class);
		ElementManager.registerRequirement("FINISHED_QUEST", FinishedQuest.class);
		
		//TODO Unsafe code
		
		getCommand("npguy").setExecutor(new NPGuyCommands());
		
		ElementManager.getCitizens().getTraitFactory().registerTrait(TraitInfo.create(NPGuy.class).withName("npguy"));
	}
	
	public static NPGuys getPlugin() {
		return plugin;
	}
	
	@Override
	public void onDisable() {
		DialogueManager.saveAll();
	}
	
}
