package ca.concordia.app.risk.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.shell.InputReader;
import ca.concordia.app.risk.shell.PromptColor;
import ca.concordia.app.risk.shell.ShellHelper;

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
    private InputReader inputReader;
	
	@Autowired
	private ShellHelper shellHelper;

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
		GameStarterDto gameStarterDTO = new GameStarterDto();
		
		if(Boolean.getBoolean(autoSave)) {
			gameStarterDTO.setAutoSave(true);
		}
		
		int numberOfPlayers=0, numberOfCountries=0;
		
		shellHelper.printInfo("Enter Game Information");
		try {
			numberOfPlayers = Integer.parseInt(inputReader.prompt("Enter Number of Players"));
			gameStarterDTO.setNumberOfPlayers(numberOfPlayers);
			numberOfCountries = Integer.parseInt(inputReader.prompt("Enter Number of Countries"));
			gameStarterDTO.setNumberOfCountries(numberOfCountries);
		} catch(NumberFormatException nfex) {
			shellHelper.printError("Integer Values only");
			return;
		}
			
		shellHelper.printInfo("Enter Players Information");
		boolean repeat = false;
		for(int i=0; i<numberOfPlayers; i++) {
			if(repeat) {
				--i;
			}
			int labelCounter = i+1;
			PlayerDto playerDTO = new PlayerDto();
			playerDTO.setName(inputReader.prompt(String.format("Enter Player %s name", labelCounter)));
			shellHelper.printInfo("Choose from " + Arrays.asList(PromptColor.values()).stream().map( n -> n.toString()).collect(Collectors.joining( "," )) );
			playerDTO.setColor(inputReader.prompt(String.format("Enter %s's color", playerDTO.getName())));
			try {
				playerDTO.validate();
				repeat = false;
			} catch(ValidationException vex) {
				shellHelper.printError(vex.getMessage());
				shellHelper.printInfo("try again");
				repeat = true;
				continue;
			}
			gameStarterDTO.getPlayersList().add(playerDTO);
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