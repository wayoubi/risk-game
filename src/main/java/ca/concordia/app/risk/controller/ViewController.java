package ca.concordia.app.risk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static Logger log = LoggerFactory.getLogger(ViewController.class);

	/**
	 * Creates the graph
	 * @param (file)
	 * @return map of the world with details of the countries' owners
	 */
	@ShellMethod("Show Full Map")
	public void showmap() {
		if (log.isDebugEnabled()) {
			log.debug("inside showmap");
		}
		JGraphXAdapterView.main(null);
	}
}