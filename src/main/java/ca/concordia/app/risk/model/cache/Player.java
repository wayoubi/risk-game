package ca.concordia.app.risk.model.cache;

import java.util.Observable;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.modes.Strategy;
import ca.concordia.app.risk.model.modes.StrategyFactory;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * This class is used to represent the current Running game It has the
 * implementation of all current player actions.
 * 
 * @author Wasim Alayoubi
 *
 */
public class Player extends Observable {

	/**
	 * playerModel player Model
	 */
	private PlayerModel playerModel;

	/**
	 * 
	 */
	private Strategy strategy;

	/**
	 * Constructor(playerModel)
	 * 
	 * @param playerModel player model
	 */
	public Player(PlayerModel playerModel) {
		this.setPlayerModel(playerModel);
		this.setStrategy(StrategyFactory.createStrategy(playerModel));
	}

	/**
	 * set player model
	 * 
	 * @param playerModel player model
	 */
	public void setPlayerModel(PlayerModel playerModel) {
		this.playerModel = playerModel;
	}

	/**
	 * get player model
	 * 
	 * @return playerModel player model
	 */
	public PlayerModel getPlayerModel() {
		return playerModel;
	}

	/**
	 * This method used for attack
	 * 
	 * @param countryNameFrom attacker country name
	 * @param countyNameTo    defender country name
	 * @param numDice         number of dices for attacker
	 */
	public void attack(String countryNameFrom, String countyNameTo, String numDice) {
		if (!RunningGame.getInstance().isCardExchangeCompleted()) {
			throw new RiskGameRuntimeException(
					"Please complete the Reinforcement - Card Exchange phase first. If you do not have any cards or you don't want to exchange please run, exchangecards -none");
		}
		if (!(this.getPlayerModel().getPlayingPhase() == "Attack")) {
			throw new RiskGameRuntimeException("You're not on Attack phase, please check the phase and come back");
		}
		if (RunningGame.getInstance().isAttackCompleted()) {
			throw new RiskGameRuntimeException(
					"You've conquered a country in the previous attack. Please move armies to the country and then continue with your attack");
		}
		
		// check if countryNameFrom belong to active player
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		PlayerModel currentPlayer = this.getPlayerModel();
		CountryModel countryModelFrom = countryDaoImpl.findByName(RunningGame.getInstance(), countryNameFrom);
		CountryModel countryModelTo = countryDaoImpl.findByName(RunningGame.getInstance(), countyNameTo);
		BorderDaoImp borderDaoImpl = new BorderDaoImp();
		BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), countryNameFrom);
		
		if (countryModelFrom.getPlayerId() != currentPlayer.getId()) {
			throw new RiskGameRuntimeException(countryNameFrom + " does not belong to " + currentPlayer.getName());
		}
		// check if countryNameTo doesn't belong to active player
		if (countryModelTo.getPlayerId() == currentPlayer.getId()) {
			throw new RiskGameRuntimeException(
					countyNameTo + " belongs to " + currentPlayer.getName() + " please choose another country");
		}
		if (!borderModel.getNeighbours().contains(countryModelTo.getId())) {
			throw new RiskGameRuntimeException(
					"The from and to country do not share borders, please select some other country to attack");
		}
		// check number of armies of attackTo
		if (countryModelTo.getNumberOfArmies() == 0) {
			throw new RiskGameRuntimeException("Number of armies in attackCountryTo is equal to zero");
		}
		if (countryModelFrom.getNumberOfArmies() == 1) {
			throw new RiskGameRuntimeException("Cannot attack from " + countryNameFrom + " since it has only 1 army left");
		}
		
		strategy.attack(countryModelFrom, countryModelTo, numDice);
	}

	/**
	 * This method used to give the number of dice to the defender country.
	 * 
	 * @param numDice number of dices for defender
	 */
	public void defend(String numDice) {
		// check max number of dice doesn't exceed 3 and not less than 1
		if (Integer.parseInt(numDice) > 2 || Integer.parseInt(numDice) < 1) {
			throw new RiskGameRuntimeException("number of dice should be 1 or 2");
		}
		if (RunningGame.getInstance().getNumDiceAttacker() == 0) {
			throw new RiskGameRuntimeException("Attack has not been initialized yet!");
		}
		strategy.defend(numDice);
	}
	
	/**
	 * This method used for fortify operation
	 *
	 * @param fromCountry    origin country to fortify
	 * @param toCountry      destination country to fortify
	 * @param numberOfArmies number of armies
	 */
	public void fortify(String fromCountry, String toCountry, int numberOfArmies) {
		if (!(this.getPlayerModel().getPlayingPhase() == "Fortification")) {
			throw new RiskGameRuntimeException("Please complete the current phase you're on first");
		}
		PlayerModel activePlayerModel = this.getPlayerModel();
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel fromCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), fromCountry);
		CountryModel toCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), toCountry);
		if (activePlayerModel == null) {
			throw new RiskGameRuntimeException("No Players have been added");
		}
		if (fromCountryModel == null) {
			throw new RiskGameRuntimeException("From Country doesn't exist");
		}
		if (toCountryModel == null) {
			throw new RiskGameRuntimeException("To Country doesn't exist");
		}
		if ((fromCountryModel.getPlayerId()) != (activePlayerModel.getId())) {
			throw new RiskGameRuntimeException(
					"From Country " + fromCountry + " is not owned by " + activePlayerModel.getName());
		}
		if ((toCountryModel.getPlayerId()) != (activePlayerModel.getId())) {
			throw new RiskGameRuntimeException(
					"To Country " + toCountry + " is not owned by " + activePlayerModel.getName());
		}
		if (fromCountryModel.getNumberOfArmies() - numberOfArmies < 1) {
			throw new RiskGameRuntimeException("From Country " + fromCountry
					+ " needs to have atleast one army after fortification. Please reduce the number of armies");
		}
		strategy.fortify(fromCountryModel, toCountryModel, numberOfArmies);
	}
	
	/**
	 * This method used for reinforcement
	 * 
	 * @param countryModel   country Model
	 * @param numberOfArmies number of armies
	 */
	public void reinforce(CountryModel countryModel, int numberOfArmies) {
		if (this.getPlayerModel().getReinforcementNoOfArmies() == 0) {
			throw new RiskGameRuntimeException("Reinforcement phase has already been completed.");
		}
		strategy.reinforce(countryModel, numberOfArmies);
	}

	/**
	 * This method used to move the number of armies from attacker to the defender
	 * country if the attacker has conquered the defender.
	 * 
	 * @param num number of armies to move from attackers country to defenders
	 *            country
	 */
	public void attackMove(String num) {
		if (!RunningGame.getInstance().isAttackCompleted()) {
			throw new RiskGameRuntimeException("You've not conquered any country to move the armies");
		}
		// if (RunningGame.getInstance().isDefenderWin()) {
		// throw new RiskGameRuntimeException("Defender had won, you can't make a
		// move");
		// }
		// check num are not greater than the number of armies in the attackCountryFrom
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModelAttackFrom = countryDaoImpl.findByName(RunningGame.getInstance(), RunningGame.getInstance().getAttackCountryNameFrom());
		if (countryModelAttackFrom.getNumberOfArmies() - 1 < Integer.parseInt(num)) {
			throw new RiskGameRuntimeException("You don't have enough armies, please reduce the number of armies");
		}
		// at least one army should be there or more depends on the number of dice
		// if (countryModelAttackFrom.getNumberOfArmies() - 1 < Integer.parseInt(num)) {
		if (RunningGame.getInstance().getAttackerDice().length > Integer.parseInt(num)) {
			throw new RiskGameRuntimeException(
					"You have to move atleast the number of armies equivalent to the dice you used to conquer! The number of dice was "
							+ RunningGame.getInstance().getAttackerDice().length);
		}
		// move armies
		CountryModel countryModelAttackTo = countryDaoImpl.findByName(RunningGame.getInstance(), RunningGame.getInstance().getAttackCountryNameTo());
		countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies() - Integer.parseInt(num));
		countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies() + Integer.parseInt(num));
		RunningGame.getInstance().setAttackCompleted(false);
		RunningGame.getInstance().getSubject().markAndNotify();
	}
	
	/**
	 * This method used for exchange cards
	 * 
	 * @param cardsArray array of cards
	 */
	public void exchangeCards(String[] cardsArray) {
		int reinforcementNoOfArmies = this.getPlayerModel().getReinforcementNoOfArmies();
		int cardExchangeCount = this.getPlayerModel().getCardExchangeCount();
		// Add additional armies
		if (cardExchangeCount == 0) {
			// Add 5 Additional Armies
			reinforcementNoOfArmies += 5;
			cardExchangeCount += 1;
			this.getPlayerModel().setReinforcementNoOfArmies(reinforcementNoOfArmies);
			this.getPlayerModel().setCardExchangeCount(cardExchangeCount);
		} else {
			// Add 5, 10 ,15, 20 , ... Additional Armies
			reinforcementNoOfArmies += 5 * (cardExchangeCount + 1);
			cardExchangeCount += 1;
			this.getPlayerModel().setReinforcementNoOfArmies(reinforcementNoOfArmies);
			this.getPlayerModel().setCardExchangeCount(cardExchangeCount);
		}
		// remove cards from the list
		this.getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[2]) - 1);
		this.getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[1]) - 1);
		this.getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[0]) - 1);
		RunningGame.getInstance().setCardExchangeCompleted(true);
		RunningGame.getInstance().setReinforceCompleted(false);
	}

	/**
	 * 
	 * @return
	 */
	public Strategy getStrategy() {
		return strategy;
	}

	/**
	 * 
	 * @param strategy
	 */
	private void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
}