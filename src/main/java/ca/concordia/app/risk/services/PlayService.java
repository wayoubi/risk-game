package ca.concordia.app.risk.services;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * 
 * PlayController class has all commanded for playing the game example
 * attachking other player
 * 
 * @author i857625
 *
 */
public class PlayService {

	@Autowired
	ShellHelper shellHelper;

	/**
	 * 
	 * @param attacker attacker country name
	 */
	public void attack(String attacker) {
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), attacker);
		playerModel.setColor("White");
	}
}
