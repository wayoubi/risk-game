package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

public class ContinentDto implements Dto {

	private int numberOfCountries;
	private String name;
	private int controlValue;

	/**
	 * Constructor()
	 */
	public ContinentDto() {
		super();
	}

	/**
	 * Constructor(numberOfCountries, continentName, )
	 */
	public ContinentDto(int numberOfCountries, String name, int controlValue) {
		super();
		this.numberOfCountries = numberOfCountries;
		this.name = name;
		this.controlValue = controlValue;
	}

	/**
	 * getter: the numberOfCountries
	 * @return 
	 */
	public int getNumberOfCountries() {
		return numberOfCountries;
	}

	/**
	 * setter: numberOfCountries the numberOfCountries to set
	 * @param (numberOfCountries)
	 */
	public void setNumberOfCountries(int numberOfCountries) {
		this.numberOfCountries = numberOfCountries;
	}

	/**
	 * getter: the continentName
	 * @return 
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter: the continentName
	 * @param (continentName)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter: the controlValue
	 * @return 
	 */
	public int getControlValue() {
		return controlValue;
	}

	/**
	 * setter: controlValue the controlValue to set
	 * @param ()
	 */
	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	/**
	 * Override hashCode method, since 'equal' method is overridden
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
	 * Override 'equals' method:
	 * To make sure for 'numberOfCountries', it's only equal if current objects are the same, not only the name of strings.
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
		
		//controlValue
		if (controlValue != other.controlValue) {
			return false;
		}
		
		//Continent name & numberOfCountries
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
