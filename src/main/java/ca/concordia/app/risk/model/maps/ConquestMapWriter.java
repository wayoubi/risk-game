package ca.concordia.app.risk.model.maps;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.stream.Collectors;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;

/**
 * 
 * @author i857625
 *
 */
public class ConquestMapWriter {

	/**
	 * 
	 */
	private PrintWriter printWriter;
	
	/**
	 * 
	 * @param printWriter
	 */
	public ConquestMapWriter(PrintWriter printWriter) {
		this.setPrintWriter(printWriter);
	}
	
	/**
	 * 
	 */
	public void writeHeader() {
		// add header
		this.getPrintWriter().printf("[Map]%s", System.lineSeparator());
		this.getPrintWriter().printf("author=SOEN 6441 Team%s", System.lineSeparator());
		this.getPrintWriter().printf("image=map.bmp%s", System.lineSeparator());
		this.getPrintWriter().printf("wrap=no%s", System.lineSeparator());
		this.getPrintWriter().printf("scroll=vertical%s", System.lineSeparator());
		this.getPrintWriter().printf("warn=yes%s", System.lineSeparator());
		this.getPrintWriter().print(System.lineSeparator());
	}
	
	/**
	 * 
	 */
	public void writeContinents() {
		// add continents
		this.getPrintWriter().printf("[Continents]%s", System.lineSeparator());
		Comparator<ContinentModel> continentModelComparator = Comparator.comparing(ContinentModel::getId);
		RunningGame.getInstance().getContinents().getList().stream().sorted(continentModelComparator)
				.forEach(continent -> this.getPrintWriter().printf("%s=%s%s", continent.getName(),
						continent.getNumberOfCountries(), System.lineSeparator()));
		this.getPrintWriter().print(System.lineSeparator());
		
	}
	
	/**
	 * 
	 */
	public void writeTerritories() { 
		Comparator<CountryModel> countryModelComparator = Comparator.comparing(CountryModel::getId);
		this.getPrintWriter().printf("[Territories]%s", System.lineSeparator());
		RunningGame.getInstance().getCountries().getList().stream().sorted(countryModelComparator)
				.forEach(country -> {
					ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
					ContinentModel continentModel =  continentDaoImpl.findById(RunningGame.getInstance(), country.getContinentId());
					CountryDaoImpl countryDaoImpl = new CountryDaoImpl();	
					String neighboursStr = countryDaoImpl.getNeighboursOf(country, RunningGame.getInstance()).stream().map(CountryModel::getName).collect(Collectors.joining(","));
					this.getPrintWriter().printf("%s,%s,%s%s",country.getName(),continentModel.getName(), neighboursStr, System.lineSeparator());
				});
	}
	
	/**
	 * 
	 * @return
	 */
	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	/**
	 * 
	 * @param printWriter
	 */
	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	
	
}
