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
 * 
 * @author i857625
 *
 */
@ShellComponent
public class PlayController {

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

	/**
	 * 
	 */
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
	 * This method "Attacks a player"
	 * 1- roll dice & randomly assign a number for players on both sides of the attach
	 * 2- checks business in {@link #playBusinessDelegate}
	 * 3- display attack result
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