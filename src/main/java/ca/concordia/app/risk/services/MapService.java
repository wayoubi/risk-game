package ca.concordia.app.risk.services;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;
import org.springframework.beans.BeanUtils;

public class MapService {

  private ObjectFactory objectFactory = new ObjectFactory();

  /**
   * 
   * @param continentDto
   */
  public void addContinent(ContinentDto continentDto) {

    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

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
  }

  /**
   * 
   * @param continentDto
   */
  public void removeContinent(ContinentDto continentDto) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

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
  }
  
  /**
   * 
   * @param countryDto
   */
  public void addCountry(CountryDto countryDto) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

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
  }

  /**
   * 
   * @param countryDto
   * @throws Exception
   */
  public void removeCountry(CountryDto countryDto) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

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
    String continentName = continentDaoImpl.findById(RunningGame.getInstance(), countryModel.getContinentId()).getName();
    RunningGame.getInstance().getContinentGraph(continentName).removeVertex(countryModel.getName());
  }

  /**
   * This method is used to add neighbour to a country
   * 
   * @param borderDto
   */
  public void addNeighbor(BorderDto borderDto) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getCountryName());
    if (countryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, border will be ignored", borderDto.getCountryName()));
    }
    CountryModel neighborCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getNeighborCountryName());
    if (neighborCountryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, border will be ignored", borderDto.getNeighborCountryName()));
    }
    this.makeBorder(countryModel.getName(), neighborCountryModel.getName());
    this.makeBorder(neighborCountryModel.getName(), countryModel.getName());
  }

  /**
   * 
   * @param countryName
   * @param neighborCountryName
   */
  private void makeBorder(String countryName, String neighborCountryName) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

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
    if(countryModel.getContinentId()==neighborCountryModel.getContinentId()) {
    	ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
    	ContinentModel continentModel = continentDaoImpl.findById(RunningGame.getInstance(), countryModel.getContinentId());
    	RunningGame.getInstance().getContinentGraph(continentModel.getName()).addEdge(countryModel.getName(), neighborCountryModel.getName());
    }
  }

  /**
   * 
   * @param borderDto
   */
  public void removeNeighbor(BorderDto borderDto) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

    this.removeBorder(borderDto.getCountryName(), borderDto.getNeighborCountryName());
    this.removeBorder(borderDto.getNeighborCountryName(), borderDto.getCountryName());
  }

  /**
   * 
   * @param countryName
   * @param neighborCountryName
   */
  private void removeBorder(String countryName, String neighborCountryName) {
    if(RunningGame.getInstance().isGamePlay())
      throw new RiskGameRuntimeException("Command cannot be performed, Current game is Running");

    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
	CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
	if (countryModel == null) {
	      throw new RiskGameRuntimeException(
	          String.format("Country [%s] doesn't exist", countryName));
	}
	CountryModel neighborCountryModel = countryDaoImpl.findByName(RunningGame.getInstance(), neighborCountryName);
	if (neighborCountryModel == null) {
	      throw new RiskGameRuntimeException(
	          String.format("Country [%s] doesn't exist", neighborCountryName));
	}
	BorderDaoImp borderDaoImp = new BorderDaoImp();
    BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryModel.getName());
    if (borderModel == null) {
      return;
    } 
    borderModel.getNeighbours().remove(Integer.valueOf(neighborCountryModel.getId()));
    RunningGame.getInstance().getGraph().removeEdge(countryName, neighborCountryName);
    if(countryModel.getContinentId()==neighborCountryModel.getContinentId()) {
    	ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
    	ContinentModel continentModel = continentDaoImpl.findById(RunningGame.getInstance(), countryModel.getContinentId());
    	RunningGame.getInstance().getContinentGraph(continentModel.getName()).removeEdge(countryModel.getName(), neighborCountryModel.getName());
    }
  }
}