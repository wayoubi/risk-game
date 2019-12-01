package ca.concordia.app.risk.test.helpers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import ca.concordia.app.risk.controller.GameController;
import ca.concordia.app.risk.controller.MapController;
import ca.concordia.app.risk.controller.PlayController;
import ca.concordia.app.risk.controller.ViewController;

/**
 * Helper class for Beans Configuration
 */
@TestConfiguration
public class RiskGameTestBeanConfig {

	/**
	 * 
	 * @return MapController
	 */
	@Bean
	public MapController mapController() {
		return new MapController();
	}

	/**
	 * 
	 * @return GameController
	 */
	@Bean
	public GameController gameController() {
		return new GameController();
	}

	/**
	 * 
	 * @return PlayController
	 */
	@Bean
	public PlayController playController() {
		return new PlayController();
	}

	/**
	 * 
	 * @return ViewController
	 */
	@Bean
	public ViewController ViewController() {
		return new ViewController();
	}
}
