package ca.concordia.app.risk.services;

import java.util.List;

import org.springframework.beans.BeanUtils;

import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.controller.dto.PlayerDto;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * 
 * @author i857625
 *
 */
public class PresentationService {

	/**
	 * 
	 */
	public PresentationService() {
		//
	}

	/**
	 * 
	 * @param playerName
	 * @return
	 * @throws Exception
	 */
	public PlayerDto getPlayerDetails(String playerName) throws Exception {
		// TODOM get contenant name later
		PlayerDaoImpl playerDao = new PlayerDaoImpl();
		PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), playerName);
		PlayerDto playerDto = new PlayerDto();
		BeanUtils.copyProperties(playerModel, playerDto);
		List<CountryModel> countries = playerDao.getCountries(RunningGame.getInstance(), playerModel);
		countries.stream().forEach(c -> playerDto.getCountryDtoList()
				.add(new CountryDto(c.getName(), c.getColor(), c.getNumberOfArmies(), "Not a problem now!")));
		return playerDto;
	}
}