package ca.concordia.app.risk.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Show game graph view as panel
 */
public class GameGraphView extends JPanel implements Observer {

	/**
	 * Graph view object
	 */
	JGraphXAdapterView jGraphXAdapterView;

	/**
	 * Creates game graph view and sets styles
	 */
	public GameGraphView() {
		Border border = BorderFactory.createTitledBorder("Game Graph View");
		this.setLayout(new GridLayout(1, 1));
		this.setBorder(border);
		jGraphXAdapterView = new JGraphXAdapterView();
		jGraphXAdapterView.init();
		this.add(jGraphXAdapterView);
		this.setVisible(true);
	}

	/**
	 * This method is called whenever the observed object is changed. <br>
	 * An application calls an Observable object's <code>notifyObservers</code>
	 * method to have all the object's observers notified of the change.
	 *
	 * @param observable the observable object.
	 * @param object     an argument passed to the <code>notifyObservers</code>
	 *                   method.
	 */
	public void update(Observable observable, Object object) {
		this.removeAll();
		jGraphXAdapterView = new JGraphXAdapterView();
		jGraphXAdapterView.init();
		this.add(jGraphXAdapterView);
		this.setVisible(false);
		this.setVisible(true);
	}
}
