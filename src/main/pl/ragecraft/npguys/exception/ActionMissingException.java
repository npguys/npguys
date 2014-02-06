package pl.ragecraft.npguys.exception;

public class ActionMissingException extends ElementMissingException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String action;
	
	public ActionMissingException(String action) {
		this.action = action;
	}
	
	public String getMessage() {
		return "Action '"+action+"' not found!";
	}
}
