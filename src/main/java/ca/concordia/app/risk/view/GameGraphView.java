package ca.concordia.app.risk.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GameGraphView extends JPanel implements Observer {

	/**
	 * 
	 */
	JGraphXAdapterView jGraphXAdapterView;

	/**
	 * 
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
	 * 
	 */
	public void update(Observable observable, Object object) {
		// System.out.println("x");
		this.removeAll();
		jGraphXAdapterView = new JGraphXAdapterView();
		jGraphXAdapterView.init();
		this.add(jGraphXAdapterView);
		this.setVisible(false);
		this.setVisible(true);
	}
}
