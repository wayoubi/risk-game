package ca.concordia.app.risk.controller.delegate;

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
	 * @param gameStarterDTO
	 * @throws Exception
	 */
	public void initGame(GameStarterDto gameStarterDTO) throws Exception{
		GameService gameService = new GameService();
		gameService.initGame(gameStarterDTO);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void saveGame() throws Exception{
		GameService gameService = new GameService();
		gameService.saveGame();
	}
}