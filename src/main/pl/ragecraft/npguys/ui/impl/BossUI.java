package pl.ragecraft.npguys.ui.impl;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import pl.ragecraft.npguys.conversation.Conversation;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.exception.UIInitializationFailedException;
import pl.ragecraft.npguys.ui.ClassicControlsUI;

public class BossUI extends ClassicControlsUI {
	public void init(ConfigurationSection config) throws UIInitializationFailedException {
		super.init(config);
		if (!Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
			throw new UIInitializationFailedException("BarAPI plugin required");
		}
	}
	
	public BossUI(Conversation conversation) {
		super(conversation);
	}

	@Override
	public void openChoiceView() {
		super.openChoiceView();
		PlayerMessage choosenResponse = getConversation().getPossibleResponses().get(getChoosenResponseIndex());
		float possibleResponsesNumber = (float)getConversation().getPossibleResponses().size();
		BarAPI.setMessage(getConversation().getPlayer(), choosenResponse.getShortcut(), 100.0F*((float)getChoosenResponseIndex()+1.0F)/possibleResponsesNumber);
	}

	@Override
	public void closeChoiceView() {
		BarAPI.removeBar(getConversation().getPlayer());
	}
}
