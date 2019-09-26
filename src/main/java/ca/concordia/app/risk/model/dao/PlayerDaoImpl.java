package ca.concordia.app.risk.model.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * 
 * @author i857625
 *
 */
public class PlayerDaoImpl implements Dao<PlayerModel> {

	@Override
	public PlayerModel findByName(String name) throws Exception {
		PlayerModel player = RunningGame.getInstance().getPlayers().getList().stream()
				.filter(p -> p.getName().equals(name)).findAny().orElse(null);
		if (player == null) {
			throw new Exception("Player Does Not Exist");
		}
		return player;
	}


	@Override
	public PlayerModel findById(int id) throws Exception {
		PlayerModel player = RunningGame.getInstance().getPlayers().getList().stream().filter(p -> p.getId() == id)
				.findAny().orElse(null);
		if (player == null) {
			throw new Exception("Player Does Not Exist");
		}
		return player;
	}

	@Override
	public void assignID(PlayerModel t) throws Exception {
		Comparator<PlayerModel> comparator = Comparator.comparing(PlayerModel::getId);
		Optional<PlayerModel> optional = RunningGame.getInstance().getPlayers().getList().stream().max(comparator); 
		if(optional.isPresent()) {
			t.setId(optional.get().getId() + 1);
		} else {
			t.setId(1);	
		}
	}


	@Override
	public void delete(PlayerModel t) {
		RunningGame.getInstance().getPlayers().getList().remove(t);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<CountryModel> getCountries(PlayerModel t){
		return RunningGame.getInstance().getCountries().getList().stream().filter(c -> t.getId()==c.getPlayerId()).collect(Collectors.toList());
		
	}
}