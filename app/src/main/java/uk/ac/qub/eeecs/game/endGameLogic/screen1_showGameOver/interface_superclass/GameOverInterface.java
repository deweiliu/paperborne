package uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.interface_superclass;

import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.BasicEndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public interface GameOverInterface extends BasicEndGameStuff {

    /**
     *
     * @param period The time for moving in millisecond
     * @return true if this action successes
     */
    boolean start(long period);
}
