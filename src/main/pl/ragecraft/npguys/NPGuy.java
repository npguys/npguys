package pl.ragecraft.npguys;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

import org.bukkit.entity.Player;

import pl.ragecraft.npguys.conversation.ConversationManager;
import pl.ragecraft.npguys.conversation.NPCMessage;
import pl.ragecraft.npguys.conversation.PlayerMessage;
import pl.ragecraft.npguys.exception.MessageNotFoundException;
import pl.ragecraft.npguys.requirement.Requirement;


public class NPGuy extends Trait {
	private String uid;
	
	public NPGuy() {
		super("npguy");
	}
	
	@Override
	public void load(DataKey key) {
		uid = key.getString("uid");
		reload();
	}
	
	@Override
	public void save(DataKey key) {
		key.setString("uid", uid);
	}
	
	public List<PlayerMessage> getPossibleResponses(NPCMessage message, Player player) {
		List<PlayerMessage> responseList = new ArrayList<PlayerMessage>();
		
		for(String responseUid : message.getPossibleResponses()) {
			try {
				PlayerMessage response = DialoguesManager.getPlayerMessage(uid, responseUid);
				if (areRequirementsDone(player, response)) {
					responseList.add(response);
				}
			} catch (MessageNotFoundException e) {
				//TODO Print exception to console
			}
		}
		
		if(responseList.isEmpty()) {
			responseList.add(DialoguesManager.getExitMessage());
		}
		
		return responseList;
	}

	private boolean areRequirementsDone(Player player, PlayerMessage response) {
		for (Requirement requirement : response.getRequirements()) {
			if (requirement.isMet(getNPC(), player) == requirement.isReversed()) {
				return false;
			}
		}
		return true;
	}
	
	public String getUID() {
		return uid;
	}

	public void setUID(String uid) {
		this.uid = uid;
		reload();
	}
	
	public void reload() {
		ConversationManager.endConversations(this);
	}
}
