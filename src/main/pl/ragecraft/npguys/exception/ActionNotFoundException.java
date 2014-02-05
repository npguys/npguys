package pl.ragecraft.npguys.exception;

public class ActionNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String action;
	
	public ActionNotFoundException(String action) {
		this.action = action;
	}
	
	public String getMessage() {
		return "Action '"+action+"' not found!";
	}
}
