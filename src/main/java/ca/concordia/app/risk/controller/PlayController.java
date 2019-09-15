package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.PlayBusinessDelegate;

@ShellComponent
public class PlayController {

	private PlayBusinessDelegate playBusinessDelegate;
	
	@Autowired
	public PlayController() {
		playBusinessDelegate = new PlayBusinessDelegate();
	}
	
	@ShellMethod("Attack a player")
	public String attack(@ShellOption(optOut = false) String attacker,
			@ShellOption(optOut = false) String defender, 
			@ShellOption(optOut = false) String from, 
			@ShellOption(optOut = false) String to
			) {
		try {
			playBusinessDelegate.attack(attacker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attacker + " Lose!";
	}
}