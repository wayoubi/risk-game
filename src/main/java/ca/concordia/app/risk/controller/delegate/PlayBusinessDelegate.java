package ca.concordia.app.risk.controller.delegate;

import ca.concordia.app.risk.services.PlayService;

/**
 * PlayBusinessDelegate
 * @author i857625
 *
 */
public class PlayBusinessDelegate {

  /**
   * attack
   * 
   * @param attacker
   * attacker's name
   */
  public void attack(String attacker) {
    PlayService playService = new PlayService();
    playService.attack(attacker);
  }
}
