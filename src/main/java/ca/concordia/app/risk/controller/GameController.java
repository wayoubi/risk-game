package ca.concordia.app.risk.controller;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.shell.ShellHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * GameController
 * 
 * @author i857625
 *
 */
@ShellComponent
public class GameController {

  private static Logger log = LoggerFactory.getLogger(GameController.class);

  private static final String NONE_DEFAULT_VALUE = "None";

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
  public String save() {

    if (log.isDebugEnabled()) {
      log.debug("inside save");
    }
    try {
      gameBusinessDelegate.saveGame();
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("Game saved successfully");
  }

  /**
   * This method saves the current map file under the saved directory. Command:
   * savemap -file [fileName]
   *
   * @param fileName file name to save
   * @return operation result (error/success)
   */
  @ShellMethod("Save the current gamemap using domination map file format under the saved directory")
  public String savemap(@ShellOption(value = { "-file" }) String fileName) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside savemap, passed parameters [%s]", fileName));
    }
    try {
      gameBusinessDelegate.saveMap(fileName);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("Game Map saved successfully");
  }

  /**
   * This method validates map, whether graph is connected or not (per continent).
   * Command: validatemap -continent [continentName]
   *
   * @param continentName continent name for validation
   * @return operation result (error/success)
   */
  @ShellMethod("Validate the current gamemap to be connected")
  public String validatemap(@ShellOption(value = { "-continent" }, defaultValue = "All") String continentName) {

    if (log.isDebugEnabled()) {
      log.debug("inside validatemap");
    }
    boolean isConnected = false;
    try {
      isConnected = gameBusinessDelegate.validateMap(continentName);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }
    if (!isConnected) {
      return shellHelper.getErrorMessage("Countries are not connected, Map is invalid");
    }

    return shellHelper.getSuccessMessage("Countries are connected, Map is valid");
  }

  /**
   * This method add/remove/edit Player.
   * 
   * Add Command: gameplayer -add [player2Add] Edit Command: gameplayer -add
   * [player2Add] [player2Remove] Remove Command: gameplayer -remove
   * [player2Remove]
   *
   * @param player2Add    player name to add
   * @param player2Remove player name to remove
   * @return operation result (error/success)
   */
  @ShellMethod("Add/Remove player")
  public String gameplayer(@ShellOption(value = { "-add" }, defaultValue = NONE_DEFAULT_VALUE) String player2Add,
      @ShellOption(value = { "-remove" }, defaultValue = "None") String player2Remove) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside gameplayer, passed parameters [%s] [%s]", player2Add, player2Remove));
    }
    try {
      if (player2Add != null && !"None".equalsIgnoreCase(player2Add)) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setName(player2Add);
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
   * This method open a map file to edit. Command: editmap --file-name [fileName]
   *
   * @param fileName file name to edit the map
   * @return operation result (error/success)
   */
  @ShellMethod("Edit map saved in file")
  public String editmap(@ShellOption(optOut = false) String fileName) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside editmap, passed parameters [%s]", fileName));
    }
    try {
      gameBusinessDelegate.editMap(fileName);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("Map file is read, you can edit now");
  }

  /**
   * This method load a saved map file. Command: loadmap --file-name [fileName]
   *
   * @param fileName file name to load the map
   * @return operation result (error/success)
   */
  @ShellMethod("Load map saved in file")
  public String loadmap(@ShellOption(optOut = false) String fileName) {

    if (log.isDebugEnabled()) {
      log.debug(String.format("inside loadmap, passed parameters [%s]", fileName));
    }
    try {
      gameBusinessDelegate.loadMap(fileName);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }
    return shellHelper.getSuccessMessage("Map file is read, map is loaded");

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
    try {
      gameBusinessDelegate.reinforce(countryName, numberOfArmies);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    return shellHelper.getSuccessMessage("reinforcement has been completed");
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

    return shellHelper.getSuccessMessage("All remaining unplaced armies have been assigned");
  }

  /**
   * This method is used for fortification of armies from one country to another.
   * The user can choose to not fortify at all.
   * 
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

    StringBuilder result = new StringBuilder();

    try {
      if (fromCountry != null && "none".equals(fromCountry)) {
        gameBusinessDelegate.moveToNextPlayer();
        return "Choose not to do a move";
      }
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    }

    try {
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
}