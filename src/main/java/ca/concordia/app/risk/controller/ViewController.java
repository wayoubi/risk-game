package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.ViewBusinessDelegate;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.shell.PromptColor;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class ViewController {

	@Autowired
	private ViewBusinessDelegate viewBusinessDelegate;
	
	@Autowired
	private ShellHelper shellHelper;
	
	/**
	 * 
	 */
	@Autowired
	public ViewController() {
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	@ShellMethod("Views the datails of game object")
	public void details(@ShellOption(optOut = false) String player) {
		try {
			PlayerDto playerDto = viewBusinessDelegate.getPlayerDetails(player);
			shellHelper.print(playerDto.toString(), PromptColor.valueOf(playerDto.getColor()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}