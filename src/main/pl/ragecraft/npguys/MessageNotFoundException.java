package pl.ragecraft.npguys;

import java.util.HashMap;

public class MessageNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String npguy, message;
	
	public MessageNotFoundException(String npguy, String message) {
		this.npguy = npguy;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
