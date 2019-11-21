package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * 
 * @author i857625
 *
 */
public class CheaterStrategy extends AbstractStrategy {

	/**
	 * 
	 * @param playerModel
	 */
	public CheaterStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

	@Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {
		super.attack(countryModelFrom, countryModelTo, numDice);
	}

	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {
		super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
	}

	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {
		super.reinforce(countryModel, numberOfArmies);
	}
}
