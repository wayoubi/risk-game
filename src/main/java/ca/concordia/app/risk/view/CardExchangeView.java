package ca.concordia.app.risk.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import ca.concordia.app.risk.model.cache.Player;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * Show card exchange view as panel
 */
public class CardExchangeView extends JPanel implements Observer {

	/**
	 * Creates card exchange view and sets styles
	 */
	public CardExchangeView() {
		Border border = BorderFactory.createTitledBorder("Cards View");
		this.setBorder(border);
		this.setLayout(new GridLayout(1, 1));
		build();
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
		build();
		this.repaint();
	}

	/**
	 * Builds the view
	 */
	private void build() {
		JTabbedPane tabbedPane = new JTabbedPane();
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if (player != null) {
			PlayerModel playerModel = player.getPlayerModel();
			if (playerModel != null) {
				JPanel jPanel = new JPanel();
				jPanel.setLayout(new BorderLayout());
				JTextArea jTextArea = new JTextArea();
				jPanel.add(jTextArea, BorderLayout.CENTER);
				tabbedPane.add(jPanel, playerModel.getName());
				jTextArea.setEditable(false);
				List<String> cardsList = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards()
						.getList();
				int i = 0;
				for (String card : cardsList) {
					jTextArea.append(String.format("Card[%s] - %s %s", ++i, card, System.lineSeparator()));
				}
			}
		}
		this.add(tabbedPane);
	}

}
