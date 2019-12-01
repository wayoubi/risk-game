package ca.concordia.app.risk.model.builder;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
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
	 * 
	 */
	private GameModel gameModel;

	/**
	 * 
	 * @return
	 */
	public GameModel getGameModel() {
		return gameModel;
	}

	/**
	 * 
	 */
	public void createGameModel() {
		this.setGameModel(new GameModel());
	}

	/**
	 * 
	 * @param gameModel
	 */
	protected void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	/**
	 * 
	 */
	public abstract void buildModel();

	/**
	 * 
	 */
	public abstract void buildGraph();
}
