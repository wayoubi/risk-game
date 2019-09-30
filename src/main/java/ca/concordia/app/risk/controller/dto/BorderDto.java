package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

public class BorderDto implements Dto {
	
	private String countryName;
	private String neighborCountryName;
	
	/**
	 * 
	 */
	public BorderDto() {
		super();
	}

	/**
	 * 
	 * @param countryName
	 * @param neighborCountryName
	 */
	public BorderDto(String countryName, String neighborCountryName) {
		super();
		this.countryName = countryName;
		this.neighborCountryName = neighborCountryName;
	}

	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the neighborCountryName
	 */
	public String getNeighborCountryName() {
		return neighborCountryName;
	}

	/**
	 * @param neighborCountryName the neighborCountryName to set
	 */
	public void setNeighborCountryName(String neighborCountryName) {
		this.neighborCountryName = neighborCountryName;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result + ((neighborCountryName == null) ? 0 : neighborCountryName.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BorderDto other = (BorderDto) obj;
		if (countryName == null) {
			if (other.countryName != null) {
				return false;
			}
		} else if (!countryName.equals(other.countryName)) {
			return false;
		}
		if (neighborCountryName == null) {
			if (other.neighborCountryName != null) {
				return false;
			}
		} else if (!neighborCountryName.equals(other.neighborCountryName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "BorderDto [countryName=" + countryName + ", neighborCountryName=" + neighborCountryName + "]";
	}
	
	/**
	 * 
	 */
	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub
		
	}

}
