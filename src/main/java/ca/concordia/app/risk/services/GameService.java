package ca.concordia.app.risk.services;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


import ca.concordia.app.risk.model.beans.PlayerModel;
import ca.concordia.app.risk.model.beans.PlayersModel;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.GameDao;

public class GameService {

	public GameService() {
	}

	public void initGame(String playerName, String playerColor) throws Exception {
		RunningGame.reset();

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		RunningGame.getInstance().setCreatedDate(xmlGregorianCalendar);
		RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);

		PlayerModel playerModel = new PlayerModel();
		playerModel.setName(playerName);
		playerModel.setColor(playerColor);

		RunningGame.getInstance().setPlayers(new PlayersModel());
		RunningGame.getInstance().getPlayers().getPlayer().add(playerModel);
	}

	public void saveGame() throws Exception {
		GameDao gameDao = new GameDao();
		gameDao.save(RunningGame.getInstance());
	}
}
