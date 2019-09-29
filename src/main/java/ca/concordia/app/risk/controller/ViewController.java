package ca.concordia.app.risk.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ca.concordia.app.risk.view.JGraphXAdapterView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.stream.Collectors;

/**
 * @author i857625
 */
@ShellComponent
public class ViewController {

	/**
	 * 
	 * @param file
	 * @return
	 */
	@ShellMethod("Show Full Map")
	public void showMap() {
		JGraphXAdapterView.main(null);
	}
}