package ca.concordia.app.risk.model.builder;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * This class is a builder design pattern's director for load & save game
 *
 */
public class GamePersistanceManager {

	/**
	 * game model builder
	 */
	private AbstractGameModelBuilder gameModelBuilder;

	/**
	 * sets {@link gameModelBuilder} from abstract model
	 * @param gameModelBuilder game model builder
	 */
	public GamePersistanceManager(AbstractGameModelBuilder gameModelBuilder) {
		this.gameModelBuilder = gameModelBuilder;
	}

	/**
	 * This method loads saved game 
	 */
	public void loadGame() {
		this.getGameModelBuilder().createGameModel();
		this.getGameModelBuilder().buildModel();
		this.getGameModelBuilder().buildGraph();
	}

	/**
	 * This method saves current game model
	 */
	public void saveGame() {
		this.getGameModelBuilder().buildGraph();
		this.getGameModelBuilder().buildModel();
	}

	/**
	 * gets {@link gameModelBuilder}
	 * @return
	 */
	public GameModel getGameMmodel() {
		return this.getGameModelBuilder().getGameModel();
	}

	/**
	 * gets {@link gameModelBuilder} from abstract model
	 * @return
	 */
	public AbstractGameModelBuilder getGameModelBuilder() {
		return gameModelBuilder;
	}

	/**
	 * sets {@link gameModelBuilder}
	 * @param gameModelBuilder game model builder
	 */
	public void setGameModelBuilder(AbstractGameModelBuilder gameModelBuilder) {
		this.gameModelBuilder = gameModelBuilder;
	}

}
