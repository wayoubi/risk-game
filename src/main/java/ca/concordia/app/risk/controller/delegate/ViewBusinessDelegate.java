package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.services.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author i857625
 */
public class ViewBusinessDelegate {

	/**
	 * 
	 */
	@Autowired
	PresentationService presentationService;
	
	/**
	 * 
	 */
	public void showmap() {
		presentationService.showmap();
	}
}