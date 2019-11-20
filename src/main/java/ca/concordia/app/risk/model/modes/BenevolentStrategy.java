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

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Attack");
		RunningGame.getInstance().getSubject().markAndNotify();

		System.out.println("Decided not to attack");

		//fortify();


	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortify");
		RunningGame.getInstance().getSubject().markAndNotify();

		super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
	}

	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforce");
		RunningGame.getInstance().getSubject().markAndNotify();


		PlayerDaoImpl playerDaoImpl=new PlayerDaoImpl();

		//get list of countries assigned to that player
		List<CountryModel> assignedCountries = playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		// get the weakest country country to reinforce
		CountryModel weakestCountry = getWeakestCountry(assignedCountries);

		super.reinforce(weakestCountry,RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());
		attack(null,null,null);
	}

	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	private CountryModel getWeakestCountry(List<CountryModel> Countries) {

		CountryDaoImpl countryDao = new CountryDaoImpl();
		int minNoOfEnemies=0;
		CountryModel weakestCountry = null;


		for(CountryModel country: Countries){

			//System.out.println(country.getNumberOfArmies() + " : " + minNoOfEnemies);
			//set min number of enemies
			if(country.getNumberOfArmies()<minNoOfEnemies || minNoOfEnemies==0){
					minNoOfEnemies=country.getNumberOfArmies();
					weakestCountry=country;
				}
			}

		return weakestCountry;

	}

}
