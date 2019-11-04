package ca.concordia.app.risk.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import ca.concordia.app.risk.model.cache.Player;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CardsModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class CardExchangeView extends JPanel implements Observer {

	/**
	 * 
	 */
	public CardExchangeView() {	
		Border border = BorderFactory.createTitledBorder("Cards View");
		this.setBorder(border);
		this.setLayout(new GridLayout(1, 1));
		build();
	}

	/**
	 *
	 */
	public void update(Observable observable, Object object) {
		this.removeAll();
		build();
		this.repaint();
	}
	
	/**
	 * build the view
	 */
	private void build() {
		JTabbedPane tabbedPane = new JTabbedPane();
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if(player!=null) {
			PlayerModel playerModel = player.getPlayerModel();
			if(playerModel!=null) {
				JPanel jPanel = new JPanel();
				jPanel.setLayout(new BorderLayout());
				JTextArea jTextArea = new JTextArea();
				jPanel.add(jTextArea, BorderLayout.CENTER);
				tabbedPane.add(jPanel, playerModel.getName());
				jTextArea.setEditable(false);
				List<String> cardsList = RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getCards().getList();
				int i=0;
				for(String card : cardsList) {
					jTextArea.append(String.format("Card[%s] - %s %s", ++i, card, System.lineSeparator()));
				}
			}
		}
		this.add(tabbedPane);
	}

}
