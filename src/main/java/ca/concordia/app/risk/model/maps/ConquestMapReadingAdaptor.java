package ca.concordia.app.risk.model.maps;

import java.io.BufferedReader;

import ca.concordia.app.risk.services.MapService;

/**
 * This class implements MapReader interface and is adaptor for reading conquest map
 * @author i857625
 *
 */
public class ConquestMapReadingAdaptor implements MapReader {

	/**
	 * conquest map reader
	 */
	private ConquestMapReader conquestMapReader;

	/**
	 * This method is adaptor for conquest map reader
	 * @param bufferedReader buffered reader
	 * @param mapService map service
	 */
	public ConquestMapReadingAdaptor(BufferedReader bufferedReader, MapService mapService) {
		this.setConquestMapReader(new ConquestMapReader(bufferedReader, mapService));

	}

	/**
	 * This method reads header
	 */
	@Override
	public void readHeader() {
		this.getConquestMapReader().readHeader();

	}

	/**
	 * This method reads continents
	 */
	@Override
	public void readContinents() {
		this.getConquestMapReader().readContinents();

	}

	/**
	 * This method reads countries
	 */
	@Override
	public void readCountries() {
		this.getConquestMapReader().readTerritories();

	}

	/**
	 * This method reads borders
	 */
	@Override
	public void readBorders() {
		// Do Nothing, already parsed
	}

	/**
	 * gets {@link conquestMapReader}
	 * @return
	 */
	public ConquestMapReader getConquestMapReader() {
		return conquestMapReader;
	}

	/**
	 * sets {@link conquestMapReader}
	 * @param conquestMapReader
	 */
	public void setConquestMapReader(ConquestMapReader conquestMapReader) {
		this.conquestMapReader = conquestMapReader;
	}

}
