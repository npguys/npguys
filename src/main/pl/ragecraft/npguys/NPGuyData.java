package pl.ragecraft.npguys;

import java.util.HashMap;
import java.util.Map;

import pl.ragecraft.npguys.conversation.PlayerMessage;

public class NPGuyData {
	public String name = "";
	
	public String welcomeMessage = "";
	
	public Map<String,PlayerMessage> dialogues = new HashMap<String,PlayerMessage>();
}
