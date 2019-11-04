package ca.concordia.app.risk.view;

import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.ContinentModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * Show player view as panel
 */
public class PlayersView extends JPanel implements Observer {

	private JTable jTable;
	private final String[] COLUMN_NAMES = { "ID", "Player", "Percentage Controlled", "Continents", "Total Number of Armies" };

	/**
	 * Creates players view and sets styles
	 */
	public PlayersView() {
		this.setLayout(new GridLayout(1, 1));
		Border border = BorderFactory.createTitledBorder("Players World View");
		this.setBorder(border);
		build();
	}

    /**
     * This method is called whenever the observed object is changed. 
     * <br>An application calls an Observable object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param   observable     the observable object.
     * @param   object   an argument passed to the <code>notifyObservers</code>
     *                 method.
     */
	public void update(Observable observable, Object object) {
		this.removeAll();
		build();
	}
	
	/**
	 * Builds players view
	 */
	private void build() {
		List<PlayerModel> players = RunningGame.getInstance().getPlayers().getList();
		String[][] data = new String[players.size()][5];
		int counter = 0;
		
		for(PlayerModel player: players) {
			String playerContinents = "";
			PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
			List<CountryModel> playerCountries = playerDaoImpl.getCountries(RunningGame.getInstance(), player);
			int totalCountryNumbers = RunningGame.getInstance().getCountries().getList().size();
			int playerCountryOwnsCount = playerCountries.size();
			
			int playerTotalNoOfArmies = 0;
			for(CountryModel playerCountry: playerCountries) {
				playerTotalNoOfArmies += playerCountry.getNumberOfArmies();
			}
			
			//Iterate over continents
			List<ContinentModel> continents = RunningGame.getInstance().getContinents().getList();
			for(ContinentModel continent: continents) {
				ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
				
				List<CountryModel> listOfCountriesInContinent = continentDaoImpl.getCountries(RunningGame.getInstance(), continent);
				int countryOwns = 0;
				for(CountryModel country: listOfCountriesInContinent) {
					if(country.getPlayerId() == player.getId()) {
						countryOwns++;
					}
				}
				
				if(countryOwns == listOfCountriesInContinent.size()) {
					playerContinents += continent.getName() + " ";
				}
				
			}
			if(playerContinents.equals("")) {
				playerContinents = "NA";
			}

			String playerId = Integer.toString(player.getId());
			String playerName = player.getName();
			String playerPercentageControl = ((playerCountryOwnsCount*100)/totalCountryNumbers) + "%";
			data[counter][0] = playerId;
			data[counter][1] = playerName;
			data[counter][2] = playerPercentageControl;
			data[counter][3] = playerContinents;
			data[counter][4] = Integer.toString(playerTotalNoOfArmies);
			counter++;
		
		}

		jTable = new JTable(data, COLUMN_NAMES);
		jTable.setBounds(30, 40, 200, 300);

		JScrollPane sp = new JScrollPane(jTable);
		this.add(sp);
		this.validate();
	}
}
