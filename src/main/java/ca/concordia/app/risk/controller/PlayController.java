package ca.concordia.app.risk.controller;

import ca.concordia.app.risk.controller.delegate.PlayBusinessDelegate;
import ca.concordia.app.risk.utility.GameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @author i857625
 */
@ShellComponent
public class PlayController {

    /**
     *
     */
    @Autowired
    private GameUtils gameUtils;

    /**
     *
     */
    @Autowired
    private PlayBusinessDelegate playBusinessDelegate;

    /**
     *
     */
    @Autowired
    public PlayController() {
        playBusinessDelegate = new PlayBusinessDelegate();
    }

    /**
     * @param attacker
     * @param defender
     * @param from
     * @param to
     * @return
     */
    @ShellMethod("Attack a player")
    public String attack(@ShellOption(optOut = false) String attacker, @ShellOption(optOut = false) String defender,
                         @ShellOption(optOut = false) String from, @ShellOption(optOut = false) String to) {
        try {
            gameUtils.rollDice();
            playBusinessDelegate.attack(attacker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attacker + " Lose!";
    }

	@ShellMethod("Fortify Phase")
	public String fortify(@ShellOption(optOut = false) String fromcountry, @ShellOption(optOut = false) String tocountry,
						 @ShellOption(optOut = false) int num) {
		try {
			gameUtils.rollDice();
			playBusinessDelegate.attack(attacker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attacker + " Lose!";
	}
}