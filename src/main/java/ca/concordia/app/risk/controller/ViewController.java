package ca.concordia.app.risk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ca.concordia.app.risk.controller.delegate.ViewBusinessDelegate;

/**
 * 
 * @author i857625
 *
 */
@ShellComponent
public class ViewController {

	@Autowired
	private ViewBusinessDelegate viewBusinessDelegate;

	/**
	 * 
	 */
	@Autowired
	public ViewController() {
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	@ShellMethod("Show Full Map")
	public void showMap() {
		viewBusinessDelegate.showmap();
		//JGraphXAdapterView.main(null);
	}
}