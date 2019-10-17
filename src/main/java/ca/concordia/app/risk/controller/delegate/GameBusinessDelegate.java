package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.services.GameService;

/**
 * GameBusinessDelegate
 * @author i857625
 *
 */
public class GameBusinessDelegate {

  /**
   * 
   * Dependency injection from GameService
   */
  @Autowired
  GameService gameService;

  /**
   * 
   * save Game
   */
  public void saveGame() {
    gameService.saveGame();
  }

  /**
   * save Map
   * @param fileName
   * file name
   */
  public void saveMap(String fileName) {
    gameService.saveMap(fileName);
  }

  /**
   * validate Map
   * 
   * @param continentName
   * continent name
   * @return true if map is valid
   */
  public boolean validateMap(String continentName) {
    return gameService.validateMap(continentName);
  }

  /**
   * edit Map
   * 
   * @param fileName
   * file name 
   */
  public void editMap(String fileName) {
    gameService.editMap(fileName);
  }

  /**
   * load Map
   * 
   * @param fileName
   * file name
   */
  public void loadMap(String fileName) {
    gameService.loadMap(fileName);
  }

  /**
   * add Player
   * 
   * @param playerDto
   * player Dto
   */
  public void addPlayer(PlayerDto playerDto) {
    gameService.addPlayer(playerDto);
  }

  /**
   * remove Player
   * 
   * @param playerDto
   * player Dto
   */
  public void removePlayer(PlayerDto playerDto) {
    gameService.removePlayer(playerDto);
  }

  /**
   * populate Countries
   * 
   */
  public void populateCountries() {
    gameService.populateCountries();
  }

  /**
   * place Army
   * 
   * @param countryName
   * country name
   */
  public void placeArmy(String countryName) {
    gameService.placeArmy(countryName);
  }

  /**
   * reinforce
   * 
   * @param countryName
   * country name
   * @param numberOfArmies
   * number of armies
   */
  public void reinforce(String countryName, int numberOfArmies) {
    gameService.reinforce(countryName, numberOfArmies);
  }

  /**
   * place all
   * 
   */
  public void placeall() {
    gameService.placeAll();
  }

  /**
   * move round To Next Player
   * 
   */
  public void moveToNextPlayer() {
    gameService.moveToNextPlayer();
  }

  /**
   * fortify
   *
   * @param fromCountry
   * origin country
   * @param toCountry
   * destination country
   * @param numberOfArmies
   * number of armies
   */
  public void fortify(String fromCountry, String toCountry, int numberOfArmies) {
    gameService.fortify(fromCountry, toCountry, numberOfArmies);
  }
}