package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

import java.util.List;
import java.util.Random;

/**
 * 
 * @author i857625
 *
 */
public class RandomStrategy extends AbstractStrategy {

	/**
	 * 
	 * @param playerModel
	 */
	public RandomStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	@Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {

		// get list of countries assigned to that player
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		// get a random country
		Random random = new Random();
		int i = random.nextInt(countryModels.size());

		CountryModel attackFrom = countryModels.get(i);
		CountryModel attackTo = getRandomNeighbourCountry(attackFrom);

		// random number of times to attack a random country

		int noOfAttack = random.nextInt(10)+1;

		while(noOfAttack>0){
			System.out.println("number of attack" +  noOfAttack);

			if(attackFrom.getNumberOfArmies()>3) {
				numDice = "3";
			} else {
				numDice = "2";
			}

			String defenderNumDice;
			if(attackTo.getNumberOfArmies()>3 && "3".equalsIgnoreCase(numDice)) {
				defenderNumDice = "2";
			} else {
				defenderNumDice = "1";
			}

			super.attack(attackFrom, attackTo, numDice);
			super.defend(defenderNumDice);
			noOfAttack--;

		}

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortify");
		RunningGame.getInstance().getSubject().markAndNotify();


		//fortify(null,null,0);

	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {
		super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
	}

	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {

		// get list of countries assigned to that player
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		// get a random country
		Random random = new Random();
		int i = random.nextInt(countryModels.size());

		CountryModel attackFrom = countryModels.get(i);

		super.reinforce(attackFrom, RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());

	}

	private CountryModel getRandomNeighbourCountry(CountryModel attackFrom) {

		// check at least one of neighbours countries is an enemy
		BorderDaoImp borderDaoImpl = new BorderDaoImp();
		BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), attackFrom.getName());

		List<Integer> neighbours = borderModel.getNeighbours();

		CountryDaoImpl countryDao = new CountryDaoImpl();

		CountryModel attackTo = null;

		for(int i =0; i<neighbours.size();i++){
			// check if enemy
			if(neighbours.get(i) != RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()){
					neighbours.remove(i);
				}
			}

		// get random neighbours

		Random random = new Random();
		int index = random.nextInt(neighbours.size());
		attackTo = countryDao.findById(RunningGame.getInstance(),neighbours.get(index));

		return attackTo;

	}
}
