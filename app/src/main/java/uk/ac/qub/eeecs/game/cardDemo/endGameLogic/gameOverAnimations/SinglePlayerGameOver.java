package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.gameOverAnimations;

import android.graphics.Bitmap;
import android.util.Log;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public class SinglePlayerGameOver implements GameOverAnimation {
    private boolean isFinished = false;
    private Game mGame;
    private MovingObject animation;
    private EndGameScreen mScreen;
    private final static float SCALE = 0.5f;

    public SinglePlayerGameOver(EndGameScreen mScreen, Bitmap winAnimation) {
        this.mScreen = mScreen;
        this.mGame = this.mScreen.getGame();
        LayerViewport layerViewport = mScreen.getLayerViewport();
        float pictureHeight = layerViewport.getHeight() * SCALE;
        float pictureWidth = layerViewport.getWidth() * SCALE;

        this.animation = new MovingObject(0, layerViewport.getBottom() - pictureHeight / 2,
                pictureWidth, pictureHeight, winAnimation, this.mScreen.getGemeScreen());

        this.animation.setDestination(mScreen.getLayerViewport().x, mScreen.getLayerViewport().y);

    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        this.animation.update(elapsedTime);
        this.isFinished = this.animation.isFinished();
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        this.animation.draw(elapsedTime, graphics2D, mScreen.getLayerViewport(), mScreen.getScreenViewPort());
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }


    @Override
    public boolean start(long movingTimeInMillis) {
        Log.d("Single player game over", ".start(long) called.");
        return this.animation.start(movingTimeInMillis);
    }
}
