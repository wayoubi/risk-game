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
  }

  /**
   * 
   * @param continentDto
   */
  public void removeContinent(ContinentDto continentDto) {
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
  }

  /**
   * 
   * @param countryDto
   */
  public void addCountry(CountryDto countryDto) {
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryDto.getName());
    if (countryModel != null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] already exist, cannot be added!", countryDto.getName()));
    }
    ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
    ContinentModel continentModel = continentDaoImpl.findByName(RunningGame.getInstance(),
        countryDto.getContenentName());
    if (continentModel == null) {
      throw new RiskGameRuntimeException(String.format("Continent [%s] does not exist, Country [%s] cannot be added!",
          countryDto.getContenentName(), countryDto.getName()));
    }
    if (continentDaoImpl.getCountries(RunningGame.getInstance(), continentModel).size() >= continentModel
        .getNumberOfCountries()) {
      throw new RiskGameRuntimeException(
          String.format("No more countries can be added to Continent [%s], Country [%s] cannot be added!",
              countryDto.getContenentName(), countryDto.getName()));
    }
    countryModel = objectFactory.createCountryModel();
    BeanUtils.copyProperties(countryDto, countryModel);
    countryModel.setContinentId(continentModel.getId());
    countryDaoImpl.assignID(RunningGame.getInstance(), countryModel);
    RunningGame.getInstance().getCountries().getList().add(countryModel);
    RunningGame.getInstance().getGraph().addVertex(countryModel.getName());
  }

  /**
   * 
   * @param countryDto
   * @throws Exception
   */
  public void removeCountry(CountryDto countryDto) {
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
  }

  /**
   * This method is used to add neighbour to a country
   * 
   * @param borderDto
   */
  public void addNeighbor(BorderDto borderDto) {
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getCountryName());
    if (countryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, border will be ignored", borderDto.getCountryName()));
    }
    countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getNeighborCountryName());
    if (countryModel == null) {
      throw new RiskGameRuntimeException(
          String.format("Country [%s] doesn't exist, border will be ignored", borderDto.getCountryName()));
    }
    this.makeBorder(borderDto.getCountryName(), borderDto.getNeighborCountryName());
    this.makeBorder(borderDto.getNeighborCountryName(), borderDto.getCountryName());
    RunningGame.getInstance().getGraph().addEdge(borderDto.getCountryName(), borderDto.getNeighborCountryName());

  }

  /**
   * 
   * @param countryName
   * @param neighborCountryName
   */
  private void makeBorder(String countryName, String neighborCountryName) {
    BorderDaoImp borderDaoImp = new BorderDaoImp();
    BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryName);
    if (borderModel == null) {
      borderModel = objectFactory.createBorderModel();
      RunningGame.getInstance().getBorders().getList().add(borderModel);
    }
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
    borderModel.setCountryId(countryModel.getId());
    countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), neighborCountryName);
    if (borderModel.getNeighbours().contains(Integer.valueOf(countryModel.getId()))) {
      throw new RiskGameRuntimeException(
          String.format("Border [%s][%s] already exist!", countryName, neighborCountryName));
    }
    borderModel.getNeighbours().add(Integer.valueOf(countryModel.getId()));
  }

  /**
   * 
   * @param borderDto
   */
  public void removeNeighbor(BorderDto borderDto) {
    this.removeBorder(borderDto.getCountryName(), borderDto.getNeighborCountryName());
    this.removeBorder(borderDto.getNeighborCountryName(), borderDto.getCountryName());
    RunningGame.getInstance().getGraph().removeEdge(borderDto.getCountryName(), borderDto.getNeighborCountryName());
  }

  /**
   * 
   * @param countryName
   * @param neighborCountryName
   */
  private void removeBorder(String countryName, String neighborCountryName) {
    BorderDaoImp borderDaoImp = new BorderDaoImp();
    BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryName);
    if (borderModel == null) {
      return;
    }
    CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
    CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), neighborCountryName);
    borderModel.getNeighbours().remove(Integer.valueOf(countryModel.getId()));
  }
}