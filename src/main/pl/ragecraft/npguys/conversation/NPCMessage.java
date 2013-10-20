package pl.ragecraft.npguys.conversation;

import java.util.ArrayList;
import java.util.List;

public class NPCMessage {
	private String message;
	private List<String> possibleResponses = new ArrayList<String>();
	
	public NPCMessage(String msg, List<String> possibleResponses) {
		message = msg;
		this.possibleResponses = possibleResponses;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<String> getPossibleResponses() {
		return possibleResponses;
	}
}
