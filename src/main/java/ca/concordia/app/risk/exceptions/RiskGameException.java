package ca.concordia.app.risk.exceptions;


/**
 * 
 * @author i857625
 *
 */
public class RiskGameException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3506929910583222530L;

	/**
	 * 
	 * @param errorMessage
	 */
	public RiskGameException(String errorMessage) {
		super(errorMessage);
	}
}
