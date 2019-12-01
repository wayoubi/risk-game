package ca.concordia.app.risk.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
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
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

/**
 * map commands - Save/Load/Validate
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
@ActiveProfiles("test")
public class GameControllerTests {

	private static Logger log = LoggerFactory.getLogger(EditContinentMapControllerTest.class);

	@Autowired
	MapController mapController;

	@Autowired
	GameController gameController;

	@Autowired
	ShellHelper shellHelper;

	@BeforeEach
	void init() {
		log.info("Inside @BeforeEach");
		assertNotNull(mapController);
		RunningGame.reset();
	}

	/**
	 * Clean resources after all tests
	 */
	@AfterAll
	public static void afterAll() {
		log.info("Inside @afterAll");
		File f = new File("saved/connectedmap.txt");
		if (f.exists()) {
			f.delete();
		}
	}

	/**
	 * save operation for a connected map file should exists after save a connected
	 * map
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#savemap(String)
	 */
	@Test
	public void testSaveConnectedmap() {
		log.info("Inside testSaveConnectedmap");
		mapController.editcontinent("Asia", "5", "None");
		mapController.editcontinent("Africa", "5", "None");
		mapController.editcontinent("Europe", "5", "None");
		mapController.editcountry("Jordan", "Asia", "None");
		mapController.editcountry("Iran", "Asia", "None");
		mapController.editcountry("India", "Asia", "None");
		mapController.editcountry("Lebanon", "Asia", "None");
		mapController.editcountry("Egypt", "Africa", "None");
		mapController.editcountry("Morocco", "Africa", "None");
		mapController.editcountry("France", "Europe", "None");
		mapController.editcountry("Italy", "Europe", "None");
		mapController.editneighbor("Jordan", "Iran", "None", "None");
		mapController.editneighbor("Iran", "India", "None", "None");
		mapController.editneighbor("Jordan", "Lebanon", "None", "None");
		mapController.editneighbor("Jordan", "Egypt", "None", "None");
		mapController.editneighbor("Egypt", "Morocco", "None", "None");
		mapController.editneighbor("Morocco", "France", "None", "None");
		mapController.editneighbor("France", "Italy", "None", "None");

		mapController.savemap("connectedmap.txt", "");
		File file = new File("saved/connectedmap.txt");
		assertTrue(file.exists());

	}

	/**
	 * save operation for a disconnected map file should not exists after save a
	 * disconnected map
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#savemap(String)
	 */
	@Test
	public void testSaveDisconnectedmap() {
		log.info("Inside testSaveDisconnectedmap");
		mapController.editcontinent("Asia", "5", "None");
		mapController.editcontinent("Africa", "5", "None");
		mapController.editcontinent("Europe", "5", "None");
		mapController.editcountry("Jordan", "Asia", "None");
		mapController.editcountry("Iran", "Asia", "None");
		mapController.editcountry("India", "Asia", "None");
		mapController.editcountry("Lebanon", "Asia", "None");
		mapController.editcountry("Egypt", "Africa", "None");
		mapController.editcountry("Morocco", "Africa", "None");
		mapController.editcountry("France", "Europe", "None");
		mapController.editcountry("Italy", "Europe", "None");
		mapController.editneighbor("Jordan", "Iran", "None", "None");
		mapController.editneighbor("Iran", "India", "None", "None");
		mapController.editneighbor("Jordan", "Lebanon", "None", "None");
		mapController.editneighbor("Jordan", "Egypt", "None", "None");
		mapController.editneighbor("Egypt", "Morocco", "None", "None");
		mapController.editneighbor("France", "Italy", "None", "None");

		mapController.savemap("disconnectedmap.txt", "");
		File file = new File("saved/disconnectedmap.txt");
		assertFalse(file.exists());
	}

	/**
	 * load operation for a connected map country model should exists after load a
	 * connected map
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#loadmap(String)
	 */
	@Test
	public void testLoadConnectedmap() {
		System.out.println(">>> Inside testLoadConnectedmap");
		log.info("Inside testLoadmap");
		mapController.loadmap("saved/testloadvalidmap.txt", "DOMINATION");

		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), "Jordan");
		assertEquals(1, countryModel.getId());

	}

	/**
	 * load operation for a disconnected map no continent/country/border should
	 * exist after load a disconnected map
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#loadmap(String)
	 */
	@Test
	public void testLoadDisconnectedmap() {
		log.info("Inside testLoadDisconnectedmap");
		RunningGame.getInstance().setGamePlay(true);
		mapController.loadmap("saved/testloadinvalidmap.txt", "DOMINATION");
		assertEquals(0, RunningGame.getInstance().getContinents().getList().size());
		assertEquals(0, RunningGame.getInstance().getCountries().getList().size());
		assertEquals(0, RunningGame.getInstance().getBorders().getList().size());
	}

	/**
	 * map validation operation should receive success message after validation
	 * operation
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#validatemap(String)
	 */
	@Test
	public void testValidateMap() {
		log.info("Inside testValidateMap");
		mapController.editcontinent("Asia", "5", "None");
		mapController.editcontinent("Africa", "5", "None");
		mapController.editcontinent("Europe", "5", "None");
		mapController.editcountry("Jordan", "Asia", "None");
		mapController.editcountry("Iran", "Asia", "None");
		mapController.editcountry("India", "Asia", "None");
		mapController.editcountry("Lebanon", "Asia", "None");
		mapController.editcountry("Egypt", "Africa", "None");
		mapController.editcountry("Morocco", "Africa", "None");
		mapController.editcountry("France", "Europe", "None");
		mapController.editcountry("Italy", "Europe", "None");
		mapController.editneighbor("Jordan", "Iran", "None", "None");
		mapController.editneighbor("Iran", "India", "None", "None");
		mapController.editneighbor("Jordan", "Lebanon", "None", "None");
		mapController.editneighbor("Jordan", "Egypt", "None", "None");
		mapController.editneighbor("Egypt", "Morocco", "None", "None");
		mapController.editneighbor("Morocco", "France", "None", "None");
		mapController.editneighbor("France", "Italy", "None", "None");

		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"),
				mapController.validatemap("All"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"),
				mapController.validatemap("Asia"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"),
				mapController.validatemap("Africa"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"),
				mapController.validatemap("Europe"));
	}

	/**
	 * map validation operation per continent should receive successful/unsuccessful
	 * message after validation operation for each continent
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#validatemap(String)
	 */
	@Test
	public void testValidateMapPerContinent() {
		log.info("Inside testPerContinent");
		mapController.editcontinent("Asia", "5", "None");
		mapController.editcontinent("Africa", "5", "None");
		mapController.editcontinent("Europe", "5", "None");
		mapController.editcountry("Jordan", "Asia", "None");
		mapController.editcountry("Iran", "Asia", "None");
		mapController.editcountry("India", "Asia", "None");
		mapController.editcountry("Lebanon", "Asia", "None");
		mapController.editcountry("Egypt", "Africa", "None");
		mapController.editcountry("Morocco", "Africa", "None");
		mapController.editcountry("France", "Europe", "None");
		mapController.editcountry("Italy", "Europe", "None");
		mapController.editneighbor("Jordan", "Iran", "None", "None");
		mapController.editneighbor("Iran", "India", "None", "None");
		mapController.editneighbor("Jordan", "Lebanon", "None", "None");
		mapController.editneighbor("Jordan", "Egypt", "None", "None");
		mapController.editneighbor("Egypt", "Morocco", "None", "None");
		mapController.editneighbor("Morocco", "France", "None", "None");
		mapController.editneighbor("Morocco", "Italy", "None", "None");
		assertEquals(shellHelper.getErrorMessage("Countries are not connected, Map is invalid"),
				mapController.validatemap("All"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"),
				mapController.validatemap("Asia"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"),
				mapController.validatemap("Africa"));
		assertEquals(shellHelper.getErrorMessage("Countries are not connected, Map is invalid"),
				mapController.validatemap("Europe"));
		assertFalse(shellHelper.getErrorMessage("Countries are connected, Map is valid")
				.equals(mapController.validatemap("Europe")));

	}

	/**
	 * Test for exchange card operation.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#exchangecards(String,
	 *      String, String)
	 */
	@Test
	public void testExchangeCard() {
		log.info("Inside testExchangeCard");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt", "Africa", "");
		mapController.editcountry("Jordan", "Asia", "");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Michael", "Human", "");
		gameController.gameplayer("Pinkal", "Random", "");

		gameController.populatecountries();

		gameController.placeall();
		RunningGame.getInstance().setGamePlay(true);
		// RunningGame.getInstance().setReinforceCompleted(true);

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");

		List<String> cards = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();

		gameController.exchangecards("1", "2", "3");

		int numOfArmies = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies();
		assertEquals(11, numOfArmies);
	}

	/**
	 * Test for exchange card operation if want to do exchange for second time.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#exchangecards(String,
	 *      String, String)
	 */
	@Test
	public void testExchangeCardSecondTime() {
		log.info("Inside testExchangeCard");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt", "Africa", "");
		mapController.editcountry("Jordan", "Asia", "");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Michael", "Human", "");
		gameController.gameplayer("Pinkal", "Human", "");

		gameController.populatecountries();

		gameController.placeall();

		RunningGame.getInstance().setGamePlay(true);
		// RunningGame.getInstance().setReinforceCompleted(true);

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");

		gameController.exchangecards("1", "2", "3");

		// RunningGame.getInstance().setReinforceCompleted(true);
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");

		gameController.exchangecards("1", "2", "3");

		int numOfArmies = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies();
		assertEquals(24, numOfArmies);
	}

	/**
	 * Test for exchange card operation if player has different card types.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#exchangecards(String,
	 *      String, String)
	 */
	@Test
	public void testExchangeCardDifferentType() {
		log.info("Inside testExchangeCard");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt", "Africa", "");
		mapController.editcountry("Jordan", "Asia", "");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Michael", "Human", "");
		gameController.gameplayer("Pinkal", "Human", "");

		gameController.populatecountries();

		gameController.placeall();
		RunningGame.getInstance().setGamePlay(true);
		RunningGame.getInstance().setReinforceCompleted(true);

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Cavalry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Artillery");

		List<String> cards = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();

		gameController.exchangecards("1", "2", "3");

		int numOfArmies = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies();
		assertEquals(11, numOfArmies);
	}

	/**
	 * Test for exchange card operation if player has 2 same cards and 1 different
	 * type.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#exchangecards(String,
	 *      String, String)
	 */
	@Test
	public void testExchangeCardTwoSameTypeOneDifferent() {
		log.info("Inside testExchangeCard");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt", "Africa", "");
		mapController.editcountry("Jordan", "Asia", "");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Michael", "Human", "");
		gameController.gameplayer("Pinkal", "Human", "");

		gameController.populatecountries();

		gameController.placeall();
		RunningGame.getInstance().setGamePlay(true);

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Infantry");
		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList().add("Artillery");

		List<String> cards = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();

		gameController.exchangecards("1", "2", "3");

		int numOfArmies = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies();
		assertEquals(3, numOfArmies);
	}

	/**
	 * Test for attack operation when attack is not complete and it needs defenders
	 * action.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#attack(String, String,
	 *      String)
	 */
	@Test
	public void testAttackWithoutDefender() {
		log.info("Inside testAttackWithoutDefender");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Jordan", "Asia", "");
		mapController.editcountry("Monaco", "Africa", "");

		mapController.editneighbor("Jordan", "Monaco", "None", "None");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Nasim", "Human", "");
		gameController.gameplayer("Wasim", "Human", "");

		gameController.populatecountries();
		gameController.placeall();

		PlayerModel player = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), player);
		String countryDoAttackFrom = "";
		for (CountryModel playerCountry : playerCountries) {
			countryDoAttackFrom = playerCountry.getName();
			gameController.reinforce(countryDoAttackFrom, 3);
		}

		String countryToDefend = "";
		if (countryDoAttackFrom.equals("Jordan")) {
			countryToDefend = "Monaco";
		} else {
			countryToDefend = "Jordan";
		}

		RunningGame.getInstance().setGamePlay(true);
		RunningGame.getInstance().setCardExchangeCompleted(true);
		player.setPlayingPhase("Attack");

		String result = gameController.attack(countryDoAttackFrom, countryToDefend, "2");
		assertEquals("Single Attack with specified number of dice initiated, waiting for defender dice", result);
	}

	/**
	 * Test for attack operation.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#attack(String, String,
	 *      String)
	 * @see ca.concordia.app.risk.controller.GameController#defend(String)
	 */
	@Test
	public void testAttack() {
		log.info("Inside testAttack");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Jordan", "Asia", "");
		mapController.editcountry("Monaco", "Africa", "");

		mapController.editneighbor("Jordan", "Monaco", "None", "None");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Nasim", "Human", "");
		gameController.gameplayer("Wasim", "Human", "");

		gameController.populatecountries();
		gameController.placeall();

		PlayerModel player = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), player);
		String countryDoAttackFrom = "";
		for (CountryModel playerCountry : playerCountries) {
			countryDoAttackFrom = playerCountry.getName();
			gameController.reinforce(countryDoAttackFrom, 3);
		}

		String countryToDefend = "";
		if (countryDoAttackFrom.equals("Jordan")) {
			countryToDefend = "Monaco";
		} else {
			countryToDefend = "Jordan";
		}

		RunningGame.getInstance().setGamePlay(true);
		RunningGame.getInstance().setCardExchangeCompleted(true);
		player.setPlayingPhase("Attack");
		// System.out.println("countryDoAttackFrom: "+countryDoAttackFrom);
		// System.out.println("countryToDefend: "+countryToDefend);
		gameController.attack(countryDoAttackFrom, countryToDefend, "2");
		String result = gameController.defend("1");

		assertEquals("Single Attack with specified dice completed", result);
	}

	/**
	 * Test for attack operation when attacker uses all his armies in 1 attack.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#attack(String, String,
	 *      String)
	 */
	@Test
	public void testAttackAllOut() {
		log.info("Inside testAttackAllOut");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Jordan", "Asia", "");
		mapController.editcountry("Monaco", "Africa", "");

		mapController.editneighbor("Jordan", "Monaco", "None", "None");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Nasim", "Human", "");
		gameController.gameplayer("Wasim", "Human", "");

		gameController.populatecountries();
		gameController.placeall();

		PlayerModel player = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), player);
		String countryDoAttackFrom = "";
		for (CountryModel playerCountry : playerCountries) {
			countryDoAttackFrom = playerCountry.getName();
			gameController.reinforce(countryDoAttackFrom, 3);
		}

		String countryToDefend = "";
		if (countryDoAttackFrom.equals("Jordan")) {
			countryToDefend = "Monaco";
		} else {
			countryToDefend = "Jordan";
		}

		RunningGame.getInstance().setGamePlay(true);
		RunningGame.getInstance().setCardExchangeCompleted(true);
		player.setPlayingPhase("Attack");

		String result = gameController.attack(countryDoAttackFrom, countryToDefend, "-allout");
		assertEquals("Attack in All-Out Mode Completed", result);
	}

	/**
	 * Test for attack operation when two countries don't have any border between
	 * them.
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#attack(String, String,
	 *      String)
	 */
	@Test
	public void testInvalidBorderAttack() {
		log.info("Inside testInvalidBorderAttack");
		mapController.editcontinent("Asia", "3", "None");
		mapController.editcontinent("Africa", "3", "None");

		mapController.editcountry("Jordan", "Asia", "");
		mapController.editcountry("Iran", "Asia", "");
		mapController.editcountry("Turkey", "Asia", "");
		mapController.editcountry("Monaco", "Africa", "");
		mapController.editcountry("Gine", "Africa", "");
		mapController.editcountry("Morocco", "Africa", "");

		mapController.editneighbor("Jordan", "Iran", "None", "None");
		mapController.editneighbor("Iran", "Turkey", "None", "None");
		mapController.editneighbor("Turkey", "Monaco", "None", "None");
		mapController.editneighbor("Monaco", "Gine", "None", "None");
		mapController.editneighbor("Gine", "Morocco", "None", "None");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Nasim", "Human", "");
		gameController.gameplayer("Wasim", "Benevolent", "");

		gameController.populatecountries();
		gameController.placeall();

		PlayerModel player = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), player);
		String countryDoAttackFrom = "";

		// get Attackers countries
		for (CountryModel playerCountry : playerCountries) {
			countryDoAttackFrom = playerCountry.getName();
			gameController.reinforce(countryDoAttackFrom, 3);
		}

		List<CountryModel> allCountries = RunningGame.getInstance().getCountries().getList();
		String countryDoAttackTo = "";

		// get Defenders countries
		for (CountryModel country : allCountries) {
			BorderDaoImp borderDaoImpl = new BorderDaoImp();
			BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), countryDoAttackFrom);
			CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
			CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryDoAttackFrom);
			// 1- Countries don't have any borders (No neighborhood)
			// 2- Country is not itself
			// 3- Current player is not owned the country
			if ((!borderModel.getNeighbours().contains(country.getId())) && (country.getId() != countryModel.getId())
					&& (player.getId() != country.getPlayerId())) {
				countryDoAttackTo = country.getName();
			}
		}
		// System.out.println("countryDoAttackFrom"+countryDoAttackFrom);
		// System.out.println("countryDoAttackTo"+countryDoAttackTo);
		RunningGame.getInstance().setGamePlay(true);
		RunningGame.getInstance().setCardExchangeCompleted(true);
		player.setPlayingPhase("Attack");

		String result = gameController.attack(countryDoAttackFrom, countryDoAttackTo, "3");
		result = result.replace("[31m", "").replace("[0m", "");

		assertEquals("The from and to country do not share borders, please select some other country to attack",
				result);
	}

	/**
	 * Test for moving armies before attack operation is done(AttackMove).
	 * 
	 * @see ca.concordia.app.risk.controller.GameController#attackmove(String,
	 *      String, String)
	 */
	@Test
	public void testMoveArmiesBeforeAttackIsDone() {
		log.info("Inside testMoveArmiesBeforeAttackIsDone");
		mapController.editcontinent("Asia", "1", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Jordan", "Asia", "");
		mapController.editcountry("Monaco", "Africa", "");

		mapController.editneighbor("Jordan", "Monaco", "None", "None");

		RunningGame.getInstance().setMapLoaded(true);

		gameController.gameplayer("Nasim", "Human", "");
		gameController.gameplayer("Wasim", "Random", "");

		gameController.populatecountries();
		gameController.placeall();

		PlayerModel player = RunningGame.getInstance().getCurrentPlayer().getPlayerModel();
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), player);
		String countryDoAttackFrom = "";
		for (CountryModel playerCountry : playerCountries) {
			countryDoAttackFrom = playerCountry.getName();
			gameController.reinforce(countryDoAttackFrom, 3);
		}

		String countryToDefend = "";
		if (countryDoAttackFrom.equals("Jordan")) {
			countryToDefend = "Monaco";
		} else {
			countryToDefend = "Jordan";
		}

		RunningGame.getInstance().setGamePlay(true);
		RunningGame.getInstance().setCardExchangeCompleted(true);

		gameController.attack(countryDoAttackFrom, countryToDefend, "3");

		String result = gameController.attackmove("25");
		result = result.replace("[31m", "").replace("[0m", "");

		assertEquals("You've not conquered any country to move the armies", result);
	}
}