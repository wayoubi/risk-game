package ca.concordia.app.risk.model.cache;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

public class RunningGame extends GameModel {
	
	private static RunningGame runningGame;

	private RunningGame() { 
		super();
	}
	
	public static GameModel getInstance() {
		if(runningGame == null) {
			runningGame = new RunningGame();
		}
		return runningGame;
	}
	
	public static void reset() {
		runningGame = new RunningGame();
	}

}
