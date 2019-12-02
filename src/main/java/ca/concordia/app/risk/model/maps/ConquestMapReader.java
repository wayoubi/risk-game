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
 * This class reads file for conquest map
 * @author i857625
 *
 */
public class ConquestMapReader {

	/**
	 * Dependency injection from GameService
	 */
	private MapService mapService;

	/**
	 * buffer reader
	 */
	private BufferedReader bufferedReader;

	/**
	 * list of headers
	 */
	private List<String> headerLines;

	/**
	 * list of continent 
	 */
	private List<String> continentLines;

	/**
	 * list of territory 
	 */
	private List<String> territoryLines;

	/**
	 * This method reads conquest map (Constructor)
	 * @param bufferedReader buffer reader
	 * @param mapService map service
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
	 * This method parses the file
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
	 * This method reads header
	 */
	public void readHeader() {
		// DO Nothing
	}

	/**
	 * This method reads continents
	 */
	public void readContinents() {
		for (String line : this.getContinentLines()) {
			StringTokenizer continentLine = new StringTokenizer(line, "=");
			ContinentDto continentDto = new ContinentDto();
			continentDto.setName(continentLine.nextToken());
			continentDto.setNumberOfCountries(Integer.parseInt(continentLine.nextToken()));
			mapService.addContinent(continentDto);
		}
	}

	/**
	 * This method read territories - countries & borders
	 */
	public void readTerritories() {

		// add countries
		for (String line : this.getTerritoryLines()) {
			StringTokenizer territoryLine = new StringTokenizer(line, ",");
			CountryDto countryDto = new CountryDto();
			countryDto.setName(territoryLine.nextToken());
			countryDto.setContinentName(territoryLine.nextToken());
			mapService.addCountry(countryDto);
		}

		// add borders
		for (String line : this.getTerritoryLines()) {
			StringTokenizer territoryLine = new StringTokenizer(line, ",");
			int counter = 0;
			String country = null;
			while (territoryLine.hasMoreTokens()) {
				if (counter == 0) {
					country = territoryLine.nextToken();
				} else if (counter == 1) {
					territoryLine.nextToken();
				} else if (counter != 1) {
					String neighborCountryName = territoryLine.nextToken();
					BorderDto borderDto = new BorderDto();
					borderDto.setCountryName(country.trim());
					borderDto.setNeighborCountryName(neighborCountryName.trim());
					try {
						mapService.addNeighbor(borderDto);
					} catch (RiskGameRuntimeException rgRuntimeExceptionx) {
						// do nothing
					}
				}
				counter++;
			}
		}
	}

	/**
	 * gets {@link bufferedReader}
	 * @return
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	/**
	 * sets {@link bufferedReader}
	 */
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	/**
	 * gets {@link headerLines}
	 * @return the headerLines
	 */
	private List<String> getHeaderLines() {
		return headerLines;
	}

	/**
	 * sets {@link headerLines}
	 * @param headerLines the headerLines to set
	 */
	private void setHeaderLines(List<String> headerLines) {
		this.headerLines = headerLines;
	}

	/**
	 * gets {@link continentLines}
	 * @return the continentLines
	 */
	private List<String> getContinentLines() {
		return continentLines;
	}

	/**
	 * sets {@link continentLines}
	 * @param continentLines the continentLines to set
	 */
	private void setContinentLines(List<String> continentLines) {
		this.continentLines = continentLines;
	}

	/**
	 * gets {@link territoryLines}
	 * @return the territoryLines
	 */
	public List<String> getTerritoryLines() {
		return territoryLines;
	}

	/**
	 * sets {@link territoryLines}
	 * @param territoryLines the territoryLines to set
	 */
	public void setTerritoryLines(List<String> territoryLines) {
		this.territoryLines = territoryLines;
	}

}
