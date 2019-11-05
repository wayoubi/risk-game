package ca.concordia.app.risk;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.shell.jline.PromptProvider;

import ca.concordia.app.risk.controller.delegate.GameBusinessDelegate;
import ca.concordia.app.risk.controller.delegate.MapBusinessDelegate;
import ca.concordia.app.risk.controller.delegate.PlayBusinessDelegate;
import ca.concordia.app.risk.controller.delegate.ViewBusinessDelegate;
import ca.concordia.app.risk.services.GameService;
import ca.concordia.app.risk.services.MapService;
import ca.concordia.app.risk.services.PresentationService;
import ca.concordia.app.risk.shell.InputReader;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.shell.SimpleSpinner;
import ca.concordia.app.risk.utility.GameUtils;

/**
 * RiskGameBeanConfig created necessary Spring Beans
 * 
 * @author i857625
 */
@Configuration
public class RiskGameBeanConfig {

	/**
	 * This method creates PromptProvider bean.
	 * 
	 * @return PromptProvider
	 */
	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("Command# ",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
	}

	/**
	 * This method creates ShellHelper bean.
	 * 
	 * @param terminal terminal
	 * @return ShellHelper
	 */
	@Bean
	public ShellHelper shellHelper(@Lazy Terminal terminal) {
		return new ShellHelper(terminal);
	}

	/**
	 * This method creates InputReader bean.
	 * 
	 * @param lineReader line reader
	 * @return InputReader
	 */
	@Bean
	public InputReader inputReader(@Lazy LineReader lineReader) {
		return new InputReader(lineReader);
	}

	/**
	 * This method creates SimpleSpinner bean.
	 * 
	 * @param terminal terminal
	 * @return SimpleSpinner
	 */
	@Bean
	public SimpleSpinner simpleSpinner(@Lazy Terminal terminal) {
		return new SimpleSpinner(terminal);
	}

	/**
	 * This method creates GameUtils bean.
	 * 
	 * @return GameUtils
	 */
	@Bean
	public GameUtils gameUtils() {
		return new GameUtils();
	}

	/**
	 * This method creates GameBusinessDelegate bean.
	 * 
	 * @return GameBusinessDelegate
	 */
	@Bean
	public GameBusinessDelegate gameBusinessDelegate() {
		return new GameBusinessDelegate();
	}

	/**
	 * This method creates PlayBusinessDelegate bean.
	 * 
	 * @return PlayBusinessDelegate
	 */
	@Bean
	public PlayBusinessDelegate playBusinessDelegate() {
		return new PlayBusinessDelegate();
	}

	/**
	 * This method creates ViewBusinessDelegate bean.
	 * 
	 * @return ViewBusinessDelegate
	 */
	@Bean
	public ViewBusinessDelegate viewBusinessDelegate() {
		return new ViewBusinessDelegate();
	}

	/**
	 * This method creates PresentationService bean.
	 * 
	 * @return PresentationService
	 */
	@Bean
	public PresentationService presentationService() {
		return new PresentationService();
	}

	/**
	 * This method creates GameService bean.
	 * 
	 * @return GameService
	 */
	@Bean
	public GameService gameService() {
		return new GameService();
	}

	/**
	 * This method creates MapBusinessDelegate bean.
	 * 
	 * @return MapBusinessDelegate
	 */
	@Bean
	public MapBusinessDelegate mapBusinessDelegate() {
		return new MapBusinessDelegate();
	}

	/**
	 * This method creates MapService bean.
	 * 
	 * @return MapService
	 */
	@Bean
	public MapService mapService() {
		return new MapService();
	}
	
	/**
	 * This method creates TaskExecutor Bean.
	 * 
	 * @return
	 */
	@Bean
	@Profile("!test")
	public TaskExecutor taskExecutor() {
	    return new SimpleAsyncTaskExecutor();
	}
	
	/**
	 * This method creates RiskGameDashboardBean Bean.
	 * 
	 * @param taskExecutor
	 * @return
	 */
	@Bean
	@Profile("!test")
	public RiskGameDashboardBean riskGameDashboardBean(TaskExecutor taskExecutor) {
	    return new RiskGameDashboardBean(taskExecutor);
	}
}