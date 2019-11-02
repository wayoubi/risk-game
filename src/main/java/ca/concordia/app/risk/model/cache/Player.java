package ca.concordia.app.risk.model.cache;

import java.util.Observable;
import java.util.Random;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
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

    public void attack(String countryNameFrom, String countyNameTo, String numDice, String allout) {

		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();

		//check if countryNameFrom belong to active player
		PlayerModel currentPlayer = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		CountryModel countryModelFrom = countryDaoImpl.findByName(RunningGame.getInstance(),countryNameFrom);
		CountryModel countryModelTo = countryDaoImpl.findByName(RunningGame.getInstance(),countyNameTo);

		if(countryModelFrom.getPlayerId() != currentPlayer.getId())
		{
			throw new RiskGameRuntimeException(countryNameFrom + " does not belong to " + currentPlayer.getName());
		}

		//check if countryNameTo doesn't belong to active player
		if(countryModelTo.getPlayerId() == currentPlayer.getId())
		{
			throw new RiskGameRuntimeException(countyNameTo + " belongs to " + currentPlayer.getName() + " please choose another country");
		}

		// check max number of dice doesn't exceed 3 and not less than 1
		if(Integer.parseInt(numDice) >3 || Integer.parseInt(numDice) <1 ) {
			throw new RiskGameRuntimeException("number of dice should be between 1 and 3");
		}

		// roll the dice
		Random random = new Random();

		int numDice1 = random.nextInt(5)+1;
		int numDice2 = random.nextInt(5)+1;
        int numDice3 = 0;
		if(Integer.parseInt(numDice)==3) {
             numDice3 = random.nextInt(5) + 1;
        }

        int[] attackerDice;
		if(Integer.parseInt(numDice)==3) {
             attackerDice = new int[]{numDice1, numDice2, numDice3};
        } else {
             attackerDice = new int[]{numDice1, numDice2};
        }

		// save the dice in running game to compare later
        RunningGame.getInstance().setAttackerDice(attackerDice);

		// save countries in running game
        RunningGame.getInstance().setAttackCountryNameFrom(countryNameFrom);
        RunningGame.getInstance().setAttackCountryNameTo(countyNameTo);

        // defender turn
	}
}