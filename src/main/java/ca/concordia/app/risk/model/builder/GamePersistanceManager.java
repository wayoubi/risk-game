package ca.concordia.app.risk.model.builder;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

public class GamePersistanceManager {
	
	/**
	 * 
	 */
	private AbstractGameModelBuilder gameModelBuilder;

	/**
	 * 
	 * @param gameModelBuilder
	 */
	public GamePersistanceManager(AbstractGameModelBuilder gameModelBuilder) {
		this.gameModelBuilder = gameModelBuilder;
	}
	
	/**
	 * 
	 */
	public void loadGame() {
		this.getGameModelBuilder().createGameModel();
		this.getGameModelBuilder().buildModel();
		this.getGameModelBuilder().buildGraph();
	}
	
	/**
	 * 
	 */
	public void saveGame() {
		this.getGameModelBuilder().buildGraph();
		this.getGameModelBuilder().buildModel();
	}
	
	/**
	 * 
	 * @return
	 */
	public GameModel getGameMmodel() {
		return this.getGameModelBuilder().getGameModel();
	}
	
	/**
	 * 
	 * @return
	 */
	public AbstractGameModelBuilder getGameModelBuilder() {
		return gameModelBuilder;
	}

	/**
	 * 
	 * @param gameModelBuilder
	 */
	public void setGameModelBuilder(AbstractGameModelBuilder gameModelBuilder) {
		this.gameModelBuilder = gameModelBuilder;
	}

}
