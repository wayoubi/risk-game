package ca.concordia.app.risk.model.modes;

import java.util.List;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * This class inherits from AbstractStrategy class to override methods and have
 * specific implementation for Aggressive Strategy
 * 
 * @author i857625
 *
 */
public class AggressiveStrategy extends AbstractStrategy {

	/**
	 * Constructor for Aggressive Strategy Class
	 * 
	 * @param playerModel player model
	 */
	public AggressiveStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

	/**
	 * {@inheritDoc} This method contains reinforce method implementation for
	 * Aggressive Strategy
	 * 
	 * @param countryModel   country model
	 * @param numberOfArmies number of armies to reinforce
	 */
	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {

		CountryModel attackFrom = getStrongestCountry();

		// check if attackFrom is not null
		if (attackFrom == null) {
			throw new RiskGameRuntimeException("attackFrom is null, reinforcement is not possible");
		}

		// reinforce with maximum number of enemies
		super.reinforce(attackFrom,
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());

		CountryModel attackTo = getWeakestNeighbourCountry(attackFrom);

		// System.out.println(attackFrom.getName() +" "+ attackTo.getName());
		// RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Exchange
		// Cards");

		RunningGame.getInstance().getCurrentPlayer().getStrategy().attack(null, null, null);
		// }
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * {@inheritDoc} This method contains attack method implementation for
	 * Aggressive Strategy
	 * 
	 * @param countryModelFrom attacking country model
	 * @param countryModelTo   defender country model
	 * @param numDice          number of dice
	 */
	@Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {

		countryModelFrom = getStrongestCountry();
		countryModelTo = getWeakestNeighbourCountry(countryModelFrom);

		// check if attackFrom is not null
		if (countryModelFrom == null) {
			throw new RiskGameRuntimeException("No attackFrom is asigned, Attack is not possible");
		}

		// check if attackTo is not null
		if (countryModelTo == null) {
			throw new RiskGameRuntimeException("No attackTo is asigned, Attack is not possible");
		}

		// attack
		boolean attack = true;
		CountryModel previousCountryModelTo = null;

		while (attack) {
			// get the Weakest Country
			countryModelTo = getWeakestNeighbourCountry(countryModelFrom);

			if (!countryModelTo.equals(previousCountryModelTo)) {
				previousCountryModelTo = countryModelTo;
				System.out.println();
				RunningGame.getInstance().setAllOut(true);
				super.attack(countryModelFrom, countryModelTo, "-allout");
			} else {
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortify");
				RunningGame.getInstance().getSubject().markAndNotify();
				attack = false;
				// fortify logic
			}

		}
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
	 * {@inheritDoc} This method contains fortify method implementation for
	 * Aggressive Strategy
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
	 * This method checks whether neighboring country is an enemy or not
	 * 
	 * @param neighbours      list of neighbors
	 * @param currentPlayerId current player id
	 * @return
	 */
	private boolean isNeighbourEnemy(List<Integer> neighbours, int currentPlayerId) {

		for (int neighbour : neighbours) {
			if (neighbour != currentPlayerId) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method returns the strongest country that have the highest number of
	 * armies to place attack from that country
	 * 
	 * @return
	 */
	private CountryModel getStrongestCountry() {

		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();

		// get all countries
		List<CountryModel> countryModels = playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		int maxNoOfArmies = 0;
		CountryModel attackFrom = null;
		List<Integer> neighbours = null;

		for (CountryModel country : countryModels) {

			int numOfArmies = country.getNumberOfArmies();
			if (numOfArmies > maxNoOfArmies) {
				// check at least one of neighbours countries is an enemy
				BorderDaoImp borderDaoImpl = new BorderDaoImp();
				BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), country.getName());
				neighbours = borderModel.getNeighbours();

				// check if at least of the neighbours is enemy
				if (isNeighbourEnemy(neighbours,
						RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId())) {
					attackFrom = country;
				}
			}
		}

		return attackFrom;
	}

	/**
	 * This method returns the weakest country that have the lowest number of armies
	 * to be a target for attacking country
	 * 
	 * @param attackFrom attack from country
	 * @return
	 */
	private CountryModel getWeakestNeighbourCountry(CountryModel attackFrom) {

		// check at least one of neighbours countries is an enemy
		BorderDaoImp borderDaoImpl = new BorderDaoImp();
		BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), attackFrom.getName());

		List<Integer> neighbours = borderModel.getNeighbours();
		CountryDaoImpl countryDao = new CountryDaoImpl();
		int minNoOfEnemies = -1;
		CountryModel attackTo = null;

		for (int neighbour : neighbours) {
			// check if enemy
			if (neighbour != RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()) {

				// get country model
				CountryModel countryModel = null;
				countryModel = countryDao.findById(RunningGame.getInstance(), neighbour);

				// get country model of the lowest number of enemies
				if (countryModel.getNumberOfArmies() < minNoOfEnemies || minNoOfEnemies == -1) {
					minNoOfEnemies = countryModel.getNumberOfArmies();
					int attackToId = neighbour;
					attackTo = countryDao.findById(RunningGame.getInstance(), attackToId);
				}
			}
		}

		return attackTo;
	}

}
