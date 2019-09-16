package ca.concordia.app.risk.model.dao;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class PlayerDaoImpl implements Dao<PlayerModel>{

	
	@Override
	public PlayerModel findByName(String name) throws Exception {
		PlayerModel player = RunningGame.getInstance().getPlayers().getPlayer().stream()
				.filter(p -> p.getName().equals(name)).findAny().orElse(null);
		if (player == null) {
			throw new Exception("Player Does Not Exist");
		}
		return player;
	}

	@Override
	public void save(PlayerModel t) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PlayerModel t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PlayerModel t) {
		// TODO Auto-generated method stub
		
	}
}