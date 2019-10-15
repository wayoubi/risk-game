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

	  //editNeighbor - Add
	  //Check borders of only one country
	  @Test
	  public void testAddNeighborCountry() {
	    log.info("Inside testAddNeighborCountry");
	    
	    mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iran", "Jordan", "None", "None");
	    assertEquals(3, RunningGame.getInstance().getBorders().getList().size());
	  }
	  
	  //editNeighbor - Add
	  //Borders between different Countries in the same Continent
	  @Test
	  public void testAddNeighborCountrySameContinent() {
	    log.info("Inside testAddNeighborCountrySameContinent");
	    
	    mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iraq", "Jordan", "None", "None");
	    assertEquals(3, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(4, RunningGame.getInstance().getGraph().edgeSet().size());
	    
	  }
	  
	  //editNeighbor - Add
	  //Borders between different Countries in the different Continent
	  @Test
	  public void testAddNeighborCountryDiffContinent() {
	    log.info("Inside testAddNeighborCountryDiffContinent");
	    
	    mapController.editneighbor("Iran", "France", "None", "None");
	    mapController.editneighbor("France", "Moracco", "None", "None");
	    assertEquals(3, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(4, RunningGame.getInstance().getGraph().edgeSet().size());
	  }
	  
	  
	  //editNeighbor - Add/Remove
	  @Test
	  public void testAddRemoveNeighborCountry() {
		log.info("Inside testAddRemoveNeighborCountry");
		
		//Add
	    mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iraq", "Jordan", "None", "None");
		
		//Add & Remove
	    mapController.editneighbor("Jordan", "Egypt", "None", "None");
	    mapController.editneighbor("None", "None", "Iran", "Iraq");
	    
	    assertEquals(4, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(4, RunningGame.getInstance().getGraph().edgeSet().size());
	  }

	  //editNeighbor - Remove
	  @Test
	  public void testRemoveNeighborCountryDoesNotExist() {
		log.info("Inside testRemoveNeighborCountryDoesNotExist");
		  
		//Add
	    mapController.editneighbor("Iran", "Iraq", "None", "None");
	    mapController.editneighbor("Iraq", "Jordan", "None", "None");
	    mapController.editneighbor("Jordan", "Egypt", "None", "None");
	    
	    //Remove 2
	    mapController.editneighbor("None", "None", "Iraq", "Jordan");
	    mapController.editneighbor("None", "None","Jordan", "Egypt");
		
	    System.out.println(RunningGame.getInstance().getBorders().getList().toArray());
		//Check
	    assertEquals(4, RunningGame.getInstance().getBorders().getList().size());
	    assertEquals(2, RunningGame.getInstance().getGraph().edgeSet().size());

	  }
	  
	  
}
