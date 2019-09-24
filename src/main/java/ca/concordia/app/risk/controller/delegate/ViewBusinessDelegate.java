package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.PlayerDto;
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
	@Autowired
	PresentationService presentationService;
	
	/**
	 * 
	 * @param playerName
	 * @return
	 * @throws Exception
	 */
	public PlayerDto getPlayerDetails(String playerName) throws Exception {
		return presentationService.getPlayerDetails(playerName);
	}
}