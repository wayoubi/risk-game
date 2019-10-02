package ca.concordia.app.risk.exceptions;

public class RiskGameRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -24908869442990571L;
	
	/**
	 * 
	 * @param errorMessage
	 */
	public RiskGameRuntimeException(String errorMessage) {
		super(errorMessage);
	}
}
