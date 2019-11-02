package ca.concordia.app.risk.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import ca.concordia.app.risk.model.xmlbeans.*;
import org.jgrapht.GraphPath;
//import com.sun.deploy.security.SelectableSecurityManager;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.utility.DateUtils;
//import sun.lwawt.macosx.CSystemTray;

/**
 * 
 * GameController has all the commands related to saving, loading and editing
 * the game
 * 
 * @author i857625
 */
public class GameService {

	private static final String GAME_CANNOT_BE_SAVED = "Game caanot be saved!";

	@Autowired
	MapService mapService;

	@Autowired
	ShellHelper shellHelper;

	ObjectFactory objectFactory = new ObjectFactory();

	private static Logger log = LoggerFactory.getLogger(GameService.class);

	/**
	 *
	 */
	public void saveGame() {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
			RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);
		} catch (DatatypeConfigurationException configurationException) {
			throw new RiskGameRuntimeException(GAME_CANNOT_BE_SAVED, configurationException);
		}
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(RunningGame.getInstance(), file);
		} catch (JAXBException jaxbException) {
			throw new RiskGameRuntimeException(GAME_CANNOT_BE_SAVED, jaxbException);
		}
	}

	/**
	 * This method saves the map file
	 * 
	 * @param fileName file name
	 */
	public void saveMap(String fileName) {
		if (!this.validateMap("All")) {
			throw new RiskGameRuntimeException("Map cannot be saved, map in invalid");
		}
		try (FileWriter fileWriter = new FileWriter(String.format("saved/%s", fileName))) {
			PrintWriter printWriter = new PrintWriter(fileWriter);

			// add continents
			printWriter.printf("[continents]%s", System.lineSeparator());
			Comparator<ContinentModel> continentModelComparator = Comparator.comparing(ContinentModel::getId);
			RunningGame.getInstance().getContinents().getList().stream().sorted(continentModelComparator)
					.forEach(continent -> printWriter.printf("%s %s%s", continent.getName(),
							continent.getNumberOfCountries(), System.lineSeparator()));
			printWriter.print(System.lineSeparator());

			// add countries
			Comparator<CountryModel> countryModelComparator = Comparator.comparing(CountryModel::getId);
			printWriter.printf("[countries]%s", System.lineSeparator());
			RunningGame.getInstance().getCountries().getList().stream().sorted(countryModelComparator)
					.forEach(country -> printWriter.printf("%s %s %s %s %s", country.getId(), country.getName(),
							country.getContinentId(), country.getNumberOfArmies(), System.lineSeparator()));
			printWriter.print(System.lineSeparator());

			// add borders
			printWriter.printf("[borders]%s", System.lineSeparator());
			Comparator<BorderModel> comparator = Comparator.comparing(BorderModel::getCountryId);
			RunningGame.getInstance().getBorders().getList().stream().sorted(comparator)
					.forEach(border -> printWriter.printf("%s %s %s", border.getCountryId(),
							border.getNeighbours().stream().map(String::valueOf).collect(Collectors.joining(" ")),
							System.lineSeparator()));
		} catch (IOException ioException) {
			throw new RiskGameRuntimeException("Game file cannot be saved", ioException);
		}
	}

	/**
	 * This method loads the map file
	 * 
	 * @param fileName file name
	 */
	public void loadMap(String fileName) {
		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
		this.editMap(fileName);

		if (!this.validateMap("All")) {
			RunningGame.reset();
			throw new RiskGameRuntimeException("Countries are not connected, Map is invalid");
		}
		
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * This method edits map file
	 * 
	 * @param fileName file name
	 */
	public void editMap(String fileName) {
		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

		RunningGame.reset();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)))) {
			int flag = 0;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if ("[continents]".equalsIgnoreCase(line)) {
					flag = 1;
				} else if ("[countries]".equalsIgnoreCase(line)) {
					flag = 2;
				} else if ("[borders]".equalsIgnoreCase(line)) {
					flag = 3;
				} else if (!System.lineSeparator().equalsIgnoreCase(line) && !"".equals(line.trim())) {
					switch (flag) {
					case 1:
						StringTokenizer continentLine = new StringTokenizer(line, " ");
						ContinentDto continentDto = new ContinentDto();
						continentDto.setName(continentLine.nextToken());
						continentDto.setNumberOfCountries(Integer.parseInt(continentLine.nextToken()));
						mapService.addContinent(continentDto);
						break;
					case 2:
						StringTokenizer countryLine = new StringTokenizer(line, " ");
						ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
						CountryDto countryDto = new CountryDto();
						countryLine.nextToken();
						countryDto.setName(countryLine.nextToken());
						countryDto.setContinentName(continentDaoImpl
								.findById(RunningGame.getInstance(), Integer.parseInt(countryLine.nextToken()))
								.getName());
						countryDto.setNumberOfArmies(Integer.parseInt(countryLine.nextToken()));
						mapService.addCountry(countryDto);
						break;
					case 3:
						StringTokenizer borderLine = new StringTokenizer(line, " ");
						CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
						BorderDto borderDto = new BorderDto();
						borderDto.setCountryName(countryDaoImpl
								.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken()))
								.getName());
						while (borderLine.hasMoreTokens()) {
							borderDto.setNeighborCountryName(countryDaoImpl
									.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken()))
									.getName());
							try {
								mapService.addNeighbor(borderDto);
							} catch (RiskGameRuntimeException riskGameRuntimeException) {
								// nothing to do
							}
						}
						break;
					default:
						break;
					}
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			throw new RiskGameRuntimeException(String.format("Map cannot be edited, [%s] does not exist", fileName),
					fileNotFoundException);
		} catch (IOException ioException) {
			throw new RiskGameRuntimeException("Map cannot be edited", ioException);
		}
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * This method validates the map
	 *
	 * @param continentName continent name to validate
	 * @return returns connectivity status of the graph vertices
	 */
	public boolean validateMap(String continentName) {
		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

		RunningGame.getInstance().setMapLoaded(true);

		if ("All".equals(continentName)) {
			int numberOfNotConnectedContinent = 0;
			List<ContinentModel> continentsList = RunningGame.getInstance().getContinents().getList();
			for (ContinentModel continentModel : continentsList) {
				ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
						RunningGame.getInstance().getContinentGraph(continentModel.getName()));
				if (!connectivityInspector.isConnected()) {
					log.info(shellHelper.getErrorMessage(
							String.format("Continent [%s] is not connected", continentModel.getName())));
					numberOfNotConnectedContinent++;
				}
			}
			if (numberOfNotConnectedContinent > 0) {
				return false;
			} else {
				ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
						RunningGame.getInstance().getGraph());
				return connectivityInspector.isConnected();
			}
		} else {
			ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
			ContinentModel continentModel = continentDaoImpl.findByName(RunningGame.getInstance(), continentName);
			if (continentModel == null) {
				throw new RiskGameRuntimeException(String.format("Continent [%s] doesn't exist", continentName));
			}
			ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
					RunningGame.getInstance().getContinentGraph(continentName));
			return connectivityInspector.isConnected();
		}
	}

	/**
	 * 
	 * @param playerDto
	 */
	public void addPlayer(PlayerDto playerDto) {

		if (!RunningGame.getInstance().isMapLoaded())
			throw new RiskGameRuntimeException("Command cannot be performed, map has not been loaded yet");

		if (RunningGame.getInstance().isCountriesPopulated())
			throw new RiskGameRuntimeException("Command cannot be performed, countries has been populated");

		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

		int numOfPlayers = 0;
		String color = null;

		PlayerModel playerModel = objectFactory.createPlayerModel();

		// check if name exists
		List<PlayerModel> isNameExist = RunningGame.getInstance().getPlayers().getList().stream()
				.filter(c -> c.getName().equals(playerDto.getName())).collect(Collectors.toList());

		if (!isNameExist.isEmpty())
			throw new RiskGameRuntimeException("Name already exists!");

		BeanUtils.copyProperties(playerDto, playerModel);
		PlayerDaoImpl playerDaoImp = new PlayerDaoImpl();
		playerDaoImp.assignID(RunningGame.getInstance(), playerModel);

		// Assign a random color
		numOfPlayers = RunningGame.getInstance().getPlayers().getList().size();

		if (numOfPlayers == 0)
			color = "Red";
		else if (numOfPlayers == 1)
			color = "Blue";
		else if (numOfPlayers == 2)
			color = "Green";
		else if (numOfPlayers == 3)
			color = "Yellow";
		else if (numOfPlayers == 4)
			color = "Black";

		playerModel.setColor(color);
		playerModel.setCards(new CardsModel());

		RunningGame.getInstance().getPlayers().getList().add(playerModel);
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * 
	 * @param playerDto
	 */
	public void removePlayer(PlayerDto playerDto) {
		if (!RunningGame.getInstance().isMapLoaded())
			throw new RiskGameRuntimeException("Command cannot be performed, map has not been loaded yet");

		if (RunningGame.getInstance().isCountriesPopulated())
			throw new RiskGameRuntimeException("Command cannot be performed, countries has been populated");

		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), playerDto.getName());
		playerDao.delete(RunningGame.getInstance(), playerModel);
	}

	/**
	 * 
	 */
	public void populateCountries() {

		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

		int numberOfCountries = RunningGame.getInstance().getCountries().getList().size();
		int numberOfPlayers = RunningGame.getInstance().getPlayers().getList().size();
		int playerID = 0;

		if (numberOfCountries == 0)
			throw new RiskGameRuntimeException("No Countries have been added to the game");

		if (numberOfPlayers == 0)
			throw new RiskGameRuntimeException("No players have been added to the game");

		List<CountryModel> countryModels = RunningGame.getInstance().getCountries().getList(); // convert list to stream

		for (CountryModel countryModel : countryModels) {
			if (playerID < numberOfPlayers) {
				playerID++;
			} else {
				playerID = 1;
			}
			countryModel.setPlayerId(playerID);
		}

		RunningGame.getInstance().setCountriesPopulated(true);
		RunningGame.getInstance().moveToNextPlayer();
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * 
	 * @param countryName
	 */
	public void placeArmy(String countryName) {
		
		

		if (RunningGame.getInstance().isGamePlay()) {
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
		}
		
		int numberOfPlayers  = RunningGame.getInstance().getPlayers().getList().size();
		if (numberOfPlayers == 0) {
			throw new RiskGameRuntimeException("No players have been added to the game");
		}
		
		PlayerModel activePlayerModel = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		if (activePlayerModel == null) {
			throw new RiskGameRuntimeException("Theer is no current player assigned yet");
		}
		
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = null;
		countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
		if (countryModel == null) {
			throw new RiskGameRuntimeException("Country Does Not Exist");
		}
		
		if (countryModel.getPlayerId() != activePlayerModel.getId()) {
			throw new RiskGameRuntimeException(countryName + " is not assigned to " + activePlayerModel.getName());
		}
		
		int totalNumberOfArmiesPerPlayer = 0;
		int numberOfAssignedArmies = 0;

		List<CountryModel> countryModels = RunningGame.getInstance().getCountries().getList();
		for (CountryModel item : countryModels) {
			if (item.getPlayerId() == activePlayerModel.getId()) {
				numberOfAssignedArmies += item.getNumberOfArmies();
			}
		}

		if (numberOfPlayers == 2) {
			totalNumberOfArmiesPerPlayer = 40;
		} else if (numberOfPlayers == 3) {
			totalNumberOfArmiesPerPlayer = 35;
		} else if (numberOfPlayers == 4) {
			totalNumberOfArmiesPerPlayer = 30;
		} else if (numberOfPlayers == 5) {
			totalNumberOfArmiesPerPlayer = 25;
		}

		if (numberOfAssignedArmies < totalNumberOfArmiesPerPlayer) {
			countryModel.setNumberOfArmies(countryModel.getNumberOfArmies() + 1);

		} else {
			throw new RiskGameRuntimeException("Total Number of Armies has been exceeded");
		}
			
		RunningGame.getInstance().moveToNextPlayer();
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 *
	 */
	public void reinforceInitialization() {

		int numberOfCountries = RunningGame.getInstance().getCountries().getList().size();
		
		PlayerModel activePlayerModel = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();

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
			activePlayerModel.setReinforcementNoOfArmies(reinforcementArmies);
			RunningGame.getInstance().setReinforceCompleted(false);
		}
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * 
	 * @param countryName
	 * @param numberOfArmies
	 */
	public void reinforce(String countryName, int numberOfArmies) {

		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
		if (countryModel == null) {
			throw new RiskGameRuntimeException("Country doesn't exist");
		}
		
		PlayerModel activePlayerModel = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		if (activePlayerModel == null) {
			throw new RiskGameRuntimeException("No Players have been added");
		}
		
		if (countryModel.getPlayerId()!=activePlayerModel.getId()) {
			throw new RiskGameRuntimeException("This country is not assigned to " + activePlayerModel.getName());
		}
		RunningGame.getInstance().getCurrentPlayer().reinforce(countryModel, numberOfArmies);
		RunningGame.getInstance().getSubject().markAndNotify();	
	}

	/**
	 * This method do fortify operation
	 *
	 * @param fromCountry    origin country to fortify
	 * @param toCountry      destination country to fortify
	 * @param numberOfArmies number of armies
	 */
	public void fortify(String fromCountry, String toCountry, int numberOfArmies) {

		if (!RunningGame.getInstance().isReinforceCompleted())
			throw new RiskGameRuntimeException("Please reinforce first ");

//		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
//		PlayerModel activePlayerModel = playerDaoImpl.findById(RunningGame.getInstance(),
//				RunningGame.getInstance().getCurrentPlayerId());
		
		PlayerModel activePlayerModel = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();

		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel fromCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), fromCountry);
		CountryModel toCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), toCountry);

		if (activePlayerModel == null) {
			throw new RiskGameRuntimeException("No Players have been added");
		}

		if (fromCountryModel == null) {
			throw new RiskGameRuntimeException("From Country doesn't exist");
		}

		if (toCountryModel == null) {
			throw new RiskGameRuntimeException("To Country doesn't exist");
		}

		if ((fromCountryModel.getPlayerId()) != (activePlayerModel.getId())) {
			throw new RiskGameRuntimeException(
					"From Country " + fromCountry + " is not owned by " + activePlayerModel.getName());
		}

		if ((toCountryModel.getPlayerId()) != (activePlayerModel.getId())) {
			throw new RiskGameRuntimeException(
					"To Country " + toCountry + " is not owned by " + activePlayerModel.getName());
		}

		if (fromCountryModel.getNumberOfArmies() - numberOfArmies < 1) {
			throw new RiskGameRuntimeException("From Country " + fromCountry
					+ " needs to have atleast one army after fortification. Please reduce the number of armies");
		}

		AllDirectedPaths<String, DefaultEdge> allDirectedPaths = new AllDirectedPaths<>(
				RunningGame.getInstance().getGraph());
		List<GraphPath<String, DefaultEdge>> allPaths = allDirectedPaths.getAllPaths(fromCountryModel.getName(),
				toCountryModel.getName(), false, 10);
		int counter = 0;
		for (GraphPath<String, DefaultEdge> graphPath : allPaths) {
			List<String> countriesInPath = graphPath.getVertexList();
			boolean connected = true;
			for (String countryName : countriesInPath) {
				CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
				if (activePlayerModel.getId() != countryModel.getPlayerId()) {
					connected = false;
					break;
				}
			}
			if (connected) {
				counter++;
				break;
			}
		}

		if (counter == 0) {

			List<GraphPath<String, DefaultEdge>> allPathsReverse = allDirectedPaths
					.getAllPaths(toCountryModel.getName(), fromCountryModel.getName(), false, 10);
			int counterNew = 0;
			for (GraphPath<String, DefaultEdge> graphPath : allPathsReverse) {
				List<String> countriesInPath = graphPath.getVertexList();
				boolean connected = true;
				for (String countryName : countriesInPath) {
					CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
					if (activePlayerModel.getId() != countryModel.getPlayerId()) {
						connected = false;
						break;
					}
				}
				if (connected) {
					counterNew++;
					break;
				}
			}
			if (counterNew == 0) {
				throw new RiskGameRuntimeException("No Path Available!");
			} else {
				toCountryModel.setNumberOfArmies(toCountryModel.getNumberOfArmies() - numberOfArmies);
				fromCountryModel.setNumberOfArmies(fromCountryModel.getNumberOfArmies() + numberOfArmies);
				
				if (!RunningGame.getInstance().isReinforceCompleted()) {
					throw new RiskGameRuntimeException("Please reinforce first");
				} else {
					RunningGame.getInstance().moveToNextPlayer();
					reinforceInitialization();

				}
				
			}
		} else {
			fromCountryModel.setNumberOfArmies(fromCountryModel.getNumberOfArmies() - numberOfArmies);
			toCountryModel.setNumberOfArmies(toCountryModel.getNumberOfArmies() + numberOfArmies);
			if (!RunningGame.getInstance().isReinforceCompleted()) {
				throw new RiskGameRuntimeException("Please reinforce first");
			} else {
				RunningGame.getInstance().moveToNextPlayer();
				reinforceInitialization();
			}
		}
		
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 * 
	 */
	public void placeAll() {

		if (RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

		int totalNumberOfArmiesPerPlayer = 0;
		int numberOfAssignedArmies = 0;
		int numberOfPlayers = 0;

		// get number of players
		// check each player
		// get all the countries for that player
		// get all the assigned armies
		// place the remaining randomly
		numberOfPlayers = RunningGame.getInstance().getPlayers().getList().size();

		if (numberOfPlayers == 0) {
			throw new RiskGameRuntimeException("No players have been added to the game");
		}

		if (numberOfPlayers == 2) {
			totalNumberOfArmiesPerPlayer = 40;
		} else if (numberOfPlayers == 3) {
			totalNumberOfArmiesPerPlayer = 35;
		} else if (numberOfPlayers == 4) {
			totalNumberOfArmiesPerPlayer = 30;
		} else if (numberOfPlayers == 5) {
			totalNumberOfArmiesPerPlayer = 25;
		}

		List<PlayerModel> playerModels = RunningGame.getInstance().getPlayers().getList(); // convert list to stream

		for (PlayerModel itemPlayerModel : playerModels) {
			numberOfAssignedArmies = 0;
			List<CountryModel> countryModels = RunningGame.getInstance().getCountries().getList().stream()
					.filter((c -> (c.getPlayerId()) == (itemPlayerModel.getId()))).collect(Collectors.toList());
			for (CountryModel cM : countryModels) {
				numberOfAssignedArmies += cM.getNumberOfArmies();
			}

			while (totalNumberOfArmiesPerPlayer - numberOfAssignedArmies > 0) {
				for (CountryModel itemCountryModel : countryModels) {
					if (totalNumberOfArmiesPerPlayer - numberOfAssignedArmies > 0) {
						itemCountryModel.setNumberOfArmies(itemCountryModel.getNumberOfArmies() + 1);
						numberOfAssignedArmies += 1;
					}
				}
			}
		}
		RunningGame.getInstance().setGamePlay(true);
		if(RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()!=1) {
			RunningGame.getInstance().moveToNextPlayer();
		}
		reinforceInitialization();
		RunningGame.getInstance().getSubject().markAndNotify();
	}

	/**
	 *
	 * @param num1
	 * @param num2
	 * @param num3
	 */
	public void exchangecards(String num1,String num2,String num3) {

		if (!RunningGame.getInstance().isGamePlay())
			throw new RiskGameRuntimeException("Command cannot be performed, Current game is Not Running");

		String[] cardsArray = new String[3];

		//Add Card numbers to an array
		cardsArray[0]=num1;
		cardsArray[1]=num2;
		cardsArray[2]=num3;

		// check if there are three valid cards to be exchanged
		for (int i =0; i<cardsArray.length; i++) {
			if ("none".equalsIgnoreCase(cardsArray[i])) {
				throw new RiskGameRuntimeException("Please enter three valid numbers");
			}
		}

		//check for negative values
		for (int i =0; i<cardsArray.length; i++) {
			if (Integer.parseInt(cardsArray[i]) <= 0) {
				throw new RiskGameRuntimeException(cardsArray[i] + " is not a valid number, ter a positive number");
			}
		}

		//Infantry
		//Cavalry
		//Artillery

		//testData

		//Get card list
		List<String> cards = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();

		//check if numbers are within limits of card list
		for (int i =0; i<cardsArray.length; i++){
			if(Integer.parseInt(cardsArray[i])>cards.size()) {
				throw new RiskGameRuntimeException(cardsArray[i] + " does not exist");
			}
		}

		//validate requested exchange cards are not null
		for (int i =0; i<cardsArray.length; i++){
			if(cards.get(Integer.parseInt(cardsArray[i])-1)==null) {
				throw new RiskGameRuntimeException(cardsArray[i] + " does not exist");
			}
		}

		boolean performCardExcange= false;
		//check if all cards are of same type
		if (cards.get(Integer.parseInt(cardsArray[0])-1) == cards.get(Integer.parseInt(cardsArray[1])-1)
				&& cards.get(Integer.parseInt(cardsArray[1])-1) == cards.get(Integer.parseInt(cardsArray[2])-1)){

			performCardExcange=true;

			//check if all cards are of different type
		} else if (cards.get(Integer.parseInt(cardsArray[0])-1) != cards.get(Integer.parseInt(cardsArray[1])-1)
				&& cards.get(Integer.parseInt(cardsArray[1])-1) != cards.get(Integer.parseInt(cardsArray[2])-1)){

			performCardExcange=true;

			// cards are neither the same nor different
		} else {
			throw new RiskGameRuntimeException("Cards are neither the same nor different, Please enter a valid card numbers");
		}

		if(performCardExcange){

			int reinforcementNoOfArmies = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies();
			int cardExchangeCount = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCardExchangeCount();

			//Add additional armies
			if(cardExchangeCount==0){
				// Add 5 Additional Armies
				reinforcementNoOfArmies +=5;
				cardExchangeCount+=1;
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setReinforcementNoOfArmies(reinforcementNoOfArmies);
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setCardExchangeCount(cardExchangeCount);

			} else {
				// Add 5, 10 ,15, 20 , ... Additional Armies

				reinforcementNoOfArmies += 5*(cardExchangeCount+1);
				cardExchangeCount+=1;
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setReinforcementNoOfArmies(reinforcementNoOfArmies);
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setCardExchangeCount(cardExchangeCount);
			}

			// remove cards from the list
			RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[2])-1);
			RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[1])-1);
			RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().remove(Integer.parseInt(cardsArray[0])-1);

			RunningGame.getInstance().getSubject().markAndNotify();
			performCardExcange=false;
		}
	}

	public void attack(String countryNameFrom, String countyNameTo, String numDice, String allout) {
		RunningGame.getInstance().getCurrentPlayer().attack(countryNameFrom, countyNameTo,numDice,allout);
	}

	/**
	 *
	 * @param numDice
	 */
	public void defend(String numDice) {

		// check max number of dice doesn't exceed 3 and not less than 1
		if(Integer.parseInt(numDice) >2 || Integer.parseInt(numDice) <1 ) {
			throw new RiskGameRuntimeException("number of dice should be 1 or 2");
		}

		// roll dice
		Random random = new Random();

		int numDice1 = random.nextInt(5)+1;
		int numDice2 = 0;
		if(Integer.parseInt(numDice)==2) {
			numDice2 = random.nextInt(5) + 1;
		}

		int[] defenderDice;
		if(Integer.parseInt(numDice)==2) {
			defenderDice = new int[]{numDice1, numDice2 };
		} else {
			defenderDice = new int[]{numDice1};
		}

        // compare with Running game attackerDice with defender Dice
		int[] attackerDice = RunningGame.getInstance().getAttackerDice();

		//sort Arrays
		Arrays.sort(attackerDice);
		Arrays.sort(defenderDice);

		//get Country Model of attackFrom and attackTo
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModelAttackFrom = countryDaoImpl.findByName(RunningGame.getInstance(),RunningGame.getInstance().getAttackCountryNameFrom());
		CountryModel countryModelAttackTo = countryDaoImpl.findByName(RunningGame.getInstance(),RunningGame.getInstance().getAttackCountryNameTo());

		//compare
		switch(numDice) {
			// in case defender use one die
			case "1":
				if(attackerDice[0]>defenderDice[0]){
					countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies()-1);
				} else {
					countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies()-1);
				}
				break;

			// in case defender use two dice
			case "2":
				if(attackerDice[0]>defenderDice[0]){
					countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies()-1);
				} else {
					countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies()-1);
				}

				if(attackerDice[1]>defenderDice[1]){
					countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies()-1);
				} else {
					countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies()-1);
				}

				break;
			default:

		}






	}

	/**
	 *
	 * @param num
	 */
	public void attackmove(String num) {

	    //check num are not greater than the number of armies in the attackCountryFrom
        CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
        CountryModel countryModelAttackFrom = countryDaoImpl.findByName(RunningGame.getInstance(),RunningGame.getInstance().getAttackCountryNameFrom());

        // at least one army should be there or more depends on the number of dice
        if (countryModelAttackFrom.getNumberOfArmies()-1<Integer.parseInt(num)){
            throw new RiskGameRuntimeException("Please decrease number of armies");
        }

        // move armies
        CountryModel countryModelAttackTo = countryDaoImpl.findByName(RunningGame.getInstance(),RunningGame.getInstance().getAttackCountryNameTo());
        countryModelAttackFrom.setNumberOfArmies(countryModelAttackFrom.getNumberOfArmies()-Integer.parseInt(num));
        countryModelAttackTo.setNumberOfArmies(countryModelAttackTo.getNumberOfArmies()+Integer.parseInt(num));

	}
}