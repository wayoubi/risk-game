package ca.concordia.app.risk;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
 * For Beans Configuration
 * @author i857625
 *
 */
@Configuration
public class RiskGameBeanConfig {

	/**
	 * 
	 * @return PromptProvider
	 */
	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("Command# ",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
	}

	/**
	 * 
	 * @param terminal
	 * @return ShellHelper
	 */
	@Bean
	public ShellHelper shellHelper(@Lazy Terminal terminal) {
		return new ShellHelper(terminal);
	}

	/**
	 * 
	 * @param lineReader
	 * @return InputReader
	 */
	@Bean
	public InputReader inputReader(@Lazy LineReader lineReader) {
		return new InputReader(lineReader);
	}

	/**
	 * 
	 * @param terminal
	 * @return SimpleSpinner
	 */
	@Bean
	public SimpleSpinner simpleSpinner(@Lazy Terminal terminal) {
		return new SimpleSpinner(terminal);
	}

	/**
	 * 
	 * @return GameUtils
	 */
	@Bean
	public GameUtils gameUtils() {
		return new GameUtils();
	}

	/**
	 * 
	 * @return GameBusinessDelegate
	 */
	@Bean
	public GameBusinessDelegate gameBusinessDelegate() {
		return new GameBusinessDelegate();
	}

	/**
	 * 
	 * @return PlayBusinessDelegate
	 */
	@Bean
	public PlayBusinessDelegate playBusinessDelegate() {
		return new PlayBusinessDelegate();
	}

	/**
	 * 
	 * @return ViewBusinessDelegate
	 */ 
	@Bean
	public ViewBusinessDelegate viewBusinessDelegate() {
		return new ViewBusinessDelegate();
	}

	/**
	 * 
	 * @return PresentationService
	 */
	@Bean
	public PresentationService presentationService() {
		return new PresentationService();
	}

	/**
	 * 
	 * @return GameService
	 */
	@Bean
	public GameService gameService() {
		return new GameService();
	}

	/**
	 * 
	 * @return MapBusinessDelegate
	 */
	@Bean
	public MapBusinessDelegate mapBusinessDelegate() {
		return new MapBusinessDelegate();
	}

	/**
	 * 
	 * @return MapService
	 */
	@Bean
	public MapService mapService() {
		return new MapService();
	}
}