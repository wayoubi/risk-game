package ca.concordia.app.risk.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.shell.InputReader;
import ca.concordia.app.risk.shell.PromptColor;
import ca.concordia.app.risk.shell.ShellHelper;

public class GameView {

	/**
	 * 
	 */
	@Autowired
	private InputReader inputReader;

	@Autowired
	private ShellHelper shellHelper;

	/**
	 * 
	 * @return
	 */
	public GameStarterDto read() {
		GameStarterDto gameStarterDTO = new GameStarterDto();
		int numberOfPlayers = 0, numberOfCountries = 0;

		shellHelper.printInfo("Enter Game Information");
		try {
			numberOfPlayers = Integer.parseInt(inputReader.prompt("Enter Number of Players"));
			gameStarterDTO.setNumberOfPlayers(numberOfPlayers);
			numberOfCountries = Integer.parseInt(inputReader.prompt("Enter Number of Countries"));
			gameStarterDTO.setNumberOfCountries(numberOfCountries);
		} catch (NumberFormatException nfex) {
			shellHelper.printError("Integer Values only");
			throw nfex;
		}
		List<PromptColor> availableColors = new ArrayList<PromptColor>(Arrays.asList(PromptColor.values()));
		shellHelper.printInfo("Enter Players Information");
		boolean repeat = false;
		for (int i = 0; i < numberOfPlayers; i++) {
			if (repeat) {
				--i;
			}
			int labelCounter = i + 1;
			PlayerDto playerDTO = new PlayerDto();

			playerDTO.setName(inputReader.prompt(String.format("Enter Player %s name", labelCounter)));
			shellHelper.printInfo("Choose from available colors: "
					+ availableColors.stream().map(n -> n.toString()).collect(Collectors.joining(",")));
			playerDTO.setColor(inputReader.prompt(String.format("Enter %s's color", playerDTO.getName())));
			availableColors.remove(PromptColor.valueOf(playerDTO.getColor()));
			try {
				playerDTO.validate();
				repeat = false;
			} catch (ValidationException vex) {
				shellHelper.printError(vex.getMessage());
				shellHelper.printInfo("try again");
				repeat = true;
				continue;
			}
			gameStarterDTO.getPlayersList().add(playerDTO);
		}
		return gameStarterDTO;
	}

}
