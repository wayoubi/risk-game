package ca.concordia.app.risk.exceptions;

/**
 * RiskGameException checked exception
 * @author i857625
 *
 */
public class RiskGameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3506929910583222530L;

	/**
	 * This method is for games exception handling
	 * @param errorMessage error message
	 */
	public RiskGameException(String errorMessage) {
		super(errorMessage);
	}
}
