package ca.concordia.app.risk.controller;

import javax.validation.ValidationException;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.model.cache.RunningGame;
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

	@ShellMethod("Add/Remove player")
	public String gameplayer(@ShellOption(value = { "--add" }, defaultValue = "None") String player2Add,
							 @ShellOption(value = { "--remove" }, defaultValue = "None") String player2Remove) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Player edited successfully";
	}

	@ShellMethod("populate countries")
	public String populatecountries() {

		gameBusinessDelegate.populateCountries();


		return "Countries has been randomly assigned to players.";
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