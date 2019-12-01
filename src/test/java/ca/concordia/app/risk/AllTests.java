package ca.concordia.app.risk;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({ RiskGameBeanConfigTest.class, RiskGameDashboardBeanTest.class, RiskGameRunnerTest.class })
public class AllTests {

}
