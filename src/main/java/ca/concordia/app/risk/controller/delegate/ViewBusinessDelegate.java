package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.PresentationService;

/**
 * 
 * @author i857625
 *
 */
public class ViewBusinessDelegate {

	/**
	 * 
	 */
	PresentationService presentaationService;
	
	/**
	 * 
	 */
	public ViewBusinessDelegate() {
		this.setPresentaationService(new PresentationService());
	}
	
	/**
	 * 
	 * @param playerName
	 * @return
	 * @throws Exception
	 */
	public String getPlayerDetails(String playerName) throws Exception {
		return this.getPresentaationService().getPlayerDetails(playerName);
	}
	
	/**
	 * 
	 * @return
	 */
	public PresentationService getPresentaationService() {
		return presentaationService;
	}

	/**
	 * 
	 * @param presentaationService
	 */
	public void setPresentaationService(PresentationService presentaationService) {
		this.presentaationService = presentaationService;
	}
}