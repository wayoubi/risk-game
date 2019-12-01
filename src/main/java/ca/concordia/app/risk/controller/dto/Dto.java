package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * 
 * Dto Marker Interface
 * 
 * @author i857625
 *
 */
public interface Dto {

	/**
	 * This method validates Dto Beans using the javax.validation package
	 * 
	 * @throws ValidationException exception
	 */
	void validate() throws ValidationException;
}