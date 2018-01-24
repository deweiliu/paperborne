package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.gameOverAnimations;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

interface GameOverAnimation extends EndGameStuff {


    boolean start(long movingTimeInMillis);


}
