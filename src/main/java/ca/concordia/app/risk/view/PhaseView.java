package ca.concordia.app.risk.view;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class PhaseView extends JPanel implements Observer {

	private JLabel currentPhaseLabel;
	private JLabel currentPlayerLabel;
	private JLabel currentActionLabel;

	private JTextField currentPhaseTextField;
	private JTextField currentPlayerTextField;
	private JTextField currentActionTextField;

	public PhaseView() {

		Border border = BorderFactory.createTitledBorder("Phase View");
		this.setBorder(border);
		// this.setLayout(new GridLayout(3,2));

		currentPhaseLabel = new JLabel("Current Phase");
		currentPhaseTextField = new JTextField();
		currentPhaseTextField.setEditable(false);
		currentPhaseTextField.setPreferredSize(new Dimension(150, 20));
		this.add(currentPhaseLabel);
		this.add(currentPhaseTextField);

		currentPlayerLabel = new JLabel("Current Player");
		currentPlayerTextField = new JTextField();
		currentPlayerTextField.setEditable(false);
		currentPlayerTextField.setPreferredSize(new Dimension(150, 20));
		this.add(currentPlayerLabel);
		this.add(currentPlayerTextField);

		currentActionLabel = new JLabel("Current Action");
		currentActionTextField = new JTextField();
		currentActionTextField.setEditable(false);
		currentActionTextField.setPreferredSize(new Dimension(150, 20));
		this.add(currentActionLabel);
		this.add(currentActionTextField);
	}

	public void update(Observable observable, Object object) {
	}
}
