package ca.concordia.app.risk.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import ca.concordia.app.risk.model.cache.Player;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * Show phase view as panel
 */
public class PhaseView extends JPanel implements Observer {

	private JLabel currentPhaseLabel;
	private JLabel currentPlayerLabel;
	private JLabel currentActionLabel;

	private JTextField currentPhaseTextField;
	private JTextField currentPlayerTextField;
	private JTextArea currentActionTextArea;

	private String cachedStr;

	/**
	 * Creates phase graph view and sets styles
	 */
	public PhaseView() {
		Border border = BorderFactory.createTitledBorder("Phase View");
		this.setBorder(border);
		this.setLayout(new BorderLayout());

		JPanel jPanelFields = new JPanel();

		currentPhaseLabel = new JLabel("Current Phase");
		currentPhaseTextField = new JTextField();
		currentPhaseTextField.setEditable(false);
		currentPhaseTextField.setPreferredSize(new Dimension(150, 20));
		jPanelFields.add(currentPhaseLabel);
		jPanelFields.add(currentPhaseTextField);

		currentPlayerLabel = new JLabel("Current Player");
		currentPlayerTextField = new JTextField();
		currentPlayerTextField.setEditable(false);
		currentPlayerTextField.setPreferredSize(new Dimension(150, 20));
		jPanelFields.add(currentPlayerLabel);
		jPanelFields.add(currentPlayerTextField);

		this.add(jPanelFields, BorderLayout.NORTH);

		JPanel jPanelTextArea = new JPanel();
		jPanelTextArea.setLayout(new BorderLayout());
		currentActionTextArea = new JTextArea();
		currentActionTextArea.setEditable(false);
		JScrollPane jScrollPane = new JScrollPane(currentActionTextArea);
		this.add(jScrollPane, BorderLayout.CENTER);

		Player player = RunningGame.getInstance().getCurrentPlayer();
		if (player != null) {
			PlayerModel playerModel = player.getPlayerModel();
			if (playerModel != null) {
				currentPlayerTextField.setText(playerModel.getName());
				currentPhaseTextField.setText(playerModel.getPlayingPhase());
			}
		}

		currentActionTextArea.append("Current Actions.....");
		currentActionTextArea.append(System.lineSeparator());
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
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if (player != null) {
			PlayerModel playerModel = player.getPlayerModel();
			if (playerModel != null) {
				currentPlayerTextField.setText(playerModel.getName());
				currentPhaseTextField.setText(playerModel.getPlayingPhase());
			}
		}
		try (BufferedReader input = new BufferedReader(new FileReader("spring-shell.log"))) {
			String last = "", line;
			while ((line = input.readLine()) != null) {
				last = line;
			}
			if (last != null && !last.equals(this.cachedStr)) {
				this.cachedStr = last;
				currentActionTextArea.append(last.substring(last.indexOf(':') + 1));
				currentActionTextArea.append(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
