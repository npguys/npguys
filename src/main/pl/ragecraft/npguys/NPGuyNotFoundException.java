package pl.ragecraft.npguys;

public class NPGuyNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String npguy;
	
	public NPGuyNotFoundException(String npguy) {
		this.npguy = npguy;
	}
	
	@Override
	public String getMessage() {
		return "NPGuy '"+npguy+"' not found!";
	}
}
