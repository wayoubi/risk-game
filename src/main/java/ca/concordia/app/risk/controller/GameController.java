package ca.concordia.app.risk.controller;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.view.GameView;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class GameController {

	/**
	 * 
	 */
	@Autowired
	private GameBusinessDelegate gameBusinessDelegate;

	/**
	 * 
	 */
	@Autowired
	private ShellHelper shellHelper;
	
	/**
	 * 
	 */
	@Autowired
	GameView gameView;

	/**
	 * 
	 */
	@Autowired
	public GameController() {
	}

	/**
	 * 
	 * @param autoSave
	 * @return
	 */
	@ShellMethod("Start a new game")
	public void init(@ShellOption(optOut = false) String autoSave) {
		GameStarterDto gameStarterDTO = gameView.read();
		
		if (Boolean.getBoolean(autoSave)) {
			gameStarterDTO.setAutoSave(true);
		}
		try {
			gameStarterDTO.validate();
		} catch (ValidationException vex) {
			shellHelper.printError(vex.getMessage());
		}
		try {
			gameBusinessDelegate.initGame(gameStarterDTO);
		} catch (Exception ex) {
			shellHelper.printError(ex.getMessage());
		}
		shellHelper.printSuccess("Game initiated successfully");
	}

	/**
	 * 
	 * @return
	 */
	@ShellMethod("Save the current game")
	public String save() {
		try {
			gameBusinessDelegate.saveGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Game saved successfully";
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	@ShellMethod("Load a Saved game")
	public String load(@ShellOption(optOut = false) String file) {
		return "loaded";
	}
}