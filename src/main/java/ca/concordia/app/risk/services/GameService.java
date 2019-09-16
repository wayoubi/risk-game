package ca.concordia.app.risk.services;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.GameStarterDTO;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.GameDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ca.concordia.app.risk.model.xmlbeans.PlayersModel;
import ca.concordia.app.risk.utility.DateUtils;


public class GameService {

	/**
	 * 
	 */
	public GameService() {
	}

	/**
	 * 
	 * @param playerName
	 * @param playerColor
	 * @throws Exception
	 */
	public void initGame(GameStarterDTO gameStarterDT) throws Exception {
		RunningGame.reset();
		XMLGregorianCalendar xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
		RunningGame.getInstance().setCreatedDate(xmlGregorianCalendar);
		RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);
		RunningGame.getInstance().setPlayers(new PlayersModel());
		for(int i=0; i<gameStarterDT.getPlayersList().size(); i++) {
			PlayerModel playerModel = new PlayerModel();
			BeanUtils.copyProperties(gameStarterDT.getPlayersList().get(i), playerModel);
			RunningGame.getInstance().getPlayers().getPlayer().add(playerModel);	
		}	
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void saveGame() throws Exception {
		GameDaoImpl gameDao = new GameDaoImpl();
		XMLGregorianCalendar xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
		RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);
		gameDao.save(RunningGame.getInstance());
	}
}