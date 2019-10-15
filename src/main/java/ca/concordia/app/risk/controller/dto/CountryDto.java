package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

public class CountryDto implements Dto {

	private String name;
	private int numberOfArmies;
	private String continentName;

	public CountryDto() {
		super();
	}

	public CountryDto(String name, int numberOfArmies, String contenentName) {
		super();
		this.name = name;
		this.numberOfArmies = numberOfArmies;
		this.continentName = contenentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfArmies() {
		return numberOfArmies;
	}

	public void setNumberOfArmies(int numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String contenentName) {
		this.continentName = contenentName;
	}

	@Override
	public String toString() {
		return "CountryDto [name=" + name + ", numberOfArmies=" + numberOfArmies + ", contenentName=" + continentName
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((continentName == null) ? 0 : continentName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfArmies;
		return result;
	}

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

	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

}
