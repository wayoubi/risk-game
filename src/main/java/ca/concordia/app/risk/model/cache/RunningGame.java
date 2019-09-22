package ca.concordia.app.risk.model.cache;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * 
 * @author i857625
 *
 */
public class RunningGame extends GameModel {
	
	/**
	 * 
	 */
	private static RunningGame runningGame;

	/**
	 * 
	 */
	private RunningGame() { 
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public static GameModel getInstance() {
		if(runningGame == null) {
			runningGame = new RunningGame();
		}
		return runningGame;
	}
	
	/**
	 * 
	 */
	public static void reset() {
		runningGame = new RunningGame();
	}

}
