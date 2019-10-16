package ca.concordia.app.risk.controller;

import ca.concordia.app.risk.RiskGameBeanConfig;
import ca.concordia.app.risk.controller.MapController;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import ca.concordia.app.risk.RiskGameBeanConfig;
import ca.concordia.app.risk.controller.MapController;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

/**
 * editcontinent command
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
public class EditContinentMapControllerTest {

  private static Logger log = LoggerFactory.getLogger(EditContinentMapControllerTest.class);

  @Autowired
  MapController mapController;

  ContinentDaoImpl continentDaoImpl;

  @BeforeEach
  void init() {
    log.info("Inside @BeforeEach");
    assertNotNull(mapController);
    RunningGame.reset();
    continentDaoImpl = new ContinentDaoImpl();
  }

  /**
   * add continents 
   * should return number of continents if continents added properly
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testAddContinent() {
    log.info("Inside testAddContinent");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "None");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
  }
  
  /**
   * duplicate continent entries 
   * should return non-duplicated count if a continent added 2 times
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testDuplicateContinent() {
	log.info("Inside testDuplicateContinent");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Asia", "6", "None");
    assertEquals(1, RunningGame.getInstance().getContinents().getList().size());
  }

  /**
   * number of countries in a continent 
   * should return not null if continent has at least 1 country
   * should return null if continent has 0 country
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testNumberOfCountriesInContinent() {
    log.info("Inside testNumberOfCountriesInContinent");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "None");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
    mapController.editcontinent("North America", "0", "None");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
    assertNull(continentDaoImpl.findByName(RunningGame.getInstance(), "North America"));
  }

  /**
   * number of countries do not exceed number of countries allowed in a continent
   * should return maximum number of countries in a continent after adding extra countries
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testExceedNumberOfCountriesInContinent() {
    log.info("Inside testExceedNumberOfCountriesInContinent");
    mapController.editcontinent("Asia", "3", "None");
    mapController.editcountry("Jordan", "Asia", "None");
    mapController.editcountry("India", "Asia", "None");
    mapController.editcountry("Iran", "Asia", "None");
    mapController.editcountry("Oman", "Asia", "None");
    ContinentModel continentModel = continentDaoImpl.findByName(RunningGame.getInstance(), "Asia");
    assertEquals(3, continentDaoImpl.getCountries(RunningGame.getInstance(), continentModel).size());
  }

  /**
   * add and remove operations on a continent
   * should return not null after add
   * should return null after remove
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testAddRemoveContinent() {
	log.info("Inside testAddRemoveContinent");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "Asia");
    assertEquals(1, RunningGame.getInstance().getContinents().getList().size());
    assertNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
  }

  /**
   * remove operation on a continent
   * should return null after remove
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testRemoveContinent() {
	log.info("Inside testRemoveContinent");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "None");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
    mapController.editcontinent("None", "0", "Asia");
    assertEquals(1, RunningGame.getInstance().getContinents().getList().size());
    assertNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
  }

  /**
   * remove continent that doesn't exist
   * should return not null for existing continents after removing a continent that doesn't exist
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testRemoveContinentDoesNotExist() {
	log.info("Inside testRemoveContinentDoesNotExist");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "None");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
    mapController.editcontinent("None", "0", "North America");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
  }

  /**
   * continent IDs after adding a continent
   * should return not null for the continent's corresponding ID
   * @see ca.concordia.app.risk.controller.MapController#editcontinent(String, String, String)
   */
  @Test
  public void testContinentIDs() {
	log.info("Inside testContinentIDs");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "None");
    mapController.editcontinent("North America", "5", "None");
    assertEquals(3, RunningGame.getInstance().getContinents().getList().size());
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "North America"));

    assertNotNull("Asia", continentDaoImpl.findById(RunningGame.getInstance(), 1).getName());
    assertNotNull("Africa", continentDaoImpl.findById(RunningGame.getInstance(), 2).getName());
    assertNotNull("North America", continentDaoImpl.findById(RunningGame.getInstance(), 3).getName());

  }

}
