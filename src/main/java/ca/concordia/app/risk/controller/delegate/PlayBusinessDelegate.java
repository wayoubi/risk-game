package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.PlayService;

/**
 * PlayBusinessDelegate, The facade class to access the PlayService
 * 
 * @see PlayService
 * @author i857625
 *
 */
public class PlayBusinessDelegate {

	/**
	 * This method used for attack.
	 * 
	 * @param attacker attacker's name
	 */
	public void attack(String attacker) {
		PlayService playService = new PlayService();
		playService.attack(attacker);
	}
}
