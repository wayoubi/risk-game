package ca.concordia.app.risk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Class to Run the Game
 * @author i857625
 *
 */
@SpringBootApplication
public class RiskGameRunner {

	/**
	 * Runs the Spring application
	 * @param args
	 */
	public static void main(String[] args){
		SpringApplicationBuilder builder = new SpringApplicationBuilder(RiskGameRunner.class);
		builder.headless(false);
		builder.run();
	}
}