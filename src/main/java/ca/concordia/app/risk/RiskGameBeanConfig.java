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
import ca.concordia.app.risk.controller.delegate.PlayBusinessDelegate;
import ca.concordia.app.risk.controller.delegate.ViewBusinessDelegate;
import ca.concordia.app.risk.services.GameService;
import ca.concordia.app.risk.services.PresentationService;
import ca.concordia.app.risk.shell.InputReader;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.shell.SimpleSpinner;
import ca.concordia.app.risk.utility.GameUtils;

/**
 * 
 * @author i857625
 *
 */
@Configuration
public class RiskGameBeanConfig {
    
	/**
	 * 
	 * @return
	 */
	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("Command# ",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
	}
	
	/**
	 * 	
	 * @param terminal
	 * @return
	 */
    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal) {
            return new ShellHelper(terminal);
    }
    
	/**
	 * 
	 * @param lineReader
	 * @return
	 */
	@Bean
	public InputReader inputReader(@Lazy LineReader lineReader) {
		return new InputReader(lineReader);
	}
	
	/**
	 * 
	 * @param terminal
	 * @return
	 */
	@Bean
	public SimpleSpinner simpleSpinner(@Lazy Terminal terminal) {
	    return new SimpleSpinner(terminal);
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public GameUtils gameUtils() {
		return new GameUtils();
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public GameBusinessDelegate gameBusinessDelegate() {
		return new GameBusinessDelegate();
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public PlayBusinessDelegate playBusinessDelegate() {
		return new PlayBusinessDelegate();
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public ViewBusinessDelegate viewBusinessDelegate() {
		return new ViewBusinessDelegate();
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public PresentationService presentationService() {
		return new PresentationService();
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public GameService gameService() {
		return new GameService();
	}
}