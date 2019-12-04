package ca.concordia.app.risk.model.modes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * specific implementation for Benevolent Strategy
 * 
 * @author i857625
 *
 */
public class BenevolentStrategy extends AbstractStrategy {

  /**
   * Constructor for Benevolent Strategy Class
   * 
   * @param playerModel player model
   */
  public BenevolentStrategy(PlayerModel playerModel) {
    super(playerModel);
  }

  /**
   * {@inheritDoc} This method contains attack method implementation for
   * Benevolent Strategy
   * 
   * @param countryModelFrom attacking country model
   * @param countryModelTo   defender country model
   * @param numDice          number of dice
   */
  @Override
  public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {

    RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Attack");
    RunningGame.getInstance().getSubject().markAndNotify();

    System.out.println("Decided not to attack as its a Benevolent Player");
    RunningGame.getInstance().setAttackCompleted(true);
    RunningGame.getInstance().setCardGiven(false);
    RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortification");
    RunningGame.getInstance().getSubject().markAndNotify();

    // get list of countries assigned to that player
    PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
    List<CountryModel> assignedCountries = playerDaoImpl.getCountries(RunningGame.getInstance(),
        RunningGame.getInstance().getCurrentPlayer().getPlayerModel());
    // get the weakest country country to reinforce
    CountryModel weakestCountry = getWeakestCountry(assignedCountries);
    CountryModel neighbourCountry = getNeighbourStrongCountry(weakestCountry);

    try {
      fortify(weakestCountry, neighbourCountry, neighbourCountry.getNumberOfArmies() - 1);
    } catch (Exception e) {
      RunningGame.getInstance().moveToNextPlayer();
      RunningGame.getInstance().exchangeCardsInitialization();
    }
  }

  /**
   * {@inheritDoc} This method contains fortify method implementation for
   * Benevolent Strategy
   * 
   * @param countryModelFrom the country fortify from model
   * @param countryModelTo   the country fortify to model
   * @param numberOfArmies   number of armies to fortify
   */
  @Override
  public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {
    RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortification");
    RunningGame.getInstance().getSubject().markAndNotify();

    super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
  }

  /**
   * {@inheritDoc} This method contains reinforce method implementation for
   * Benevolent Strategy
   * 
   * @param countryModel   country model
   * @param numberOfArmies number of armies to reinforce
   */
  @Override
  public void reinforce(CountryModel countryModel, int numberOfArmies) {

    RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforce");
    RunningGame.getInstance().getSubject().markAndNotify();
    PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();

    // get list of countries assigned to that player
    List<CountryModel> assignedCountries = playerDaoImpl.getCountries(RunningGame.getInstance(),
        RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

    // get the weakest country country to reinforce
    CountryModel weakestCountry = getWeakestCountry(assignedCountries);

    try {
      super.reinforce(weakestCountry,
          RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      RunningGame.getInstance().setReinforceCompleted(true);
      this.getPlayerModel().setPlayingPhase("Attack");
      RunningGame.getInstance().getSubject().markAndNotify();
    }
    attack(null, null, null);
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
   * This method returns the weakest country that have the lowest number of armies
   * to do reinforcement
   * 
   * @param Countries list of current countries
   * @return
   */
  private CountryModel getWeakestCountry(List<CountryModel> Countries) {

    int minNoOfEnemies = 0;
    CountryModel weakestCountry = null;

    for (CountryModel country : Countries) {
      // set min number of enemies
      if (country.getNumberOfArmies() < minNoOfEnemies || minNoOfEnemies == 0) {
        minNoOfEnemies = country.getNumberOfArmies();
        weakestCountry = country;
      }
    }

    return weakestCountry;
  }

  /**
   * {@inheritDoc} This method returns a neighboring country
   * 
   * @param fortifyFrom the country to fortify from
   * @return
   */
  private CountryModel getNeighbourStrongCountry(CountryModel fortifyFrom) {

    // check at least one of neighbours countries is not an enemy
    BorderDaoImp borderDaoImpl = new BorderDaoImp();
    BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), fortifyFrom.getName());

    List<Integer> allNeighbours = borderModel.getNeighbours();
    CountryDaoImpl countryDao = new CountryDaoImpl();
    CountryModel fortifyTo = null;
    List<Integer> alliedNeighbours = new ArrayList<>();

    for (int i = 0; i < allNeighbours.size(); i++) {
      // check if not an enemy
      // System.out.println("hi"+neighbours.get(i));
      if (allNeighbours.get(i) == RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()) {
        alliedNeighbours.add(allNeighbours.get(i));
      }
    }

    // get random neighbours
    Random random = new Random();
    if (alliedNeighbours.size() > 0) {
      int index = random.nextInt(alliedNeighbours.size());
      fortifyTo = countryDao.findById(RunningGame.getInstance(), alliedNeighbours.get(index));
    }

    return fortifyTo;
  }
}
