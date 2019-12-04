package ca.concordia.app.risk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * GameController has all the commands related to saving, loading and editing
 * the game
 * 
 * @author i857625
 */
@ShellComponent
public class GameController {

  /**
   * Class Logger
   */
  private static Logger log = LoggerFactory.getLogger(GameController.class);

  /**
   * Default Shell parameter value
   */
  private static final String NONE_DEFAULT_VALUE = "None";

  /**
   * Default Player Strategy
   */
  public static final String DEFAULT_PLAYER_STRATEGY = "HUMAN";

  /**
   * shellHelper bean
   */
  @Autowired
  ShellHelper shellHelper;

  /**
   * Dependency injection from GameBusinessDelegate
   */
  @Autowired
  private GameBusinessDelegate gameBusinessDelegate;

  /**
   * This method saves current game state. Command: save
   *
   * @return operation result (error/success)
   */
  @ShellMethod("Save the current game state")
  public String savegame() {

    if (log.isDebugEnabled()) {
      log.debug("inside save game");
    }
    try {
      gameBusinessDelegate.saveGame();
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }
    return shellHelper.getSuccessMessage("Game saved successfully");
  }

  /**
   * This method saves current game state. Command: save
   *
   * @return operation result (error/success)
   */
  @ShellMethod("load the saved game state")
  public String loadgame() {

    if (log.isDebugEnabled()) {
      log.debug("inside load game");
    }
    try {
      gameBusinessDelegate.loadGame();
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }
    return shellHelper.getSuccessMessage("Game loaded successfully");
  }

