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

  private boolean gamePlay = false;

  private boolean mapLoaded = false;

  private boolean countriesPopulated = false;

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
  * @param currentPlayerId
  */
  public void setCurrentPlayerId(int currentPlayerId) {
    this.currentPlayerId = currentPlayerId;
  }

  /**
   *
   * @param continentName
   * @return Graph
   */
  public void addContinentGraph(String continentName) {
    this.contientsGraphsMap.computeIfAbsent(continentName,
        k -> GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false).allowingSelfLoops(false)
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
   *
   * @param continentName
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

  public boolean isGamePlay() {
	  return gamePlay;
  }

  /**
  * sets {@link gamePlay}
  * 
  * @param gamePlay
  */
  public void setGamePlay(boolean gamePlay) {
     this.gamePlay = gamePlay;
  }

  /**
  *
  * @return mapLoaded
  */
  public boolean isMapLoaded() {
     return mapLoaded;
  }

  /**
  * sets {@link mapLoaded}
  * 
  * @param mapLoaded
  */
  public void setMapLoaded(boolean mapLoaded) {
     this.mapLoaded = mapLoaded;
  }

  /**
  *
  * @return countriesPopulated
  */
  public boolean isCountriesPopulated() {
    return countriesPopulated;
  }

  /**
  * sets {@link countriesPopulated}
  * 
  * @param countriesPopulated
  */
  public void setCountriesPopulated(boolean countriesPopulated) {
    this.countriesPopulated = countriesPopulated;
  }

  /**
  *
  * @return reinforceCompleted
  */
  public boolean isReinforceCompleted() {
    return reinforceCompleted;
  }

  /**
  * sets {@link reinforceCompleted}
  * 
  * @param reinforceCompleted
  */
  public void setReinforceCompleted(boolean reinforceCompleted) {
    this.reinforceCompleted = reinforceCompleted;
  }
}
