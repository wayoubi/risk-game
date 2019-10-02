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
	 * 
	 * @param file
	 * @return
	 */
	@ShellMethod("Show Full Map")
	public void showMap() {
		JGraphXAdapterView.main(null);
	}
}