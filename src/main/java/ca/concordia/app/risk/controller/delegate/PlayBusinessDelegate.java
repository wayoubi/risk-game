package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.PlayService;

/**
 * 
 * @author i857625
 *
 */
public class PlayBusinessDelegate {

  /**
   * 
   * @param attacker
   * @throws Exception
   */
  public void attack(String attacker) throws Exception {
    PlayService playService = new PlayService();
    playService.attack(attacker);
  }

  public void fortify(String fromCountry, String toCountry, int noOfArmies) {
    PlayService playService = new PlayService();
    playService.fortify(fromCountry, toCountry, noOfArmies);
  }
}
