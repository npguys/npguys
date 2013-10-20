package pl.ragecraft.npguys;

public class NPGuyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String npguy;
	
	public NPGuyExistsException(String npguy) {
		this.npguy = npguy;
	}
	
	@Override
	public String getMessage() {
		return "NPGuy '"+npguy+"' already exists!";
	}
}
