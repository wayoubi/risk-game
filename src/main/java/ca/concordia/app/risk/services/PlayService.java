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
  public void attack(String attacker) throws Exception {
    PlayerDaoImpl playerDao = new PlayerDaoImpl();
    PlayerModel playerModel = playerDao.findByName(RunningGame.getInstance(), attacker);
    playerModel.setColor("White");
  }

  /**
   * 
   * @param fromCountry
   * @param toCountry
   * @param numberOfArmies
   */
  public String fortify(String fromCountry, String toCountry, int numberOfArmies) {
    CountryModel fromCountryModel = RunningGame.getInstance().getCountries().getList().stream()
        .filter((c -> (c.getName().equals(fromCountry)))).findAny().orElse(null);

    if (fromCountryModel != null) {
      fromCountryModel.setNumberOfArmies(fromCountryModel.getNumberOfArmies() + numberOfArmies);
    } else {
      throw new RiskGameRuntimeException("From Country does not exist");
    }

    CountryModel toCountryModel = RunningGame.getInstance().getCountries().getList().stream()
        .filter((c -> (c.getName().equals(toCountry)))).findAny().orElse(null);

    if (toCountryModel != null) {
      if (toCountryModel.getNumberOfArmies() < numberOfArmies
          || toCountryModel.getNumberOfArmies() - numberOfArmies < 1) {
        throw new RiskGameRuntimeException("Cannot fortify since the no of armies exceed the allowed limit");
      } else {
        toCountryModel.setNumberOfArmies(toCountryModel.getNumberOfArmies() - numberOfArmies);
      }
    } else {
      throw new RiskGameRuntimeException("To Country does not exist");
    }

    return shellHelper.getSuccessMessage("Fortification Done Successfully");
  }

}
