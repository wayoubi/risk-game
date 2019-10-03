package ca.concordia.app.risk.model.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

public class CountryDaoImpl implements Dao<CountryModel> {

	/**
	 * 
	 */
	@Override
	public CountryModel findByName(@NotNull GameModel gameModel, String name){
		return gameModel.getCountries().getList().stream().filter(c -> c.getName().equals(name))
				.findAny().orElse(null);
	}

	/**
	 * 
	 */
	@Override
	public CountryModel findById(@NotNull GameModel gameModel, int id){
		return gameModel.getCountries().getList().stream().filter(c -> c.getId() == id).findAny()
				.orElse(null);
	}

	/**
	 * 
	 */
	@Override
	public void assignID(@NotNull GameModel gameModel, CountryModel t){
		Comparator<CountryModel> comparator = Comparator.comparing(CountryModel::getId);
		Optional<CountryModel> optional = gameModel.getCountries().getList().stream().max(comparator);
		if (optional.isPresent()) {
			t.setId(optional.get().getId() + 1);
		} else {
			t.setId(1);
		}
	}

	/**
	 * 
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, CountryModel t){
		gameModel.getCountries().getList().remove(t);

	}

	/**
	 * 
	 * @param gameModel
	 * @return
	 */
	public List<CountryModel> getCountries(@NotNull GameModel gameModel){
		return gameModel.getCountries().getList().stream().collect(Collectors.toList());
	}

}
