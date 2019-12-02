package ca.concordia.app.risk.model.builder;

import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.text.StringEscapeUtils;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.DOTImporter;
import org.jgrapht.io.GraphImporter;
import org.jgrapht.io.ImportException;
import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.Player;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.cache.RunningGameSubject;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * This class inherits from AbstractGameModelBuilder to implement running game's builder
 * @author i857625
 *
 */
public class RunningGameBuilder extends AbstractGameModelBuilder {

	/**
	 * This method implements model builder for running game
	 */
	@Override
	public void buildModel() {
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			GameModel gameModel = (GameModel) jaxbUnmarshaller.unmarshal(file);
			this.setGameModel(gameModel);
		} catch (JAXBException jaxbException) {
			jaxbException.printStackTrace();
			throw new RiskGameRuntimeException(AbstractGameModelBuilder.GAME_CANNOT_BE_LOADED, jaxbException);
		}
		RunningGameSubject subject = RunningGame.getInstance().getSubject();
		RunningGame.reset();
		BeanUtils.copyProperties(this.getGameModel(), RunningGame.getInstance());
		RunningGame.getInstance().setSubject(subject);
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		RunningGame.getInstance().setCurrentPlayer(new Player(
				playerDaoImpl.findById(RunningGame.getInstance(), RunningGame.getInstance().getSavedPlayerId())));
	}

	/**
	 * This method implements graph builder for running game
	 */
	@Override
	public void buildGraph() {
		GraphImporter<String, DefaultEdge> importer = new DOTImporter<>((label, attributes) -> label,
				(from, to, label, attributes) -> new DefaultEdge());
		try {
			String unserilaizedGraph = StringEscapeUtils.unescapeXml(this.getGameModel().getSerilaizedGraph());
			importer.importGraph(RunningGame.getInstance().getGraph(), new StringReader(unserilaizedGraph));
		} catch (ImportException importException) {
			importException.printStackTrace();
			throw new RiskGameRuntimeException(GAME_CANNOT_BE_LOADED, importException);
		}

	}
}
