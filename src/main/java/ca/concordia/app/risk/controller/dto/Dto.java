package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * @param <T>
 * @author i857625
 */
public interface Dto {

    /**
     * @param t
     * @throws ValidationException
     */
    void validate() throws ValidationException;
}