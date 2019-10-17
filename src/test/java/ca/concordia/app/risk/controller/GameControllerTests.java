package ca.concordia.app.risk.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.concordia.app.risk.RiskGameBeanConfig;
import ca.concordia.app.risk.controller.GameController;
import ca.concordia.app.risk.controller.MapController;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

/**
 * map commands - Save/Load/Validate
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({RiskGameBeanConfig.class, RiskGameTestBeanConfig.class})
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
		if(f.exists()) {
			f.delete();
		}
	}

	/**
	 * save operation for a connected map
	 * file should exists after save a connected map
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
		
		gameController.savemap("connectedmap.txt");
		File file = new File("saved/connectedmap.txt");
		assertTrue(file.exists());
		
	}

	/**
	 * save operation for a disconnected map
	 * file should not exists after save a disconnected map
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
		
		gameController.savemap("disconnectedmap.txt");
		File file = new File("saved/disconnectedmap.txt");
		assertFalse(file.exists()); 
	}

	/**
	 * load operation for a connected map
	 * country model should exists after load a connected map
	 * @see ca.concordia.app.risk.controller.GameController#loadmap(String)
	 */	
	@Test
	public void testLoadConnectedmap() {
		log.info("Inside testLoadmap");
		gameController.loadmap("saved/testloadvalidmap.txt");
		
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), "Jordan");
		assertEquals(1, countryModel.getId());

	}

	/**
	 * load operation for a disconnected map
	 * no continent/country/border should exist after load a disconnected map
	 * @see ca.concordia.app.risk.controller.GameController#loadmap(String)
	 */	
	@Test
	public void testLoadDisconnectedmap() {
		log.info("Inside testLoadDisconnectedmap");
		RunningGame.getInstance().setGamePlay(true);
		gameController.loadmap("saved/testloadinvalidmap.txt");
		assertEquals(0, RunningGame.getInstance().getContinents().getList().size());
		assertEquals(0, RunningGame.getInstance().getCountries().getList().size());
		assertEquals(0, RunningGame.getInstance().getBorders().getList().size());
	}

	/**
	 * map validation operation
	 * should receive success message after validation operation
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
		
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"), gameController.validatemap("All"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"), gameController.validatemap("Asia"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"), gameController.validatemap("Africa"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"), gameController.validatemap("Europe"));
	}

	/**
	 * map validation operation per continent
	 * should receive successful/unsuccessful message after validation operation for each continent
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
		assertEquals(shellHelper.getErrorMessage("Countries are not connected, Map is invalid"), gameController.validatemap("All"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"), gameController.validatemap("Asia"));
		assertEquals(shellHelper.getSuccessMessage("Countries are connected, Map is valid"), gameController.validatemap("Africa"));
		assertEquals(shellHelper.getErrorMessage("Countries are not connected, Map is invalid"), gameController.validatemap("Europe"));
		assertFalse(shellHelper.getErrorMessage("Countries are connected, Map is valid").equals(gameController.validatemap("Europe")));
		
	}
}
