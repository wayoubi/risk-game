package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

import java.util.List;

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

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Attack");
		RunningGame.getInstance().getSubject().markAndNotify();

		// get countries that have been owned by the player
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels = playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		List<Integer> neighbours = null;

		for (CountryModel country : countryModels) {

			// check at least one of neighbours countries is an enemy
			BorderDaoImp borderDaoImpl = new BorderDaoImp();
			BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), country.getName());
			neighbours = borderModel.getNeighbours();

			CountryDaoImpl countryDao = new CountryDaoImpl();

			for (int neighbour : neighbours) {

				if (neighbour != RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()) {
					CountryModel countryModel = null;
					countryModel = countryDao.findById(RunningGame.getInstance(), neighbour);
					System.out.println(countryModel.getName() + " : " + neighbour );

					//assigned to the new player
					countryModel.setPlayerId(RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId());

					//move one army to the new country
					countryModel.setNumberOfArmies(1);
					country.setNumberOfArmies(country.getNumberOfArmies()-1);

					// update neighbours

					RunningGame.getInstance().getSubject().markAndNotify();
				}
			}
		}
		fortify(null,null,0);
	}


	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortify");
		RunningGame.getInstance().getSubject().markAndNotify();



		// get countries that have been owned by the player
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels = playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		List<Integer> neighbours = null;

		for (CountryModel country : countryModels) {

			// check at least one of neighbours countries is an enemy
			BorderDaoImp borderDaoImpl = new BorderDaoImp();
			BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), country.getName());
			neighbours = borderModel.getNeighbours();

			if(isNeighbourEnemy(neighbours,RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId())){
				country.setNumberOfArmies(country.getNumberOfArmies() * 2);
				RunningGame.getInstance().getSubject().markAndNotify();
			}
		}

		RunningGame.getInstance().moveToNextPlayer();
		RunningGame.getInstance().reinforceInitialization();
		RunningGame.getInstance().getSubject().markAndNotify();


	}

	private boolean isNeighbourEnemy(List<Integer> neighbours, int currentPlayerId) {

		for(int neighbour:neighbours){
			if(neighbour != currentPlayerId)
				return true;
		}
		return false;
	}


	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforcement");
		RunningGame.getInstance().getSubject().markAndNotify();

		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		for(CountryModel country : countryModels){
			country.setNumberOfArmies(country.getNumberOfArmies() * 2);
		}

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforcement - Exchange Cards");
		RunningGame.getInstance().getSubject().markAndNotify();
		RunningGame.getInstance().setReinforceCompleted(true);


	}
}
