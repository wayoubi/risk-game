package ca.concordia.app.risk.controller.dto;

import javax.validation.ValidationException;

public class ContinentDto implements Dto {

    private int numberOfCountries;
    private String name;
    private int controlValue;

    public ContinentDto() {
        super();
    }

    public ContinentDto(int numberOfCountries, String name, int controlValue) {
        super();
        this.numberOfCountries = numberOfCountries;
        this.name = name;
        this.controlValue = controlValue;
    }

    /**
     * @return the numberOfCountries
     */
    public int getNumberOfCountries() {
        return numberOfCountries;
    }

    /**
     * @param numberOfCountries the numberOfCountries to set
     */
    public void setNumberOfCountries(int numberOfCountries) {
        this.numberOfCountries = numberOfCountries;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the controlValue
     */
    public int getControlValue() {
        return controlValue;
    }

    /**
     * @param controlValue the controlValue to set
     */
    public void setControlValue(int controlValue) {
        this.controlValue = controlValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + controlValue;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + numberOfCountries;
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
