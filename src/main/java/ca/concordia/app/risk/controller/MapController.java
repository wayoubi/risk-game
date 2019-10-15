package ca.concordia.app.risk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import ca.concordia.app.risk.controller.delegate.MapBusinessDelegate;
import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.shell.ShellHelper;

@ShellComponent
public class MapController {

	private static Logger log = LoggerFactory.getLogger(MapController.class);

	private static final String NONE_DEFAULT_VALUE = "None";
	private static final String COMMAND_EXECUTED_SUCCESSFULLY = "Command executed successfully";

	@Autowired
	private MapBusinessDelegate mapBusinessDelegate;

	@Autowired
	private ShellHelper shellHelper;

	/**
	 * This method edits(add/remove) Continent's properties: continentName, numberOfCountries
	 * Example -> editcontinent -add [continentName] [numberOfCountries] -remove [continentName]
	 * 
	 * @param continentName2Add
	 * @param numberOfCountries
	 * @param continentName2Remove
	 * @return operation result (error/success)
	 */
	@ShellMethod("Add/ Remove Continent")
	public String editcontinent(
			@ShellOption(value = {"-add"}, defaultValue = NONE_DEFAULT_VALUE) String continentName2Add,
			@ShellOption(value = {"-value"}, defaultValue = "0") String numberOfCountries,
			@ShellOption(value = {"-remove"}, defaultValue = NONE_DEFAULT_VALUE) String continentName2Remove) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("inside editcontinent, passed parameters [%s] [%s] [%s]", continentName2Add,
					numberOfCountries, continentName2Remove));
		}	
		StringBuilder result = new StringBuilder();
		try {
			if (continentName2Add != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(continentName2Add)) {
				ContinentDto continentDto2Add = new ContinentDto();
				continentDto2Add.setName(continentName2Add);
				int numberOfCountriesValue = Integer.parseInt(numberOfCountries);
				if (numberOfCountriesValue != 0) {
					continentDto2Add.setNumberOfCountries(numberOfCountriesValue);
				} else {
					throw new RiskGameRuntimeException("Number of Countries cannot be 0");
				}
				mapBusinessDelegate.addContinent(continentDto2Add);
			}
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
		}
		try {
			if (continentName2Remove != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(continentName2Remove)) {
				ContinentDto continentDto2Remove = new ContinentDto();
				continentDto2Remove.setName(continentName2Remove);
				mapBusinessDelegate.removeContinent(continentDto2Remove);
			}
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
		}
		if (result.length() == 0) {
			result.append(shellHelper.getSuccessMessage(COMMAND_EXECUTED_SUCCESSFULLY));
		}
		return result.toString();
	}

	/**
	 * This method edits(add/remove) Country's properties: countryName, continentName
	 * Example -> editcountry --add [countryName] [continentName] --remove [countryName]
	 * 
	 * @param countryName2Add
	 * @param continent
	 * @param countryName2Remove
	 * @return operation result (error/success)
	 */
	@ShellMethod("Add/ Remove Country")
	public String editcountry(
			@ShellOption(value = {"-add"}, defaultValue = NONE_DEFAULT_VALUE) String countryName2Add,
			@ShellOption(value = {"-continent"}, defaultValue = NONE_DEFAULT_VALUE) String continent,
			@ShellOption(value = {"-remove"}, defaultValue = NONE_DEFAULT_VALUE) String countryName2Remove) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("inside editcountry, passed parameters [%s] [%s] [%s]", countryName2Add, continent, countryName2Remove));
		}
		StringBuilder result = new StringBuilder();
		try {
			if (countryName2Add != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(countryName2Add)) {
				CountryDto countryDto = new CountryDto();
				countryDto.setName(countryName2Add);
				countryDto.setContinentName(continent);
				mapBusinessDelegate.addCountry(countryDto);
			}
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
		}
		try {
			if (countryName2Remove != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(countryName2Remove)) {
				CountryDto countryDto = new CountryDto();
				countryDto.setName(countryName2Remove);
				mapBusinessDelegate.removeCountry(countryDto);
			}
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
		}
		if (result.length() == 0) {
			result.append(shellHelper.getSuccessMessage(COMMAND_EXECUTED_SUCCESSFULLY));
		}
		return result.toString();
	}

	/**
	 * This method edits(add/remove) Neighbors: neighborCountryName
	 * Example -> editNeighbor -add [countryName] [neighborCountryName] -remove [countryName] [neighborCountryName]s
	 * 
	 * @param countryNameAdd
	 * @param neighborCountryNameAdd
	 * @param countryNameRemove
	 * @param neighborCountryNameRemove
	 * @return operation result (error/success)
	 */
	@ShellMethod("Add/Remove Country ")
	public String editneighbor(
			@ShellOption(value = {"-add"}, defaultValue = NONE_DEFAULT_VALUE) String countryNameAdd,
			@ShellOption(value = {"-neighborcountrytoadd"}, defaultValue = NONE_DEFAULT_VALUE) String neighborCountryNameAdd,
			@ShellOption(value = {"-remove"}, defaultValue = NONE_DEFAULT_VALUE) String countryNameRemove,
			@ShellOption(value = {"-neighborcountrytoremove"}, defaultValue = NONE_DEFAULT_VALUE) String neighborCountryNameRemove) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("inside editcontinent, passed parameters [%s] [%s] [%s] [%s]", countryNameAdd, neighborCountryNameAdd, countryNameRemove, neighborCountryNameRemove));
		}
		StringBuilder result = new StringBuilder();
		try {
			if (countryNameAdd != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(countryNameAdd)) {
				BorderDto borderDto = new BorderDto();
				borderDto.setCountryName(countryNameAdd);
				if (neighborCountryNameAdd != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(neighborCountryNameAdd)) {
					borderDto.setNeighborCountryName(neighborCountryNameAdd);
				} else {
					throw new RiskGameRuntimeException("Neighbor Country Name is required");
				}
				mapBusinessDelegate.addNeighbor(borderDto);
			}
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
		}
		try {
			if (countryNameRemove != null && !NONE_DEFAULT_VALUE.equalsIgnoreCase(countryNameRemove)) {
				BorderDto borderDto = new BorderDto();
				borderDto.setCountryName(countryNameRemove);
				if (neighborCountryNameRemove != null
						&& !NONE_DEFAULT_VALUE.equalsIgnoreCase(neighborCountryNameRemove)) {
					borderDto.setNeighborCountryName(neighborCountryNameRemove);
				} else {
					throw new RiskGameRuntimeException("Neighbor Country Name is required");
				}
				mapBusinessDelegate.removeNeighbor(borderDto);
			}
		} catch (RiskGameRuntimeException riskGameRuntimeException) {
			result.append(shellHelper.getErrorMessage(riskGameRuntimeException.getMessage()));
		}
		if (result.length() == 0) {
			result.append(shellHelper.getSuccessMessage(COMMAND_EXECUTED_SUCCESSFULLY));
		}
		return result.toString();
	}
}