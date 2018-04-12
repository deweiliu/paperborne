package uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public interface BasicEndGameStuff {

    /**
     * Update the object
     *
     * @param elapsedTime Elapsed time information
     */
    void update(ElapsedTime elapsedTime);

    /**
     * Draw the object
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    /**
     *
     * @return if all action of this object has been finished
     */
    boolean isFinished();
}
