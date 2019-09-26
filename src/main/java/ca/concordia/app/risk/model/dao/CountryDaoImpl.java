package ca.concordia.app.risk.model.dao;

import java.util.Comparator;
import java.util.Optional;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;

public class CountryDaoImpl implements Dao<CountryModel>{

	@Override
	public CountryModel findByName(String name) throws Exception {
		CountryModel country = RunningGame.getInstance().getCountries().getList().stream()
				.filter(c -> c.getName().equals(name)).findAny().orElse(null);
		if (country == null) {
			throw new Exception("Country Does Not Exist");
		}
		return country;
	}

	@Override
	public CountryModel findById(int id) throws Exception {
		CountryModel country = RunningGame.getInstance().getCountries().getList().stream().filter(c -> c.getId() == id)
				.findAny().orElse(null);
		if (country == null) {
			throw new Exception("Country Does Not Exist");
		}
		return country;
	}

	@Override
	public void assignID(CountryModel t) throws Exception {
		Comparator<CountryModel> comparator = Comparator.comparing(CountryModel::getId);
		Optional<CountryModel> optional = RunningGame.getInstance().getCountries().getList().stream().max(comparator); 
		if(optional.isPresent()) {
			t.setId(optional.get().getId() + 1);
		} else {
			t.setId(1);	
		}
	}

	@Override
	public void delete(CountryModel t) {
		RunningGame.getInstance().getCountries().getList().remove(t);
		
	}

}
