package ca.concordia.app.risk.model.maps;

import java.io.PrintWriter;

/**
 * 
 * @author i857625
 *
 */
public class ConquestMapWritingAdaptor implements MapWriter {

	/**
	 * 
	 */
	private ConquestMapWriter conquestMapWriter;

	/**
	 * 
	 * @param printWriter
	 */
	public ConquestMapWritingAdaptor(PrintWriter printWriter) {
		this.setConquestMapWriter(new ConquestMapWriter(printWriter));
	}

	/**
	 * 
	 */
	@Override
	public void writeHeader() {
		this.getConquestMapWriter().writeHeader();

	}

	/**
	 * 
	 */
	@Override
	public void writeContinents() {
		this.getConquestMapWriter().writeContinents();

	}

	/**
	 * 
	 */
	@Override
	public void writeCountries() {
		this.getConquestMapWriter().writeTerritories();

	}

	/**
	 * 
	 */
	@Override
	public void writeBorders() {
		// Do Nothing

	}

	/**
	 * 
	 * @return
	 */
	public ConquestMapWriter getConquestMapWriter() {
		return conquestMapWriter;
	}

	/**
	 * 
	 * @param conquestMapWriter
	 */
	public void setConquestMapWriter(ConquestMapWriter conquestMapWriter) {
		this.conquestMapWriter = conquestMapWriter;
	}
}
