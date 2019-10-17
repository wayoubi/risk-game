package ca.concordia.app.risk.model.cache;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.*;
import ca.concordia.app.risk.utility.DateUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Date;
import java.util.HashMap;

/**
 * This is the most important class, it is implemented using the singleton
 * design pattern There is one RunningGame object in the JVM during the whole
 * game This object holds the state of the running game
 * 
 * @author i857625
 *
 */
public class RunningGame extends GameModel {

	/**
	 * objectFactory to create Game model objects
	 */
	private ObjectFactory objectFactory;

	/**
	 * Current running game
	 */
	private static RunningGame runningGame;

	/**
	 * Current games graph
	 */
	private Graph<String, DefaultEdge> graph;

	/**
	 * A Hashmap holding continents graphs
	 */
	private HashMap<String, Graph<String, DefaultEdge>> contientsGraphsMap;

	/**
	 * Current player's id
	 */
	private int currentPlayerId;

	/**
	 * determines if a game is active or not
	 */
	private boolean gamePlay = false;

	/**
	 * determines if a map is active or not
	 */
	private boolean mapLoaded = false;

	/**
	 * determines if a countries has been populated
	 */
	private boolean countriesPopulated = false;

	/**
	 * determines if the reinforcement phase is completed or not
	 */
	private boolean reinforceCompleted = false;

	/**
	 * Make models to start a new game - ContinentsModel, PlayersModel,
	 * CountriesModel, BordersModel Make the graph No player yet
	 */
	private RunningGame() {
		super();

		try {
			this.setCreatedDate(DateUtils.getXMLDateTime(new Date()));
		} catch (DatatypeConfigurationException configurationException) {
			throw new RiskGameRuntimeException(configurationException.getMessage());
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

		graph = GraphTypeBuilder.<String, DefaultEdge>directed().allowingMultipleEdges(false).allowingSelfLoops(false)
				.edgeClass(DefaultEdge.class).weighted(false).buildGraph();

		contientsGraphsMap = new HashMap<>();

		this.setCurrentPlayerId(0);
	}

	/**
	 * retrun the only one Running Game object
	 * 
	 * @return runningGame
	 */
	public static RunningGame getInstance() {
		if (runningGame == null) {
			runningGame = new RunningGame();
		}
		return runningGame;
	}

	/**
	 * This method restarts the runningGame (current game)
	 */
	public static void reset() {
		runningGame = new RunningGame();
	}

	/**
	 * 
	 * @return graph of current game
	 */
	public Graph<String, DefaultEdge> getGraph() {
		return this.graph;
	}

	/**
	 * gets {@link currentPlayerId}
	 * 
	 * @return currentPlayerId
	 */
	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	/**
	 * sets {@link currentPlayerId}
	 * 
	 * @param currentPlayerId current player's id
	 */
	public void setCurrentPlayerId(int currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	/**
	 * add Continent to the Graph
	 *
	 * @param continentName continent name
	 */
	public void addContinentGraph(String continentName) {
		this.contientsGraphsMap.computeIfAbsent(continentName,
				k -> GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false)
						.allowingSelfLoops(false).edgeClass(DefaultEdge.class).weighted(false).buildGraph());
	}

	/**
	 * remove Continent from the Graph
	 * 
	 * @param continentName continent name
	 */
	public void removeContinentGraph(String continentName) {
		this.contientsGraphsMap.remove(continentName);
	}

	/**
	 * gets continent graph
	 * 
	 * @param continentName continent name
	 * @return Graph
	 */
	public Graph<String, DefaultEdge> getContinentGraph(String continentName) {

		ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
		ContinentModel continentModel = continentDaoImpl.findByName(this, continentName);
		if (continentModel == null) {
			throw new RiskGameRuntimeException(String.format("Continent [%s] does not exist", continentName));
		}

		return this.contientsGraphsMap.get(continentName);
	}

	/**
	 * boolean returns game playing status
	 * 
	 * @return gamePlay
	 */
	public boolean isGamePlay() {
		return gamePlay;
	}

	/**
	 * sets {@link gamePlay}
	 * 
	 * @param gamePlay set game play
	 */
	public void setGamePlay(boolean gamePlay) {
		this.gamePlay = gamePlay;
	}

	/**
	 * boolean returns map loading status
	 * 
	 * @return mapLoaded
	 */
	public boolean isMapLoaded() {
		return mapLoaded;
	}

	/**
	 * sets {@link mapLoaded}
	 * 
	 * @param mapLoaded set map loaded
	 */
	public void setMapLoaded(boolean mapLoaded) {
		this.mapLoaded = mapLoaded;
	}

	/**
	 * boolean returns countries populated status
	 * 
	 * @return countriesPopulated
	 */
	public boolean isCountriesPopulated() {
		return countriesPopulated;
	}

	/**
	 * sets {@link countriesPopulated}
	 * 
	 * @param countriesPopulated set country populated
	 */
	public void setCountriesPopulated(boolean countriesPopulated) {
		this.countriesPopulated = countriesPopulated;
	}

	/**
	 * boolean returns reinforce status
	 *
	 * @return reinforceCompleted
	 */
	public boolean isReinforceCompleted() {
		return reinforceCompleted;
	}

	/**
	 * sets {@link reinforceCompleted}
	 * 
	 * @param reinforceCompleted set reinforcement completes
	 */
	public void setReinforceCompleted(boolean reinforceCompleted) {
		this.reinforceCompleted = reinforceCompleted;
	}
}
