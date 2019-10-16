package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.services.MapService;

public class MapBusinessDelegate {

    /**
     * 
     * Dependency injection from GameService
     */
	@Autowired
	private MapService mapService;

	/**
	 * 
	 * @param continentDto
	 */
	public void addContinent(ContinentDto continentDto){
		mapService.addContinent(continentDto);

	}

	/**
	 * 
	 * @param continentDto
	 */
	public void removeContinent(ContinentDto continentDto){
		mapService.removeContinent(continentDto);
	}

	/**
	 * 
	 * @param countryDto
	 * @throws Exception
	 */
	public void addCountry(CountryDto countryDto){
		mapService.addCountry(countryDto);

	}

	/**
	 * 
	 * @param countryDto
	 * @throws Exception
	 */
	public void removeCountry(CountryDto countryDto){
		mapService.removeCountry(countryDto);

	}

	/**
	 * 
	 * @param borderDto
	 * @throws Exception
	 */
	public void addNeighbor(BorderDto borderDto){
		mapService.addNeighbor(borderDto);
	}

	/**
	 * 
	 * @param borderDto
	 * @throws Exception
	 */
	public void removeNeighbor(BorderDto borderDto){
		mapService.removeNeighbor(borderDto);
	}
}
