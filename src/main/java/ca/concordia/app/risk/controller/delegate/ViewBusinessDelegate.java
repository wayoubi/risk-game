package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.PresentationService;

public class ViewBusinessDelegate {

	PresentationService presentaationService;
	
	public String getPlayerDetails(String playerName) throws Exception {
		PresentationService presentaationService = new PresentationService();
		return presentaationService.getPlayerDetails(playerName);
	}
}