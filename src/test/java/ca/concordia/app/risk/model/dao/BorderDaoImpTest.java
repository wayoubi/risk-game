package ca.concordia.app.risk.model.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.concordia.app.risk.RiskGameBeanConfig;
import ca.concordia.app.risk.test.helpers.RiskGameTestBeanConfig;
import ca.concordia.app.risk.test.helpers.TestApplicationRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationRunner.class)
@Import({ RiskGameBeanConfig.class, RiskGameTestBeanConfig.class })
@ActiveProfiles("test")
class BorderDaoImpTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindByName() {
		// TODO
	}

	@Test
	void testFindById() {
		// TODO

	}

	@Test
	void testAssignID() {
		// TODO

	}

	@Test
	void testDelete() {
		// TODO

	}

}
