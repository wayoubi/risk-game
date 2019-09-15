package ca.concordia.app.risk.model.dao;

import ca.concordia.app.risk.model.beans.PlayerModel;
import ca.concordia.app.risk.model.cache.RunningGame;

public class PlayerDao {

	public PlayerModel findByName(String playerName) throws Exception {

		PlayerModel player = RunningGame.getInstance().getPlayers().getPlayer().stream()
				.filter(p -> p.getName().equals(playerName)).findAny().orElse(null);

		if (player == null) {
			throw new Exception("Player Does Not Exist");
		}
		return player;
	}
}




/*
 * File file = new File("saved/game001.xml"); JAXBContext jaxbContext =
 * JAXBContext.newInstance(GameModel.class); Unmarshaller jaxbUnmarshaller =
 * jaxbContext.createUnmarshaller(); GameModel game = (GameModel)
 * jaxbUnmarshaller.unmarshal(file); PlayerModel player =
 * game.getPlayers().getPlayer().stream().filter(p ->
 * p.getName().equals(playerName)).findAny().orElse(null);
 */
