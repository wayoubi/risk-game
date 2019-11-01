package ca.concordia.app.risk.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

public class PlayersView extends JPanel implements Observer {

	private JTable jTable;

	public PlayersView() {

		this.setLayout(new GridLayout(1, 1));

		// Data to be displayed in the JTable
		String[][] data = { { "1", "Wasim", "25%", "NA", "15" }, { "2", "Pinkal", "25%", "NA", "15" },
				{ "3", "Michael", "25%", "NA", "15" }, { "4", "Nasim", "25%", "NA", "15" } };

		// Column Names
		String[] columnNames = { "ID", "Player", "Percentage Controlled", "Contientns", "Total Number of Armies" };

		// Initializing the JTable
		jTable = new JTable(data, columnNames);
		jTable.setBounds(30, 40, 200, 300);

		// adding it to JScrollPane
		JScrollPane sp = new JScrollPane(jTable);

		Border border = BorderFactory.createTitledBorder("Players World View");
		this.setBorder(border);
		this.add(sp);

	}

	public void update(Observable observable, Object object) {
	}
}
