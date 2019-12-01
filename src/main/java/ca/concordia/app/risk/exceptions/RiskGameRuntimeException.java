package ca.concordia.app.risk.exceptions;

/**
 * RiskGameRuntimeException Run time exception
 */
public class RiskGameRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -24908869442990571L;

	/**
	 * This method is for games exception handling
	 * 
	 * @param errorMessage error message
	 */
	public RiskGameRuntimeException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * This method is for games run time exception handling
	 * 
	 * @param errorMessage error message
	 * @param exception    exception
	 */
	public RiskGameRuntimeException(String errorMessage, Exception exception) {
		super(errorMessage, exception);
	}
}
