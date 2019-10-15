package ca.concordia.app.risk.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
public class EditCountryMapControllerTest {
	
	private static Logger log = LoggerFactory.getLogger(EditCountryMapControllerTest.class);
	
	  @Autowired
	  MapController mapController;

	  CountryDaoImpl countryDaoImpl;
	  
	  @BeforeEach
	  void init() {
	    log.info("Inside @BeforeEach");
	    assertNotNull(mapController);
	    RunningGame.reset();
	    countryDaoImpl = new CountryDaoImpl();

	    //add continent
	    mapController.editcontinent("Asia", "5", "None");
	    mapController.editcontinent("Africa", "6", "None");
	    mapController.editcontinent("Europe", "7", "None");
	  }

	  //editCountry - Add Only
	  @Test
	  public void testAddCountry() {
	    log.info("Inside testAddCountry");

	    mapController.editcountry("Iran", "Asia", "None");
	    mapController.editcountry("Pakistan", "Asia", "None");
	    
	    //test
	    assertEquals(2, RunningGame.getInstance().getCountries().getList().size());
	  }
	  
	  //editCountry - Duplicate Add
	  @Test
	  public void testDuplicateCountryInSameContinent() {
		log.info("Inside testDuplicateCountryInSameContinent");
		  
	    mapController.editcountry("Iran", "Asia", "None");
	    mapController.editcountry("Iran", "Asia", "None");
	    
	    //test
	    assertEquals(1, RunningGame.getInstance().getCountries().getList().size());
	  }
	  
	  //editCountry - Add
	  @Test
	  public void testDuplicateCountryInDiffCountries() {
		log.info("Inside testDuplicateCountryInDiffContinents");
		  
	    mapController.editcountry("Iran", "Asia", "None");
	    mapController.editcountry("Iran", "Africa", "None");
	    
	    //test
	    assertEquals(1, RunningGame.getInstance().getCountries().getList().size());
	  }
	  
	  //editCountry - Remove
	  @Test
	  public void testRemoveCountry() {
		log.info("Inside testRemoveContinent");

	    mapController.editcountry("Turkey", "Asia", "None");
	    mapController.editcountry("France", "Europe", "None");

	    mapController.editcountry("none", "Asia", "Turkey");
	    assertEquals(1, RunningGame.getInstance().getCountries().getList().size());
	    assertNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Turkey"));
	    assertNotNull(countryDaoImpl.findByName(RunningGame.getInstance(), "France"));
	  }
	  
	  //editCountry - Add/Remove
	  @Test
	  public void testAddRemoveCountry() {
		log.info("Inside testAddRemoveCountry");
		
		//Add
		mapController.editcountry("Morocco", "Africa", "None");
		
		//Add & Remove
	    mapController.editcountry("Iran", "Asia", "None");
	    mapController.editcountry("none", "Asia", "Iran");
	    
	    assertEquals(1, RunningGame.getInstance().getCountries().getList().size());
	    assertNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Iran"));
	    assertNotNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Morocco"));
	  }

	  //editCountry - Remove
	  @Test
	  public void testRemoveCountryDoesNotExist() {
		log.info("Inside testRemoveCountryDoesNotExist");

		//Add
		mapController.editcountry("Morocco", "Africa", "None");
		mapController.editcountry("Iran", "Asia", "None");

		//Check
	    assertEquals(2, RunningGame.getInstance().getCountries().getList().size());
	    assertNotNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Morocco"));
	    assertNotNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Iran"));

	    mapController.editcountry("none", "Europe", "France");
	    assertEquals(2, RunningGame.getInstance().getCountries().getList().size());
	    assertNotNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Morocco"));
	    assertNotNull(countryDaoImpl.findByName(RunningGame.getInstance(), "Iran"));
	  }
}
