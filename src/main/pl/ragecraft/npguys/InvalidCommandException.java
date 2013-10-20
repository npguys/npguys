package pl.ragecraft.npguys;

public class InvalidCommandException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	
	public InvalidCommandException(String message) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessage() {
		return message;
	}
}
