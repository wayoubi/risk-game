package ca.concordia.app.risk.services;

import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.ObjectFactory;

public class MapService {

	private ObjectFactory objectFactory = new ObjectFactory();

	/**
	 * 
	 * @param continentDto
	 */
	public void addContinent(ContinentDto continentDto) throws Exception {
		ContinentModel continentModel = objectFactory.createContinentModel();
		BeanUtils.copyProperties(continentDto, continentModel);
		ContinentDaoImpl continentDaoImp = new ContinentDaoImpl();
		continentDaoImp.assignID(RunningGame.getInstance(), continentModel);
		RunningGame.getInstance().getContinents().getList().add(continentModel);
	}

	/**
	 * 
	 * @param continentDto
	 */
	public void removeContinent(ContinentDto continentDto) throws Exception {
		ContinentDaoImpl continentDao = new ContinentDaoImpl();
		ContinentModel continentModel = continentDao.findByName(RunningGame.getInstance(), continentDto.getName());
		continentDao.getCountries(RunningGame.getInstance(), continentModel).stream().forEach(countryModel -> {
			CountryDto countryDto = new CountryDto();
			BeanUtils.copyProperties(countryModel, countryDto);
			try {
				removeCountry(countryDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		continentDao.delete(RunningGame.getInstance(), continentModel);
	}

	/**
	 * 
	 * @param countryDto
	 * @throws Exception
	 */
	public void addCountry(CountryDto countryDto) throws Exception {
		CountryModel countryModel = objectFactory.createCountryModel();
		BeanUtils.copyProperties(countryDto, countryModel);
		ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
		ContinentModel continentModel = continentDaoImpl.findByName(RunningGame.getInstance(),
				countryDto.getContenentName());
		countryModel.setContinentId(continentModel.getId());
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		countryDaoImpl.assignID(RunningGame.getInstance(), countryModel);
		RunningGame.getInstance().getCountries().getList().add(countryModel);
		RunningGame.getInstance().getGraph().addVertex(countryModel.getName());
		
	}

	/**
	 * 
	 * @param countryDto
	 * @throws Exception
	 */
	public void removeCountry(CountryDto countryDto) throws Exception {
		BorderDaoImp borderDaoImp = new BorderDaoImp();
		BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), countryDto.getName());
		borderDaoImp.delete(RunningGame.getInstance(), borderModel);
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryDto.getName());
		countryDaoImpl.delete(RunningGame.getInstance(), countryModel);
		RunningGame.getInstance().getGraph().removeVertex(countryModel.getName());
	}

	/**
	 * 
	 * @param borderDto
	 */
	public void addNeighbor(BorderDto borderDto) throws Exception {
		this.makeBorder(borderDto);
		BorderDto borderDto2 = new BorderDto();
		borderDto2.setCountryName(borderDto.getNeighborCountryName());
		borderDto2.setNeighborCountryName(borderDto.getCountryName());
		this.makeBorder(borderDto2);
		RunningGame.getInstance().getGraph().addEdge(borderDto.getCountryName(), borderDto.getNeighborCountryName());
		
	}

	/**
	 * 
	 * @param borderDto
	 * @throws Exception
	 */
	private void makeBorder(BorderDto borderDto) throws Exception {
		BorderDaoImp borderDaoImp = new BorderDaoImp();
		BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), borderDto.getCountryName());
		if (borderModel == null) {
			borderModel = objectFactory.createBorderModel();
			RunningGame.getInstance().getBorders().getList().add(borderModel);
		}
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getCountryName());
		borderModel.setCountryId(countryModel.getId());
		countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), borderDto.getNeighborCountryName());
		borderModel.getNeighbours().add(countryModel.getId());
	}

	/**
	 * 
	 * @param borderDto
	 * @throws Exception
	 */
	public void removeNeighbor(BorderDto borderDto) throws Exception {
		this.removeBorder(borderDto);
		BorderDto borderDto2 = new BorderDto();
		borderDto2.setCountryName(borderDto.getNeighborCountryName());
		borderDto2.setNeighborCountryName(borderDto.getCountryName());
		this.removeBorder(borderDto2);
		RunningGame.getInstance().getGraph().removeEdge(borderDto.getCountryName(), borderDto.getNeighborCountryName());
	}

	/**
	 * 
	 * @param borderDto
	 */
	private void removeBorder(BorderDto borderDto) throws Exception {
		BorderDaoImp borderDaoImp = new BorderDaoImp();
		BorderModel borderModel = borderDaoImp.findByName(RunningGame.getInstance(), borderDto.getCountryName());
		if (borderModel == null) {
			return;
		}
		CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
		CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(),
				borderDto.getNeighborCountryName());
		borderModel.getNeighbours().remove(new Integer(countryModel.getId()));
	}
}