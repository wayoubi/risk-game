package ca.concordia.app.risk.controller;

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
	@ShellMethod("Save the current game")
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