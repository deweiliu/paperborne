package uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver;

import android.graphics.Bitmap;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.interface_superclass.GameOverSuperclass;
import uk.ac.qub.eeecs.game.ui.Moving;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public class SinglePlayerGameOver extends GameOverSuperclass {
    private final static float SCALE = 0.5f;

    public SinglePlayerGameOver(EndGameScreen screen, Bitmap bitmap) {
        super(screen);

        LayerViewport layerViewport = mScreen.getLayerViewport();
        float pictureHeight = layerViewport.getHeight() * SCALE;
        float pictureWidth = layerViewport.getWidth() * SCALE;

        //Set up the player's animation (Game result)
        Moving animation = new Moving(0, layerViewport.getBottom() - pictureHeight / 2,
                pictureWidth, pictureHeight, bitmap, this.mScreen.getGameScreen());
        animation.setDestination(mScreen.getLayerViewport().x, mScreen.getLayerViewport().y);
        animations.add(animation);
    }

}
