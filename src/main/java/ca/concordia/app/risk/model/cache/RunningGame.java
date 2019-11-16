package ca.concordia.app.risk.model.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BordersModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentsModel;
import ca.concordia.app.risk.model.xmlbeans.CountriesModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ca.concordia.app.risk.model.xmlbeans.PlayersModel;
import ca.concordia.app.risk.utility.DateUtils;

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
   * 
   */
  RunningGameSubject subject;

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
  // private int currentPlayerId;

  /**
   * 
   */
  private Player currentPlayer;

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
   *
   */
  private int[] attackerDice = new int[3];

  /**
   *
   */
  private int[] defenderDice = new int[2];

  /**
   *
   */

  private boolean isAttackerWin;

  /**
   *
   */
  private boolean isDefenderWin;

  /**
   *
   */
  private boolean isAttackCompleted;

  /**
   *
   */
  private boolean isCardGiven;

  /**
   *
   */
  private boolean isCardExchangeCompleted;

  /**
   *
   */
  private boolean isAllOut;

  /**
   * 
   * @return
   */
  public int getNumDiceAttacker() {
    return numDiceAttacker;
  }

  /**
   * 
   * @return
   */
  public boolean isCardExchangeCompleted() {
    return isCardExchangeCompleted;
  }

  /**
   * 
   * @param isCardExchangeCompleted
   */
  public void setCardExchangeCompleted(boolean isCardExchangeCompleted) {
    this.isCardExchangeCompleted = isCardExchangeCompleted;
  }

  /**
   * 
   * @param numDiceAttacker
   */
  public void setNumDiceAttacker(int numDiceAttacker) {
    this.numDiceAttacker = numDiceAttacker;
  }

  /**
   * 
   */
  private int numDiceAttacker;

  /**
   *
   * @return
   */
  public boolean isAllOut() {
    return isAllOut;
  }

  /**
   *
   * @param allOut
   */
  public void setAllOut(boolean allOut) {
    isAllOut = allOut;
  }

  /**
   *
   * @return
   */
  public boolean isAttackCompleted() {
    return isAttackCompleted;
  }

  /**
   *
   * @param attackCompleted
   */
  public void setAttackCompleted(boolean attackCompleted) {
    isAttackCompleted = attackCompleted;
  }

  /**
   *
   * @return
   */
  public boolean isCardGiven() {
    return isCardGiven;
  }

  /**
   *
   * @param cardGiven
   */
  public void setCardGiven(boolean cardGiven) {
    isCardGiven = cardGiven;
  }

  /**
   *
   * @return
   */
  public boolean isAttackerWin() {
    return isAttackerWin;
  }

  /**
   *
   * @param attackerWin
   */
  public void setAttackerWin(boolean attackerWin) {
    isAttackerWin = attackerWin;
  }

  /**
   *
   * @return
   */
  public boolean isDefenderWin() {
    return isDefenderWin;
  }

  /**
   *
   * @param defenderWin
   */
  public void setDefenderWin(boolean defenderWin) {
    isDefenderWin = defenderWin;
  }

  /**
   *
   */
  public int[] getAttackerDice() {
    return attackerDice;
  }

  /**
   *
   * @param attackerDice
   */
  public void setAttackerDice(int[] attackerDice) {
    this.attackerDice = attackerDice;
  }

  /**
   *
   * @return
   */
  public int[] getDefenderDice() {
    return defenderDice;
  }

  /**
   *
   * @param defenderDice
   */
  public void setDefenderDice(int[] defenderDice) {
    this.defenderDice = defenderDice;
  }

  /**
   *
   * @return
   */
  public String getAttackCountryNameFrom() {
    return attackCountryNameFrom;
  }

  /**
   *
   * @param attackCountryNameFrom
   */
  public void setAttackCountryNameFrom(String attackCountryNameFrom) {
    this.attackCountryNameFrom = attackCountryNameFrom;
  }

  /**
   *
   * @return
   */
  public String getAttackCountryNameTo() {
    return attackCountryNameTo;
  }

  /**
   *
   * @param attackCountryNameTo
   */
  public void setAttackCountryNameTo(String attackCountryNameTo) {
    this.attackCountryNameTo = attackCountryNameTo;
  }

  /**
   *
   */
  private String attackCountryNameFrom;

  /**
   *
   */
  private String attackCountryNameTo;

  /**
   * Make models to start a new game - ContinentsModel, PlayersModel,
   * CountriesModel, BordersModel Make the graph No player yet
   */
  private RunningGame() {
    super();

    this.setSubject(new RunningGameSubject());

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

    // this.setCurrentPlayerId(0);
    // this.setCurrentPlayer(new Player());
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
    RunningGameSubject subject = RunningGame.getInstance().getSubject();
    runningGame = new RunningGame();
    RunningGame.getInstance().setSubject(subject);
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
  // public int getCurrentPlayerId() {
  // return currentPlayerId;
  // }
  //
  // /**
  // * sets {@link currentPlayerId}
  // *
  // * @param currentPlayerId current player's id
  // */
  // public void setCurrentPlayerId(int currentPlayerId) {
  // this.currentPlayerId = currentPlayerId;
  // }

  /**
   * add Continent to the Graph
   *
   * @param continentName continent name
   */
  public void addContinentGraph(String continentName) {
    this.contientsGraphsMap.computeIfAbsent(continentName,
        k -> GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false).allowingSelfLoops(false)
            .edgeClass(DefaultEdge.class).weighted(false).buildGraph());
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

  /**
   * 
   * @return
   */
  public RunningGameSubject getSubject() {
    return subject;
  }

  /**
   * 
   * @param observer
   */
  private void setSubject(RunningGameSubject subject) {
    this.subject = subject;
  }

  /**
   * 
   * @return
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * 
   * @param currentPlayer
   */
  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  /**
   * 
   */
  public void moveToNextPlayer() {
    PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
    if (this.getCurrentPlayer() == null) {
      this.setCurrentPlayer(new Player(playerDaoImpl.findById(RunningGame.getInstance(), 1)));
    } else {
      int turn = this.getCurrentPlayer().getPlayerModel().getId();
      if (turn == this.getPlayers().getList().size()) {
        this.setCurrentPlayer(new Player(playerDaoImpl.findById(RunningGame.getInstance(), 1)));
      } else {
        this.setCurrentPlayer(new Player(playerDaoImpl.findById(RunningGame.getInstance(), ++turn)));
      }
    }
  }

  /**
  *
  */
  public void reinforceInitialization() {

    int numberOfCountries = this.getCountries().getList().size();

    PlayerModel activePlayerModel = this.getCurrentPlayer().getPlayerModel();

    this.setCardGiven(false);
    this.setAllOut(false);
    this.setAttackCompleted(false);

    int reinforcementArmies = 0;
    boolean fullContinentOccupy = false;

    if (Math.floor((float) numberOfCountries / 3) > 3) {
      reinforcementArmies = Math.floorDiv(numberOfCountries, 3);
    } else {
      reinforcementArmies = 3;
    }

    List<ContinentModel> continentModels = RunningGame.getInstance().getContinents().getList();
    ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();

    for (ContinentModel item : continentModels) {

      fullContinentOccupy = true;
      List<CountryModel> countryModels = continentDaoImpl.getCountries(RunningGame.getInstance(), item);

      for (CountryModel countryModel : countryModels) {
        if (countryModel.getPlayerId() != activePlayerModel.getId())
          fullContinentOccupy = false;
      }
      if (fullContinentOccupy) {
        reinforcementArmies += item.getControlValue();
      }
    }
    if (activePlayerModel != null) {
      activePlayerModel.setPlayingPhase("Reinforcement");
      activePlayerModel.setReinforcementNoOfArmies(reinforcementArmies);
      this.setReinforceCompleted(false);
    }
  }
}
