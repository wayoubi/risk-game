package ca.concordia.app.risk.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import ca.concordia.app.risk.model.cache.Player;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class CardExchangeView extends JPanel implements Observer {

	JTabbedPane tabbedPane;

	/**
	 * 
	 */
	public CardExchangeView() {	
		this.setLayout(new GridLayout(1, 1));
		tabbedPane = new JTabbedPane();
		Border border = BorderFactory.createTitledBorder("Cards View");
		this.setBorder(border);
		this.add(tabbedPane);
		
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if(player!=null) {
			PlayerModel playerModel = player.getPlayerModel();
			if(playerModel!=null) {
				JPanel jPanel = new JPanel();
				this.tabbedPane.add(jPanel, playerModel.getName());
			}
		}	
	}

	/**
	 *
	 */
	public void update(Observable observable, Object object) {
		this.tabbedPane.removeAll();
		
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if(player!=null) {
			PlayerModel playerModel = player.getPlayerModel();
			if(playerModel!=null) {
				JPanel jPanel = new JPanel();
				this.tabbedPane.add(jPanel, playerModel.getName());
			}
		}	
	}

}
