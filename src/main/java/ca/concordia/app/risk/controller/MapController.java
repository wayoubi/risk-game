package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.MapBusinessDelegate;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;

@ShellComponent
public class MapController {

	@Autowired
	private MapBusinessDelegate mapBusinessDelegate;

	/**
	 * 
	 * @param continentName2Add
	 * @param numberOfCountries
	 * @param continentName2Remove
	 * @return
	 */
	@ShellMethod("Add/ Remove Continent")
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
		return "Continent edited successfully";
	}

	/**
	 * 
	 * @param continentName2Add
	 * @param numberOfCountries
	 * @param continentName2Remove
	 * @return
	 */
	@ShellMethod("Save the current game")
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
		return "Continent edited successfully";
	}
}
