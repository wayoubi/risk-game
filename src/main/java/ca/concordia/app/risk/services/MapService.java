package ca.concordia.app.risk.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.maps.ConquestMapReadingAdaptor;
import ca.concordia.app.risk.model.maps.ConquestMapWritingAdaptor;
import ca.concordia.app.risk.model.maps.DefaultMapReader;
import ca.concordia.app.risk.model.maps.DefaultMapWriter;
import ca.concordia.app.risk.model.maps.MapReader;
import ca.concordia.app.risk.model.maps.MapWriter;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;
import ca.concordia.app.risk.shell.ShellHelper;

/**
 * MapController has all commands for altering the game map example, adding
 * continent, country and border
 * 
 * @author i857625
 *
 */
public class MapService {

  /**
   * Dependency injection from ShellHelper
   */
  @Autowired
  ShellHelper shellHelper;

  /**
   * Game Model Factory Object
   */
  private ObjectFactory objectFactory = new ObjectFactory();

  /**
   * 
   */
  private static Logger log = LoggerFactory.getLogger(MapService.class);

  /**
   * This method adds continent to the map
   * 
   * @param continentDto continent DTO
   */
  public void addContinent(ContinentDto continentDto) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    ContinentDaoImpl continentDaoImp = new ContinentDaoImpl();
    ContinentModel continentModel = continentDaoImp.findByName(RunningGame.getInstance(), continentDto.getName());
    if (continentModel != null) {
      throw new RiskGameRuntimeException(
          String.format("Continent [%s] already exist, cannot be added!", continentDto.getName()));
    }
    continentModel = objectFactory.createContinentModel();
    BeanUtils.copyProperties(continentDto, continentModel);
    continentDaoImp.assignID(RunningGame.getInstance(), continentModel);
    RunningGame.getInstance().getContinents().getList().add(continentModel);
    RunningGame.getInstance().addContinentGraph(continentModel.getName());

    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method removes continent from the map
   * 
   * @param continentDto continent DTO
   */
  public void removeContinent(ContinentDto continentDto) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    ContinentDaoImpl continentDao = new ContinentDaoImpl();
    ContinentModel continentModel = continentDao.findByName(RunningGame.getInstance(), continentDto.getName());
    if (continentModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Continent [%s] doesn't exist, nothing to remove!", continentDto.getName()));
    }
    continentDao.getCountries(RunningGame.getInstance(), continentModel).stream().forEach(countryModel -> {
      CountryDto countryDto = new CountryDto();
      BeanUtils.copyProperties(countryModel, countryDto);
      removeCountry(countryDto);
    });
    continentDao.delete(RunningGame.getInstance(), continentModel);
    RunningGame.getInstance().removeContinentGraph(continentModel.getName());
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method adds country to the continent
   * 
   * @param countryDto country DTO
   */
  public void addCountry(CountryDto countryDto) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryDto.getName());
    if (countryModel != null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] already exist, cannot be added!", countryDto.getName()));
    }
    ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
    ContinentModel continentModel = continentDaoImpl.findByName(RunningGame.getInstance(),
        countryDto.getContinentName());
    if (continentModel == null) {
      throw new RiskGameRuntimeException(String.format("Continent [%s] does not exist, Country [%s] cannot be added!",
          countryDto.getContinentName(), countryDto.getName()));
    }
    if (continentDaoImpl.getCountries(RunningGame.getInstance(), continentModel).size() >= continentModel
        .getNumberOfCountries()) {
      throw new RiskGameRuntimeException(
          String.format("No more countries can be added to Continent [%s], Country [%s] cannot be added!",
              countryDto.getContinentName(), countryDto.getName()));
    }
    countryModel = objectFactory.createCountryModel();
    BeanUtils.copyProperties(countryDto, countryModel);
    countryModel.setContinentId(continentModel.getId());
    countryDaoImpl.assignID(RunningGame.getInstance(), countryModel);
    RunningGame.getInstance().getCountries().getList().add(countryModel);
    RunningGame.getInstance().getGraph().addVertex(countryModel.getName());
    RunningGame.getInstance().getContinentGraph(continentModel.getName()).addVertex(countryModel.getName());
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method removes country from a continent
   * 
   * @param countryDto country DTO
   */
  public void removeCountry(CountryDto countryDto) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryDto.getName());
    if (countryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, nothing to remove!", countryDto.getName()));
    }
    BorderDaoImp borderDaoImp = new BorderDaoImp();
    BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryDto.getName());
    borderDaoImp.delete(RunningGame.getInstance(), borderModel);
    countryDaoImpl.delete(RunningGame.getInstance(), countryModel);
    RunningGame.getInstance().getGraph().removeVertex(countryModel.getName());

    ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
    String continentName = continentDaoImpl.findById(RunningGame.getInstance(), countryModel.getContinentId())
        .getName();
    RunningGame.getInstance().getContinentGraph(continentName).removeVertex(countryModel.getName());
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method is used to add neighbor to a country
   * 
   * @param borderDto border DTO
   */
  public void addNeighbor(BorderDto borderDto) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getCountryName());
    if (countryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, border will be ignored", borderDto.getCountryName()));
    }
    CountryModel neighborCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(),
        borderDto.getNeighborCountryName());
    if (neighborCountryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, border will be ignored", borderDto.getNeighborCountryName()));
    }
    this.makeBorder(countryModel.getName(), neighborCountryModel.getName());
    this.makeBorder(neighborCountryModel.getName(), countryModel.getName());

    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method makes the borders from one country to another
   * 
   * @param countryName         country name to make the border from
   * @param neighborCountryName country name to make the border to
   */
  private void makeBorder(String countryName, String neighborCountryName) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    BorderDaoImp borderDaoImp = new BorderDaoImp();
    BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryName);
    if (borderModel == null) {
      borderModel = objectFactory.createBorderModel();
      RunningGame.getInstance().getBorders().getList().add(borderModel);
    }
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
    borderModel.setCountryId(countryModel.getId());
    CountryModel neighborCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), neighborCountryName);
    if (borderModel.getNeighbours().contains(Integer.valueOf(neighborCountryModel.getId()))) {
      throw new RiskGameRuntimeException(
          String.format("Border [%s][%s] already exist!", countryName, neighborCountryName));
    }
    borderModel.getNeighbours().add(Integer.valueOf(neighborCountryModel.getId()));
    RunningGame.getInstance().getGraph().addEdge(countryName, neighborCountryName);
    if (countryModel.getContinentId() == neighborCountryModel.getContinentId()) {
      ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
      ContinentModel continentModel = continentDaoImpl.findById(RunningGame.getInstance(),
          countryModel.getContinentId());
      RunningGame.getInstance().getContinentGraph(continentModel.getName()).addEdge(countryModel.getName(),
          neighborCountryModel.getName());
    }
  }

  /**
   * This method removes neighbor of a country
   * 
   * @param borderDto border DTO
   */
  public void removeNeighbor(BorderDto borderDto) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    this.removeBorder(borderDto.getCountryName(), borderDto.getNeighborCountryName());
    this.removeBorder(borderDto.getNeighborCountryName(), borderDto.getCountryName());

    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method removes a border between two countries
   * 
   * @param countryName         country name
   * @param neighborCountryName neighbor country name
   */
  private void removeBorder(String countryName, String neighborCountryName) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
    if (countryModel == null) {
      throw new RiskGameRuntimeException(String.format("Country [%s] doesn't exist", countryName));
    }
    CountryModel neighborCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), neighborCountryName);
    if (neighborCountryModel == null) {
      throw new RiskGameRuntimeException(String.format("Country [%s] doesn't exist", neighborCountryName));
    }
    BorderDaoImp borderDaoImp = new BorderDaoImp();
    BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryModel.getName());
    if (borderModel == null) {
      return;
    }
    borderModel.getNeighbours().remove(Integer.valueOf(neighborCountryModel.getId()));
    RunningGame.getInstance().getGraph().removeEdge(countryName, neighborCountryName);
    if (countryModel.getContinentId() == neighborCountryModel.getContinentId()) {
      ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
      ContinentModel continentModel = continentDaoImpl.findById(RunningGame.getInstance(),
          countryModel.getContinentId());
      RunningGame.getInstance().getContinentGraph(continentModel.getName()).removeEdge(countryModel.getName(),
          neighborCountryModel.getName());
    }
  }

  /**
   * This method saves the map file
   * 
   * @param fileName file name
   */
  public void saveMap(String fileName, String format) {

    if (!this.validateMap("All")) {
      throw new RiskGameRuntimeException("Map cannot be saved, map in invalid");
    }

    try (FileWriter fileWriter = new FileWriter(String.format("saved/%s", fileName))) {
      PrintWriter printWriter = new PrintWriter(fileWriter);
      MapWriter mapWriter = null;
      if ("DOMINATION".equalsIgnoreCase(format)) {
        mapWriter = new DefaultMapWriter(printWriter);
      } else if ("CONQUEST".equalsIgnoreCase(format)) {
        mapWriter = new ConquestMapWritingAdaptor(printWriter);
      } else {
        throw new RiskGameRuntimeException("Invalid map format");
      }
      mapWriter.writeHeader();
      mapWriter.writeContinents();
      mapWriter.writeCountries();
      mapWriter.writeBorders();
    } catch (IOException ioException) {
      throw new RiskGameRuntimeException("Game file cannot be saved", ioException);
    }
  }

  /**
   * This method loads the map file
   * 
   * @param fileName file name
   */
  public void loadMap(String fileName, String format) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    this.editMap(fileName, format);

    if (!this.validateMap("All")) {
      RunningGame.reset();
      throw new RiskGameRuntimeException("Countries are not connected, Map is invalid");
    }

    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method edits map file
   * 
   * @param fileName file name
   */
  public void editMap(String fileName, String format) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    RunningGame.reset();

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)))) {
      MapReader mapReader = null;
      if ("DOMINATION".equalsIgnoreCase(format)) {
        mapReader = new DefaultMapReader(bufferedReader, this);
      } else if ("CONQUEST".equalsIgnoreCase(format)) {
        mapReader = new ConquestMapReadingAdaptor(bufferedReader, this);
      } else {
        throw new RiskGameRuntimeException("Invalid map format");
      }
      mapReader.readHeader();
      mapReader.readContinents();
      mapReader.readCountries();
      mapReader.readBorders();
    } catch (FileNotFoundException fileNotFoundException) {
      throw new RiskGameRuntimeException(String.format("Map cannot be edited, [%s] does not exist", fileName),
          fileNotFoundException);
    } catch (IOException ioException) {
      throw new RiskGameRuntimeException("Map cannot be edited", ioException);
    }
    RunningGame.getInstance().getSubject().markAndNotify();
  }

  /**
   * This method validates the map
   *
   * @param continentName continent name to validate
   * @return returns connectivity status of the graph vertices
   */
  public boolean validateMap(String continentName) {

    if (RunningGame.getInstance().isGamePlay()) {
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");
    }

    RunningGame.getInstance().setMapLoaded(true);

    if ("All".equals(continentName)) {
      int numberOfNotConnectedContinent = 0;
      List<ContinentModel> continentsList = RunningGame.getInstance().getContinents().getList();
      for (ContinentModel continentModel : continentsList) {
        ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
            RunningGame.getInstance().getContinentGraph(continentModel.getName()));
        if (!connectivityInspector.isConnected()) {
          log.info(
              shellHelper.getErrorMessage(String.format("Continent [%s] is not connected", continentModel.getName())));
          numberOfNotConnectedContinent++;
        }
      }
      if (numberOfNotConnectedContinent > 0) {
        return false;
      } else {
        ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
            RunningGame.getInstance().getGraph());
        return connectivityInspector.isConnected();
      }
    } else {
      ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
      ContinentModel continentModel = continentDaoImpl.findByName(RunningGame.getInstance(), continentName);
      if (continentModel == null) {
        throw new RiskGameRuntimeException(String.format("Continent [%s] doesn't exist", continentName));
      }
      ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(
          RunningGame.getInstance().getContinentGraph(continentName));
      return connectivityInspector.isConnected();
    }
  }
}