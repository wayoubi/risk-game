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

/**
 * PlayController class has all commanded for playing the game example
 * attacking other player
 * 
 * @author i857625
 */
@ShellComponent
public class PlayController {

	/**
	 * Shell default parameter value
	 */
	private static final String NONE_DEFAULT_VALUE = "None";

	/**
	 * Successful execution message
	 */
	private static final String COMMAND_EXECUTED_SUCCESSFULLY = "Command executed successfully";

	/**
	 * Class Logger
	 */
	private static Logger log = LoggerFactory.getLogger(PlayController.class);

	/**
	 * Dependency injection from GameUtils
	 */
	@Autowired
	private GameUtils gameUtils;

	/**
	 * Dependency injection from PlayBusinessDelegate
	 */
	@Autowired
	private PlayBusinessDelegate playBusinessDelegate;

	/**
	 * Dependency injection from ShellHelper
	 */
	@Autowired
	private ShellHelper shellHelper;

	/**
	 * Dependency injection from PlayBusinessDelegate
	 */
	@Autowired
	public PlayController() {
		playBusinessDelegate = new PlayBusinessDelegate();
	}

	/**
	 * This method "Attacks a player" 1- roll dice and randomly assign a number for
	 * players on both sides of the attach 2- checks business in
	 * {@link #playBusinessDelegate} 3- display attack result
	 * 
	 * Command: attack --attacker [attackerPlayerName] --defender
	 * [defenderPlayerName] --from [attackerCountryName] --to [defenderCountryName]
	 * 
	 * @param attacker attacker's country name
	 * @param defender defender's country name
	 * @param from     origin country of attack
	 * @param to       destination country of the attack (defender's country)
	 * @return The result of attack, Example: "player X lose"
	 */
	@ShellMethod("Attack a player")
	public String Oldattack(@ShellOption(optOut = false) String attacker, @ShellOption(optOut = false) String defender,
			@ShellOption(optOut = false) String from, @ShellOption(optOut = false) String to) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("inside attack, passed parameters [%s] [%s] [%s] [%s]", attacker, defender, from,
					to));
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
}