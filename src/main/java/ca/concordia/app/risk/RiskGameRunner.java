package ca.concordia.app.risk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * RiskGameRunner is the entry class for the Spring boot application
 * 
 * @author i857625
 */
@SpringBootApplication
public class RiskGameRunner {

	/**
	 * This is the entry point method to run the Spring application and the game.
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(RiskGameRunner.class);
		builder.headless(false);
		builder.run();
	}
}