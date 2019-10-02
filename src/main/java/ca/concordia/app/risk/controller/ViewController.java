package ca.concordia.app.risk.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ca.concordia.app.risk.view.JGraphXAdapterView;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class ViewController {

	/**
	 * Creates the graph
	 * @param (file)
	 * @return map of the world with details of the countries' owners
	 */
	@ShellMethod("Show Full Map")
	public void showMap() {
		JGraphXAdapterView.main(null);
	}
}