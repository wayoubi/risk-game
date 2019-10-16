package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.services.MapService;

/**
 * MapBusinessDelegate
 *
 */
public class MapBusinessDelegate {

    /**
     * 
     * Dependency injection from GameService
     */
	@Autowired
	private MapService mapService;

	/**
	 * add Continent
	 * 
	 * @param continentDto
	 * continent DTO
	 */
	public void addContinent(ContinentDto continentDto){
		mapService.addContinent(continentDto);

	}

	/**
	 * remove Continent
	 * 
	 * @param continentDto
	 * continent DTO
	 */
	public void removeContinent(ContinentDto continentDto){
		mapService.removeContinent(continentDto);
	}

	/**
	 * add Country
	 * 
	 * @param countryDto
	 * country DTO
	 */
	public void addCountry(CountryDto countryDto){
		mapService.addCountry(countryDto);

	}

	/**
	 * remove Country
	 * 
	 * @param countryDto
	 * country DTO
	 */
	public void removeCountry(CountryDto countryDto){
		mapService.removeCountry(countryDto);

	}

	/**
	 * add Neighbor
	 * 
	 * @param borderDto
	 * border Dto
	 */
	public void addNeighbor(BorderDto borderDto){
		mapService.addNeighbor(borderDto);
	}

	/**
	 * remove Neighbor
	 * 
	 * @param borderDto
	 * border Dto
	 */
	public void removeNeighbor(BorderDto borderDto){
		mapService.removeNeighbor(borderDto);
	}
}
