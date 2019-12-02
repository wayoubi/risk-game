package ca.concordia.app.risk.model.maps;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.stream.Collectors;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;

/**
 * This method implements MapWriter interface to implement the default map writer
 * @author i857625
 *
 */
public class DefaultMapWriter implements MapWriter {

	/**
	 * print writer
	 */
	private PrintWriter printWriter;

	/**
	 * Constructor
	 * @param printWriter print writer
	 */
	public DefaultMapWriter(PrintWriter printWriter) {
		this.setPrintWriter(printWriter);
	}

	/**
	 * This method write headers for the conquest map
	 */
	@Override
	public void writeHeader() {
		// TODO Auto-generated method stub

	}

	/**
	 * This method write continents for the default conquest map
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
	 * This method write countries for the default conquest map
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
	 * This method write borders for the default conquest map
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
	 * gets {@link printWriter}
	 * @return
	 */
	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	/**
	 * sets {@link printWriter}
	 * @param printWriter print writer
	 */
	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

}
