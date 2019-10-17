package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * 
 * ContinentDto
 *
 */
public class ContinentDto implements Dto {

	private int numberOfCountries;
	private String name;
	private int controlValue;

	public ContinentDto() {
		super();
	}

	/**
	 * 
	 * Initialize Class Properties
	 * 
	 * @param numberOfCountries
	 * number of countries
	 * @param name
	 * continent name
	 * @param controlValue
	 * control value
	 */
	public ContinentDto(int numberOfCountries, String name, int controlValue) {
		super();
		this.numberOfCountries = numberOfCountries;
		this.name = name;
		this.controlValue = controlValue;
	}

	/**
	 * gets {@link numberOfCountries}
	 *  
	 * @return numberOfCountries
	 */
	public int getNumberOfCountries() {
		return numberOfCountries;
	}

	/**
	 * sets {@link numberOfCountries}
	 * 
	 * @param numberOfCountries
	 * sets number of countries
	 */
	public void setNumberOfCountries(int numberOfCountries) {
		this.numberOfCountries = numberOfCountries;
	}

	/**
	 * gets {@link name}
	 * 
	 * @return continent name
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets {@link name}
	 * 
	 * @param name
	 * sets continent name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets {@link controlValue}
	 * 
	 * @return controlValue
	 */
	public int getControlValue() {
		return controlValue;
	}

	/**
	 * sets {@link controlValue}
	 * 
	 * @param controlValue
	 * sets control value
	 */
	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	/**
	 * 
	 * @return result
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + controlValue;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfCountries;
		return result;
	}

	/**
	 * 
	 * @param obj
	 * check object is equals
	 * @return true/false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContinentDto other = (ContinentDto) obj;
		if (controlValue != other.controlValue) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		} else if (numberOfCountries != other.numberOfCountries) {
			return false;
		}
		return true;
	}

	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
