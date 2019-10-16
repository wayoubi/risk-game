package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * 
 * @author i857625
 *
 */
public interface Dto {

	void validate() throws ValidationException;
}