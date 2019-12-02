package ca.concordia.app.risk.model.maps;

/**
 * This is an interface for writing the map
 */
public interface MapWriter {

	/**
	 * This is method to write to the header
	 */
	public void writeHeader();

	/**
	 * This is method to write continents
	 */
	public void writeContinents();

	/**
	 * This is method to write countries
	 */
	public void writeCountries();

	/**
	 * This is method to write borders
	 */
	public void writeBorders();
}
