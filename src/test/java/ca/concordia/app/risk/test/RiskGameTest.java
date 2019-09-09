package ca.concordia.app.risk.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ca.concordia.app.risk.RiskGame;


public class RiskGameTest {
	
	RiskGame riskGame = new RiskGame(); 

	@Test
	public void testGreeting() {
		 assertEquals("Hello World!", riskGame.greet());
	}

}
