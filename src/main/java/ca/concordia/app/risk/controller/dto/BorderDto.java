package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

public class BorderDto implements Dto {

	private String countryName;
	private String neighborCountryName;

	/**
	 * Constructor()
	 */
	public BorderDto() {
		super();
	}

	/**
	 * Constructor(countryName, neighborCountryName)
	 * 
	 * @param (countryName)
	 * @param (neighborCountryName)
	 */
	public BorderDto(String countryName, String neighborCountryName) {
		super();
		this.countryName = countryName;
		this.neighborCountryName = neighborCountryName;
	}

	/**
	 * getter: the countryName
	 * @return 
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * setter: countryName the countryName to set
	 * @param (countryName)
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * getter: the neighborCountryName
	 * @return 
	 */
	public String getNeighborCountryName() {
		return neighborCountryName;
	}

	/**
	 * setter: neighborCountryName the neighborCountryName to set
	 * @param (neighborCountryName)
	 */
	public void setNeighborCountryName(String neighborCountryName) {
		this.neighborCountryName = neighborCountryName;
	}

	/**
	 * Override hashCode method, since 'equal' method is overridden
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
	 * Override 'equals' method:
	 * To make sure for 'Country' & 'Neighbor Country', it's only equal if current objects are the same, not only the name of strings.
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
		
		//countryName
		if (countryName == null) {
			if (other.countryName != null) {
				return false;
			}
		} else if (!countryName.equals(other.countryName)) {
			return false;
		}
		
		//neighborCountryName
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
	 * Override toString method
	 * @return current player's: ['countryName' & 'neighborCountryName']
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
