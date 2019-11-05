package ca.concordia.app.risk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
@ActiveProfiles("test")
class RiskGameBeanConfigTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testMyPromptProvider() {
		//TODO
	}

	@Test
	void testShellHelper() {
		//TODO
	}

	@Test
	void testInputReader() {
		//TODO
	}

	@Test
	void testSimpleSpinner() {
		//TODO
	}

	@Test
	void testGameUtils() {
		//TODO
	}

	@Test
	void testGameBusinessDelegate() {
		//TODO
	}

	@Test
	void testPlayBusinessDelegate() {
		//TODO
	}

	@Test
	void testViewBusinessDelegate() {
		//TODO
	}

	@Test
	void testPresentationService() {
		//TODO
	}

	@Test
	void testGameService() {
		//TODO
	}

	@Test
	void testMapBusinessDelegate() {
		//TODO
	}

	@Test
	void testMapService() {
		//TODO
	}

	@Test
	void testTaskExecutor() {
		//TODO
	}

	@Test
	void testRiskGameDashboardBean() {
		//TODO
	}

}
