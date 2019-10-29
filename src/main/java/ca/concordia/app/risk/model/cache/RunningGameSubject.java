package ca.concordia.app.risk.model.cache;

import java.util.Observable;

public class RunningGameSubject extends Observable {
	
	public void markAndNotify() {
		this.setChanged();
		this.notifyObservers();
	}

}
