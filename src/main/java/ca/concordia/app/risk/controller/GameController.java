package ca.concordia.app.risk.controller;

import javax.validation.ValidationException;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.model.cache.RunningGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class GameController {
	
	@Autowired
	ShellHelper shellHelper;

	/**
	 * 
	 */
	@Autowired
	private GameBusinessDelegate gameBusinessDelegate;

	/**
	 * 
	 * @return
	 */
	@ShellMethod("Save the current game state")
	public String save() {
		try {
			gameBusinessDelegate.saveGame();
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		return shellHelper.getSuccessMessage("Game saved successfully");
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	@ShellMethod("Save the current gamemap using domination map file format under the saved direcory")
	public String savemap(@ShellOption(value = { "--file"}) String fileName) {
		try {
			gameBusinessDelegate.saveMap(fileName);
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		return shellHelper.getSuccessMessage("Game Map saved successfully");
	}
	
	/**
	 * 
	 * @return
	 */
	@ShellMethod("Validate the current gamemap to be connected")
	public String validatemap() {
		boolean isConnected = false;
		try {
			isConnected = gameBusinessDelegate.validateMap();
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		if(!isConnected) {
			return shellHelper.getErrorMessage("Countries are not connected, Map is invalid");
		}
		return shellHelper.getSuccessMessage("Countries are connected, Map is valid");
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

		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		return "Player edited successfully";
	}

	@ShellMethod("populate countries")
	public String populatecountries() {
		gameBusinessDelegate.populateCountries();
		return "Countries has been randomly assigned to players.";
	}

	@ShellMethod("placearmy")
	public String placearmy(@ShellOption(value = { "--countryname" }, defaultValue = "None") String countryName)  {
		try {
			gameBusinessDelegate.placeArmy(countryName);
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		return "An Army has been assigned to this country.";
	}


	/**
	 * 
	 * @param file
	 * @return
	 */
	@ShellMethod("Edit map saved in file")
	public String editmap(@ShellOption(optOut = false) String fileName) {
		try {
			gameBusinessDelegate.editMap(fileName);
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		return shellHelper.getSuccessMessage("Map file is read, you can edit now");
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	@ShellMethod("load map saved in file")
	public String loadmap(@ShellOption(optOut = false) String fileName) {
		try {
			gameBusinessDelegate.loadMap(fileName);
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			return  shellHelper.getErrorMessage(riskGameRuntimeException.getMessage());
		}
		return shellHelper.getSuccessMessage("Map file is read, map is loaded");
	}

	/**
	 *
	 * @return
	 */
	@ShellMethod("reinforcement")
	public String reinforce(@ShellOption(value = { "--countryName" }, defaultValue = "None") String countryName,
							@ShellOption(value = { "--number" }, defaultValue = "None") int numberOfArmies) {
		gameBusinessDelegate.reinforce(countryName,numberOfArmies);
		return "reinforcement has been completed.";
	}


	@ShellMethod("Place All")
	public String placeall() {
		gameBusinessDelegate.placeall();
		return "All remaining unplaced armies have been assigned.";
	}
}