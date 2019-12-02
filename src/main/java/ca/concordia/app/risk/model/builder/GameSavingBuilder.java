package ca.concordia.app.risk.model.builder;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.text.StringEscapeUtils;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;
import org.jgrapht.io.StringComponentNameProvider;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.utility.DateUtils;

/**
 * This class inherits from AbstractGameModelBuilder to implement saving game's builder
 * @author i857625
 *
 */
public class GameSavingBuilder extends AbstractGameModelBuilder {

	/**
	 * This method implements model builder for saving game
	 */
	@Override
	public void buildModel() {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
			this.getGameModel().setLastSavedDate(xmlGregorianCalendar);
		} catch (DatatypeConfigurationException configurationException) {
			throw new RiskGameRuntimeException(AbstractGameModelBuilder.GAME_CANNOT_BE_SAVED, configurationException);
		}
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(this.getGameModel(), file);
		} catch (JAXBException jaxbException) {
			throw new RiskGameRuntimeException(AbstractGameModelBuilder.GAME_CANNOT_BE_SAVED, jaxbException);
		}
	}

	/**
	 * This method implements graph builder for saving game
	 */
	@Override
	public void buildGraph() {
		this.setGameModel(RunningGame.getInstance());
		try {
			Writer writer = new StringWriter();
			GraphExporter<String, DefaultEdge> exporter = new DOTExporter<>(new StringComponentNameProvider<String>(),
					new StringComponentNameProvider<String>(), null);
			exporter.exportGraph(RunningGame.getInstance().getGraph(), writer);
			String serilaizedGraph = StringEscapeUtils.escapeXml11(writer.toString());
			this.getGameModel().setSerilaizedGraph(serilaizedGraph);
		} catch (ExportException exportException) {
			throw new RiskGameRuntimeException(AbstractGameModelBuilder.GAME_CANNOT_BE_SAVED, exportException);
		}
	}
}
