package ca.concordia.app.risk.controller;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.InputReader;
import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.controller.dto.PlayerDto;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class GameController {
	
	GameBusinessDelegate gameBusinessDelegate;
	
	@Autowired
    InputReader inputReader;

	/**
	 * 
	 */
	@Autowired
	public GameController() {
		gameBusinessDelegate = new GameBusinessDelegate();
	}
	

	/**
	 * 
	 * @param autoSave
	 * @return
	 */
	@ShellMethod("Start a new game")
	public String init(@ShellOption(optOut = false) String autoSave) {
		GameStarterDto gameStarterDTO = new GameStarterDto();
		if(Boolean.getBoolean(autoSave)) {
			gameStarterDTO.setAutoSave(true);
		}
		try {
			int numberOfPlayers = Integer.parseInt(inputReader.prompt("Enter Number of Players"));
			gameStarterDTO.setNumberOfPlayers(numberOfPlayers);
			int numberOfCountries = Integer.parseInt(inputReader.prompt("Enter Number of Countries"));
			gameStarterDTO.setNumberOfCountries(numberOfCountries);
			
			boolean repeat = false;
			for(int i=0; i<numberOfPlayers; i++) {
				if(repeat) {
					--i;
				}
				int labelCounter = i+1;
				PlayerDto playerDTO = new PlayerDto();
				playerDTO.setName(inputReader.prompt("Enter Player["+labelCounter+"] name"));
				playerDTO.setColor(inputReader.prompt("Enter player["+labelCounter+"] color"));
				try {
					playerDTO.validate();
					repeat = false;
				} catch(ValidationException validationException) {
					System.out.println(validationException.getMessage());
					repeat = true;
					continue;
				}
				gameStarterDTO.getPlayersList().add(playerDTO);
			}
			gameStarterDTO.validate();
			gameBusinessDelegate.initGame(gameStarterDTO);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "Game created successfully";
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