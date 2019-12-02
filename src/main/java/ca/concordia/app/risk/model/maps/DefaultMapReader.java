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
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.services.MapService;

/**
 * This method implements MapReader interface to implement the default map reader
 * @author i857625
 *
 */
public class DefaultMapReader implements MapReader {

	/**
	 * Dependency injection from GameService
	 */
	private MapService mapService;

	/**
	 * buffered reader
	 */
	private BufferedReader bufferedReader;

	/**
	 * list of headers
	 */
	private List<String> headerLines;

	/**
	 * list of continents
	 */
	private List<String> continentLines;

	/**
	 * list of countries
	 */
	private List<String> countryLines;

	/**
	 * list of borders
	 */
	private List<String> borderLines;

	/**
	 * This method reads the default conquest map (Constructor)
	 * @param bufferedReader buffer reader
	 * @param mapService map service
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
	 * This method parses the file
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
	 * This method reads header
	 */
	@Override
	public void readHeader() {
		// DO Nothing
	}

	/**
	 * This method reads continents
	 */
	@Override
	public void readContinents() {
		for (String line : this.getContinentLines()) {
			StringTokenizer continentLine = new StringTokenizer(line, " ");
			ContinentDto continentDto = new ContinentDto();
			continentDto.setName(continentLine.nextToken());
			continentDto.setNumberOfCountries(Integer.parseInt(continentLine.nextToken()));
			mapService.addContinent(continentDto);
		}
	}

	/**
	 * This method reads countries
	 */
	@Override
	public void readCountries() {
		for (String line : this.getCountryLines()) {
			StringTokenizer countryLine = new StringTokenizer(line, " ");
			ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
			CountryDto countryDto = new CountryDto();
			countryLine.nextToken();
			countryDto.setName(countryLine.nextToken());
			countryDto.setContinentName(continentDaoImpl
					.findById(RunningGame.getInstance(), Integer.parseInt(countryLine.nextToken())).getName());
			countryDto.setNumberOfArmies(Integer.parseInt(countryLine.nextToken()));
			mapService.addCountry(countryDto);
		}
	}

	/**
	 * This method reads borders
	 */
	@Override
	public void readBorders() {

		for (String line : this.getBorderLines()) {
			StringTokenizer borderLine = new StringTokenizer(line, " ");
			CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
			BorderDto borderDto = new BorderDto();
			borderDto.setCountryName(countryDaoImpl
					.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken())).getName());
			while (borderLine.hasMoreTokens()) {
				borderDto.setNeighborCountryName(countryDaoImpl
						.findById(RunningGame.getInstance(), Integer.parseInt(borderLine.nextToken())).getName());
				try {
					mapService.addNeighbor(borderDto);
				} catch (RiskGameRuntimeException riskGameRuntimeException) {
					// nothing to do
				}
			}
		}
	}

	/**
	 * sets {@link bufferedReader}
	 * @param bufferedReader buffered reader
	 */
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	/**
	 * gets {@link bufferedReader}
	 * @return
	 */
	public BufferedReader getBufferedReader() {
		return bufferedReader;
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
	 * gets {@link countryLines}
	 * @return the countryLines
	 */
	private List<String> getCountryLines() {
		return countryLines;
	}

	/**
	 * sets {@link countryLines}
	 * @param countryLines the countryLines to set
	 */
	private void setCountryLines(List<String> countryLines) {
		this.countryLines = countryLines;
	}

	/**
	 * gets {@link borderLines}
	 * @return the borderLines
	 */
	private List<String> getBorderLines() {
		return borderLines;
	}

	/**
	 * sets {@link borderLines}
	 * @param borderLines the borderLines to set
	 */
	private void setBorderLines(List<String> borderLines) {
		this.borderLines = borderLines;
	}

}
