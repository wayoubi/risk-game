package ca.concordia.app.risk.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class GameStarterDTO {
	
	private int numberOfPlayers;
	private int numberOfCountries;
	
	private List<PlayerDTO> playersList;

	public GameStarterDTO() {
		this.setPlayersList(new ArrayList<PlayerDTO>());
	}
	
	public GameStarterDTO(int numberOfPlayers, int numberOfCountries) {
		super();
		this.numberOfPlayers = numberOfPlayers;
		this.numberOfCountries = numberOfCountries;
	}
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public int getNumberOfCountries() {
		return numberOfCountries;
	}
	
	public void setNumberOfCountries(int numberOfCountries) {
		this.numberOfCountries = numberOfCountries;
	}
	
	public List<PlayerDTO> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(List<PlayerDTO> playersList) {
		this.playersList = playersList;
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
		GameStarterDTO other = (GameStarterDTO) obj;
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
