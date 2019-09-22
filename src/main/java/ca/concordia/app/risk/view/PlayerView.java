package ca.concordia.app.risk.view;

/**
 * 
 * @author i857625
 *
 */
public class PlayerView {

	/**
	 * 
	 * @param details
	 * @return
	 */
	public String format(String details) {
		return "${Ansi.RED} formatted" + details;
	}

}
