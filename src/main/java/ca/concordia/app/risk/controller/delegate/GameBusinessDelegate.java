package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.controller.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.services.GameService;

/**
 * 
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
   * @throws Exception
   */
  public void saveGame() {
    gameService.saveGame();
  }

  /**
   * 
   * @param fileName
   */
  public void saveMap(String fileName) {
    gameService.saveMap(fileName);
  }

  /**
   * 
   * @return true if map is valid
   */
  public boolean validateMap(String continentName) {
    return gameService.validateMap(continentName);
  }

  /**
   * 
   * @param fileName
   */
  public void editMap(String fileName) {
    gameService.editMap(fileName);
  }

  /**
   * 
   * @param fileName
   */
  public void loadMap(String fileName) {
    gameService.loadMap(fileName);
  }

  /**
   * 
   * @param playerDto
   */
  public void addPlayer(PlayerDto playerDto) {
    gameService.addPlayer(playerDto);
  }

  /**
   * 
   * @param playerDto
   */
  public void removePlayer(PlayerDto playerDto) {
    gameService.removePlayer(playerDto);
  }

  /**
   * 
   * @param populateCountries
   */
  public void populateCountries() {
    gameService.populateCountries();
  }

  /**
   * 
   * @param countryName
   */
  public void placeArmy(String countryName) {
    gameService.placeArmy(countryName);
  }

  /**
   * 
   * @param countryName
   * @param numberOfArmies
   */
  public void reinforce(String countryName, int numberOfArmies) {
    gameService.reinforce(countryName, numberOfArmies);
  }

  public void placeall() {
    gameService.placeAll();
  }

  public void moveToNextPlayer() {
    gameService.moveToNextPlayer();
  }

  /**
   *
   * @param fromCountry
   * @param toCountry
   * @param numberOfArmies
   */
  public void fortify(String fromCountry, String toCountry, int numberOfArmies) {
    gameService.fortify(fromCountry, toCountry, numberOfArmies);
  }
}