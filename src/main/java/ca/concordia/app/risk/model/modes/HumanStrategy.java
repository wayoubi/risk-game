package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * This class inherits from AbstractStrategy class to override methods and have
 * specific implementation for Human Strategy
 * 
 * @author i857625
 *
 */
public class HumanStrategy extends AbstractStrategy {

	/**
	 * {@inheritDoc}
	 * 
	 * @param playerModel player model
	 */
	public HumanStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param countryModelFrom attacking country model
	 * @param countryModelTo   defender country model
	 * @param numDice          number of dice
	 */
	@Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {
		super.attack(countryModelFrom, countryModelTo, numDice);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param numDice number of dice
	 */
	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param countryModelFrom the country fortify from model
	 * @param countryModelTo   the country fortify to model
	 * @param numberOfArmies   number of armies to fortify
	 */
	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {
		super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param countryModel   country model
	 * @param numberOfArmies number of armies to reinforce
	 */
	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {
		super.reinforce(countryModel, numberOfArmies);
	}
}
