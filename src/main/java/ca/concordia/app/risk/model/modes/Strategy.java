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
	 * @param countryModel
	 * @param numberOfArmies
	 */
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice);
	
	/**
	 * 
	 * @param numDice
	 */
	public void defend(String numDice);
	
	/**
	 * 
	 * @param fromCountry
	 * @param toCountry
	 * @param numberOfArmies
	 */
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies);
	
	/**
	 * 
	 * @param countryModel
	 * @param numberOfArmies
	 */
	public void reinforce(CountryModel countryModel, int numberOfArmies);
	
}
