package ca.concordia.app.risk.services;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.*;
import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.utility.DateUtils;

/**
 * 
 * @author i857625
 *
 */
public class GameService {

	/**
	 * 
	 */
	ObjectFactory objectFactory = new ObjectFactory();;

	/**
	 * 
	 */
	public GameService() {
		this.setObjectFactory(new ObjectFactory());
	}

	/**
	 * 
	 * @param gameStarterDTO
	 * @throws Exception
	 */
	public void initGame(GameStarterDto gameStarterDTO) throws Exception {
		RunningGame.reset();
		XMLGregorianCalendar xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
		RunningGame.getInstance().setCreatedDate(xmlGregorianCalendar);
		RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);
		RunningGame.getInstance().setPlayers(this.getObjectFactory().createPlayersModel());
		for (int i = 0; i < gameStarterDTO.getPlayersList().size(); i++) {
			PlayerModel playerModel = this.getObjectFactory().createPlayerModel();
			BeanUtils.copyProperties(gameStarterDTO.getPlayersList().get(i), playerModel);
			PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
			playerDaoImpl.assignID(RunningGame.getInstance(), playerModel);
			RunningGame.getInstance().getPlayers().getList().add(playerModel);
		}

		RunningGame.getInstance().setCountries(this.getObjectFactory().createCountriesModel());
		for (int i = 0; i < gameStarterDTO.getNumberOfCountries(); i++) {
			CountryModel countryModel = this.getObjectFactory().createCountryModel();
			CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
			countryDaoImpl.assignID(RunningGame.getInstance(), countryModel);
			countryModel.setColor("Black");
			countryModel.setContinentId(1);
			countryModel.setName("Country" + (i + 1));
			countryModel.setNumberOfArmies(10);
			countryModel.setPlayerId(1);
			RunningGame.getInstance().getCountries().getList().add(countryModel);
			RunningGame.getInstance().getGraph().addVertex(countryModel.getName());
		}

		Iterator<String> iterator = RunningGame.getInstance().getGraph().vertexSet().iterator();
		while (iterator.hasNext()) {
			String source = iterator.next();
			for (int i = 1; i <= gameStarterDTO.getNumberOfCountries(); i++) {
				String destination = "Country" + i;
				if (!destination.equals(source)) {
					RunningGame.getInstance().getGraph().addEdge(source, destination);
				}
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void saveGame() throws Exception {
		XMLGregorianCalendar xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
		RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(RunningGame.getInstance(), file);
		} catch (JAXBException e) {
			throw new Exception("Game caanot be saved");
		}
	}

	/**
	 * 
	 * @param objectFactory
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	/**
	 * 
	 * @return
	 */
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

    public void addPlayer(PlayerDto playerDto) throws Exception {
		PlayerModel playerModel = objectFactory.createPlayerModel();
		BeanUtils.copyProperties(playerDto, playerModel);
		PlayerDaoImpl playerDaoImp = new PlayerDaoImpl();
		playerDaoImp.assignID(RunningGame.getInstance(), playerModel);
		RunningGame.getInstance().getPlayers().getList().add(playerModel);
	}

	public void removePlayer(PlayerDto playerDto) throws Exception {
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), playerDto.getName());
		playerDao.delete(RunningGame.getInstance(), playerModel);


	}
}