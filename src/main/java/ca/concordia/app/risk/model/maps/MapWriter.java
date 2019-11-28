package ca.concordia.app.risk.model.maps;

/**
 * 
 */
public interface MapWriter {

	/**
	 * 
	 */
	public void writeHeader();
	
	/**
	 * 
	 */
	public void writeContinents();
	
	/**
	 * 
	 */
	public void writeCountries();
	
	/**
	 * 
	 */
	public void writeBorders();
}
