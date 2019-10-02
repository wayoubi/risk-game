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
 * @author i857625
 *
 */
public class PlayerDto implements Dto {

	/**
	 * 
	 */
	private int id;

	/**
	 * 
	 */
	@NotBlank(message = "Name cannot be null or empty")
	@Size(min = 5, message = "Name must be minumum 5 characters")
	private String name;

	/**
	 * 
	 */
	@NotBlank(message = "Color cannot be null or empty")
	private String color;

	/**
	 * Constructor()
	 */
	public PlayerDto() {
		super();
	}

	/**
	 * Constructor(name, color)
	 * 
	 * @param name
	 * @param color
	 */
	public PlayerDto(String name, String color) {
		this.setName(name);
		this.setColor(color);
	}

	/**
	 * Constructor(playerDTO)
	 * 
	 * @param playerDTO
	 */
	public PlayerDto(@NotNull final PlayerDto playerDTO) {
		this.setName(playerDTO.getName());
		this.setColor(playerDTO.getColor());
	}

	/**
	 * getter: the player's id
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * setter: the player's id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * getter: the player's name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter: the player's name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter: the player's color
	 * @return
	 */
	public String getColor() {
		return color;
	}

	/**
	 * setter: the player's color
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Override hashCode method, since 'equal' method is overridden
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		
		return result;
	}

	/**
	 * Override 'equals' method:
	 * To make sure for 'color' & 'id' & 'player name', it's only equal if current objects are the same, not only the name of strings.
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
		
		//color
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color)) {
			return false;
		}
		
		//id
		if (id != other.id)
			return false;
		
		//player name
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		
		
		return true;
	}

	/**
	 * Override toString method
	 * @return current player's: ['id' & 'name' & 'color']
	 */
	@Override
	public String toString() {
		return "PlayerDto [id=" + id + ", name=" + name + ", color=" + color + "]";
	}

	@Override
	public void validate() throws ValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PlayerDto>> constraintViolations = validator.validate(this);
		
		if (constraintViolations != null && !constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

}
