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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import ca.concordia.app.risk.model.cache.Player;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

public class PhaseView extends JPanel implements Observer {

	private JLabel currentPhaseLabel;
	private JLabel currentPlayerLabel;
	private JLabel currentActionLabel;

	private JTextField currentPhaseTextField;
	private JTextField currentPlayerTextField;
	private JTextArea currentActionTextArea;
	
	private String cachedStr;

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
		//currentActionLabel = new JLabel("Current Action");
		currentActionTextArea = new JTextArea();
		currentActionTextArea.setEditable(false);
		//this.add(currentActionLabel);
		this.add(currentActionTextArea, BorderLayout.CENTER);		

		
		
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if(player!=null) {
			PlayerModel playerModel = player.getPlayerModel();
			if(playerModel!=null) {
				currentPlayerTextField.setText(playerModel.getName());
			}
		}
		
		currentActionTextArea.append("Current Actions.....");
		currentActionTextArea.append(System.lineSeparator());
		
	}

	/**
	 * 
	 */
	public void update(Observable observable, Object object) {
		Player player = RunningGame.getInstance().getCurrentPlayer();
		if(player!=null) {
			PlayerModel playerModel = player.getPlayerModel();
			if(playerModel!=null) {
				currentPlayerTextField.setText(playerModel.getName());
			}
		}
	    try(BufferedReader input = new BufferedReader(new FileReader("spring-shell.log"))) {
	    	String last = "", line;
			while ((line = input.readLine()) != null) { 
			    last = line;
			}
			if(last != null && !last.equals(this.cachedStr)) {
				this.cachedStr = last;
				currentActionTextArea.append(last.substring(last.indexOf(':')+1));
				currentActionTextArea.append(System.lineSeparator());
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
