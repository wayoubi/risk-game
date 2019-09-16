package ca.concordia.app.risk;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
public class RiskGameRunner {

	public static void main(String[] args) throws Exception {
		//ConfigurableApplicationContext context = SpringApplication.run(RiskGameRunner.class, args);
		SpringApplication.run(RiskGameRunner.class, args);
	}

	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("Command# ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
	}
}