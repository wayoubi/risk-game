package ca.concordia.app.risk.model.cache;

import java.util.Observable;

/**
 * 
 * This class is used to implement the Risk Game Observable object
 * it notifies views (observers) when state is changed
 * 
 * @author Wasim Alayoubi
 *
 */
public class RunningGameSubject extends Observable {
	
	/**
	 * This method notify the observers that on Running Game state change
	 */
	public void markAndNotify() {
		this.setChanged();
		this.notifyObservers();
	}

}
