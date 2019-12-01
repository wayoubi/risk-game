package ca.concordia.app.risk.test.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class TestApplicationRunner implements ApplicationRunner {

	private static Logger log = LoggerFactory.getLogger(TestApplicationRunner.class);

	public TestApplicationRunner() {
		log.debug("Test Application Runner started!");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.debug("About to do nothing!");
	}

}