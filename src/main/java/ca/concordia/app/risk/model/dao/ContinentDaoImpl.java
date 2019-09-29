package ca.concordia.app.risk.model.dao;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;

public class ContinentDaoImpl implements Dao<ContinentModel> {

	/**
	 * 
	 */
	@Override
	public ContinentModel findByName(@NotNull GameModel gameModel, String name) {
		return gameModel.getContinents().getList().stream()
				.filter(c -> c.getName().equals(name)).findAny().orElse(null);
	}

	/**
	 * 
	 */
	@Override
	public ContinentModel findById(@NotNull GameModel gameModel, int id) {
		return gameModel.getContinents().getList().stream().filter(c -> c.getId() == id)
				.findAny().orElse(null);
	}

	/**
	 * 
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
	 * 
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, ContinentModel t) {
		RunningGame.getInstance().getContinents().getList().remove(t);
	}

	/**
	 * 
	 * @param gameModel
	 * @param t
	 * @return
	 */
	public List<CountryModel> getCountries(@NotNull GameModel gameModel, ContinentModel t) {
		return gameModel.getCountries().getList().stream().filter(c -> t.getId() == c.getContinentId())
				.collect(Collectors.toList());

    @Override
    public ContinentModel findByName(@NotNull GameModel gameModel, String name) throws Exception {
        ContinentModel continentModel = gameModel.getContinents().getList().stream()
                .filter(c -> c.getName().equals(name)).findAny().orElse(null);
        if (continentModel == null) {
            throw new Exception("Continent Does Not Exist");
        }
        return continentModel;
    }

    @Override
    public ContinentModel findById(@NotNull GameModel gameModel, int id) throws Exception {
        ContinentModel continentModel = gameModel.getContinents().getList().stream().filter(c -> c.getId() == id)
                .findAny().orElse(null);
        if (continentModel == null) {
            throw new Exception("Continent Does Not Exist");
        }
        return continentModel;
    }

    @Override
    public void assignID(@NotNull GameModel gameModel, ContinentModel t) throws Exception {
        Comparator<ContinentModel> comparator = Comparator.comparing(ContinentModel::getId);
        Optional<ContinentModel> optional = gameModel.getContinents().getList().stream().max(comparator);
        if (optional.isPresent()) {
            t.setId(optional.get().getId() + 1);
        } else {
            t.setId(1);
        }

    }

    @Override
    public void delete(@NotNull GameModel gameModel, ContinentModel t) {
        RunningGame.getInstance().getContinents().getList().remove(t);
    }

    /**
     * @param t
     * @return
     */
    public List<CountryModel> getCountries(@NotNull GameModel gameModel, ContinentModel t) {
        return gameModel.getCountries().getList().stream().filter(c -> t.getId() == c.getContinentId())
                .collect(Collectors.toList());

    }

}
