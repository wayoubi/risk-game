package ca.concordia.app.risk.model.maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.services.MapService;

/**
 * 
 * @author i857625
 *
 */
public class DefaultMapReader implements MapReader {
	
	/**
	 * Dependency injection from GameService
	 */
	private MapService mapService;
	
	/**
	 * 
	 */
	private BufferedReader bufferedReader;
	
	/**
	 * 
	 */
	private List<String> headerLines;
	


	/**
	 * 
	 */
	private List<String> continentLines;
	


	/**
	 * 
	 */
	private List<String> countryLines;
	


	/**
	 * 
	 */
	private List<String> borderLines;
	
	/**
	 * 
	 * @param bufferedReader
	 */
	public DefaultMapReader(BufferedReader bufferedReader, MapService mapService) {
		this.mapService = mapService;
		this.setBufferedReader(bufferedReader);
		this.setHeaderLines(new ArrayList<String>());
		this.setContinentLines(new ArrayList<String>());
		this.setCountryLines(new ArrayList<String>());
		this.setBorderLines(new ArrayList<String>());
		this.parse();
	}
	
	/**
	 * 
	 */
	private void parse() {
		int flag = 0;
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if ("[continents]".equalsIgnoreCase(line)) {
					flag = 1;
				} else if ("[countries]".equalsIgnoreCase(line)) {
					flag = 2;
				} else if ("[borders]".equalsIgnoreCase(line)) {
					flag = 3;
				} else if (!System.lineSeparator().equalsIgnoreCase(line) && !"".equals(line.trim())) {
					switch (flag) {
					case 1:
						this.getContinentLines().add(line);
						break;
					case 2:
						this.getCountryLines().add(line);
						break;
					case 3:
						this.getBorderLines().add(line);
						break;
					default:
						break;
					}
				}
			}
		} catch (NumberFormatException nfException) {
			throw new RiskGameRuntimeException(nfException.getMessage(), nfException);
		} catch (IOException ioException) {
			throw new RiskGameRuntimeException(ioException.getMessage(), ioException);
		}
	}

	/**
	 * 
	 */
	@Override
	public void readHeader() {
		//DO Nothing
	}

	/**
	 * 
	 */
	@Override
	public void readContinents() {
		for(String line: this.getContinentLines()) {
			StringTokenizer continentLine = new StringTokenizer(line, " ");
			ContinentDto continentDto = new ContinentDto();
			continentDto.setName(continentLine.nextToken());
			continentDto.setNumberOfCountries(Integer.parseInt(continentLine.nextToken()));
			mapService.addContinent(continentDto);
		}
	}

	/**
	 * 
	 */
	@Override
	public void readCountries() {
		for(String line: this.getCountryLines()) {
			StringTokenizer countryLine = new StringTokenizer(line, " ");
			ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
			CountryDto countryDto = new CountryDto();
			countryLine.nextToken();
			countryDto.setName(countryLine.nextToken());
			countryDto.setContinentName(continentDaoImpl
					.findById(RunningGame.getInstance(), Integer.parseInt(countryLine.nextToken()))
					.getName());
			countryDto.setNumberOfArmies(Integer.parseInt(countryLine.nextToken()));
			mapService.addCountry(countryDto);
		}
	}

	/**
	 * 
	 */
	@Override
	public void readBorders() {
		
		for(String line: this.getBorderLines()) {
			StringTokenizer borderLine = new StringTokenizer(line, " ");
			CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
			BorderDto borderDto = new BorderDto();
			borderDto.setCountryName(countryDaoImpl
					.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken()))
					.getName());
			while (borderLine.hasMoreTokens()) {
				borderDto.setNeighborCountryName(countryDaoImpl
						.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken()))
						.getName());
				try {
					mapService.addNeighbor(borderDto);
				} catch (RiskGameRuntimeException riskGameRuntimeException) {
					// nothing to do
				}
			}
		}
	}
	
	/**
	 * 
	 * @param bufferedReader
	 */
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}
	
	/**
	 * 
	 * @return
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}
	
	/**
	 * @return the headerLines
	 */
	private List<String> getHeaderLines() {
		return headerLines;
	}

	/**
	 * @param headerLines the headerLines to set
	 */
	private void setHeaderLines(List<String> headerLines) {
		this.headerLines = headerLines;
	}
	
	/**
	 * @return the continentLines
	 */
	private List<String> getContinentLines() {
		return continentLines;
	}

	/**
	 * @param continentLines the continentLines to set
	 */
	private void setContinentLines(List<String> continentLines) {
		this.continentLines = continentLines;
	}
	
	/**
	 * @return the countryLines
	 */
	private List<String> getCountryLines() {
		return countryLines;
	}

	/**
	 * @param countryLines the countryLines to set
	 */
	private void setCountryLines(List<String> countryLines) {
		this.countryLines = countryLines;
	}
	
	/**
	 * @return the borderLines
	 */
	private List<String> getBorderLines() {
		return borderLines;
	}

	/**
	 * @param borderLines the borderLines to set
	 */
	private void setBorderLines(List<String> borderLines) {
		this.borderLines = borderLines;
	}
	
}
