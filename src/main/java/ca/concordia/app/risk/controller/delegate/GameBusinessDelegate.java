package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.services.GameService;

/**
 * GameBusinessDelegate, The facade class to access the GameService
 * 
 * @see GameService
 * @author i857625
 *
 */
public class GameBusinessDelegate {

  /**
   * Dependency injection from GameService
   */
  @Autowired
  GameService gameService;

  /**
   * This method used for saving the Game.
   */
  public void saveGame() {
    gameService.saveGame();
  }
  
  /**
   * This method used for loading the saved Game.
   */
  public void loadGame() {
	  gameService.loadGame();
  }

  /**
   * This method used for saving the Map.
   * 
   * @param fileName file name
   */
  public void saveMap(String fileName) {
    gameService.saveMap(fileName);
  }

  /**
   * This method used for validating the Map.
   * 
   * @param continentName continent name
   * @return true if map is valid
   */
  public boolean validateMap(String continentName) {
    return gameService.validateMap(continentName);
  }

  /**
   * This method used for editing the Map.
   * 
   * @param fileName file name
   */
  public void editMap(String fileName) {
    gameService.editMap(fileName);
  }

  /**
   * This method used for loading the Map.
   * 
   * @param fileName file name
   */
  public void loadMap(String fileName) {
    gameService.loadMap(fileName);
  }

  /**
   * This method used for adding a player.
   * 
   * @param playerDto player Dto
   */
  public void addPlayer(PlayerDto playerDto) {
    gameService.addPlayer(playerDto);
  }

  /**
   * This method used for removing a player.
   * 
   * @param playerDto player Dto
   */
  public void removePlayer(PlayerDto playerDto) {
    gameService.removePlayer(playerDto);
  }

  /**
   * This method used for populating Countries.
   * 
   */
  public void populateCountries() {
    gameService.populateCountries();
  }

  /**
   * This method used for place Army.
   * 
   * @param countryName country name
   */
  public void placeArmy(String countryName) {
    gameService.placeArmy(countryName);
  }

  /**
   * This method used for reinforcement.
   * 
   * @param countryName    country name
   * @param numberOfArmies number of armies
   */
  public void reinforce(String countryName, int numberOfArmies) {
    gameService.reinforce(countryName, numberOfArmies);
  }

  /**
   * This method used for place all.
   * 
   */
  public void placeall() {
    gameService.placeAll();
  }

  /**
   * This method used for fortify.
   *
   * @param fromCountry    origin country
   * @param toCountry      destination country
   * @param numberOfArmies number of armies
   */
  public void fortify(String fromCountry, String toCountry, int numberOfArmies) {
    gameService.fortify(fromCountry, toCountry, numberOfArmies);
  }

  /**
   * This method used for exchanging the cards.
   *
   * @param num1 card 1
   * @param num2 card 2
   * @param num3 card 3
   */
  public void exchangecards(String num1, String num2, String num3) {
    gameService.exchangecards(num1, num2, num3);
  }

  /**
   * This method used for attack.
   *
   * @param countryNameFrom attacking country
   * @param countyNameTo    defending country
   * @param numDice         number of dices attacker want to use
   */
  public void attack(String countryNameFrom, String countyNameTo, String numDice) {
    gameService.attack(countryNameFrom, countyNameTo, numDice);
  }

  /**
   * This method used for defend.
   * 
   * @param numDice number of dices defender use
   */
  public void defend(String numDice) {
    gameService.defend(numDice);
  }

  /**
   * This method used for moving number of armies to the defeated country.
   * 
   * @param num number of dices defender use
   */
  public void attackmove(String num) {
    gameService.attackMove(num);
  }

  public void tournament(String mapFiles, String playerStrategies, String noOfGames, String maxTurns) {
    gameService.tournament(mapFiles, playerStrategies, noOfGames, maxTurns);
  }
}