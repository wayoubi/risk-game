package ca.concordia.app.risk.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.dto.GameStarterDTO;
import ca.concordia.app.risk.controller.dto.PlayerDTO;

@ShellComponent
public class GameController {
	
	GameBusinessDelegate gameBusinessDelegate;

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
		//if(autoSave) {
		//	//engage auto save module
		//}
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter Number of Players");
			int numberOfPlayers = scanner.nextInt();
			System.out.println("Enter Number of Countries");
			int numberOfCountries = scanner.nextInt();
			
			GameStarterDTO gameStarterDTO = new GameStarterDTO();
			gameStarterDTO.setNumberOfCountries(numberOfCountries);
			gameStarterDTO.setNumberOfPlayers(numberOfPlayers);
			
			for(int i=0; i<numberOfPlayers; i++) {
				PlayerDTO playerDTO = new PlayerDTO();
				System.out.println("Enter Player["+i+"] name");
				playerDTO.setName(scanner.next());
				System.out.println("Enter player["+i+"] color");
				playerDTO.setColor(scanner.next());
				gameStarterDTO.getPlayersList().add(playerDTO);
			}
			
			gameBusinessDelegate.initGame(gameStarterDTO);
		} catch (Exception e) {
			e.printStackTrace();
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