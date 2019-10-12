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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({RiskGameBeanConfig.class, RiskGameTestBeanConfig.class})
public class GameControllerTests {
	
	private static Logger log = LoggerFactory.getLogger(MapControllerTest.class);
	
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
	
	@AfterAll
	public static void afterAll() {
		log.info("Inside @afterAll");
		File f = new File("saved/connectedmap.txt");
		if(f.exists()) {
			f.delete();
		}
	}
	
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
	
	@Test
	public void testLoadConnectedmap() {
		log.info("Inside testLoadmap");
		gameController.loadmap("saved/testloadvalidmap.txt");
		
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), "Jordan");
		assertEquals(1, countryModel.getId());

	}
	
	@Test
	public void testLoadDisconnectedmap() {
		log.info("Inside testLoadDisconnectedmap");
		gameController.loadmap("saved/testloadinvalidmap.txt");
		assertEquals(0, RunningGame.getInstance().getContinents().getList().size());
		assertEquals(0, RunningGame.getInstance().getCountries().getList().size());
		assertEquals(0, RunningGame.getInstance().getBorders().getList().size());
	}
	
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
