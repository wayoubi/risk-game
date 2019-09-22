package ca.concordia.app.risk.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author i857625
 *
 */
public class GameStarterDto implements Dto {

	/**
	 * 
	 */
	private boolean autoSave;

	/**
	 * 
	 */
	@Min(value = 2, message = "Number of Players should not be less than 2")
	@Max(value = 5, message = "Number of Players should not be greater than 5")
	private int numberOfPlayers;

	/**
	 * 
	 */
	@Min(value = 1, message = "Number of Countries should not be less than 1")
	@Max(value = 20, message = "Number of Countries should not be greater than 20")
	private int numberOfCountries;

	/**
	 * 
	 */
	@NotEmpty
	private List<PlayerDto> playersList;

	/**
	 * 
	 */
	public GameStarterDto() {
		this.setPlayersList(new ArrayList<PlayerDto>());
	}

	/**
	 * 
	 * @param numberOfPlayers
	 * @param numberOfCountries
	 */
	public GameStarterDto(int numberOfPlayers, int numberOfCountries) {
		super();
		this.numberOfPlayers = numberOfPlayers;
		this.numberOfCountries = numberOfCountries;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * 
	 * @param numberOfPlayers
	 */
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberOfCountries() {
		return numberOfCountries;
	}

	/**
	 * 
	 * @param numberOfCountries
	 */
	public void setNumberOfCountries(int numberOfCountries) {
		this.numberOfCountries = numberOfCountries;
	}

	public List<PlayerDto> getPlayersList() {
		return playersList;
	}

	/**
	 * 
	 * @param playersList
	 */
	public void setPlayersList(List<PlayerDto> playersList) {
		this.playersList = playersList;
	}

	/**
	 * @return the autoSave
	 */
	public boolean isAutoSave() {
		return autoSave;
	}

	/**
	 * @param autoSave
	 *            the autoSave to set
	 */
	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	@Override
	public void validate() throws ValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<GameStarterDto>> constraintViolations = validator.validate(this);
		if (constraintViolations != null && !constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfCountries;
		result = prime * result + numberOfPlayers;
		result = prime * result + ((playersList == null) ? 0 : playersList.hashCode());
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
		GameStarterDto other = (GameStarterDto) obj;
		if (numberOfCountries != other.numberOfCountries)
			return false;
		if (numberOfPlayers != other.numberOfPlayers)
			return false;
		if (playersList == null) {
			if (other.playersList != null)
				return false;
		} else if (!playersList.equals(other.playersList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameStarterDTO [numberOfPlayers=" + numberOfPlayers + ", numberOfCountries=" + numberOfCountries + "]";
	}
}
