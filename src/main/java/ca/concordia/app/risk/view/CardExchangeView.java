package ca.concordia.app.risk.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

public class CardExchangeView extends JPanel implements Observer {

	JTabbedPane tabbedPane;

	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPanel jPanel4;

	public CardExchangeView() {

		this.setLayout(new GridLayout(1, 1));

		tabbedPane = new JTabbedPane();

		jPanel1 = new JPanel();
		jPanel2 = new JPanel();
		jPanel3 = new JPanel();
		jPanel4 = new JPanel();

		tabbedPane.add("Wasim", jPanel1);
		tabbedPane.add("Pinkal", jPanel2);
		tabbedPane.add("Michael", jPanel3);
		tabbedPane.add("Nasim", jPanel4);

		Border border = BorderFactory.createTitledBorder("Cards View");
		this.setBorder(border);
		this.add(tabbedPane);
	}

	public void update(Observable observable, Object object) {
	}

}
