package ca.concordia.app.risk.controller.dto;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * PlayerDTO, used as a place holder across layers
 * 
 * @author i857625 PlayerDto
 *
 */
public class PlayerDto implements Dto {

	/**
	 * player id
	 */
	private int id;

	/**
	 * player name which should be at least 5 characters & can't be empty
	 */
	@NotBlank(message = "Name cannot be null or empty")
	@Size(min = 5, message = "Name must be minumum 5 characters")
	private String name;

	/**
	 * player color
	 */
	@NotBlank(message = "Color cannot be null or empty")
	private String color;
	
	/**
	 * 
	 */
	@NotBlank(message = "Strategy cannot be null or empty")
	private String strategy;
	

	/**
	 * Constructor
	 */
	public PlayerDto() {
		super();
	}

	/**
	 * Constructor(name, color)
	 * @param name  player name
	 * @param color player color
	 */
	public PlayerDto(String name, String color) {
		this.setName(name);
		this.setColor(color);
	}

	/**
	 * Constructor(playerDTO)
	 * @param playerDTO player DTO
	 */
	public PlayerDto(@NotNull final PlayerDto playerDTO) {
		this.setName(playerDTO.getName());
		this.setColor(playerDTO.getColor());
	}

	/**
	 * gets {@link id}
	 * 
	 * @return playerId
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets {@link id}
	 * 
	 * @param id sets player Id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * gets {@link name}
	 * 
	 * @return playerName
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets {@link name}
	 * 
	 * @param name sets player Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets {@link color}
	 * 
	 * @return playerColor
	 */
	public String getColor() {
		return color;
	}

	/**
	 * sets {@link color}
	 * 
	 * @param color sets player color
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * 
	 * @param strategy
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * {@inheritDoc}
	 * @throws ValidationException exception
	 */
	@Override
	public void validate() throws ValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PlayerDto>> constraintViolations = validator.validate(this);
		if (constraintViolations != null && !constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((strategy == null) ? 0 : strategy.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerDto other = (PlayerDto) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (strategy == null) {
			if (other.strategy != null)
				return false;
		} else if (!strategy.equals(other.strategy))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "PlayerDto [id=" + id + ", name=" + name + ", color=" + color + ", strategy=" + strategy + "]";
	}
	

}
