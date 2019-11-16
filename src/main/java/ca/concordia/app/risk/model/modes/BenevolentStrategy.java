package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

import java.util.List;

/**
 * 
 * @author i857625
 *
 */
public class BenevolentStrategy extends AbstractStrategy {

	/**
	 * 
	 * @param playerModel
	 */
	public BenevolentStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

	@Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {

		System.out.println("Decided not to attack");

		//fortify();


	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {
		super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
	}

	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {

		PlayerDaoImpl playerDaoImpl=new PlayerDaoImpl();

		//get list of countries assigned to that player
		List<CountryModel> assignedCountries = playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		// get the weakest country country to reinforce
		CountryModel weakestCountry = getWeakestCountry(assignedCountries);

		super.reinforce(weakestCountry, RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());
		attack(null,null,null);
	}


	private CountryModel getWeakestCountry(List<CountryModel> Countries) {

		CountryDaoImpl countryDao = new CountryDaoImpl();
		int minNoOfEnemies=0;
		CountryModel weakestCountry = null;

		for(CountryModel country: Countries){

			//set min number of enemies
			if(country.getNumberOfArmies()<minNoOfEnemies){
					minNoOfEnemies=country.getNumberOfArmies();
				}
			}

		return weakestCountry;

	}

}
