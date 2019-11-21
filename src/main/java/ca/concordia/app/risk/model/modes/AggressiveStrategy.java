package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ch.qos.logback.core.net.SyslogOutputStream;
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

		CountryModel attackTo = getWeakestNeighbourCountry(attackFrom);

//		System.out.println(attackFrom.getName() +" "+ attackTo.getName());
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Exchange Cards");
		RunningGame.getInstance().getSubject().markAndNotify();
    }



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

		//attack
		boolean attack=true;
		CountryModel previousCountryModelTo=null;

		while(attack) {
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

	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {
		super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
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


        List<Integer> neighbours=null;

        for(CountryModel country:countryModels){

            int numOfArmies = country.getNumberOfArmies();
            if(numOfArmies>maxNoOfArmies) {

                // check at least one of neighbours countries is an enemy 
				BorderDaoImp borderDaoImpl = new BorderDaoImp();
				BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), country.getName());

                neighbours = borderModel.getNeighbours();

                // check if at least of the neighbours is enemy
                if(isNeighbourEnemy(neighbours,RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId())){
                    attackFrom = country;
                }
            }
        }
        return attackFrom;
    }


	private CountryModel getWeakestNeighbourCountry(CountryModel attackFrom) {



		// check at least one of neighbours countries is an enemy
		BorderDaoImp borderDaoImpl = new BorderDaoImp();
		BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), attackFrom.getName());

		List<Integer> neighbours = borderModel.getNeighbours();

		CountryDaoImpl countryDao = new CountryDaoImpl();

		int minNoOfEnemies=-1;
		CountryModel attackTo = null;


		for(int neighbour: neighbours){
			// check if enemy
			if(neighbour != RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()){

				// get country model
				CountryModel countryModel = null;
				countryModel = countryDao.findById(RunningGame.getInstance(),neighbour);

				//get country model of the lowest number of enemies
				if(countryModel.getNumberOfArmies()<minNoOfEnemies || minNoOfEnemies==-1){
					minNoOfEnemies=countryModel.getNumberOfArmies();
					int attackToId = neighbour;
					attackTo = countryDao.findById(RunningGame.getInstance(),attackToId);
				}
			}
		}
		return attackTo;

	}


}
