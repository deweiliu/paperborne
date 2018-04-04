package uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

/**
 * Created by 40216004 on 23/01/2018.
 */

public interface BasicEndGameStuff {
    void update(ElapsedTime elapsedTime);

    void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    boolean isFinished();
}