  /**
   * This method add/remove/edit Player.
   * <p>
   * <br>
   * Add Command: gameplayer -add [player2Add] <br>
   * Edit Command: gameplayer -add [player2Add] [player2Remove] <br>
   * Remove Command: gameplayer -remove [player2Remove]
   *
   * @param player2Add    player name to add
   * @param player2Remove player name to remove
   * @return operation result (error/success)
   */
  @ShellMethod("Add/Remove player")
  public String gameplayer(@ShellOption(value = { "-add" }, defaultValue = NONE_DEFAULT_VALUE) String player2Add,
      @ShellOption(value = { "-strategy" }, defaultValue = DEFAULT_PLAYER_STRATEGY) String playerStrategy,
      @ShellOption(value = { "-remove" }, defaultValue = NONE_DEFAULT_VALUE) String player2Remove) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside gameplayer, passed parameters [%s] [%s]", player2Add, player2Remove));
    }
    try {
      if (player2Add != null && !"None".equalsIgnoreCase(player2Add)) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setName(player2Add);
        playerDto.setStrategy(playerStrategy.toUpperCase());
        gameBusinessDelegate.addPlayer(playerDto);
      }
      if (player2Remove != null && !"None".equalsIgnoreCase(player2Remove)) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setName(player2Remove);
        gameBusinessDelegate.removePlayer(playerDto);
      }
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return "Player edited successfully";
  }

  /**
   * This method assigns countries to the players. Command: populatecountries
   *
   * @return operation result (error/success)
   */
  @ShellMethod("Populate countries")
  public String populatecountries() {

    if (log.isDebugEnabled()) {
      log.debug("inside populatecountries");
    }
    gameBusinessDelegate.populateCountries();

    return "Countries has been randomly assigned to players.";
  }

  /**
   * This method assigns armies to the countries. Command: placearmy -countryname
   * [countryName]
   *
   * @param countryName country name to place the army
   * @return operation result (error/success)
   */
  @ShellMethod("Placearmy")
  public String placearmy(
      @ShellOption(value = { "-countryname" }, defaultValue = NONE_DEFAULT_VALUE) String countryName) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside placearmy, passed parameters [%s]", countryName));
    }
    try {
      gameBusinessDelegate.placeArmy(countryName);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return "An Army has been assigned to this country.";
  }

  /**
   * This method does Reinforcement. Command: reinforce -countryName [countryName]
   * -number [numberOfArmies]
   *
   * @param countryName    country name which is doing reinforcement
   * @param numberOfArmies number of armies
   * @return operation result (error/success)
   */
  @ShellMethod("Reinforcement")
  public String reinforce(
      @ShellOption(value = { "-countryName" }, defaultValue = NONE_DEFAULT_VALUE) String countryName,
      @ShellOption(value = { "-number" }, defaultValue = "None") int numberOfArmies) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside loadmap, passed parameters [%s] [%s]", countryName, numberOfArmies));
    }
    if (numberOfArmies < 0) {
      throw new RiskGameRuntimeException("The armies cannot be negative");
    }
    try {
      gameBusinessDelegate.reinforce(countryName, numberOfArmies);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("Reinforcement has been completed");
  }

  /**
   * This method place all remaining armies. Command: placeall
   *
   * @return operation result (error/success)
   */
  @ShellMethod("Place all")
  public String placeall() {

    if (log.isDebugEnabled()) {
      log.debug("inside placeall");
    }
    try {
      gameBusinessDelegate.placeall();
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("Ok, Done!");
  }

  /**
   * This method is used for fortification of armies from one country to another.
   * The user can choose to not fortify at all.
   * <p>
   * Command: fortify -fromcountry [fromCountryname] -tocountry [toCountryname]
   * -num [numberOfArmies]
   *
   * @param fromCountry    origin country to fortify from
   * @param toCountry      destination country to fortify to
   * @param numberOfArmies number of armies to fortify
   * @return operation result (error/success)
   */
  @ShellMethod("Fortify, Sample: fortify -fromcountry [countryname] -tocountry [countryname] -num [noofarmies]")
  public String fortify(@ShellOption(value = { "-fromcountry" }, defaultValue = NONE_DEFAULT_VALUE) String fromCountry,
      @ShellOption(value = { "-tocountry" }, defaultValue = NONE_DEFAULT_VALUE) String toCountry,
      @ShellOption(value = { "-num" }, defaultValue = NONE_DEFAULT_VALUE) String numberOfArmies) {

    if (log.isDebugEnabled()) {
      log.debug(
          String.format("inside fortify, passed parameters [%s] [%s] [%s]", fromCountry, toCountry, numberOfArmies));
    }

    try {
      if (fromCountry != null && "none".equals(fromCountry)) {
        RunningGame.getInstance().moveToNextPlayer();
        RunningGame.getInstance().exchangeCardsInitialization();
        RunningGame.getInstance().getSubject().markAndNotify();

        return "Choose not to do a move";
      }
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    try {
      if (Integer.parseInt(numberOfArmies) < 1) {
        throw new RiskGameRuntimeException("The number of armies has to be atleast 1!!");
      }
      if (fromCountry != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(fromCountry) && toCountry != null
          && !NONE_DEFAULT_VALUE.equalsIgnoreCase(toCountry) && numberOfArmies != null
          && !NONE_DEFAULT_VALUE.equalsIgnoreCase(numberOfArmies)) {

        gameBusinessDelegate.fortify(fromCountry, toCountry, Integer.parseInt(numberOfArmies));
      } else {
        throw new RiskGameRuntimeException("From Country, to Country and No of Armies all are required");
      }
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("Fortification was successful");
  }

  /**
   * This method is used for exchange cards to get armies in return.
   * <p>
   * Command: exchangecards -num1 [num1] -num2 [num2] -num3 [num3]
   * 
   * @param num1 card 1
   * @param num2 card 2
   * @param num3 card 3
   * @return log message related to exchange cards operation
   */
  @ShellMethod("exchangecards")
  public String exchangecards(@ShellOption(value = { "-num1" }, defaultValue = NONE_DEFAULT_VALUE) String num1,
      @ShellOption(value = { "-num2" }, defaultValue = NONE_DEFAULT_VALUE) String num2,
      @ShellOption(value = { "-num3" }, defaultValue = NONE_DEFAULT_VALUE) String num3) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside exchangecards, passed parameters [%s] [%s] [%s]", num1, num2, num3));
    }
    if ("-none".equalsIgnoreCase(num1)) {
      if (RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().size() >= 5) {
        RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforcement - Exchange Cards");
        RunningGame.getInstance().setCardExchangeCompleted(false);
        RunningGame.getInstance().getSubject().markAndNotify();
        throw new RiskGameRuntimeException(
            "You have " + RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().size()
                + " You've to compulsorily exchange your cards first");
      }

      RunningGame.getInstance().setCardExchangeCompleted(true);
      RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Reinforce");
      RunningGame.getInstance().getSubject().markAndNotify();

      // if
      // (!"HUMAN".equalsIgnoreCase(RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getStrategy()))
      // {
      // RunningGame.getInstance().getCurrentPlayer().getStrategy().attack(null, null,
      // null);
      // }

      return "No cards have been exchanged";
    } else {
      try {
        gameBusinessDelegate.exchangecards(num1, num2, num3);
      } catch (RiskGameRuntimeException riskGameRuntimeException) {
        return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
      }
      return "Card " + num1 + " " + num2 + " " + num3 + " have been exchanged";
    }
  }

  /**
   * This method used for attack from country to another country.
   * <p>
   * Command: attack -countryNameFrom [countryNameFrom] -countyNameTo
   * [countyNameTo] -numdice [numDice]
   * 
   * @param countryNameFrom attacker country name
   * @param countyNameTo    defender country name
   * @param numDice         number of dices for attacker
   * @return log message related to attack operation
   */
  @ShellMethod("attack")
  public String attack(
      @ShellOption(value = { "-countrynamefrom" }, defaultValue = NONE_DEFAULT_VALUE) String countryNameFrom,
      @ShellOption(value = { "-countynameto" }, defaultValue = NONE_DEFAULT_VALUE) String countyNameTo,
      @ShellOption(value = { "-numdice" }, defaultValue = NONE_DEFAULT_VALUE) String numDice) {

    if (log.isDebugEnabled()) {
      log.debug(
          String.format("inside attack, passed parameters [%s] [%s] [%s]", countryNameFrom, countyNameTo, numDice));
    }

    if ("-noattack".equalsIgnoreCase(countryNameFrom)) {
      RunningGame.getInstance().setAttackCompleted(true);
      RunningGame.getInstance().setCardGiven(false);
      RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortification");
      RunningGame.getInstance().getSubject().markAndNotify();
      return "Ending the Attack phase, moving to Fortification";
    } else if (numDice.equalsIgnoreCase("None")) {
      throw new RiskGameRuntimeException("You need to pass either the number of dice or run attack in -allout mode");
    } else {
      try {
        gameBusinessDelegate.attack(countryNameFrom, countyNameTo, numDice);
      } catch (RiskGameRuntimeException riskGameRuntimeException) {
        return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
      }
      if (numDice.equalsIgnoreCase("-allout")) {
        return "Attack in All-Out Mode Completed";
      } else {
        return "Single Attack with specified number of dice initiated, waiting for defender dice";
      }
    }
  }

  /**
   * This method used to give the number of dice to the defender country.
   * <p>
   * Command: defend -numdice[numDice]
   * 
   * @param numDice number of dices for defender
   * @return log message related to defend operation
   */
  @ShellMethod("defend")
  public String defend(@ShellOption(value = { "-numdice" }, defaultValue = NONE_DEFAULT_VALUE) String numDice) {
    try {
      gameBusinessDelegate.defend(numDice);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return "Single Attack with specified dice completed";
  }

  /**
   * This method used to move the number of armies from attacker to the defender
   * country if the attacker has conquered the defender.
   * <p>
   * Command: defend -num[num]
   * 
   * @param num number of armies to move from attackers country to defenders
   *            country
   * @return log message related to moved armies operation
   */
  @ShellMethod("attackmove")
  public String attackmove(@ShellOption(value = { "-num" }, defaultValue = NONE_DEFAULT_VALUE) String num) {
    if (Integer.parseInt(num) < 1) {
      throw new RiskGameRuntimeException("The number of armies has to be atleast 1!!");
    }
    try {
      gameBusinessDelegate.attackmove(num);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return "Armies have been moved to the conquered country";
  }

  /**
   * This method is used for tournament mode .
   * <p>
   * Command: attack -countryNameFrom [countryNameFrom] -countyNameTo
   * [countyNameTo] -numdice [numDice]
   * 
   * @param countryNameFrom attacker country name
   * @param countyNameTo    defender country name
   * @param numDice         number of dices for attacker
   * @return log message related to attack operation
   */
  @ShellMethod("attack")
  public String tournament(@ShellOption(value = { "-M" }, defaultValue = NONE_DEFAULT_VALUE) String mapFiles,
      @ShellOption(value = { "-P" }, defaultValue = NONE_DEFAULT_VALUE) String playerStrategies,
      @ShellOption(value = { "-G" }, defaultValue = NONE_DEFAULT_VALUE) String noOfGames,
      @ShellOption(value = { "-D" }, defaultValue = NONE_DEFAULT_VALUE) String maxTurns) {

    StringBuilder result = new StringBuilder();

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside tournament, passed parameters [%s] [%s] [%s] [%s]", mapFiles, playerStrategies,
          noOfGames, maxTurns));
    }

    try {
      gameBusinessDelegate.tournament(mapFiles, playerStrategies, noOfGames, maxTurns);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }
    return "Tournament Complete";
  }

}