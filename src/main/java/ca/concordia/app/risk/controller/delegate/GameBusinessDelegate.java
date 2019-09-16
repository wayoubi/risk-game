package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.controller.dto.GameStarterDTO;
import ca.concordia.app.risk.services.GameService;

public class GameBusinessDelegate {
 
	
	public void initGame(GameStarterDTO gameStarterDTO) throws Exception{
		GameService gameService = new GameService();
		gameService.initGame(gameStarterDTO);
	}

	public void saveGame() throws Exception{
		GameService gameService = new GameService();
		gameService.saveGame();
	}
}