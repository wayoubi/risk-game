package ca.concordia.app.risk;

import org.springframework.core.task.TaskExecutor;

import ca.concordia.app.risk.view.RiskGameDashboard;

/**
 * RiskGameDashboardBean created necessary Spring Beans
 * 
 * @author i857625
 */
public class RiskGameDashboardBean {

	/**
	 * This method creates RiskGameDashboard bean.
	 * 
	 * @param taskExecutor runnable code to execute
	 */
	public RiskGameDashboardBean(TaskExecutor taskExecutor) {
		taskExecutor.execute(new RiskGameDashboard());
	}

}
