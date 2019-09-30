package ca.concordia.app.risk.services;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;
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
	ObjectFactory objectFactory;

	/**
	 * 
	 */
	public GameService() {
		this.setObjectFactory(new ObjectFactory());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void saveGame() throws Exception{
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
}