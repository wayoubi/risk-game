package ca.concordia.app.risk.test;

import ca.concordia.app.risk.RiskGameBeanConfig;
import ca.concordia.app.risk.controller.GameController;
import ca.concordia.app.risk.controller.MapController;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.services.GameService;
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

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({RiskGameBeanConfig.class, RiskGameTestBeanConfig.class})

public class PlayerControllerTest {
	
	 
	private static Logger log = LoggerFactory.getLogger(PlayerControllerTest.class);
	

	@Autowired
	GameController gameController;

	@Autowired
	MapController mapController;

	PlayerDaoImpl playerDaoImpl;


	@BeforeEach
	void init() {
		log.info("Inside @BeforeEach");
		assertNotNull(gameController);
		RunningGame.reset();
		playerDaoImpl = new PlayerDaoImpl();
	}
	
	@Test
	public void testAddPlayer() {
		log.info("Inside testAddContinent");
		gameController.gameplayer("Michael", "");
		gameController.gameplayer("Wassim", "");
		gameController.gameplayer("Pinkal", "Michael");
		gameController.gameplayer("Nassim", "");
		assertEquals(3, RunningGame.getInstance().getPlayers().getList().size());
	}


	@Test
	public void testDuplicateName() {
		gameController.gameplayer("Michael", "");
		gameController.gameplayer("Michael", "");
		assertEquals(1, RunningGame.getInstance().getPlayers().getList().size());
	}


	@Test
	public void testPlayerColor(){
		gameController.gameplayer("Wassim","");
		gameController.gameplayer("Pinkal","");
		assertEquals("Blue",RunningGame.getInstance().getPlayers().getList().get(1).getColor());
	}

	@Test
	public void testPopulateCountries(){
		mapController.editcontinent("Africa", "1", "None");
		mapController.editcontinent("Asia", "3", "None");
		mapController.editcontinent("North America", "1", "None");

		mapController.editcountry("Egypt","Africa","");
		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");
		mapController.editcountry("Canada","Africa","");

		gameController.gameplayer("Michael","");
		gameController.gameplayer("Wassim","");
		gameController.populatecountries();

		assertEquals(3,RunningGame.getInstance().getCountries().getList().stream().filter(c->c.getPlayerId()==1).collect(Collectors.toList()).size());
		assertEquals(2,RunningGame.getInstance().getCountries().getList().stream().filter(c->c.getPlayerId()==2).collect(Collectors.toList()).size());

	}

	@Test
	public void testplaceArmyNotActivePlayerTurn(){

		mapController.editcontinent("Asia", "3", "None");

		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");

		gameController.gameplayer("Nassim","");
		gameController.gameplayer("Wassim","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);
		gameController.placearmy("India");

		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->"India".equalsIgnoreCase(c.getName())).collect(Collectors.toList());
		int actual=countryModel.get(0).getNumberOfArmies();
		assertEquals(0,actual);
	}

	@Test
	public void testplaceArmyActivePlayerTurn(){

		mapController.editcontinent("Asia", "3", "None");

		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");

		gameController.gameplayer("Nassim","");
		gameController.gameplayer("Wassim","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);
		gameController.placearmy("Jordan");

		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->"Jordan".equalsIgnoreCase(c.getName())).collect(Collectors.toList());
		int actual=countryModel.get(0).getNumberOfArmies();
		assertEquals(1,actual);
	}

	@Test
	public void testplaceArmyCountryDoesNotExist(){

		mapController.editcontinent("Asia", "3", "None");

		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");

		gameController.gameplayer("Nassim","");
		gameController.gameplayer("Wassim","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);
		gameController.placearmy("Egypt");

		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->"Egypt".equalsIgnoreCase(c.getName())).collect(Collectors.toList());
		assertEquals(0,countryModel.size());
	}

	@Test
	public void testplaceArmyNumberOfArmies(){

		mapController.editcontinent("Asia", "3", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt","Africa","");
		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");

		gameController.gameplayer("Michael","");
		gameController.gameplayer("Pinkal","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);
		gameController.placearmy("Egypt");
		gameController.placearmy("Jordan");
		gameController.placearmy("Egypt");


		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->"Egypt".equalsIgnoreCase(c.getName())).collect(Collectors.toList());
		assertEquals(2,countryModel.get(0).getNumberOfArmies());
	}

	@Test
	public void testplaceArmyExceedNumberOfArmies(){

		mapController.editcontinent("Asia", "3", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt","Africa","");
		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");

		gameController.gameplayer("Michael","");
		gameController.gameplayer("Pinkal","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);

		for(int i=0; i<50; i++)
		{
			gameController.placearmy("Egypt");
			gameController.placearmy("Jordan");
		}

		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->"Egypt".equalsIgnoreCase(c.getName())).collect(Collectors.toList());
		assertEquals(40,countryModel.get(0).getNumberOfArmies());
	}

	@Test
	public void testplaceAll(){

		mapController.editcontinent("Asia", "3", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt","Africa","");
		mapController.editcountry("Jordan","Asia","");
		mapController.editcountry("India","Asia","");
		mapController.editcountry("Iran","Asia","");

		gameController.gameplayer("Michael","");
		gameController.gameplayer("Pinkal","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);

		for(int i=0; i<30; i++)
		{
			gameController.placearmy("Egypt");
			gameController.placearmy("Jordan");
		}
		gameController.placeall();

		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->c.getPlayerId() ==1).collect(Collectors.toList());
		assertEquals(40,countryModel.get(0).getNumberOfArmies() + countryModel.get(1).getNumberOfArmies());
	}

	@Test
	public void testReinforceActivePlayerTurn(){

		mapController.editcontinent("Asia", "2", "None");
		mapController.editcontinent("Africa", "1", "None");

		mapController.editcountry("Egypt","Africa","");
		mapController.editcountry("Jordan","Asia","");

		gameController.gameplayer("Michael","");
		gameController.gameplayer("Pinkal","");

		gameController.populatecountries();
		RunningGame.getInstance().setCurrentPlayerId(1);

		gameController.placeall();

		gameController.reinforce("Egypt",3);

		List<CountryModel> countryModel = RunningGame.getInstance().getCountries().getList().stream().filter(c->"Egypt".equalsIgnoreCase(c.getName())).collect(Collectors.toList());
		assertEquals(43,countryModel.get(0).getNumberOfArmies());
	}
}
