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
 * 
 * @author i857625
 *
 */
public class RunningGame extends GameModel {

	private ObjectFactory objectFactory;

	/**
	 * The current running game
	 */
	private static RunningGame runningGame;

	/**
	 * The current games graph
	 */
	private Graph<String, DefaultEdge> graph;
	
	/**
	 * A Hasmap holding continents graphs
	 */
	private HashMap<String, Graph<String, DefaultEdge>> contientsGraphsMap;
	
	/**
	 * Tha current player's id
	 */
	private int currentPlayerId;

	private boolean gamePlay =false;

	private boolean mapLoaded = false;

	private boolean countriesPopulated=false;


	/**
	 * Make models to start a new game - ContinentsModel, PlayersModel, CountriesModel, BordersModel
	 * Make the graph
	 * No player yet
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

		graph = GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false).allowingSelfLoops(false)
				.edgeClass(DefaultEdge.class).weighted(false).buildGraph();
		
		contientsGraphsMap = new HashMap<>();

		this.setCurrentPlayerId(0);
	}

	/**
	 * @return runningGame
	 */
	public static RunningGame getInstance() {
		if (runningGame == null) {
			runningGame = new RunningGame();
		}
		return runningGame;
	}

	/**
	 * This method restarts the runningGame - current game
	 */
	public static void reset() {
		runningGame = new RunningGame();
	}

	/**
	 * 
	 * @return graph of the current game
	 */
	public Graph<String, DefaultEdge> getGraph() {
		return this.graph;
	}
	
	/**
	 * @return the currentPlayerId
	 */
	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	/**
	 * This method set current player's id - setter for currentPlayerId
	 * @param currentPlayerId
	 */
	public void setCurrentPlayerId(int currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}
	
	/**
	 *	This methods return the graph of the passed as parameter continent
	 * @param continentName
	 * @return
	 */
	public void addContinentGraph(String continentName) {
		this.contientsGraphsMap.computeIfAbsent(continentName, k -> GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false).allowingSelfLoops(false)
				.edgeClass(DefaultEdge.class).weighted(false).buildGraph());
	}
	
	/**
	 * 
	 * @param continentName
	 */
	public void removeContinentGraph(String continentName) {
		this.contientsGraphsMap.remove(continentName);
	}
	
	/**
	 * This methods return the graph of the passed as parameter continent
	 * @param continentName
	 * @return
	 */
	public Graph<String, DefaultEdge>  getContinentGraph(String continentName) {
		ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
		ContinentModel continentModel = continentDaoImpl.findByName(this, continentName);
		if(continentModel==null) {
			throw new RiskGameRuntimeException(String.format("Continent [%s] does not exist",continentName));
		}
		return this.contientsGraphsMap.get(continentName);
	}

	public boolean isGamePlay() {
		return gamePlay;
	}

	public void setGamePlay(boolean gamePlay) {
		this.gamePlay = gamePlay;
	}

	public boolean isMapLoaded() {
		return mapLoaded;
	}

	public void setMapLoaded(boolean mapLoaded) {
		this.mapLoaded = mapLoaded;
	}

	public boolean isCountriesPopulated() {
		return countriesPopulated;
	}

	public void setCountriesPopulated(boolean countriesPopulated) {
		this.countriesPopulated = countriesPopulated;
	}
}
