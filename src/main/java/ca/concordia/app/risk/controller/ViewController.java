package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.ViewBusinessDelegate;
import ca.concordia.app.risk.view.PlayerView;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class ViewController {

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
	public String details(@ShellOption(optOut = false) String player) {
		String response = null;
		ViewBusinessDelegate viewBusinessDelegate = new ViewBusinessDelegate();
		try {
			String details = viewBusinessDelegate.getPlayerDetails(player);
			PlayerView playerView = new PlayerView();
			response =  playerView.format(details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}