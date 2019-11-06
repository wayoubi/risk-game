package ca.concordia.app.risk.model.cache;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class Player extends Observable {

  /**
   * playerModel player Model
   */
  private PlayerModel playerModel;

  /**
   * Constructor(playerModel)
   * 
   * @param playerModel player model
   */
  public Player(PlayerModel playerModel) {
    this.setPlayerModel(playerModel);
  }

  /**
   * set player model
   * 
   * @param playerModel player model
   */
  public void setPlayerModel(PlayerModel playerModel) {
    this.playerModel = playerModel;
  }

  /**
   * get player model
   * 
   * @return playerModel player model
   */
  public PlayerModel getPlayerModel() {
    return playerModel;
  }

  /**
   * This method used for reinforcement
   * 
   * @param countryModel   country Model
   * @param numberOfArmies number of armies
   */
  public void reinforce(CountryModel countryModel, int numberOfArmies) {

    if (this.getPlayerModel().getReinforcementNoOfArmies() == 0) {
      throw new RiskGameRuntimeException("Reinforcement phase has already been completed.");
    }
    if (this.getPlayerModel().getReinforcementNoOfArmies() >= numberOfArmies) {
      countryModel.setNumberOfArmies(countryModel.getNumberOfArmies() + numberOfArmies);
      this.getPlayerModel()
          .setReinforcementNoOfArmies(this.getPlayerModel().getReinforcementNoOfArmies() - numberOfArmies);

      if (this.getPlayerModel().getReinforcementNoOfArmies() == 0) {
        System.out.println("Reinforcement is entirely complete now!");
        RunningGame.getInstance().setReinforceCompleted(true);
        this.getPlayerModel().setPlayingPhase("Reinforcement - Exchange Cards");
        RunningGame.getInstance().getSubject().markAndNotify();
      }
      System.out
          .println("You have " + this.getPlayerModel().getReinforcementNoOfArmies() + " armies left to reinforce");

    } else {
      throw new RiskGameRuntimeException("Please reduce number of armies, you have "
          + this.getPlayerModel().getReinforcementNoOfArmies() + " armies left to reinforce");
    }
  }

  /**
   * This method used for exchange cards
   * 
   * @param cardsArray array of cards
   */
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
    RunningGame.getInstance().setCardExchangeCompleted(true);
    RunningGame.getInstance().setReinforceCompleted(false);

  }

  /**
   * This method used for attack
   * 
   * @param countryNameFrom attacker country name
   * @param countyNameTo    defender country name
   * @param numDice         number of dices for attacker
   */
  public void attack(String countryNameFrom, String countyNameTo, String numDice) {

    if (!RunningGame.getInstance().isCardExchangeCompleted()) {
      throw new RiskGameRuntimeException(
          "Please complete the Reinforcement - Card Exchange phase first. If you do not have any cards or you don't want to exchange please run, exchangecards -none");
    }

    if (!(this.getPlayerModel().getPlayingPhase() == "Attack")) {
      throw new RiskGameRuntimeException("You're not on Attack phase, please check the phase and come back");
    }

    if (RunningGame.getInstance().isAttackCompleted()) {
      throw new RiskGameRuntimeException(
          "You've conquered a country in the previous attack. Please move armies to the country and then continue with your attack");
    }

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();

    // check if countryNameFrom belong to active player
    PlayerModel currentPlayer = this.getPlayerModel();
    CountryModel countryModelFrom = countryDaoImpl.findByName(RunningGame.getInstance(), countryNameFrom);
    CountryModel countryModelTo = countryDaoImpl.findByName(RunningGame.getInstance(), countyNameTo);

    BorderDaoImp borderDaoImpl = new BorderDaoImp();
    BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), countryNameFrom);

    if (countryModelFrom.getPlayerId() != currentPlayer.getId()) {
      throw new RiskGameRuntimeException(countryNameFrom + " does not belong to " + currentPlayer.getName());
    }

    // check if countryNameTo doesn't belong to active player
    if (countryModelTo.getPlayerId() == currentPlayer.getId()) {
      throw new RiskGameRuntimeException(
          countyNameTo + " belongs to " + currentPlayer.getName() + " please choose another country");
    }

    if (!borderModel.getNeighbours().contains(countryModelTo.getId())) {
      throw new RiskGameRuntimeException(
          "The from and to country do not share borders, please select some other country to attack");
    }
    // check number of armies of attackTo
    if (countryModelTo.getNumberOfArmies() == 0) {
      throw new RiskGameRuntimeException("Number of armies in attackCountryTo is equal to zero");
    }

    if (countryModelFrom.getNumberOfArmies() == 1)
      throw new RiskGameRuntimeException("Cannot attack from " + countryNameFrom + " since it has only 1 army left");

    // check max number of dice doesn't exceed 3 and not less than 1
    if (numDice.equalsIgnoreCase("-allout")) {
      int maxDiceAttacker = 3;
      if (countryModelFrom.getNumberOfArmies() <= maxDiceAttacker)
        maxDiceAttacker = countryModelFrom.getNumberOfArmies() - 1;
      RunningGame.getInstance().setAttackCountryNameFrom(countryNameFrom);
      RunningGame.getInstance().setAttackCountryNameTo(countyNameTo);
      RunningGame.getInstance().setNumDiceAttacker(maxDiceAttacker);

      int maxDiceDefender = 2;
      if (maxDiceAttacker < maxDiceDefender)
        maxDiceDefender = maxDiceAttacker;
      if (countryModelTo.getNumberOfArmies() < maxDiceDefender)
        maxDiceDefender = countryModelTo.getNumberOfArmies();

      defend(String.valueOf(maxDiceDefender));

    } else {
      if (Integer.parseInt(numDice) > 3 || Integer.parseInt(numDice) < 1) {
        throw new RiskGameRuntimeException("Number of dice should be between 1 and 3");
      }
      // check number of armies within the country
      if (countryModelFrom.getNumberOfArmies() <= Integer.parseInt(numDice)) {
        throw new RiskGameRuntimeException(
            "Number of dice should be less than the number of armies allocated within the country");
      }

      // save countries in running game
      RunningGame.getInstance().setAttackCountryNameFrom(countryNameFrom);
      RunningGame.getInstance().setNumDiceAttacker(Integer.parseInt(numDice));
      RunningGame.getInstance().setAttackCountryNameTo(countyNameTo);
    }

  }

  /**
   * This method used to give the number of dice to the defender country.
   * 
   * @param numDice number of dices for defender
   */
  public void defend(String numDice) {
    // check max number of dice doesn't exceed 3 and not less than 1
    if (Integer.parseInt(numDice) > 2 || Integer.parseInt(numDice) < 1) {
      throw new RiskGameRuntimeException("number of dice should be 1 or 2");
    }

    if (RunningGame.getInstance().getNumDiceAttacker() == 0) {
      throw new RiskGameRuntimeException("Attack has not been initialized yet!");
    }
    // get Country Model of attackFrom and attackTo
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModelAttackFrom = countryDaoImpl.findByName(RunningGame.getInstance(),
        RunningGame.getInstance().getAttackCountryNameFrom());
    CountryModel countryModelAttackTo = countryDaoImpl.findByName(RunningGame.getInstance(),
        RunningGame.getInstance().getAttackCountryNameTo());

    // roll the Attacker dice
    do {
      Random random = new Random();

      int numDice1 = random.nextInt(5) + 1;
      int numDice2 = 0;
      if (RunningGame.getInstance().getNumDiceAttacker() >= 2) {
        numDice2 = random.nextInt(5) + 1;
      }
      int numDice3 = 0;
      if (RunningGame.getInstance().getNumDiceAttacker() == 3) {
        numDice3 = random.nextInt(5) + 1;
      }

      int[] attackerDice;
      if (RunningGame.getInstance().getNumDiceAttacker() == 3) {
        attackerDice = new int[] { numDice1, numDice2, numDice3 };
      } else if (RunningGame.getInstance().getNumDiceAttacker() == 2) {
        attackerDice = new int[] { numDice1, numDice2 };
      } else {
        attackerDice = new int[] { numDice1 };
      }

      // save the dice in running game to compare later
      RunningGame.getInstance().setAttackerDice(attackerDice);

      // roll dice
      int numDice1D = random.nextInt(5) + 1;
      int numDice2D = 0;
      if (Integer.parseInt(numDice) == 2) {
        numDice2D = random.nextInt(5) + 1;
      }

      int[] defenderDice;
      if (Integer.parseInt(numDice) == 2) {
        defenderDice = new int[] { numDice1D, numDice2D };
      } else {
        defenderDice = new int[] { numDice1D };
      }

      // sort Arrays
      Arrays.sort(attackerDice);
      Arrays.sort(defenderDice);

      for (int i = 0, j = attackerDice.length - 1, tmp; i < j; i++, j--) {
        tmp = attackerDice[i];
        attackerDice[i] = attackerDice[j];
        attackerDice[j] = tmp;
      }

      for (int i = 0, j = defenderDice.length - 1, tmp; i < j; i++, j--) {
        tmp = defenderDice[i];
        defenderDice[i] = defenderDice[j];
        defenderDice[j] = tmp;
      }

      System.out.println("Attacker Dice Roll Results");
      for (int die : attackerDice) {
        System.out.print(die + " ");
      }
      System.out.println();
      System.out.println("Defender Dice Roll Results");
      for (int die : defenderDice) {
        System.out.print(die + " ");
      }
      System.out.println();

      // compare
      switch (numDice) {
      // in case defender use one die
      case "1":
        if (attackerDice[0] > defenderDice[0]) {
          countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies() - 1);
        } else {
          countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies() - 1);
        }
        break;

      // in case defender use two dice
      case "2":
        if (attackerDice[0] > defenderDice[0]) {
          countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies() - 1);
        } else {
          countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies() - 1);
        }

        if (attackerDice[1] > defenderDice[1]) {
          if (countryModelAttackTo.getNumberOfArmies() > 0) {
            countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies() - 1);
          }
        } else {
          if (countryModelAttackFrom.getNumberOfArmies() > 0) {
            countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies() - 1);
          }
        }
        break;

      default:
      }

      if (RunningGame.getInstance().isAllOut()
          && countryModelAttackFrom.getNumberOfArmies() < (RunningGame.getInstance().getNumDiceAttacker())) {
        RunningGame.getInstance().setNumDiceAttacker(countryModelAttackFrom.getNumberOfArmies() - 1);
        numDice = String.valueOf(countryModelAttackFrom.getNumberOfArmies() - 1);
      }

      if (RunningGame.getInstance().isAllOut()
          && countryModelAttackTo.getNumberOfArmies() < Integer.parseInt(numDice)) {
        numDice = String.valueOf(countryModelAttackTo.getNumberOfArmies());
      }

      // Check if defender won
      if (countryModelAttackFrom.getNumberOfArmies() == 1) {
        RunningGame.getInstance().setAttackerWin(false);
        RunningGame.getInstance().setDefenderWin(true);
        // RunningGame.getInstance().setAttackCompleted(true);
        RunningGame.getInstance().setAllOut(false);
        System.out.println("Attacker " + this.getPlayerModel().getName() + " loses");

        // Check if Attacker won
      } else if (countryModelAttackTo.getNumberOfArmies() == 0) {
        RunningGame.getInstance().setAttackerWin(true);
        RunningGame.getInstance().setDefenderWin(false);
        RunningGame.getInstance().setAttackCompleted(true);
        RunningGame.getInstance().setAllOut(false);
        countryModelAttackTo.setPlayerId(this.getPlayerModel().getId());
        if (!RunningGame.getInstance().isCardGiven()) {
          giveCard();
          RunningGame.getInstance().setCardGiven(true);
        }
        System.out.println(
            "Attacker " + this.getPlayerModel().getName() + " has conquered country " + countryModelAttackTo.getName());
      }

      RunningGame.getInstance().getSubject().markAndNotify();

    } while (RunningGame.getInstance().isAllOut()
        && countryModelAttackFrom.getNumberOfArmies() >= (RunningGame.getInstance().getNumDiceAttacker()));

    RunningGame.getInstance().setNumDiceAttacker(0);
  }

  /**
   * This method used to move the number of armies from attacker to the defender
   * country if the attacker has conquered the defender.
   * 
   * @param num number of armies to move from attackers country to defenders
   *            country
   */
  public void attackMove(String num) {

    if (!RunningGame.getInstance().isAttackCompleted()) {
      throw new RiskGameRuntimeException("You've not conquered any country to move the armies");
    }

    // if (RunningGame.getInstance().isDefenderWin()) {
    // throw new RiskGameRuntimeException("Defender had won, you can't make a
    // move");
    // }

    // check num are not greater than the number of armies in the attackCountryFrom
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModelAttackFrom = countryDaoImpl.findByName(RunningGame.getInstance(),
        RunningGame.getInstance().getAttackCountryNameFrom());

    if (countryModelAttackFrom.getNumberOfArmies() - 1 < Integer.parseInt(num)) {
      throw new RiskGameRuntimeException("You don't have enough armies, please reduce the number of armies");
    }
    // at least one army should be there or more depends on the number of dice
    // if (countryModelAttackFrom.getNumberOfArmies() - 1 < Integer.parseInt(num)) {
    if (RunningGame.getInstance().getAttackerDice().length > Integer.parseInt(num)) {
      throw new RiskGameRuntimeException(
          "You have to move atleast the number of armies equivalent to the dice you used to conquer! The number of dice was "
              + RunningGame.getInstance().getAttackerDice().length);
    }

    // move armies
    CountryModel countryModelAttackTo = countryDaoImpl.findByName(RunningGame.getInstance(),
        RunningGame.getInstance().getAttackCountryNameTo());
    countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies() - Integer.parseInt(num));
    countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies() + Integer.parseInt(num));
    RunningGame.getInstance().setAttackCompleted(false);
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method used for fortify operation
   *
   * @param fromCountry    origin country to fortify
   * @param toCountry      destination country to fortify
   * @param numberOfArmies number of armies
   */
  public void fortify(String fromCountry, String toCountry, int numberOfArmies) {

    if (!(this.getPlayerModel().getPlayingPhase() == "Fortification"))
      throw new RiskGameRuntimeException("Please complete the current phase you're on first");

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
        RunningGame.getInstance().moveToNextPlayer();
        RunningGame.getInstance().reinforceInitialization();
      }
    } else {
      fromCountryModel.setNumberOfArmies(fromCountryModel.getNumberOfArmies() - numberOfArmies);
      toCountryModel.setNumberOfArmies(toCountryModel.getNumberOfArmies() + numberOfArmies);
      RunningGame.getInstance().moveToNextPlayer();
      RunningGame.getInstance().reinforceInitialization();
    }
  }

  /**
   * Type of cards
   */

  enum cards {
    Infantry, Cavalry, Artillery
  }

  /**
   * This method is used for randomly distribute the cards to players
   */
  public void giveCard() {
    Random random = new Random();
    int num = random.nextInt(3);
    this.getPlayerModel().getCards().getList().add(String.valueOf(cards.values()[num]));
  }
}