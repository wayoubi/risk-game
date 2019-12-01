package ca.concordia.app.risk.model.maps;

import java.io.BufferedReader;

import ca.concordia.app.risk.services.MapService;

/**
 * 
 * @author i857625
 *
 */
public class ConquestMapReadingAdaptor implements MapReader {

	/**
	 * 
	 */
	private ConquestMapReader conquestMapReader;

	/**
	 * 
	 * @param bufferedReader
	 */
	public ConquestMapReadingAdaptor(BufferedReader bufferedReader, MapService mapService) {
		this.setConquestMapReader(new ConquestMapReader(bufferedReader, mapService));

	}

	@Override
	public void readHeader() {
		this.getConquestMapReader().readHeader();

	}

	@Override
	public void readContinents() {
		this.getConquestMapReader().readContinents();

	}

	@Override
	public void readCountries() {
		this.getConquestMapReader().readTerritories();

	}

	@Override
	public void readBorders() {
		// Do Nothing, already parsed
	}

	/**
	 * 
	 * @return
	 */
	public ConquestMapReader getConquestMapReader() {
		return conquestMapReader;
	}

	/**
	 * 
	 * @param conquestMapReader
	 */
	public void setConquestMapReader(ConquestMapReader conquestMapReader) {
		this.conquestMapReader = conquestMapReader;
	}

}
