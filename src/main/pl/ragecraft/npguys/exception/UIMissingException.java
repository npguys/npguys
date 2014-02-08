package pl.ragecraft.npguys.exception;

public class UIMissingException extends ElementMissingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String ui;
	
	public UIMissingException(String ui) {
		this.ui = ui;
	}
	
	public String getMessage() {
		return "UI type '"+ui+"' not found!";
	}

}
