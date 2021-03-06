package ca.concordia.app.risk.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.builder.GamePersistanceManager;
import ca.concordia.app.risk.model.builder.GameSavingBuilder;
import ca.concordia.app.risk.model.builder.RunningGameBuilder;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CardsModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * GameController has all the commands related to saving, loading and editing
 * the game
 * 
 * @author i857625
 */
public class GameService {

  @Autowired
  MapService mapService;

  /**
   * Dependency injection from ShellHelper
   */
  @Autowired
  ShellHelper shellHelper;

  /**
   * Dependency injection from ObjectFactory
   */
  ObjectFactory objectFactory = new ObjectFactory();

  /**
   * 
   */
  private static Logger log = LoggerFactory.getLogger(GameService.class);

  public static int turnCount = 0;
  public static int maxTurnsCount = 100;

  /**
   * This method saves the game
   */
  public void saveGame() {
    GameSavingBuilder gameSavingBuilder = new GameSavingBuilder();
    GamePersistanceManager gamePersistanceManager = new GamePersistanceManager(gameSavingBuilder);
    gamePersistanceManager.saveGame();
  }

  /**
   * This method saves the game
   */
  public void loadGame() {
    RunningGameBuilder runningGameBuilder = new RunningGameBuilder();
    GamePersistanceManager gamePersistanceManager = new GamePersistanceManager(runningGameBuilder);
    gamePersistanceManager.loadGame();
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method adds a player to the running game
   * 
   * @param playerDto player Dto
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

    if (!isNameExist.isEmpty()) {
      throw new RiskGameRuntimeException("Name already exists!");
    }

    BeanUtils.copyProperties(playerDto, playerModel);
    PlayerDaoImpl playerDaoImp = new PlayerDaoImpl();
    playerDaoImp.assignID(RunningGame.getInstance(), playerModel);

    // Assign a random color
    numOfPlayers = RunningGame.getInstance().getPlayers().getList().size();

    if (numOfPlayers == 0) {
      color = "Red";
    } else if (numOfPlayers == 1) {
      color = "Blue";
    } else if (numOfPlayers == 2) {
      color = "Green";
    } else if (numOfPlayers == 3) {
      color = "Yellow";
    } else if (numOfPlayers == 4) {
      color = "Black";
    }

    playerModel.setColor(color);
    playerModel.setCards(new CardsModel());

    RunningGame.getInstance().getPlayers().getList().add(playerModel);
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method removes a player from the running game
   * 
   * @param playerDto player Dto
   */
  public void removePlayer(PlayerDto playerDto) {

    if (!RunningGame.getInstance().isMapLoaded()) {
      throw new RiskGameRuntimeException("Command cannot be performed, map has not been loaded yet");
    }
    if (RunningGame.getInstance().isCountriesPopulated()) {
      throw new RiskGameRuntimeException("Command cannot be performed, countries has been populated");
    }
    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    PlayerDaoImpl playerDao = new PlayerDaoImpl();
    PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), playerDto.getName());
    playerDao.delete(RunningGame.getInstance(), playerModel);
  }

  /**
   * This method populates countries
   */
  public void populateCountries() {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    int numberOfCountries = RunningGame.getInstance().getCountries().getList().size();
    int numberOfPlayers = RunningGame.getInstance().getPlayers().getList().size();
    int playerID = 0;

    if (numberOfCountries == 0) {
      throw new RiskGameRuntimeException("No Countries have been added to the game");
    }
    if (numberOfPlayers == 0) {
      throw new RiskGameRuntimeException("No players have been added to the game");
    }

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
   * This method places armies
   * 
   * @param countryName country name to place army
   */
  public void placeArmy(String countryName) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    int numberOfPlayers = RunningGame.getInstance().getPlayers().getList().size();
    if (numberOfPlayers == 0) {
      throw new RiskGameRuntimeException("No players have been added to the game");
    }

    PlayerModel activePlayerModel = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
    if (activePlayerModel == null) {
      throw new RiskGameRuntimeException("There is no current player assigned yet");
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
   * This method does reinforcement
   * 
   * @param countryName    country name to do reinforcement
   * @param numberOfArmies number of armies to reinforce
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

    if (countryModel.getPlayerId() != activePlayerModel.getId()) {
      throw new RiskGameRuntimeException("This country is not assigned to " + activePlayerModel.getName());
    }
    RunningGame.getInstance().getCurrentPlayer().reinforce(countryModel, numberOfArmies);
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method does fortification
   * 
   * @param fromCountry    origin country to fortify
   * @param toCountry      destination country to fortify
   * @param numberOfArmies number of armies
   */
  public void fortify(String fromCountry, String toCountry, int numberOfArmies) {
    RunningGame.getInstance().getCurrentPlayer().fortify(fromCountry, toCountry, numberOfArmies);
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method place all the armies
   */
  public void placeAll() {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }
    try {
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
      if (RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId() != 1) {
        RunningGame.getInstance().moveToNextPlayer();
      }
      RunningGame.getInstance().getSubject().markAndNotify();
      RunningGame.getInstance().exchangeCardsInitialization();
    } catch (Exception e) {

    }
  }

  /**
   * This method exchanges cards
   * 
   * @param num1 Card 1
   * @param num2 Card 2
   * @param num3 Card 3
   */
  public void exchangecards(String num1, String num2, String num3) {

    if (!RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Not Running");

    // if (!RunningGame.getInstance().isReinforceCompleted()) {
    // throw new RiskGameRuntimeException("Command cannot be performed, please
    // complete Reinforcement Phase first");
    // }
    String[] cardsArray = new String[3];

    // Add Card numbers to an array
    cardsArray[0] = num1;
    cardsArray[1] = num2;
    cardsArray[2] = num3;

    // check if there are three valid cards to be exchanged
    for (int i = 0; i < cardsArray.length; i++) {
      if ("none".equalsIgnoreCase(cardsArray[i])) {
        throw new RiskGameRuntimeException("Please enter three valid numbers");
      }
    }

    // check for negative values
    for (int i = 0; i < cardsArray.length; i++) {
      if (Integer.parseInt(cardsArray[i]) <= 0) {
        throw new RiskGameRuntimeException(cardsArray[i] + " is not a valid number, enter a positive number");
      }
    }

    // Infantry
    // Cavalry
    // Artillery

    // Get card list
    List<String> cards = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();

    // check if numbers are within limits of card list
    for (int i = 0; i < cardsArray.length; i++) {
      if (Integer.parseInt(cardsArray[i]) > cards.size()) {
        throw new RiskGameRuntimeException(cardsArray[i] + " does not exist");
      }
    }

    // validate requested exchange cards are not null
    for (int i = 0; i < cardsArray.length; i++) {
      if (cards.get(Integer.parseInt(cardsArray[i]) - 1) == null) {
        throw new RiskGameRuntimeException(cardsArray[i] + " does not exist");
      }
    }

    boolean performCardExchange = false;
    // check if all cards are of same type
    if (cards.get(Integer.parseInt(cardsArray[0]) - 1) == cards.get(Integer.parseInt(cardsArray[1]) - 1)
        && cards.get(Integer.parseInt(cardsArray[1]) - 1) == cards.get(Integer.parseInt(cardsArray[2]) - 1)) {

      performCardExchange = true;

      // check if all cards are of different type
    } else if (cards.get(Integer.parseInt(cardsArray[0]) - 1) != cards.get(Integer.parseInt(cardsArray[1]) - 1)
        && cards.get(Integer.parseInt(cardsArray[1]) - 1) != cards.get(Integer.parseInt(cardsArray[2]) - 1)) {

      performCardExchange = true;

      // cards are neither the same nor different
    } else {
      throw new RiskGameRuntimeException("Cards are neither the same nor different, Please enter valid card numbers");
    }

    if (performCardExchange) {
      RunningGame.getInstance().getCurrentPlayer().exchangeCards(cardsArray);
      RunningGame.getInstance().getSubject().markAndNotify();
      performCardExchange = false;
    }
  }

  /**
   * This method does attack. Command: attack -countryNameFrom [countryNameFrom]
   * -countyNameTo [countyNameTo] -number [numberOfArmies]
   * 
   * @param countryNameFrom attacker country name
   * @param countyNameTo    defender country name
   * @param numDice         count of dices for attacker
   */
  public void attack(String countryNameFrom, String countyNameTo, String numDice) {
    if (numDice.equalsIgnoreCase("-allout")) {
      RunningGame.getInstance().setAllOut(true);
    } else {
      RunningGame.getInstance().setAllOut(false);
    }
    RunningGame.getInstance().getCurrentPlayer().attack(countryNameFrom, countyNameTo, numDice);
  }

  /**
   *
   * @param numDice dice count for defender
   */
  public void defend(String numDice) {
    RunningGame.getInstance().getCurrentPlayer().defend(numDice);
  }

  /**
   *
   * @param num number of armies to move from attacker to defender Country
   */
  public void attackMove(String num) {
    RunningGame.getInstance().getCurrentPlayer().attackMove(num);
  }

  public static String gameWinner = "";
  public static List<String> tournamentFinalValue = new ArrayList<String>();

  public void tournament(String mapFiles, String playerStrategies, String noOfGames, String maxTurns) {
    if (Integer.parseInt(noOfGames) < 1 && Integer.parseInt(noOfGames) > 5) {
      throw new RiskGameRuntimeException("Number of Games should be within the range 1-5");
    }

    if (Integer.parseInt(maxTurns) < 10 && Integer.parseInt(maxTurns) > 50) {
      throw new RiskGameRuntimeException("Number of Turns should be within the range 10-50");
    }

    String[] maps = mapFiles.split(",");

    if (maps.length > 5 || maps.length < 1) {
      throw new RiskGameRuntimeException("Number of Map Files should be within the range 1-5");
    }

    String[] players = playerStrategies.split(",");

    if (players.length > 4 || players.length < 2) {
      throw new RiskGameRuntimeException("Number of Players should be within the range 2-4");
    }

    for (int i = 0; i < players.length; i++) {
      if (players[i].equalsIgnoreCase("Human")) {
        throw new RiskGameRuntimeException("Human Player cannot be a strategy in tournament mode");
      }
    }

    GameService.maxTurnsCount = Integer.parseInt(maxTurns);

    for (int i = 0; i < maps.length; i++) {
      for (int j = 0; j < Integer.parseInt(noOfGames); j++) {
        mapService.loadMap(maps[i], "DOMINATION");
        for (int k = 0; k < players.length; k++) {
          PlayerDto playerDto = new PlayerDto();
          playerDto.setName("Id" + (k + 1));
          playerDto.setStrategy(players[k].toUpperCase());
          this.addPlayer(playerDto);
        }
        this.populateCountries();
        try {
          this.placeAll();
          tournamentFinalValue.add("Map " + (i + 1) + ", Game " + (j + 1) + ", " + gameWinner);
          gameWinner = "";
          turnCount = 0;
        } catch (Exception e) {
          tournamentFinalValue.add("Map " + (i + 1) + ", Game " + (j + 1) + ", " + gameWinner);
          gameWinner = "";
          turnCount = 0;
        }
      }
    }

    for (int x = 0; x < tournamentFinalValue.size(); x++) {
      System.out.println(tournamentFinalValue.get(x));
    }

  }

}
