package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * 
 * @author i857625
 *
 */
public class StrategyFactory {
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public static Strategy createStrategy(PlayerModel playerModel) {
		String criteria = playerModel.getStrategy();
		if ("".contentEquals(criteria) || "HUMAN".equals(criteria)) {
			return new HumanStrategy(playerModel);
		} else if ("AGGRESSIVE".equals(criteria)) {
			return new AggressiveStrategy(playerModel);
		} else if ("BENEVOLENT".equals(criteria)) {
			return new BenevolentStrategy(playerModel);
		} else if ("RANDOM".equals(criteria)) {
			return new RandomStrategy(playerModel);
		} else if ("CHEATER".equals(criteria)) {
			return new CheaterStrategy(playerModel);
		}
		
		return null;
	}
}
