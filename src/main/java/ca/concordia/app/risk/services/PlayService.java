package ca.concordia.app.risk.services;

import ca.concordia.app.risk.model.beans.PlayerModel;
import ca.concordia.app.risk.model.dao.PlayerDao;

public class PlayService {

	public void attack(String attacker) throws Exception {
		PlayerDao playerDao = new PlayerDao();
		PlayerModel playerModel = playerDao.findByName(attacker);
		playerModel.setColor("White");
	}
}
