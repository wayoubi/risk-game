package ca.concordia.app.risk;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

/**
 * 
 * @author i857625
 *
 */
@SpringBootApplication
public class RiskGameRunner {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//ConfigurableApplicationContext context = SpringApplication.run(RiskGameRunner.class, args);
		SpringApplication.run(RiskGameRunner.class, args);
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("Command# ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
	}
}