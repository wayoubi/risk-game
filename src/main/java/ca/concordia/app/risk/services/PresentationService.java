package ca.concordia.app.risk.services;

import org.springframework.stereotype.Component;

import ca.concordia.app.risk.model.beans.PlayerModel;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDao;

@Component
public class PresentationService {

	public PresentationService() {
	}

	public String getPlayerDetails(String playerName) throws Exception {
		PlayerDao playerDao = new PlayerDao();
		PlayerModel playerModel = playerDao.findByName(playerName);
		return playerModel.getName() + playerModel.getColor();

	}
}