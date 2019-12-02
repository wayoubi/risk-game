package ca.concordia.app.risk.model.builder;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * This is an abstract class which contains builders for the game
 * 
 * @author i857625
 *
 */
public abstract class AbstractGameModelBuilder {

	/**
	 * 
	 */
	public static final String GAME_CANNOT_BE_SAVED = "Game caanot be saved!";

	/**
	 * 
	 */
	public static final String GAME_CANNOT_BE_LOADED = "Game caanot be loaded!";

	/**
	 * game model to build
	 */
	private GameModel gameModel;

	/**
	 * gets {@link gameModel}
	 * @return
	 */
	public GameModel getGameModel() {
		return gameModel;
	}

	/**
	 * This method created the game model
	 */
	public void createGameModel() {
		this.setGameModel(new GameModel());
	}

	/**
	 * sets {@link gameModel}
	 * @param gameModel
	 */
	protected void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	/**
	 * Abstract method to build the model
	 */
	public abstract void buildModel();

	/**
	 * Abstract method to build the graph
	 */
	public abstract void buildGraph();
}
