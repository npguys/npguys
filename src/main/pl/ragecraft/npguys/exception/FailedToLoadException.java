package pl.ragecraft.npguys.exception;

public class FailedToLoadException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	
	public FailedToLoadException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}