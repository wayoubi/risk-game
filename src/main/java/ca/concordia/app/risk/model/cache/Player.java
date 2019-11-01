package ca.concordia.app.risk.model.cache;

import java.util.Observable;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class Player extends Observable {
	
	private PlayerModel playerModel;
	
	public Player(PlayerModel playerModel) {
		this.setPlayerModel(playerModel);
	}
	
	public void setPlayerModel(PlayerModel playerModel) {
		this.playerModel = playerModel;
	}
	
	public PlayerModel getPlayerModel() {
		return playerModel;
	}
	
	/**
	 * 
	 * @param countryModel
	 * @param numberOfArmies
	 */
	public void reinforce(CountryModel countryModel, int numberOfArmies) {
	
		if (this.getPlayerModel().getReinforcementNoOfArmies() == 0) {
			throw new RiskGameRuntimeException("Reinforcement phase has been completed");
		}
		if (this.getPlayerModel().getReinforcementNoOfArmies() >= numberOfArmies) {
			countryModel.setNumberOfArmies(countryModel.getNumberOfArmies() + numberOfArmies);
			if (this.getPlayerModel().getReinforcementNoOfArmies() - numberOfArmies == 0) {
				RunningGame.getInstance().setReinforceCompleted(true);
			}
			this.getPlayerModel().setReinforcementNoOfArmies(this.getPlayerModel().getReinforcementNoOfArmies() - numberOfArmies);
		} else {
			throw new RiskGameRuntimeException("Please reduce number of armies");
		}
	}
}
