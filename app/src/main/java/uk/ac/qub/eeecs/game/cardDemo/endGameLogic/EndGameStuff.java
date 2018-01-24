package uk.ac.qub.eeecs.game.cardDemo.endGameLogic;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by 40216004 on 23/01/2018.
 */

public interface EndGameStuff {
    void update(ElapsedTime elapsedTime);

    void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    boolean isFinished();
}
