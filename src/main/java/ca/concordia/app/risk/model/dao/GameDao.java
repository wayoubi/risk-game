package ca.concordia.app.risk.model.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ca.concordia.app.risk.model.beans.GameModel;

public class GameDao {

	public void save(GameModel game) throws Exception{
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(game, file);
		      } catch (JAXBException e) {
		    	  throw new Exception("Game caanot be saved");
		      }
		}
}