package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.services.PresentationService;

/**
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
	 * This method shows map
	 */
	public void showmap() {
		presentationService.showmap();
	}
}