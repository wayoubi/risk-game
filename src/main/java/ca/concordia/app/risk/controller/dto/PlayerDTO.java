package ca.concordia.app.risk.controller.dto;

public class PlayerDTO {

	/**
	 * 
	 */
	private String name;
	
	/**
	 * 
	 */
	private String color;
	
	/**
	 * 
	 */
	public PlayerDTO() {	
	}
	
	/**
	 * 
	 * @param name
	 * @param color
	 */
	public PlayerDTO(String name, String color) {
		this.setName(name);
		this.setColor(color);
	}
	
	/**
	 * @param playerDTO
	 */
	public PlayerDTO(PlayerDTO playerDTO) {
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

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PlayerDTO other = (PlayerDTO) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "PlayerDTO [name=" + name + ", color=" + color + "]";
	}
}
