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
   * @return
   */
  public boolean validateMap() {
    return gameService.validateMap();
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

  public void addPlayer(PlayerDto playerDto) {
    gameService.addPlayer(playerDto);
  }

  public void removePlayer(PlayerDto playerDto) {
    gameService.removePlayer(playerDto);
  }

  public void populateCountries() {
    gameService.populateCountries();
  }

  public void placeArmy(String countryName) {
    gameService.placeArmy(countryName);
  }

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