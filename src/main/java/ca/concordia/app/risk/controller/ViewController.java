package ca.concordia.app.risk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ca.concordia.app.risk.view.JGraphXAdapterView;

/**
 * ViewController has the commands that shows the details of the game
 * 
 * @author i857625
 */
@ShellComponent
public class ViewController {

	/**
	 * Class Logger
	 */
	private static Logger log = LoggerFactory.getLogger(ViewController.class);

	/**
	 * This method shows map of the world (Graph) using Applet
	 */
	@ShellMethod("Show Full Map")
	public void showmap() {
		if (log.isDebugEnabled()) {
			log.debug("inside showmap");
		}

		JGraphXAdapterView.main(null);
	}
}