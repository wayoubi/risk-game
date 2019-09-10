package ca.concordia.app.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Game {

	@Autowired
	public Game() {
	}

	@ShellMethod("Start a new game")
	public String start(@ShellOption(optOut = false) String name) {
		return "Hello " + name;
	}

	@ShellMethod("Save the current game")
	public String save(@ShellOption(optOut = false) String name) {
		for (int i = 0; i < 10; i++) {
			System.out.println("Hello " + name);
		}
		return "completed";
	}
}