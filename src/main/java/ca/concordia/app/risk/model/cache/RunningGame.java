package ca.concordia.app.risk.model.cache;

import ca.concordia.app.risk.model.xmlbeans.*;
import ca.concordia.app.risk.utility.DateUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Date;

/**
 * @author i857625
 */
public class RunningGame extends GameModel {

    private ObjectFactory objectFactory;

    /**
     *
     */
    private static RunningGame runningGame;

    /**
     *
     */
    private Graph<String, DefaultEdge> graph;

    /**
     *
     */
    private RunningGame() {
        super();

        try {
            this.setCreatedDate(DateUtils.getXMLDateTime(new Date()));
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        objectFactory = new ObjectFactory();
        ContinentsModel continentsModel = objectFactory.createContinentsModel();
        this.setContinents(continentsModel);
        PlayersModel playersModel = objectFactory.createPlayersModel();
        this.setPlayers(playersModel);
        CountriesModel countriesModel = objectFactory.createCountriesModel();
        this.setCountries(countriesModel);
        BordersModel bordersModel = objectFactory.createBordersModel();
        this.setBorders(bordersModel);
        graph = GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false).allowingSelfLoops(false)
                .edgeClass(DefaultEdge.class).weighted(false).buildGraph();
    }

    /**
     * @return
     */
    public static RunningGame getInstance() {
        if (runningGame == null) {
            runningGame = new RunningGame();
        }
        return runningGame;
    }

    /**
     *
     */
    public static void reset() {
        runningGame = new RunningGame();
    }

    /**
     * @return
     */
    public Graph<String, DefaultEdge> getGraph() {
        return this.graph;
    }

}
