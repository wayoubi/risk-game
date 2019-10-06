package ca.concordia.app.risk.test.helpers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import ca.concordia.app.risk.controller.MapController;

@TestConfiguration
public class RiskGameTestBeanConfig {
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public MapController mapController() {
		return new MapController();
	}

}
