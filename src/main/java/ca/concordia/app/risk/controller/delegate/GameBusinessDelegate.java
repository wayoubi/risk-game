package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.GameService;

public class GameBusinessDelegate {
 
	
	public void initGame(String playerName, String playerColor) throws Exception{
		GameService gameService = new GameService();
		gameService.initGame(playerName, playerColor);
	}

	public void saveGame() throws Exception{
		GameService gameService = new GameService();
		gameService.saveGame();
	}
}