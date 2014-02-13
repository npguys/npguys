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

package pl.ragecraft.npguys;

import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.plugin.java.JavaPlugin;

import pl.ragecraft.npguys.action.AbandonConversation;
import pl.ragecraft.npguys.action.GivePermission;
import pl.ragecraft.npguys.action.RunCommand;
import pl.ragecraft.npguys.action.TakePermission;
import pl.ragecraft.npguys.action.quest.BeginQuest;
import pl.ragecraft.npguys.action.quest.CompleteObjectives;
import pl.ragecraft.npguys.action.quest.CompleteQuest;
import pl.ragecraft.npguys.action.rpgitem.TakeRPGItem;
import pl.ragecraft.npguys.action.rpgitem.GiveRPGItem;
import pl.ragecraft.npguys.commands.NPGuyCommands;
import pl.ragecraft.npguys.requirement.RequiredPermission;
import pl.ragecraft.npguys.requirement.quest.ActiveObjectives;
import pl.ragecraft.npguys.requirement.quest.CompletedObjectives;
import pl.ragecraft.npguys.requirement.quest.FinishedQuest;
import pl.ragecraft.npguys.requirement.quest.PerformedQuest;
import pl.ragecraft.npguys.requirement.rpgitem.RequiredRPGItem;
import pl.ragecraft.npguys.ui.impl.ScoreboardUI;

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
		
		ElementManager.registerUI("SCOREBOARD", ScoreboardUI.class);
		
		ElementManager.registerAction("RUN_COMMAND", RunCommand.class);
		ElementManager.registerAction("GIVE_PERMISSION", GivePermission.class);
		ElementManager.registerAction("TAKE_PERMISSION", TakePermission.class);
		ElementManager.registerAction("GIVE_RPGITEM", GiveRPGItem.class);
		ElementManager.registerAction("TAKE_RPGITEM", TakeRPGItem.class);
		ElementManager.registerAction("BEGIN_QUEST", BeginQuest.class);
		ElementManager.registerAction("FINISH_QUEST", CompleteQuest.class);
		ElementManager.registerAction("COMPLETE_OBJECTIVES", CompleteObjectives.class);
		ElementManager.registerAction("ABANDON_CONVERSATION", AbandonConversation.class);
		
		ElementManager.registerRequirement("PERMISSION", RequiredPermission.class);
		ElementManager.registerRequirement("REQUIRED_RPGITEM", RequiredRPGItem.class);
		ElementManager.registerRequirement("PERFORMED_QUEST", PerformedQuest.class);
		ElementManager.registerRequirement("FINISHED_QUEST", FinishedQuest.class);
		ElementManager.registerRequirement("ACTIVE_OBJECTIVES", ActiveObjectives.class);
		ElementManager.registerRequirement("COMPLETED_OBJECTIVES", CompletedObjectives.class);
		
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
