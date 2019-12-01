package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;

/**
 * This is an interface for Strategy design pattern contains methods which will
 * be implemented differently depend on the strategy
 * 
 * @author i857625
 *
 */
public interface Strategy {

	/**
	 * This method is to attack a country depend on the strategy
	 * 
	 * @param countryModelFrom attacker country model
	 * @param countryModelTo   defender country model
	 * @param numDice          number of dice
	 */
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice);

	/**
	 * This method is for a country to defend depend on the strategy
	 * 
	 * @param numDice number of dice
	 */
	public void defend(String numDice);

	/**
	 * This method is to fortify depend on the strategy
	 * 
	 * @param countryModelFrom the country fortify from
	 * @param countryModelTo   destination country
	 * @param numberOfArmies   number of armies
	 */
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies);

	/**
	 * This method is to reinforce depend on the strategy
	 * 
	 * @param countryModel   country model
	 * @param numberOfArmies number of armies
	 */
	public void reinforce(CountryModel countryModel, int numberOfArmies);

}
