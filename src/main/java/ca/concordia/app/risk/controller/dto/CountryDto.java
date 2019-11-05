package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * 
 * CountryDto, used as a place holder across layers
 *
 */
public class CountryDto implements Dto {

	private String name;
	private int numberOfArmies;
	private String continentName;

	/**
	 * Constructor
	 */
	public CountryDto() {
		super();
	}

	/**
	 * Constructor(name, numberOfArmies, continentName)
	 * Initialize Class Properties
	 * 
	 * @param name           country name
	 * @param numberOfArmies number of armies of the country
	 * @param continentName  continent name of the country
	 */
	public CountryDto(String name, int numberOfArmies, String continentName) {
		super();
		this.name = name;
		this.numberOfArmies = numberOfArmies;
		this.continentName = continentName;
	}

	/**
	 * gets {@link name}
	 * 
	 * @return country name
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets {@link name}
	 * 
	 * @param name sets country name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets {@link numberOfArmies}
	 * 
	 * @return number Of Armies
	 */
	public int getNumberOfArmies() {
		return numberOfArmies;
	}

	/**
	 * sets {@link numberOfArmies}
	 * 
	 * @param numberOfArmies sets number of armies
	 */
	public void setNumberOfArmies(int numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}

	/**
	 * gets {@link continentName}
	 * 
	 * @return continent name
	 */
	public String getContinentName() {
		return continentName;
	}

	/**
	 * sets {@link continentName}
	 * 
	 * @param continentName sets continent Name
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * {@inheritDoc}
	 * @return CountryDto [name, numberOfArmies, continentName]
	 */
	@Override
	public String toString() {
		return "CountryDto [name=" + name + ", numberOfArmies=" + numberOfArmies + ", continentName=" + continentName
				+ "]";
	}

	/**
	 * {@inheritDoc}
	 * @return result
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((continentName == null) ? 0 : continentName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfArmies;
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @param obj check object is equals
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
		CountryDto other = (CountryDto) obj;
		if (continentName == null) {
			if (other.continentName != null) {
				return false;
			}
		} else if (!continentName.equals(other.continentName)) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		} else if (numberOfArmies != other.numberOfArmies) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @throws ValidationException exception
	 */
	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

}
