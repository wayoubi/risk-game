package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.controller.dto.GameStarterDTO;
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
	public void initGame(GameStarterDTO gameStarterDTO) throws Exception{
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