package ca.concordia.app.risk.services;

import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * 
 * @author i857625
 *
 */
public class PresentationService {
	
	/**
	 * 
	 */
	public PresentationService() {
		
	}

	/**
	 * 
	 * @param playerName
	 * @return
	 * @throws Exception
	 */
	public PlayerDto getPlayerDetails(String playerName) throws Exception {
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(playerName);
		PlayerDto playerDto = new PlayerDto();
		BeanUtils.copyProperties(playerModel, playerDto);
		return playerDto;
	}
}