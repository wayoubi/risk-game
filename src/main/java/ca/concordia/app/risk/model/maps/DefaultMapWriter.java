package ca.concordia.app.risk.model.maps;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.stream.Collectors;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;

/**
 * 
 * @author i857625
 *
 */
public class DefaultMapWriter implements MapWriter {

	/**
	 * 
	 */
	private PrintWriter printWriter;

	/**
	 * 
	 */
	public DefaultMapWriter(PrintWriter printWriter) {
		this.setPrintWriter(printWriter);
	}

	/**
	 * 
	 */
	@Override
	public void writeHeader() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	@Override
	public void writeContinents() {
		// add continents
		this.getPrintWriter().printf("[continents]%s", System.lineSeparator());
		Comparator<ContinentModel> continentModelComparator = Comparator.comparing(ContinentModel::getId);
		RunningGame.getInstance().getContinents().getList().stream().sorted(continentModelComparator)
				.forEach(continent -> this.getPrintWriter().printf("%s %s%s", continent.getName(),
						continent.getNumberOfCountries(), System.lineSeparator()));
		this.getPrintWriter().print(System.lineSeparator());

	}

	/**
	 * 
	 */
	@Override
	public void writeCountries() {
		// add countries
		Comparator<CountryModel> countryModelComparator = Comparator.comparing(CountryModel::getId);
		this.getPrintWriter().printf("[countries]%s", System.lineSeparator());
		RunningGame.getInstance().getCountries().getList().stream().sorted(countryModelComparator)
				.forEach(country -> this.getPrintWriter().printf("%s %s %s %s %s", country.getId(), country.getName(),
						country.getContinentId(), country.getNumberOfArmies(), System.lineSeparator()));
		this.getPrintWriter().print(System.lineSeparator());

	}

	/**
	 * 
	 */
	@Override
	public void writeBorders() {
		// add borders
		this.getPrintWriter().printf("[borders]%s", System.lineSeparator());
		Comparator<BorderModel> comparator = Comparator.comparing(BorderModel::getCountryId);
		RunningGame.getInstance().getBorders().getList().stream().sorted(comparator)
				.forEach(border -> this.getPrintWriter().printf("%s %s %s", border.getCountryId(),
						border.getNeighbours().stream().map(String::valueOf).collect(Collectors.joining(" ")),
						System.lineSeparator()));
		this.getPrintWriter().print(System.lineSeparator());
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
