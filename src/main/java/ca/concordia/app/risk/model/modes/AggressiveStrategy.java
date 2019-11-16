package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import org.junit.runners.Suite;

import java.util.List;

/**
 * 
 * @author i857625
 *
 */
public class AggressiveStrategy extends AbstractStrategy {

	/**
	 * 
	 * @param playerModel
	 */
	public AggressiveStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

    @Override
    public void reinforce(CountryModel countryModel, int numberOfArmies) {

	    CountryModel attackFrom = getStrongestCountry();

        // check if attackFrom is not null
        if (attackFrom == null) {
            throw new RiskGameRuntimeException("attackFrom is null, reinforcement is not possible");
        }

        // reinforce with maximum number of enemies
        super.reinforce(attackFrom,RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());
    }


    @Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {


		PlayerDaoImpl playerDaoImpl	= new PlayerDaoImpl();

		//get all countries
		List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		int maxNoOfArmies=0;

		CountryModel attackFrom =null;

		CountryModel attackTo=null;

		BorderModel borderModel=null;

		List<Integer> neighbours=null;

		for(CountryModel country:countryModels){

			int numOfArmies = country.getNumberOfArmies();
			if(numOfArmies>maxNoOfArmies) {

				//check at least one of neighbours countries is an enemy
				neighbours = borderModel.getNeighbours();

				int currentPlayerId=RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId();

				// check if at least of the neighbours is enemy
				if(isNeighbourEnemy(neighbours,currentPlayerId)){

					attackFrom = country;
					// get neighbour country with the min number of armies
					attackTo = GetWeakestCountry(neighbours,currentPlayerId);
				}
			}
		}

		// check if attackFrom is not null
		if (attackFrom == null) {
			throw new RiskGameRuntimeException("No attackFrom is asigned, Attack is not possible");
		}

		// check if attackTo is not null
		if (attackTo == null) {
			throw new RiskGameRuntimeException("No attackTo is asigned, Attack is not possible");
		}

		// reinforce with maximum number of enemies
		super.reinforce(attackFrom,RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());

		//attack
		boolean attack=true;

		while(attack) {

		    // get the Weakest Country
			attackTo = GetWeakestCountry(neighbours,RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId());

			if (attackTo != null) {
				super.attack(attackFrom, attackTo, "-allout");
			} else {
				attack = false;

				// Fortify logic comes here


			}

		}
	}



	private CountryModel GetWeakestCountry(List<Integer> neighbours,int currentPlayerId) {

		CountryDaoImpl countryDao = new CountryDaoImpl();
		int minNoOfEnemies=0;
		int attackToId = -1;


		for(int neighbour: neighbours){
			// check if enemy
			if(neighbour != currentPlayerId){
				CountryModel countryModel=null;
				countryModel = countryDao.findById(RunningGame.getInstance(),neighbour);

				//set min number of enemies
				if(countryModel.getNumberOfArmies()<minNoOfEnemies){
					minNoOfEnemies=countryModel.getNumberOfArmies();
					attackToId=neighbour;
				}
			}
		}
		return countryDao.findById(RunningGame.getInstance(),attackToId);

	}

	private boolean isNeighbourEnemy(List<Integer> neighbours, int currentPlayerId) {

		for(int neighbour:neighbours){
			if(neighbour != currentPlayerId)
				return true;
		}
		return false;
	}

    private CountryModel getStrongestCountry() {

        PlayerDaoImpl playerDaoImpl	= new PlayerDaoImpl();

        //get all countries
        List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
                RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

        int maxNoOfArmies=0;

        CountryModel attackFrom =null;

        BorderModel borderModel=null;

        List<Integer> neighbours=null;

        for(CountryModel country:countryModels){

            int numOfArmies = country.getNumberOfArmies();
            if(numOfArmies>maxNoOfArmies) {

                // check at least one of neighbours countries is an enemy 
                
                // check if this is the right syntax to get neighbours of "country"
                neighbours = borderModel.getNeighbours();

                int currentPlayerId=RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId();

                // check if at least of the neighbours is enemy
                if(isNeighbourEnemy(neighbours,currentPlayerId)){
                    attackFrom = country;
                }
            }
        }
        return attackFrom;
    }

}
