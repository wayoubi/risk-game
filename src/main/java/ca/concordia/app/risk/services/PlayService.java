package ca.concordia.app.risk.services;

import org.springframework.beans.factory.annotation.Autowired;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.shell.ShellHelper;
import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;

/**
 * 
 * @author i857625
 *
 */
public class PlayService {

  @Autowired
  ShellHelper shellHelper;

  /**
   * 
   * @param attacker
   * @throws Exception
   */
  public void attack(String attacker) {
    PlayerDaoImpl playerDao = new PlayerDaoImpl();
    PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), attacker);
    playerModel.setColor("White");
  }
}
