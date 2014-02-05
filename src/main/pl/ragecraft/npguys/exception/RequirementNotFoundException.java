package pl.ragecraft.npguys.exception;

public class RequirementNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String requirement;
	
	public RequirementNotFoundException(String requirement) {
		this.requirement = requirement;
	}
	
	public String getMessage() {
		return "Requirement '"+requirement+"' not found!";
	}
}
