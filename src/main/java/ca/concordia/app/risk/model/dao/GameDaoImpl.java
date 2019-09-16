package ca.concordia.app.risk.model.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

public class GameDaoImpl implements Dao<GameModel>{

	@Override
	public GameModel findByName(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void save(GameModel game) throws Exception {
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// Set it to true if you need to include the JSON root element in the JSON output
			//jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(game, file);
		      } catch (JAXBException e) {
		    	  throw new Exception("Game caanot be saved");
		      }
		}

	@Override
	public void update(GameModel t, String[] params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(GameModel t) {
		// TODO Auto-generated method stub	
	}	
}