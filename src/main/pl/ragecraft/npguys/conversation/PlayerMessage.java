package pl.ragecraft.npguys.conversation;

import java.util.ArrayList;
import java.util.List;

import pl.ragecraft.npguys.action.Action;
import pl.ragecraft.npguys.requirement.Requirement;


public class PlayerMessage {
	private String shortcut;
	private String message;
	private NPCMessage response;
	private List<Action> actions = new ArrayList<Action>();
	private List<Requirement> requirements = new ArrayList<Requirement>();
	
	public PlayerMessage(String shortcut, String message, NPCMessage response, List<Requirement> requirements, List<Action> actions) {
		this.shortcut = shortcut;
		this.message = message;
		this.response = response;
		this.requirements = requirements;
		this.actions = actions;
	}
	
	public String getShortcut(){
		return shortcut;
	}
	
	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}
	
	public NPCMessage getNPCMessage() {
		return response;
	}
	
	public List<Requirement> getRequirements() {
		return requirements;
	}
	
	public List<Action> getActions() {
		return actions;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
