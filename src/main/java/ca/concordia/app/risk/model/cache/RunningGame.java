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
 */
public class RunningGame extends GameModel {

	/**
	 * running game subject
	 */
	RunningGameSubject subject;

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
	 * currentPlayer current player
	 */
	private Player currentPlayer;

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

		ObjectFactory objectFactory = new ObjectFactory();

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
	}

	/**
	 * return the only one Running Game object
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
	 * gets {@link graph}
	 * @return graph of current game
	 */
	public Graph<String, DefaultEdge> getGraph() {
		return this.graph;
	}

	/**
	 * sets {@link graph}
	 * @param graph
	 */
	public void setGraph(Graph<String, DefaultEdge> graph) {
		this.graph = graph;
	}

	/**
	 * add Continent to the Graph
	 * @param continentName continent name
	 */
	public void addContinentGraph(String continentName) {
		this.contientsGraphsMap.computeIfAbsent(continentName,
				k -> GraphTypeBuilder.<String, DefaultEdge>undirected().allowingMultipleEdges(false)
						.allowingSelfLoops(false).edgeClass(DefaultEdge.class).weighted(false).buildGraph());
	}

	/**
	 * remove Continent from the Graph
	 * @param continentName continent name
	 */
	public void removeContinentGraph(String continentName) {
		this.contientsGraphsMap.remove(continentName);
	}

	/**
	 * gets continent graph
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
	 * This method change turn between players
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
			List<CountryModel> countryModels = playerDaoImpl.getCountries(RunningGame.getInstance(),
					RunningGame.getInstance().getCurrentPlayer().getPlayerModel());
			if (countryModels.size() == 0) {
				moveToNextPlayer();
			}
		}
		this.setSavedPlayerId(this.getCurrentPlayer().getPlayerModel().getId());
	}

	/**
	 * This method initializes exchange cards phase
	 */
	public void exchangeCardsInitialization() {
		PlayerModel activePlayerModel = this.getCurrentPlayer().getPlayerModel();
		List<String> cards = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();

		if (cards.size() >= 3) {
			int infantryCount = 0, cavalryCount = 0, artilleryCount = 0;
			for (int i = 0; i < cards.size(); i++) {
				if (cards.get(i) == "Infantry") {
					infantryCount += 1;
				} else if (cards.get(i) == "Cavalry") {
					cavalryCount += 1;
				} else if (cards.get(i) == "Artillery") {
					artilleryCount += 1;
				}
			}
			if ((infantryCount >= 1 && cavalryCount >= 1 && artilleryCount >= 1)) {
				if (!"Human".equalsIgnoreCase(
						RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getStrategy())) {
					String[] cardsArray = new String[3];
					int count = 0;
					boolean infantryDone = false, artilleryDone = false, cavalryDone = false;
					for (int i = 0; i < cards.size(); i++) {
						if (cards.get(i) == "Infantry" && !infantryDone) {
							cardsArray[count] = Integer.toString(i + 1);
							infantryDone = true;
							count++;
						} else if (cards.get(i) == "Cavalry" && !cavalryDone) {
							cardsArray[count] = Integer.toString(i + 1);
							cavalryDone = true;
							count++;
						} else if (cards.get(i) == "Artillery" && !artilleryDone) {
							cardsArray[count] = Integer.toString(i + 1);
							artilleryDone = true;
							count++;
						}
						if (count == 3) {
							break;
						}
					}
					RunningGame.getInstance().getCurrentPlayer().exchangeCards(cardsArray);
				} else {
					activePlayerModel.setPlayingPhase("Reinforcement - Exchange Cards");
				}
			} else if (infantryCount >= 3 || cavalryCount >= 3 || artilleryCount >= 3) {
				if (!"Human".equalsIgnoreCase(
						RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getStrategy())) {
					String[] cardsArray = new String[3];
					int count = 0;
					String checkType = infantryCount >= 3 ? "Infantry" : cavalryCount >= 3 ? "Cavalry" : "Artillery";
					for (int i = 0; i < cards.size(); i++) {
						if (cards.get(i) == checkType) {
							cardsArray[count] = Integer.toString(i + 1);
							count++;
						}
						if (count == 3) {
							break;
						}
					}
					RunningGame.getInstance().getCurrentPlayer().exchangeCards(cardsArray);
				} else {
					activePlayerModel.setPlayingPhase("Reinforcement - Exchange Cards");
				}
			}
		} else {
			this.setCardExchangeCompleted(true);
			this.reinforceInitialization();
		}
	}

	/**
	 * This method checks whether game has completed or not
	 */
	public boolean checkGameCompleted() {
		PlayerModel activePlayerModel = this.getCurrentPlayer().getPlayerModel();
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), activePlayerModel);
		int totalCountryNumbers = RunningGame.getInstance().getCountries().getList().size();
		int playerCountryOwnsCount = playerCountries.size();
		if (totalCountryNumbers == playerCountryOwnsCount) {
			return true;
		}
		
		return false;
	}

	/**
	 * This method initializes reinforcement phase
	 */
	public void reinforceInitialization() {

		int numberOfCountries = this.getCountries().getList().size();
		PlayerModel activePlayerModel = this.getCurrentPlayer().getPlayerModel();

		this.setCardGiven(false);
		this.setAllOut(false);
		this.setAttackCompleted(false);

		int reinforcementArmies = activePlayerModel.getReinforcementNoOfArmies();
		boolean fullContinentOccupy = false;

		if (Math.floor((float) numberOfCountries / 3) > 3) {
			reinforcementArmies += Math.floorDiv(numberOfCountries, 3);
		} else {
			reinforcementArmies += 3;
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

			// if not human execute reinforce automatically
			System.out.println(RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getStrategy());

			if (!"Human"
					.equalsIgnoreCase(RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getStrategy())) {
				RunningGame.getInstance().getCurrentPlayer().getStrategy().reinforce(null, 0);
			}

		}
	}

	/**
	 * gets {@link subject}
	 * @return
	 */
	public RunningGameSubject getSubject() {
		return subject;
	}

	/**
	 * 
	 * @param observer
	 */
	public void setSubject(RunningGameSubject subject) {
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
}
