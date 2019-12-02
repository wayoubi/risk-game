package ca.concordia.app.risk.model.maps;

import java.io.PrintWriter;

/**
 * This class implements MapWriter interface and is adaptor for writing conquest map
 * @author i857625
 *
 */
public class ConquestMapWritingAdaptor implements MapWriter {

	/**
	 * conquest map writer
	 */
	private ConquestMapWriter conquestMapWriter;

	/**
	 * This method is adaptor for conquest map writer
	 * @param printWriter print writer
	 */
	public ConquestMapWritingAdaptor(PrintWriter printWriter) {
		this.setConquestMapWriter(new ConquestMapWriter(printWriter));
	}

	/**
	 * This method writes header
	 */
	@Override
	public void writeHeader() {
		this.getConquestMapWriter().writeHeader();

	}

	/**
	 * This method writes continents
	 */
	@Override
	public void writeContinents() {
		this.getConquestMapWriter().writeContinents();

	}

	/**
	 * This method writes countries
	 */
	@Override
	public void writeCountries() {
		this.getConquestMapWriter().writeTerritories();

	}

	/**
	 * This method writes borders
	 */
	@Override
	public void writeBorders() {
		// Do Nothing

	}

	/**
	 * gets {@link conquestMapWriter}
	 * @return
	 */
	public ConquestMapWriter getConquestMapWriter() {
		return conquestMapWriter;
	}

	/**
	 * sets {@link conquestMapWriter}
	 * @param conquestMapWriter
	 */
	public void setConquestMapWriter(ConquestMapWriter conquestMapWriter) {
		this.conquestMapWriter = conquestMapWriter;
	}
}
