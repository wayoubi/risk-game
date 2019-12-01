package ca.concordia.app.risk.model.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.services.MapService;

/**
 * 
 * @author i857625
 *
 */
public class ConquestMapReader {
	
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
	private List<String> territoryLines;

	/**
	 * 
	 * @param bufferedReader
	 */
	public ConquestMapReader(BufferedReader bufferedReader, MapService mapService) {
		this.mapService = mapService;
		this.setBufferedReader(bufferedReader);
		this.setHeaderLines(new ArrayList<String>());
		this.setContinentLines(new ArrayList<String>());
		this.setTerritoryLines(new ArrayList<String>());
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
				if ("[Map]".equalsIgnoreCase(line)) {
					flag = 1;
				} else if ("[Continents]".equalsIgnoreCase(line)) {
					flag = 2;
				} else if ("[Territories]".equalsIgnoreCase(line)) {
					flag = 3;
				} else if (!System.lineSeparator().equalsIgnoreCase(line) && !"".equals(line.trim())) {
					switch (flag) {
					case 1:
						this.getHeaderLines().add(line);
						break;
					case 2:
						this.getContinentLines().add(line);
						break;
					case 3:
						this.getTerritoryLines().add(line);
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
	public void readHeader() {
		//DO Nothing
	}
	
	/**
	 * 
	 */
	public void readContinents() {
		for(String line: this.getContinentLines()) {
			StringTokenizer continentLine = new StringTokenizer(line, "=");
			ContinentDto continentDto = new ContinentDto();
			continentDto.setName(continentLine.nextToken());
			continentDto.setNumberOfCountries(Integer.parseInt(continentLine.nextToken()));
			mapService.addContinent(continentDto);
		}
	}
		
	/**
	 * 
	 */
	public void readTerritories() {
		
		// add countries
		for(String line: this.getTerritoryLines()) {
			StringTokenizer territoryLine = new StringTokenizer(line, ",");
			CountryDto countryDto = new CountryDto();
			countryDto.setName(territoryLine.nextToken());
			countryDto.setContinentName(territoryLine.nextToken()); 
			mapService.addCountry(countryDto);
		}	
		
		//add borders
		for(String line: this.getTerritoryLines()) {
			StringTokenizer territoryLine = new StringTokenizer(line, ",");
			int counter = 0;
			String country = null;
			while(territoryLine.hasMoreTokens()) {
				if(counter==0) {
					country = territoryLine.nextToken();
				} else if(counter==1) { 
					territoryLine.nextToken();
				} else if(counter!=1) {
					String neighborCountryName = territoryLine.nextToken();
					BorderDto borderDto = new BorderDto();
					borderDto.setCountryName(country.trim());
					borderDto.setNeighborCountryName(neighborCountryName.trim());
					try {
						mapService.addNeighbor(borderDto);
					} catch(RiskGameRuntimeException rgRuntimeExceptionx) {
						// do nothing
					}
				}
				counter++;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	/*
	 * 
	 */
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
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
	 * @return the territoryLines
	 */
	public List<String> getTerritoryLines() {
		return territoryLines;
	}

	/**
	 * @param territoryLines the territoryLines to set
	 */
	public void setTerritoryLines(List<String> territoryLines) {
		this.territoryLines = territoryLines;
	}

}
