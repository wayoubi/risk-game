package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.services.GameService;

/**
 * 
 * @author i857625
 *
 */
public class GameBusinessDelegate {

	/**
	 * 
	 */
	@Autowired
	GameService gameService;

	/**
	 * 
	 * @param gameStarterDTO
	 * @throws Exception
	 */
	public void initGame(GameStarterDto gameStarterDTO) throws Exception {
		gameService.initGame(gameStarterDTO);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void saveGame() throws Exception {
		gameService.saveGame();
	}

    public void addPlayer(PlayerDto playerDto) throws Exception {
		gameService.addPlayer(playerDto);
    }

	public void removePlayer(PlayerDto playerDto) throws Exception {
		gameService.removePlayer(playerDto);
	}

	public void populateCountries() {
		gameService.populateCountries();
	}

	public void placeArmy(String countryName) throws Exception {
		gameService.placeArmy(countryName);
	}
}