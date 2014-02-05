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
		
		NPGuyManager.init(this);
		PluginsManager.init(this);
		
		NPGuyManager.registerAction("GIVE_RPGITEM", GiveRPGItem.class);
		NPGuyManager.registerAction("TAKE_RPGITEM", TakeRPGItem.class);
		NPGuyManager.registerAction("BEGIN_QUEST", BeginQuest.class);
		NPGuyManager.registerAction("FINISH_QUEST", CompleteQuest.class);
		NPGuyManager.registerAction("COMPLETE_OBJECTIVES", CompleteObjectives.class);
		NPGuyManager.registerAction("ABANDON_CONVERSATION", AbandonConversation.class);
		
		NPGuyManager.registerRequirement("REQUIRED_RPGITEM", RequiredRPGItem.class);
		NPGuyManager.registerRequirement("PERFORMED_QUEST", PerformedQuest.class);
		NPGuyManager.registerRequirement("FINISHED_QUEST", FinishedQuest.class);
		
		//TODO Unsafe code
		
		getCommand("npguy").setExecutor(new NPGuyCommands());
		
		PluginsManager.getCitizens().getTraitFactory().registerTrait(TraitInfo.create(NPGuy.class).withName("npguy"));
	}
	
	public static NPGuys getPlugin() {
		return plugin;
	}
	
	@Override
	public void onDisable() {
		NPGuyManager.saveAll();
	}
	
}
