package ca.concordia.app.risk.model.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * This class is DAO(Data Access Layer) for continent - to access a continent's
 *
 * @author i857625
 *
 */
public class ContinentDaoImpl implements Dao<ContinentModel> {

	/**
	 * This method finds a continent by name
	 *
	 * @param gameModel game model
	 * @param name      name
	 * @return continent object
	 */
	@Override
	public ContinentModel findByName(@NotNull GameModel gameModel, String name) {
		return gameModel.getContinents().getList().stream().filter(c -> c.getName().equals(name)).findAny()
				.orElse(null);
	}

	/**
	 * This method finds a continent by id
	 *
	 * @param gameModel game model
	 * @param id        id
	 * @return continent object
	 */
	@Override
	public ContinentModel findById(@NotNull GameModel gameModel, int id) {
		return gameModel.getContinents().getList().stream().filter(c -> c.getId() == id).findAny().orElse(null);
	}

	/**
	 * This method assigns id to a continent
	 *
	 * @param gameModel game model
	 * @param t         continent model
	 */
	@Override
	public void assignID(@NotNull GameModel gameModel, ContinentModel t) {
		Comparator<ContinentModel> comparator = Comparator.comparing(ContinentModel::getId);
		Optional<ContinentModel> optional = gameModel.getContinents().getList().stream().max(comparator);
		if (optional.isPresent()) {
			t.setId(optional.get().getId() + 1);
		} else {
			t.setId(1);
		}
	}

	/**
	 * This method removes a list of continents
	 * 
	 * @param gameModel game model
	 * @param t         continent model
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, ContinentModel t) {
		RunningGame.getInstance().getContinents().getList().remove(t);
	}

	/**
	 * This method gets list of countries of a continent
	 * 
	 * @param gameModel game model
	 * @param t         continent model
	 * @return list of continents
	 */
	public List<CountryModel> getCountries(@NotNull GameModel gameModel, ContinentModel t) {
		return gameModel.getCountries().getList().stream().filter(c -> t.getId() == c.getContinentId())
				.collect(Collectors.toList());

	}

}
