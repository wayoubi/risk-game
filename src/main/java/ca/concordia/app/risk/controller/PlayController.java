package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.PlayBusinessDelegate;
import ca.concordia.app.risk.utility.GameUtils;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class PlayController {

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
	public PlayController() {
		playBusinessDelegate = new PlayBusinessDelegate();
	}

	/**
	 * Attack
	 * @param attacker
	 * @param defender
	 * @param from
	 * @param to
	 * @return
	 */
	@ShellMethod("Attack a player")
	public String attack(@ShellOption(optOut = false) String attacker, @ShellOption(optOut = false) String defender,
			@ShellOption(optOut = false) String from, @ShellOption(optOut = false) String to) {
		try {
			gameUtils.rollDice();
			playBusinessDelegate.attack(attacker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attacker + " Lose!";
	}
}