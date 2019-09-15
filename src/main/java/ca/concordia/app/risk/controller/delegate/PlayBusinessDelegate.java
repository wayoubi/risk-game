package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.PlayService;

public class PlayBusinessDelegate {

	public void attack(String attacker) throws Exception {
		PlayService playService = new PlayService();
		playService.attack(attacker);
	}
}
