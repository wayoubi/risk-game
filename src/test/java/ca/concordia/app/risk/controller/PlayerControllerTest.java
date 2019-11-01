package ca.concordia.app.risk.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.concordia.app.risk.RiskGameBeanConfig;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

/**
 * player commands
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({RiskGameBeanConfig.class, RiskGameTestBeanConfig.class})
@ActiveProfiles("test")
public class PlayerControllerTest {

  private static Logger log = LoggerFactory.getLogger(PlayerControllerTest.class);

  @Autowired GameController gameController;

  @Autowired MapController mapController;

  PlayerDaoImpl playerDaoImpl;

  @BeforeEach
  void init() {
    log.info("Inside @BeforeEach");
    assertNotNull(gameController);
    RunningGame.reset();
    playerDaoImpl = new PlayerDaoImpl();
  }

  /**
   * add player
   * should return number of players even in case of edit a player's name
   * @see ca.concordia.app.risk.controller.GameController#gameplayer(String, String)
   */
  @Test
  public void testAddPlayer() {
    log.info("Inside testAddContinent");
    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Wassim", "");
    gameController.gameplayer("Pinkal", "Michael");
    gameController.gameplayer("Nassim", "");
    assertEquals(3, RunningGame.getInstance().getPlayers().getList().size());
  }

  /**
   * duplicate player entries
   * should return non-duplicated count of players if a player added 2 times
   * @see ca.concordia.app.risk.controller.GameController#gameplayer(String, String)
   */
  @Test
  public void testDuplicateName() {
	log.info("Inside testDuplicateName");
    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Michael", "");
    assertEquals(1, RunningGame.getInstance().getPlayers().getList().size());
  }

  /**
   * color of the player
   * should return color of the related player
   * @see ca.concordia.app.risk.controller.GameController#gameplayer(String, String)
   */
  @Test
  public void testPlayerColor() {
	log.info("Inside testPlayerColor");
    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Wassim", "");
    gameController.gameplayer("Pinkal", "");
    assertEquals("Blue", RunningGame.getInstance().getPlayers().getList().get(1).getColor());
  }

  /**
   * populate countries among player
   * should return number of countries per player
   * @see ca.concordia.app.risk.controller.GameController#populatecountries()
   */
  @Test
  public void testPopulateCountries() {
	log.info("Inside testPopulateCountries");
    mapController.editcontinent("Africa", "1", "None");
    mapController.editcontinent("Asia", "3", "None");
    mapController.editcontinent("North America", "1", "None");

    mapController.editcountry("Egypt", "Africa", "");
    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");
    mapController.editcountry("Canada", "North America", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Wassim", "");
    gameController.populatecountries();

    assertEquals(
        3,
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> c.getPlayerId() == 1)
            .collect(Collectors.toList())
            .size());
    assertEquals(
        2,
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> c.getPlayerId() == 2)
            .collect(Collectors.toList())
            .size());
  }

  /**
   * place army for non-active player
   * should return 0 as a number of armies placed for a non-active player
   * @see ca.concordia.app.risk.controller.GameController#placearmy(String)
   */
  @Test
  public void testPlaceArmyNotActivePlayerTurn() {
	log.info("Inside testPlaceArmyNotActivePlayerTurn");
    mapController.editcontinent("Asia", "3", "None");

    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Nassim", "");
    gameController.gameplayer("Wassim", "");

    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);
    gameController.placearmy("India");

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> "India".equalsIgnoreCase(c.getName()))
            .collect(Collectors.toList());
    int actual = countryModel.get(0).getNumberOfArmies();
    assertEquals(0, actual);
  }

  /**
   * place army for active player
   * should return 1 as a number of armies placed for an active player
   * @see ca.concordia.app.risk.controller.GameController#placearmy(String)
   */
  @Test
  public void testPlaceArmyActivePlayerTurn() {
	log.info("Inside testPlaceArmyActivePlayerTurn");
    mapController.editcontinent("Asia", "3", "None");

    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Nassim", "");
    gameController.gameplayer("Wassim", "");

    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);
    gameController.placearmy("Jordan");

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> "Jordan".equalsIgnoreCase(c.getName()))
            .collect(Collectors.toList());
    int actual = countryModel.get(0).getNumberOfArmies();
    assertEquals(1, actual);
  }

  /**
   * place army for non-exists country
   * should return 0 as a number of armies placed for a player
   * @see ca.concordia.app.risk.controller.GameController#placearmy(String)
   */
  @Test
  public void testPlaceArmyCountryDoesNotExist() {
	log.info("Inside testPlaceArmyCountryDoesNotExist");
    mapController.editcontinent("Asia", "3", "None");

    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Nassim", "");
    gameController.gameplayer("Wassim", "");

    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);
    gameController.placearmy("Egypt");

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> "Egypt".equalsIgnoreCase(c.getName()))
            .collect(Collectors.toList());
    assertEquals(0, countryModel.size());
  }

  /**
   * number of armies after placing army in countries
   * should return corresponding number
   * @see ca.concordia.app.risk.controller.GameController#placearmy(String)
   */
  @Test
  public void testPlaceArmyNumberOfArmies() {
	log.info("Inside testPlaceArmyNumberOfArmies");
    mapController.editcontinent("Asia", "3", "None");
    mapController.editcontinent("Africa", "1", "None");

    mapController.editcountry("Egypt", "Africa", "");
    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Pinkal", "");

    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);
    gameController.placearmy("Egypt");
    gameController.placearmy("Jordan");
    gameController.placearmy("Egypt");

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> "Egypt".equalsIgnoreCase(c.getName()))
            .collect(Collectors.toList());
    assertEquals(2, countryModel.get(0).getNumberOfArmies());
  }

  /**
   * number of armies do not exceed number maximum
   * should return maximum of 40 armies per country
   * @see ca.concordia.app.risk.controller.GameController#placearmy(String)
   */
  @Test
  public void testPlaceArmyExceedNumberOfArmies() {
	log.info("Inside testPlaceArmyExceedNumberOfArmies");
    mapController.editcontinent("Asia", "3", "None");
    mapController.editcontinent("Africa", "1", "None");

    mapController.editcountry("Egypt", "Africa", "");
    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Pinkal", "");

    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);

    for (int i = 0; i < 50; i++) {
      gameController.placearmy("Egypt");
      gameController.placearmy("Jordan");
    }

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> "Egypt".equalsIgnoreCase(c.getName()))
            .collect(Collectors.toList());
    assertEquals(40, countryModel.get(0).getNumberOfArmies());
  }

  /**
   * placing all armies
   * should return maximum of 40 armies per country
   * @see ca.concordia.app.risk.controller.GameController#placeall()
   */
  @Test
  public void testPlaceAll() {
	log.info("Inside testPlaceAll");
    mapController.editcontinent("Asia", "3", "None");
    mapController.editcontinent("Africa", "1", "None");

    mapController.editcountry("Egypt", "Africa", "");
    mapController.editcountry("Jordan", "Asia", "");
    mapController.editcountry("India", "Asia", "");
    mapController.editcountry("Iran", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Pinkal", "");


    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);

    for (int i = 0; i < 30; i++) {
      gameController.placearmy("Egypt");
      gameController.placearmy("Jordan");
    }
    gameController.placeall();

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> c.getPlayerId() == 1)
            .collect(Collectors.toList());
    assertEquals(
        40, countryModel.get(0).getNumberOfArmies() + countryModel.get(1).getNumberOfArmies());
  }

  /**
   * armies reinforcement
   * should return corresponding number of armies
   * @see ca.concordia.app.risk.controller.GameController#reinforce(String, int)
   */
  @Test
  public void testReinforceActivePlayerTurn() {
	log.info("Inside testReinforceActivePlayerTurn");
    mapController.editcontinent("Asia", "2", "None");
    mapController.editcontinent("Africa", "1", "None");

    mapController.editcountry("Egypt", "Africa", "");
    mapController.editcountry("Jordan", "Asia", "");

    RunningGame.getInstance().setMapLoaded(true);

    gameController.gameplayer("Michael", "");
    gameController.gameplayer("Pinkal", "");

    gameController.populatecountries();
    //RunningGame.getInstance().setCurrentPlayerId(1);

    gameController.placeall();

    gameController.reinforce("Egypt", 3);

    List<CountryModel> countryModel =
        RunningGame.getInstance().getCountries().getList().stream()
            .filter(c -> "Egypt".equalsIgnoreCase(c.getName()))
            .collect(Collectors.toList());
    assertEquals(43, countryModel.get(0).getNumberOfArmies());
  }
}
