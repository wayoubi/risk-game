package ca.concordia.app.risk.services;

import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
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
		continentDao.delete(RunningGame.getInstance(), continentModel);
	}

}