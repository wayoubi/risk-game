package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;

/**
 * 
 * @author i857625
 *
 */
public interface Strategy {
	
	/**
	 * 
	 * @param countryModel country model
	 * @param numberOfArmies number of armies
	 */
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice);
	
	/**
	 * 
	 * @param numDice number of dice
	 */
	public void defend(String numDice);
	
	/**
	 * 
	 * @param fromCountry the country fortify from
	 * @param toCountry destination country
	 * @param numberOfArmies number of armies
	 */
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies);
	
	/**
	 * 
	 * @param countryModel country model
	 * @param numberOfArmies number of armies
	 */
	public void reinforce(CountryModel countryModel, int numberOfArmies);
	
}
