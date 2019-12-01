package ca.concordia.app.risk.model.maps;

/**
 * 
 * @author i857625
 *
 */
public interface MapReader {

	/**
	 * 
	 */
	public void readHeader();

	/**
	 * 
	 */
	public void readContinents();

	/**
	 * 
	 */
	public void readCountries();

	/**
	 * 
	 */
	public void readBorders();
}
