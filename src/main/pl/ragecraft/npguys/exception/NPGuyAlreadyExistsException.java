package pl.ragecraft.npguys.exception;

public class NPGuyAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String npguy;
	
	public NPGuyAlreadyExistsException(String npguy) {
		this.npguy = npguy;
	}
	
	@Override
	public String getMessage() {
		return "NPGuy '"+npguy+"' already exists!";
	}
}
