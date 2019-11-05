package ca.concordia.app.risk.model.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * This class is DAO(Data Access Layer) for country - to access a country's info
 *
 * @author i857625
 *
 */
public class CountryDaoImpl implements Dao<CountryModel> {

	/**
	 * This method finds a country by name
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param name      country name
	 * @return country object
	 */
	@Override
	public CountryModel findByName(@NotNull GameModel gameModel, String name) {
		return gameModel.getCountries().getList().stream().filter(c -> c.getName().equals(name)).findAny().orElse(null);
	}

	/**
	 * This method finds a country by id
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param id        id
	 * @return country object
	 */
	@Override
	public CountryModel findById(@NotNull GameModel gameModel, int id) {
		return gameModel.getCountries().getList().stream().filter(c -> c.getId() == id).findAny().orElse(null);
	}

	/**
	 * This method assigns id to a country
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param t         country model
	 */
	@Override
	public void assignID(@NotNull GameModel gameModel, CountryModel t) {
		Comparator<CountryModel> comparator = Comparator.comparing(CountryModel::getId);
		Optional<CountryModel> optional = gameModel.getCountries().getList().stream().max(comparator);
		if (optional.isPresent()) {
			t.setId(optional.get().getId() + 1);
		} else {
			t.setId(1);
		}
	}

	/**
	 * This method removes a list of countries
	 * {@inheritDoc}
	 * @param gameModel game model
	 * @param t         country model
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, CountryModel t) {
		gameModel.getCountries().getList().remove(t);

	}

	/**
	 * This method gets list of countries
	 * 
	 * @param gameModel game model
	 * @return list of countries
	 */
	public List<CountryModel> getCountries(@NotNull GameModel gameModel) {
		return gameModel.getCountries().getList().stream().collect(Collectors.toList());
	}

}
