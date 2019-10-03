package ca.concordia.app.risk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.PlayBusinessDelegate;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.utility.GameUtils;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class PlayController {

  private static final String NONE_DEFAULT_VALUE = "None";
  private static final String COMMAND_EXECUTED_SUCCESSFULLY = "Command executed successfully";
  private static Logger log = LoggerFactory.getLogger(PlayController.class);
  /**
   * 
   */
  @Autowired
  private GameUtils gameUtils;

  /**
   * 
   */
  @Autowired
  private PlayBusinessDelegate playBusinessDelegate;

  @Autowired
  private ShellHelper shellHelper;

  /**
   * 
   */
  @Autowired
  public PlayController() {
    playBusinessDelegate = new PlayBusinessDelegate();
  }

  /**
   * This method "Attacks a player" 1- roll dice & randomly assign a number for
   * players on both sides of the attach 2- checks business in
   * {@link #playBusinessDelegate} 3- display attack result
   * 
   * @param attacker
   * @param defender
   * @param from
   * @param to
   * @return The result of attack -> Example: "player X lose"
   */
  @ShellMethod("Attack a player")
  public String attack(@ShellOption(optOut = false) String attacker, @ShellOption(optOut = false) String defender,
      @ShellOption(optOut = false) String from, @ShellOption(optOut = false) String to) {
    if (log.isDebugEnabled()) {
      log.debug(String.format("inside attack, passed parameters [%s] [%s] [%s] [%s]", attacker, defender, from, to));
    }
    try {
      gameUtils.rollDice();
      playBusinessDelegate.attack(attacker);
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      return shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return shellHelper.getErrorMessage(interruptedException.getMessage());
    }
    return shellHelper.getInfoMessage(String.format("Attacker [%s] lose", attacker));
  }

  @ShellMethod("Fortify, Sample: fortify --fromcountry [countryname] --tocountry [countryname] --num [noofarmies]")
  public String fortify(@ShellOption(value = { "--fromcountry" }, defaultValue = NONE_DEFAULT_VALUE) String fromCountry,
      @ShellOption(value = { "--tocountry" }, defaultValue = NONE_DEFAULT_VALUE) String toCountry,
      @ShellOption(value = { "--none" }, defaultValue = NONE_DEFAULT_VALUE) String none,
      @ShellOption(value = { "--num" }, defaultValue = NONE_DEFAULT_VALUE) String noOfArmies) {
    StringBuilder result = new StringBuilder();
    try {
      if (none != null && !NONE_DEFAULT_VALUE.equals(none)) {
        return "Choose not to do a move";
      }
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
    }
    try {
      if (fromCountry != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(fromCountry) && toCountry != null
          && !NONE_DEFAULT_VALUE.equalsIgnoreCase(toCountry) && noOfArmies != null
          && !NONE_DEFAULT_VALUE.equalsIgnoreCase(noOfArmies)) {
        playBusinessDelegate.fortify(fromCountry, toCountry, Integer.parseInt(noOfArmies));
      } else {
        throw new RiskGameRuntimeException("From Country, to Country and No of Armies all are required");
      }
    } catch (RiskGameRuntimeException riskGameRuntimeException) {
      result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
    }
    if (result.length() == 0) {
      result.append(shellHelper.getSuccessMessage(COMMAND_EXECUTED_SUCCESSFULLY));
    }
    return result.toString();
  }
}