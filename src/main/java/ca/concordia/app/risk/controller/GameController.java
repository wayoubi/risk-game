package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;

@ShellComponent
public class GameController {
	
	GameBusinessDelegate gameBusinessDelegate;

	@Autowired
	public GameController() {
		gameBusinessDelegate = new GameBusinessDelegate();
	}

	@ShellMethod("Start a new game")
	public String init(@ShellOption(optOut = false) String playerName,
			@ShellOption(optOut = false) String playerColor) {
		try {
			gameBusinessDelegate.initGame(playerName, playerColor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Game created successfully";
		
	}

	@ShellMethod("Save the current game")
	public String save() {
		try {
			gameBusinessDelegate.saveGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Game saved successfully";
	}
	
	@ShellMethod("Load a Saved game")
	public String load(@ShellOption(optOut = false) String file) {
		return "loaded";
	}
}