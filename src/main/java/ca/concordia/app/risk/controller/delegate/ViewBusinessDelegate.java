package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.services.PresentationService;

/**
 * ViewBusinessDelegate, The facade class to access the PresentationService
 * 
 * @author i857625
 *
 */
public class ViewBusinessDelegate {

	/**
	 * 
	 * Dependency injection from PresentationService
	 */
	@Autowired
	PresentationService presentationService;

	/**
	 * show map details
	 */
	public void showmap() {
		presentationService.showmap();
	}
}