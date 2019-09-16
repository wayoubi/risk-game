package ca.concordia.app.risk.services;

import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class PlayService {

	public void attack(String attacker) throws Exception {
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(attacker);
		playerModel.setColor("White");
	}
}
