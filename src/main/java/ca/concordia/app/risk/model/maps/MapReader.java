package ca.concordia.app.risk.model.maps;

/**
 * This is an interface for reading the map
 * @author i857625
 *
 */
public interface MapReader {

	/**
	 * This is method to read the header
	 */
	public void readHeader();

	/**
	 * This is method to read the continents
	 */
	public void readContinents();

	/**
	 * This is method to read the countries
	 */
	public void readCountries();

	/**
	 * This is method to read the borders
	 */
	public void readBorders();
}
