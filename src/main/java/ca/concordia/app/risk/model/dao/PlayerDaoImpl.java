package ca.concordia.app.risk.model.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * 
 * @author i857625
 *
 */
public class PlayerDaoImpl implements Dao<PlayerModel> {

	/**
	 * 
	 */
	@Override
	public PlayerModel findByName(@NotNull GameModel gameModel, String name){
		return gameModel.getPlayers().getList().stream().filter(p -> p.getName().equals(name)).findAny()
				.orElse(null);
	}

	/**
	 * 
	 */
	@Override
	public PlayerModel findById(@NotNull GameModel gameModel, int id){
		return gameModel.getPlayers().getList().stream().filter(p -> p.getId() == id).findAny()
				.orElse(null);
	}

	/**
	 * 
	 */
	@Override
	public void assignID(@NotNull GameModel gameModel, PlayerModel t) {
		Comparator<PlayerModel> comparator = Comparator.comparing(PlayerModel::getId);
		Optional<PlayerModel> optional = gameModel.getPlayers().getList().stream().max(comparator);
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
	public void delete(@NotNull GameModel gameModel, PlayerModel t) {
		gameModel.getPlayers().getList().remove(t);
	}

	/**
	 * 
	 * @return
	 */
	public List<CountryModel> getCountries(@NotNull GameModel gameModel, PlayerModel t) {
		return gameModel.getCountries().getList().stream().filter(c -> t.getId() == c.getPlayerId())
				.collect(Collectors.toList());

	}
}