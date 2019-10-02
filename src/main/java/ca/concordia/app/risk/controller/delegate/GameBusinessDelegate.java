package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

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
	 * @throws Exception
	 */
	public void saveGame(){
		gameService.saveGame();
	}

	/**
	 * 
	 * @param fileLocation
	 */
	public void saveMap(String fileName) {
		gameService.saveMap(fileName);
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateMap() {
		return gameService.validateMap();
	}

	/**
	 * 
	 * @param fileName
	 */
	public void editMap(String fileName) {
		gameService.editMap(fileName);
	}

	/**
	 * 
	 * @param fileName
	 */
	public void loadMap(String fileName) {
		gameService.loadMap(fileName);
	}
}