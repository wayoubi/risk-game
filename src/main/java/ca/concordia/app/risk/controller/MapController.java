package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.MapBusinessDelegate;
import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;

@ShellComponent
public class MapController {

	@Autowired
	private MapBusinessDelegate mapBusinessDelegate;
	
	/**
	 * 
	 * Example > editcontinent --add [continentName] --value [numberOfCountries]
	 * --remove [continentName]
	 * 
	 * @param continentName2Add
	 * @param numberOfCountries
	 * @param continentName2Remove
	 * @return
	 */
	@ShellMethod("Add/ Remove Continent, Sample: editcontinent --add [continentName] --value [numberOfCountries] --remove [continentName]")
	public String editcontinent(@ShellOption(value = { "--add" }, defaultValue = "None") String continentName2Add,
			@ShellOption(value = { "--value" }, defaultValue = "0") String numberOfCountries,
			@ShellOption(value = { "--remove" }, defaultValue = "None") String continentName2Remove) {
		try {
			if (continentName2Add != null && !"None".equalsIgnoreCase(continentName2Add)) {
				ContinentDto continentDto2Add = new ContinentDto();
				continentDto2Add.setName(continentName2Add);
				int numberOfCountriesValue = Integer.parseInt(numberOfCountries);
				if (numberOfCountriesValue != 0) {
					continentDto2Add.setNumberOfCountries(numberOfCountriesValue);
				} else {
					throw new IllegalArgumentException("Number of Countries cannot be 0");
				}
				mapBusinessDelegate.addContinent(continentDto2Add);
			}
			if (continentName2Remove != null && !"None".equalsIgnoreCase(continentName2Remove)) {
				ContinentDto continentDto2Remove = new ContinentDto();
				continentDto2Remove.setName(continentName2Remove);
				mapBusinessDelegate.removeContinent(continentDto2Remove);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Game Continent edited successfully";
	}

	/**
	 * 
	 * Example editcountry -add [countryname] --continent [continentName] -remove
	 * [countryName]
	 * 
	 * @param continentName2Add
	 * @param numberOfCountries
	 * @param continentName2Remove
	 * @return
	 */
	@ShellMethod("Add/ Remove Country, Sample: editcountry --add countryname --continent continentname --remove countryname")
	public String editcountry(@ShellOption(value = { "--add" }, defaultValue = "None") String countryName2Add,
			@ShellOption(value = { "--continent" }, defaultValue = "None") String continent,
			@ShellOption(value = { "--remove" }, defaultValue = "None") String countryName2Remove) {
		try {
			if (countryName2Add != null && !"None".equalsIgnoreCase(countryName2Add)) {
				CountryDto countryDto = new CountryDto();
				countryDto.setName(countryName2Add);
				countryDto.setContenentName(continent);
				mapBusinessDelegate.addCountry(countryDto);

			}
			if (countryName2Remove != null && !"None".equalsIgnoreCase(countryName2Remove)) {
				CountryDto countryDto = new CountryDto();
				countryDto.setName(countryName2Remove);
				mapBusinessDelegate.removeCountry(countryDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Game Countries edited successfully";
	}

	/**
	 * 
	 * Example > editneighbor --add [countryName] --neighborcountry
	 * [neighborCountryName] --remove [countryName] --neighborcountry
	 * [neighborCountryName]
	 * 
	 * @param countryName
	 * @param neighborCountryName
	 * @param countryNameRemove
	 * @param neighborCountryNameRemove
	 * @return
	 */
	@ShellMethod("Add/ Remove Country, Sample: editneighbor --add [countryName] --neighborcountrytoadd [neighborCountryName] --remove [countryName]  --neighborcountrytoremove [neighborCountryName]")
	public String editneighbor(@ShellOption(value = { "--add" }, defaultValue = "None") String countryNameAdd,
			@ShellOption(value = { "--neighborcountrytoadd" }, defaultValue = "None") String neighborCountryNameAdd,
			@ShellOption(value = { "--remove" }, defaultValue = "None") String countryNameRemove, 
			@ShellOption(value = {"--neighborcountrytoremove" }, defaultValue = "None") String neighborCountryNameRemove) {
		try {
			if (countryNameAdd != null && !"None".equalsIgnoreCase(countryNameAdd)) {
				BorderDto borderDto = new BorderDto();
				borderDto.setCountryName(countryNameAdd);
				if (neighborCountryNameAdd != null && !"None".equalsIgnoreCase(neighborCountryNameAdd)) {
					borderDto.setNeighborCountryName(neighborCountryNameAdd);
				} else {
					throw new IllegalArgumentException("Neighbor Country Name is required");
				}
				mapBusinessDelegate.addNeighbor(borderDto);
			}
			if (countryNameRemove != null && !"None".equalsIgnoreCase(countryNameRemove)) {
				BorderDto borderDto = new BorderDto();
				borderDto.setCountryName(countryNameRemove);
				if (neighborCountryNameRemove != null && !"None".equalsIgnoreCase(neighborCountryNameRemove)) {
					borderDto.setNeighborCountryName(neighborCountryNameRemove);
				} else {
					throw new IllegalArgumentException("Neighbor Country Name is required");
				}
				mapBusinessDelegate.removeNeighbor(borderDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Game Borders edited successfully";
	}

}
