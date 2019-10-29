package ca.concordia.app.risk.view;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.concordia.app.risk.model.cache.RunningGame;

public class RiskGameDashboard extends JFrame implements Runnable {

	/**
	 * 
	 */
	PlayersView playersView;

	/**
	 * 
	 */
	CardExchangeView cardExchangeView;

	/**
	 * 
	 */
	PhaseView phaseView;

	GameGraphView gameGraphView = new GameGraphView();

	public RiskGameDashboard() {
		playersView = new PlayersView();
		cardExchangeView = new CardExchangeView();
		phaseView = new PhaseView();
		gameGraphView = new GameGraphView();

		RunningGame.getInstance().getSubject().addObserver(playersView);
		RunningGame.getInstance().getSubject().addObserver(cardExchangeView);
		RunningGame.getInstance().getSubject().addObserver(phaseView);
		RunningGame.getInstance().getSubject().addObserver(gameGraphView);

	}

	public void run() {
		this.setLayout(new GridLayout(1, 2));

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(3, 1));
		jPanel.add(playersView);
		jPanel.add(phaseView);
		jPanel.add(cardExchangeView);

		this.add(jPanel);
		this.add(gameGraphView);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);

	}

}
