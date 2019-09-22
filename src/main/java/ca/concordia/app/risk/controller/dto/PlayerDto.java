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
public class PlayerDto implements Dto{

	/**
	 * 
	 */
	private int id;

	/**
	 * 
	 */
	@NotBlank(message = "Name cannot be null or empty")
	@Size(min = 10, max = 200, message = "Name must be between 8 and 20 characters")
	private String name;
	
	/**
	 * 
	 */
	private String color;
	
	/**
	 * 
	 */
	public PlayerDto() {	
	}
	
	/**
	 * 
	 * @param name
	 * @param color
	 */
	public PlayerDto(String name, String color) {
		this.setName(name);
		this.setColor(color);
	}
	
	/**
	 * @param playerDTO
	 */
	public PlayerDto(@NotNull final PlayerDto playerDTO) {
		if(playerDTO == null) {
			throw new IllegalArgumentException("PlayerDTO cannot be null");
		}
		this.setName(playerDTO.getName());
		this.setColor(playerDTO.getColor());
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getColor() {
		return color;
	}

	/**
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "PlayerDTO [id=" + id + ", name=" + name + ", color=" + color + "]";
	}
}
