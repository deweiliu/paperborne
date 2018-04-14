package uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.interface_superclass.GameOverSuperclass;
import uk.ac.qub.eeecs.game.ui.Moving;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public class TwoPlayerGameOver extends GameOverSuperclass {

    private final static float SCALE = 0.25f;

    public TwoPlayerGameOver(EndGameScreen screen, Bitmap player1Animation, Bitmap player2Animation) {
        super(screen);

        LayerViewport layer = screen.getLayerViewport();
        float pictureWidth = layer.getWidth() * SCALE;
        float pictureHeight = layer.getHeight() * SCALE;

        //Set up player 1 animation (Game result)
        Moving player1 = new Moving(0, layer.getBottom() - pictureHeight / 2,
                pictureWidth, pictureHeight, player1Animation, mScreen.getGameScreen());
        player1.setDestination(screen.getLayerViewport().x, screen.getLayerViewport().y - layer.halfHeight / 2);
        animations.add(player1);

        //Set up player 1 animation (Game result)
        player2Animation = RotateBitmap(player2Animation, 180);
        Moving player2 = new Moving(0, layer.getTop() + pictureHeight / 2,
                pictureWidth, pictureHeight, player2Animation, mScreen.getGameScreen());
        player2.setDestination(screen.getLayerViewport().x, screen.getLayerViewport().y + layer.halfHeight / 2);
        animations.add(player2);
    }


    /**
     * Got this function from Stackoverflow on 23 Jan 2018
     * https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
     *
     * @param source
     * @param angle
     * @return
     */
    private static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
