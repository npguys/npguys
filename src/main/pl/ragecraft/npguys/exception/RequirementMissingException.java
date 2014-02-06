package pl.ragecraft.npguys.exception;

public class RequirementMissingException extends ElementMissingException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String requirement;
	
	public RequirementMissingException(String requirement) {
		this.requirement = requirement;
	}
	
	public String getMessage() {
		return "Requirement '"+requirement+"' not found!";
	}
}
