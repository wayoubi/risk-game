package ca.concordia.app.risk.model.cache;

import java.util.List;
import java.util.Observable;
import java.util.Random;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class Player extends Observable {

  private PlayerModel playerModel;

  public Player(PlayerModel playerModel) {
    this.setPlayerModel(playerModel);
  }

  public void setPlayerModel(PlayerModel playerModel) {
    this.playerModel = playerModel;
  }

  public PlayerModel getPlayerModel() {
    return playerModel;
  }

  /**
   * 
   * @param countryModel
   * @param numberOfArmies
   */
  public void reinforce(CountryModel countryModel, int numberOfArmies) {

    if (this.getPlayerModel().getReinforcementNoOfArmies() == 0) {
      throw new RiskGameRuntimeException("Reinforcement phase has been completed");
    }
    if (this.getPlayerModel().getReinforcementNoOfArmies() >= numberOfArmies) {
      countryModel.setNumberOfArmies(countryModel.getNumberOfArmies() + numberOfArmies);
      if (this.getPlayerModel().getReinforcementNoOfArmies() - numberOfArmies == 0) {
        RunningGame.getInstance().setReinforceCompleted(true);
      }
      this.getPlayerModel()
          .setReinforcementNoOfArmies(this.getPlayerModel().getReinforcementNoOfArmies() - numberOfArmies);
    } else {
      throw new RiskGameRuntimeException("Please reduce number of armies");
    }
  }

  public void exchangeCards(String[] cardsArray) {

    int reinforcementNoOfArmies = this.getPlayerModel().getReinforcementNoOfArmies();
    int cardExchangeCount = this.getPlayerModel().getCardExchangeCount();

    // Add additional armies
    if (cardExchangeCount == 0) {
      // Add 5 Additional Armies
      reinforcementNoOfArmies += 5;
      cardExchangeCount += 1;
      this.getPlayerModel().setReinforcementNoOfArmies(reinforcementNoOfArmies);
      this.getPlayerModel().setCardExchangeCount(cardExchangeCount);

    } else {
      // Add 5, 10 ,15, 20 , ... Additional Armies

      reinforcementNoOfArmies += 5 * (cardExchangeCount + 1);
      cardExchangeCount += 1;
      this.getPlayerModel().setReinforcementNoOfArmies(reinforcementNoOfArmies);
      this.getPlayerModel().setCardExchangeCount(cardExchangeCount);
    }

    // remove cards from the list
    this.getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[2]) - 1);
    this.getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[1]) - 1);
    this.getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[0]) - 1);

  }

  public void attack(String countryNameFrom, String countyNameTo, String numDice, String allout) {

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();

    // check if countryNameFrom belong to active player
    PlayerModel currentPlayer = this.getPlayerModel();
    CountryModel countryModelFrom = countryDaoImpl.findByName(RunningGame.getInstance(), countryNameFrom);
    CountryModel countryModelTo = countryDaoImpl.findByName(RunningGame.getInstance(), countyNameTo);

    if (countryModelFrom.getPlayerId() != currentPlayer.getId()) {
      throw new RiskGameRuntimeException(countryNameFrom + " does not belong to " + currentPlayer.getName());
    }

    // check if countryNameTo doesn't belong to active player
    if (countryModelTo.getPlayerId() == currentPlayer.getId()) {
      throw new RiskGameRuntimeException(
          countyNameTo + " belongs to " + currentPlayer.getName() + " please choose another country");
    }

    // check max number of dice doesn't exceed 3 and not less than 1
    if (Integer.parseInt(numDice) > 3 || Integer.parseInt(numDice) < 1) {
      throw new RiskGameRuntimeException("number of dice should be between 1 and 3");
    }

    // check number of armies within the country
    if (countryModelFrom.getNumberOfArmies() <= Integer.parseInt(numDice)) {
      throw new RiskGameRuntimeException(
          "Number of dice should be less than the number of armies allocated within the country");
    }

    // check number of armies of attackTo
    if (countryModelTo.getNumberOfArmies() == 0) {
      throw new RiskGameRuntimeException("Number of attackCountryTo is equal to zero");
    }

    // save countries in running game
    RunningGame.getInstance().setAttackCountryNameFrom(countryNameFrom);
    RunningGame.getInstance().setNumDiceAttacker(3);
    RunningGame.getInstance().setAttackCountryNameTo(countyNameTo);

    // defender turn
  }

  /**
   * This method do fortify operation
   *
   * @param fromCountry    origin country to fortify
   * @param toCountry      destination country to fortify
   * @param numberOfArmies number of armies
   */
  public void fortify(String fromCountry, String toCountry, int numberOfArmies) {

    if (!RunningGame.getInstance().isReinforceCompleted())
      throw new RiskGameRuntimeException("Please reinforce first ");

    PlayerModel activePlayerModel = this.getPlayerModel();

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel fromCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), fromCountry);
    CountryModel toCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), toCountry);

    if (activePlayerModel == null) {
      throw new RiskGameRuntimeException("No Players have been added");
    }

    if (fromCountryModel == null) {
      throw new RiskGameRuntimeException("From Country doesn't exist");
    }

    if (toCountryModel == null) {
      throw new RiskGameRuntimeException("To Country doesn't exist");
    }

    if ((fromCountryModel.getPlayerId()) != (activePlayerModel.getId())) {
      throw new RiskGameRuntimeException(
          "From Country " + fromCountry + " is not owned by " + activePlayerModel.getName());
    }

    if ((toCountryModel.getPlayerId()) != (activePlayerModel.getId())) {
      throw new RiskGameRuntimeException("To Country " + toCountry + " is not owned by " + activePlayerModel.getName());
    }

    if (fromCountryModel.getNumberOfArmies() - numberOfArmies < 1) {
      throw new RiskGameRuntimeException("From Country " + fromCountry
          + " needs to have atleast one army after fortification. Please reduce the number of armies");
    }

    AllDirectedPaths<String, DefaultEdge> allDirectedPaths = new AllDirectedPaths<>(
        RunningGame.getInstance().getGraph());
    List<GraphPath<String, DefaultEdge>> allPaths = allDirectedPaths.getAllPaths(fromCountryModel.getName(),
        toCountryModel.getName(), false, 10);
    int counter = 0;
    for (GraphPath<String, DefaultEdge> graphPath : allPaths) {
      List<String> countriesInPath = graphPath.getVertexList();
      boolean connected = true;
      for (String countryName : countriesInPath) {
        CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
        if (activePlayerModel.getId() != countryModel.getPlayerId()) {
          connected = false;
          break;
        }
      }
      if (connected) {
        counter++;
        break;
      }
    }

    if (counter == 0) {

      List<GraphPath<String, DefaultEdge>> allPathsReverse = allDirectedPaths.getAllPaths(toCountryModel.getName(),
          fromCountryModel.getName(), false, 10);
      int counterNew = 0;
      for (GraphPath<String, DefaultEdge> graphPath : allPathsReverse) {
        List<String> countriesInPath = graphPath.getVertexList();
        boolean connected = true;
        for (String countryName : countriesInPath) {
          CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
          if (activePlayerModel.getId() != countryModel.getPlayerId()) {
            connected = false;
            break;
          }
        }
        if (connected) {
          counterNew++;
          break;
        }
      }
      if (counterNew == 0) {
        throw new RiskGameRuntimeException("No Path Available!");
      } else {
        toCountryModel.setNumberOfArmies(toCountryModel.getNumberOfArmies() - numberOfArmies);
        fromCountryModel.setNumberOfArmies(fromCountryModel.getNumberOfArmies() + numberOfArmies);

        // if (!RunningGame.getInstance().isReinforceCompleted()) {
        // throw new RiskGameRuntimeException("Please reinforce first");
        // } else {
        RunningGame.getInstance().moveToNextPlayer();
        RunningGame.getInstance().reinforceInitialization();

        // }

      }
    } else {
      fromCountryModel.setNumberOfArmies(fromCountryModel.getNumberOfArmies() - numberOfArmies);
      toCountryModel.setNumberOfArmies(toCountryModel.getNumberOfArmies() + numberOfArmies);
      // if (!RunningGame.getInstance().isReinforceCompleted()) {
      // throw new RiskGameRuntimeException("Please reinforce first");
      // } else {
      RunningGame.getInstance().moveToNextPlayer();
      RunningGame.getInstance().reinforceInitialization();
      // }
    }
  }
}