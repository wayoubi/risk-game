package ca.concordia.app.risk.model.dao;

import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is DAO(Data Access Layer) for player - to access a player's info
 *
 * @author i857625
 *
 */
public class PlayerDaoImpl implements Dao<PlayerModel> {

	/**
	 * This method finds a player by name
	 *
	 * @param gameModel
	 * @param name
	 * @return player object
	 */
	@Override
	public PlayerModel findByName(@NotNull GameModel gameModel, String name){
		return gameModel.getPlayers().getList().stream().filter(p -> p.getName().equals(name)).findAny()
				.orElse(null);
	}

	/**
	 * This method finds a player by id
	 *
	 * @param gameModel
	 * @param id
	 * @return player object
	 */
	@Override
	public PlayerModel findById(@NotNull GameModel gameModel, int id){
		return gameModel.getPlayers().getList().stream().filter(p -> p.getId() == id).findAny()
				.orElse(null);
	}

	/**
	 * This method assigns id to a player
	 *
	 * @param gameModel
	 * @param t
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
	 * This method removes a list of players
	 */
	@Override
	public void delete(@NotNull GameModel gameModel, PlayerModel t) {
		gameModel.getPlayers().getList().remove(t);
	}

	/**
	 * @param gameModel
	 * @param t
	 * @return list of countries of the current player
	 */
	public List<CountryModel> getCountries(@NotNull GameModel gameModel, PlayerModel t) {
		return gameModel.getCountries().getList().stream().filter(c -> t.getId() == c.getPlayerId())
				.collect(Collectors.toList());

	}
}