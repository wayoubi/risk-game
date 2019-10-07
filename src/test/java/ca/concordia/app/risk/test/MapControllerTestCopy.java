package ca.concordia.app.risk.test;

import static org.junit.jupiter.api.Assertions.*;

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
import ca.concordia.app.risk.controller.MapController;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
public class MapControllerTestCopy {

  private static Logger log = LoggerFactory.getLogger(MapControllerTest.class);

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

  @Test
  public void testAddContinent() {
    log.info("Inside testAddContinent");
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "None");
    assertEquals(2, RunningGame.getInstance().getContinents().getList().size());
  }

  @Test
  public void testDuplicateContinent() {
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Asia", "6", "None");
    assertEquals(1, RunningGame.getInstance().getContinents().getList().size());
  }

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

  @Test
  public void testAddRemoveContinent() {
    mapController.editcontinent("Asia", "5", "None");
    mapController.editcontinent("Africa", "6", "Asia");
    assertEquals(1, RunningGame.getInstance().getContinents().getList().size());
    assertNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Asia"));
    assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "Africa"));
  }

  @Test
  public void testRemoveContinent() {
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

  @Test
  public void testRemoveContinentDoesNotExist() {
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

  // @Test
  // public void testContinentIDs() {
  // mapController.editcontinent("Asia", "5", "None");
  // mapController.editcontinent("Africa", "6", "None");
  // mapController.editcontinent("North America", "5", "None");
  // assertEquals(3, RunningGame.getInstance().getContinents().getList().size());
  // assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(),
  // "Asia"));
  // assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(),
  // "Africa"));
  // assertNotNull(continentDaoImpl.findByName(RunningGame.getInstance(), "North
  // America"));

  // assertNotNull("Asia", continentDaoImpl.findById(RunningGame.getInstance(),
  // 1).getName());
  // assertNotNull("Africa", continentDaoImpl.findById(RunningGame.getInstance(),
  // 2).getName());
  // assertNotNull("North America",
  // continentDaoImpl.findById(RunningGame.getInstance(), 3).getName());

  // }

}
