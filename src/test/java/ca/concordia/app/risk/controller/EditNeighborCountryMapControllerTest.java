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
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

/**
 * editneighbor command
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
public class EditNeighborCountryMapControllerTest {
	
	private static Logger log = LoggerFactory.getLogger(EditNeighborCountryMapControllerTest.class);
	
	  @Autowired
	  MapController mapController;

	  BorderDaoImp borderDaoImp;

	  @BeforeEach
	  void init() {
	    log.info("Inside @BeforeEach");
	    assertNotNull(mapController);
	    RunningGame.reset();
	    borderDaoImp = new BorderDaoImp();
	    
	    //add continents
	    mapController.editcontinent("Asia", "5", "None");
	    mapController.editcontinent("Africa", "6", "None");
	    mapController.editcontinent("Europe", "7", "None");
	    
	    //add countries
	    mapController.editcountry("Iran", "Asia", "None");
	    mapController.editcountry("Iraq", "Asia", "None");
	    mapController.editcountry("Jordan", "Asia", "None");
	    mapController.editcountry("Egypt", "Asia", "None");
	    
	    mapController.editcountry("France", "Europe", "None");
	    mapController.editcountry("Italy", "Europe", "None");
	    
	    mapController.editcountry("Moracco", "Africa", "None");
	    mapController.editcountry("Zimbabwe", "Africa", "None");
	    
	  }

	  /**
	   * add neighbor country(borders)
	   * should return number of countries if all the countries are connected by neighbors
	   * @see ca.concordia.app.risk.controller.MapController#editneighbor(String, String, String, String)
	   */
	  @Test
	  public void testAddNeighborCountry() {
	    log.info("Inside testAddNeighborCountry");
	    mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iran", "Jordan", "None", "None");
	    assertEquals(3, RunningGame.getInstance().getBorders().getList().size());
	  }
	  
	  /**
	   * add neighbor country(edge)-all countries are in the same continent 
	   * should return number of countries if all the countries are connected by neighbors
	   * should return number of edges in graph * 2, since graph is bidirectional
	   * @see ca.concordia.app.risk.controller.MapController#editneighbor(String, String, String, String)
	   */
	  @Test
	  public void testAddNeighborCountrySameContinent() {
	    log.info("Inside testAddNeighborCountrySameContinent");
	    mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iraq", "Jordan", "None", "None");
	    assertEquals(3, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(4, RunningGame.getInstance().getGraph().edgeSet().size());
	    
	  }
	  
	  /**
	   * add neighbor country(edge)-countries are in different continents
	   * should return number of countries if all the countries are connected by neighbors
	   * should return number of edges in graph * 2, since graph is bidirectional
	   * @see ca.concordia.app.risk.controller.MapController#editneighbor(String, String, String, String)
	   */
	  @Test
	  public void testAddNeighborCountryDiffContinent() {
	    log.info("Inside testAddNeighborCountryDiffContinent");
	    mapController.editneighbor("Iran", "France", "None", "None");
	    mapController.editneighbor("France", "Moracco", "None", "None");
	    assertEquals(3, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(4, RunningGame.getInstance().getGraph().edgeSet().size());
	  }
	  
	  /**
	   * add and remove operations on neighbor countries(borders)
	   * should return number of edges in graph * 2, since graph is bidirectional
	   * @see ca.concordia.app.risk.controller.MapController#editneighbor(String, String, String, String)
	   */
	  @Test
	  public void testAddRemoveNeighborCountry() {
		log.info("Inside testAddRemoveNeighborCountry");
		mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iraq", "Jordan", "None", "None");
		mapController.editneighbor("Jordan", "Egypt", "None", "None");
	    mapController.editneighbor("None", "None", "Iran", "Iraq");
	    assertEquals(4, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(4, RunningGame.getInstance().getGraph().edgeSet().size());
	  }

	  /**
	   * remove operation on neighbor countries(borders)
	   * should return number of edges in graph * 2, since graph is bidirectional
	   * @see ca.concordia.app.risk.controller.MapController#editneighbor(String, String, String, String)
	   */
	  @Test
	  public void testRemoveNeighborCountryDoesNotExist() {
		log.info("Inside testRemoveNeighborCountryDoesNotExist");
		mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iraq", "Jordan", "None", "None");
	    mapController.editneighbor("Jordan", "Egypt", "None", "None");
	    mapController.editneighbor("None", "None", "Iraq", "Jordan");
	    mapController.editneighbor("None", "None","Jordan", "Egypt");
	    assertEquals(4, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(2, RunningGame.getInstance().getGraph().edgeSet().size());

	  }
	  
	  
}
