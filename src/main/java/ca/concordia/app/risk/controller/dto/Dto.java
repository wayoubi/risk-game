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
	 * validates Dto Beans using the javax.validation package
	 * 
	 * @throws ValidationException
	 */
	void validate() throws ValidationException;
}