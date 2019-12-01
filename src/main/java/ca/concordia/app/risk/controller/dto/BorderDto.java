package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

/**
 * BorderDto, used as a place holder across layers
 */
public class BorderDto implements Dto {

	private String countryName;
	private String neighborCountryName;

	/**
	 * Constructor
	 */
	public BorderDto() {
		super();
	}

	/**
	 * Constructor(countryName, neighborCountryName)
	 * @param countryName         country name
	 * @param neighborCountryName neighbor country name
	 */
	public BorderDto(String countryName, String neighborCountryName) {
		super();
		this.countryName = countryName;
		this.neighborCountryName = neighborCountryName;
	}

	/**
	 * gets {@link countryName}
	 * 
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * sets {@link countryName}
	 * 
	 * @param countryName sets country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * gets {@link neighborCountryName}
	 * 
	 * @return neighborCountryName
	 */
	public String getNeighborCountryName() {
		return neighborCountryName;
	}

	/**
	 * sets {@link neighborCountryName}
	 * 
	 * @param neighborCountryName neighbor country name
	 */
	public void setNeighborCountryName(String neighborCountryName) {
		this.neighborCountryName = neighborCountryName;
	}

	/**
	 * {@inheritDoc}
	 * @return result
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
	 * {@inheritDoc}
	 * @param obj check object is equals
	 * @return true/false
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
	 * {@inheritDoc}
	 * @return BorderDto [countryName, neighborCountryName]
	 */
	@Override
	public String toString() {
		return "BorderDto [countryName=" + countryName + ", neighborCountryName=" + neighborCountryName + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

}
