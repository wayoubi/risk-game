package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

public class CountryDto implements Dto{

	private String name;
	private int numberOfArmies;
	private String contenentName;

	public CountryDto() {
		super();
	}

	public CountryDto(String name, int numberOfArmies, String contenentName) {
		super();
		this.name = name;
		this.numberOfArmies = numberOfArmies;
		this.contenentName = contenentName;
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

	public String getContenentName() {
		return contenentName;
	}

	public void setContenentName(String contenentName) {
		this.contenentName = contenentName;
	}

	@Override
	public String toString() {
		return "CountryDto [name=" + name + ", numberOfArmies=" + numberOfArmies
				+ ", contenentName=" + contenentName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contenentName == null) ? 0 : contenentName.hashCode());
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
		if (contenentName == null) {
			if (other.contenentName != null) {
				return false;
			}
		} else if (!contenentName.equals(other.contenentName)) {
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
