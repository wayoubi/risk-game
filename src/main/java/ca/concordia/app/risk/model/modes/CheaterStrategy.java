package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

import java.util.List;

/**
 * This class inherits from AbstractStrategy class to override methods and have
 * specific implementation for Cheater Strategy
 * 
 * @author i857625
 *
 */
public class CheaterStrategy extends AbstractStrategy {

  /**
   * Constructor for Cheater Strategy Class
   * 
   * @param playerModel player model
   */
  public CheaterStrategy(PlayerModel playerModel) {
    super(playerModel);
  }

  /**
   * {@inheritDoc} This method contains attack method implementation for Cheater
   * Strategy
   * 
   * @param countryModelFrom attacking country model
   * @param countryModelTo   defender country model
   * @param numDice          number of dice
   */
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
          System.out.println(countryModel.getName() + " : " + neighbour);

          // assigned to the new player
          countryModel.setPlayerId(RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId());

          // move one army to the new country
          countryModel.setNumberOfArmies(1);
          country.setNumberOfArmies(country.getNumberOfArmies() - 1);

          // update neighbours

          RunningGame.getInstance().getSubject().markAndNotify();
        }
      }
    }

    boolean gameCompleted = RunningGame.getInstance().checkGameCompleted();
    if (gameCompleted) {
      RunningGame.reset();
    } else {
      fortify(null, null, 0);
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
   * {@inheritDoc} This method contains fortify method implementation for Cheater
   * Strategy
   * 
   * @param countryModelFrom the country fortify from model
   * @param countryModelTo   the country fortify to model
   * @param numberOfArmies   number of armies to fortify
   */
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

      if (isNeighbourEnemy(neighbours, RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId())) {
        country.setNumberOfArmies(country.getNumberOfArmies() * 2);
        RunningGame.getInstance().getSubject().markAndNotify();
      }
    }

    RunningGame.getInstance().moveToNextPlayer();
    RunningGame.getInstance().exchangeCardsInitialization();
    RunningGame.getInstance().getSubject().markAndNotify();
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
   * {@inheritDoc} This method contains reinforce method implementation for
   * Cheater Strategy
   * 
   * @param countryModel   country model
   * @param numberOfArmies number of armies to reinforce
   */
  @Override
  public void reinforce(CountryModel countryModel, int numberOfArmies) {

    RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforcement");
    RunningGame.getInstance().getSubject().markAndNotify();

    PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
    List<CountryModel> countryModels = playerDaoImpl.getCountries(RunningGame.getInstance(),
        RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

    for (CountryModel country : countryModels) {
      country.setNumberOfArmies(country.getNumberOfArmies() * 2);
    }

    RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Attack");
    RunningGame.getInstance().getSubject().markAndNotify();
    RunningGame.getInstance().setReinforceCompleted(true);
    attack(null, null, null);
  }
}
