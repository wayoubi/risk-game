package ca.concordia.app.risk.utility;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.shell.SimpleSpinner;

/**
 * Has all Game helper methods like rolling dice
 * 
 * @author i857625
 *
 */
public class GameUtils {

	/**
	 * Dependency injection from SimpleSpinner
	 */
	@Autowired
	SimpleSpinner simpleSpinner;

	/**
	 * Rolls dices randomly, each time an attack happens in order to specify the winner
	 * @exception InterruptedException Interrupted Exception
	 * @return result
	 */
	public int rollDice() throws InterruptedException {
		int result = 0;
		for (int i = 1; i <= 6; i++) {
			Thread.sleep(100);
			SecureRandom secureRandom = new SecureRandom();
			int randomNumber = secureRandom.nextInt(6);

			simpleSpinner.display(i, "Rolling Dice [" + i + "] + Random [" + randomNumber + "]");
			if (randomNumber == i) {
				result = i;
				break;
			}
			if (i == 6) {
				i = 0;
			}
		}
		simpleSpinner.reset();
		return result;
	}
}
