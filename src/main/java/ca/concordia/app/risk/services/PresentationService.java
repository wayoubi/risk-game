package ca.concordia.app.risk.services;

import org.springframework.stereotype.Component;

import ca.concordia.app.risk.model.cache.RunningGame;
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
	public String getPlayerDetails(String playerName) throws Exception {
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(playerName);
		return playerModel.getName() + playerModel.getColor();

	}
}