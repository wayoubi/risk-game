package ca.concordia.app.risk.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.*;
import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.GameStarterDto;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.utility.DateUtils;

/**
 * 
 * @author i857625
 *
 */
public class GameService {
	
	@Autowired
	MapService mapService;

	private static final String GAME_CANNOT_BE_SAVED = "Game caanot be saved!";

	/**
	 * 
	 */
	public void saveGame() {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DateUtils.getXMLDateTime(new Date());
			RunningGame.getInstance().setLastSavedDate(xmlGregorianCalendar);
		} catch (DatatypeConfigurationException configurationException) {
			throw new RiskGameRuntimeException(GAME_CANNOT_BE_SAVED, configurationException);
		}
		try {
			File file = new File("saved/game.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(RunningGame.getInstance(), file);
		} catch (JAXBException jaxbException) {
			throw new RiskGameRuntimeException(GAME_CANNOT_BE_SAVED, jaxbException);
		}
	}
	ObjectFactory objectFactory = new ObjectFactory();;

	/**
	 * 
	 * @param fileName
	 */
	public void saveMap(String fileName) {
		if(!this.validateMap()) {
			throw new RiskGameRuntimeException("Map cannot be saved, map in invalid");
		}
		try (FileWriter fileWriter = new FileWriter(String.format("saved/%s", fileName))) {
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			//add continents
			printWriter.printf("[continents]%s", System.lineSeparator());
			Comparator<ContinentModel> continentModelComparator = Comparator.comparing(ContinentModel::getId);
			RunningGame.getInstance().getContinents().getList().stream().sorted(continentModelComparator).forEach(continent -> printWriter
					.printf("%s %s%s", continent.getName(), continent.getNumberOfCountries(), System.lineSeparator()));
			printWriter.print(System.lineSeparator());
			
			//add countries		
			Comparator<CountryModel> countryModelComparator = Comparator.comparing(CountryModel::getId);
			printWriter.printf("[countries]%s", System.lineSeparator());
			RunningGame.getInstance().getCountries().getList().stream().sorted(countryModelComparator)
					.forEach(country -> printWriter.printf("%s %s %s %s %s", country.getId(), country.getName(), country.getContinentId(),
							country.getNumberOfArmies(), System.lineSeparator()));
			printWriter.print(System.lineSeparator());
			
			//add borders
			printWriter.printf("[borders]%s", System.lineSeparator());
			Comparator<BorderModel> comparator = Comparator.comparing(BorderModel::getCountryId);
			RunningGame.getInstance().getBorders().getList().stream().sorted(comparator)
					.forEach(border -> printWriter.printf("%s %s %s", border.getCountryId(),
							border.getNeighbours().stream().map(String::valueOf).collect(Collectors.joining(" ")),System.lineSeparator()));
		} catch (IOException ioException) {
			throw new RiskGameRuntimeException("Game file cannot be saved", ioException);
		}
	}
	
	/**
	 * 
	 * @param fileName
	 */
	public void loadMap(String fileName) {
		this.editMap(fileName);
		if(!this.validateMap()) {
			throw new RiskGameRuntimeException("Countries are not connected, Map is invalid");
		}
	}
	
	/**
	 * 
	 * @param fileName
	 */
	public void editMap(String fileName) {
		RunningGame.reset();
		try (BufferedReader bufferedReader  = new BufferedReader(new FileReader(new File(fileName)))) {
			int flag=0;
			String line;
			while((line = bufferedReader.readLine()) != null) {
				if("[continents]".equalsIgnoreCase(line)) {
					flag = 1;
				} else if("[countries]".equalsIgnoreCase(line)) {
					flag = 2;
				} else if("[borders]".equalsIgnoreCase(line)) {
					flag = 3;
				} else if(!System.lineSeparator().equalsIgnoreCase(line) && !"".equals(line.trim())){
					switch(flag) {
					case 1:
						StringTokenizer continentLine = new StringTokenizer(line," ");
						ContinentDto continentDto = new ContinentDto();
						continentDto.setName(continentLine.nextToken());
						continentDto.setNumberOfCountries(Integer.parseInt(continentLine.nextToken()));
						mapService.addContinent(continentDto);
						break;
					case 2:
						StringTokenizer countryLine = new StringTokenizer(line," ");
						ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
						CountryDto countryDto = new CountryDto();
						countryLine.nextToken();
						countryDto.setName(countryLine.nextToken());
						countryDto.setContenentName(continentDaoImpl.findById(RunningGame.getInstance(), Integer.parseInt(countryLine.nextToken())).getName());
						countryDto.setNumberOfArmies(Integer.parseInt(countryLine.nextToken()));
						mapService.addCountry(countryDto);
						break;
					case 3:
						StringTokenizer borderLine = new StringTokenizer(line," ");
						CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
						BorderDto borderDto = new BorderDto();
						borderDto.setCountryName(countryDaoImpl.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken())).getName());
						while(borderLine.hasMoreTokens()){
							borderDto.setNeighborCountryName(countryDaoImpl.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken())).getName());
							try {
								mapService.addNeighbor(borderDto);
							}catch(RiskGameRuntimeException riskGameRuntimeException){
								//nothing to do
							}
						}
						break;
					default:
						break;
					}					
				}				
			}
		} catch(FileNotFoundException fileNotFoundException) {
			throw new RiskGameRuntimeException(String.format("Map cannot be edited, [%s] does not exist", fileName), fileNotFoundException);
		} catch(IOException ioException) {
			throw new RiskGameRuntimeException("Map cannot be edited", ioException);
		}
	}

	/**
	 * 
	 */
	public boolean validateMap() {
		ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(RunningGame.getInstance().getGraph());
		return connectivityInspector.isConnected();
	}

    public void addPlayer(PlayerDto playerDto) throws Exception {
		PlayerModel playerModel = objectFactory.createPlayerModel();
		BeanUtils.copyProperties(playerDto, playerModel);
		PlayerDaoImpl playerDaoImp = new PlayerDaoImpl();
		playerDaoImp.assignID(RunningGame.getInstance(), playerModel);
		RunningGame.getInstance().getPlayers().getList().add(playerModel);
	}

	public void removePlayer(PlayerDto playerDto) throws Exception {
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), playerDto.getName());
		playerDao.delete(RunningGame.getInstance(), playerModel);
	}

	public void populateCountries() {
		int numberOfCountries = RunningGame.getInstance().getCountries().getList().size();
		int numberOfPlayers = RunningGame.getInstance().getPlayers().getList().size();
		int playerID=0;

		Iterator it = RunningGame.getInstance().getCountries().getList().iterator();

		while(it.hasNext())
		{
			if(playerID<numberOfPlayers)
			{
				playerID++;
			}
			else
			{
				playerID=1;
			}
			CountryModel countryModel = (CountryModel) it.next();
			countryModel.setPlayerId(playerID);
		}
	}

	public void placeArmy(String countryName) throws Exception {

		CountryModel countryModel = RunningGame.getInstance().getCountries().getList().stream().filter((c -> c.getName()==countryName)).findAny().orElse(null);;

		if (countryModel == null) {
			throw new Exception("Country Does Not Exist");
		}
		countryModel.setNumberOfArmies(countryModel.getNumberOfArmies()+1);
	}
}