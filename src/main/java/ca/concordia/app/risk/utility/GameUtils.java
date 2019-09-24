package ca.concordia.app.risk.utility;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.shell.SimpleSpinner;


public class GameUtils {

	@Autowired
	SimpleSpinner simpleSpinner;

	public int rollDice() throws InterruptedException {
		int result = 0;
		for (int i = 1; i <= 6; i++) {
			Thread.sleep(100);
			Random random = new Random();
			int randomNumber = random.nextInt(6);

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
