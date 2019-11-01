package ca.concordia.app.risk;

import org.springframework.core.task.TaskExecutor;

import ca.concordia.app.risk.view.RiskGameDashboard;

public class RiskGameDashboardBean {
	
	public RiskGameDashboardBean(TaskExecutor taskExecutor) {
		taskExecutor.execute(new RiskGameDashboard());
	}
	
}
