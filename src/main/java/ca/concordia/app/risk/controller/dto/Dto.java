package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * 
 * @author i857625
 *
 * @param <T>
 */
public interface Dto {
	
	/**
	 * 
	 * @param t
	 * @throws ValidationException
	 */
	void validate() throws ValidationException;
}