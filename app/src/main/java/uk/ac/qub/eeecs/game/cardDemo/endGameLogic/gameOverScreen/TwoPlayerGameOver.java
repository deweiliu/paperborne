package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.gameOverScreen;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.animationsOfGameObject.MovingAnimation;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public class TwoPlayerGameOver implements GameOverAnimation {
    private boolean isFinished = false;
    private Game mGame;
    private MovingAnimation player1, player2;
    private EndGameScreen mScreen;
    private final static float SCALE = 0.25f;


    public TwoPlayerGameOver(EndGameScreen mScreen, Bitmap player1Animation, Bitmap player2Animation) {
        this.mScreen = mScreen;
        this.mGame = this.mScreen.getGame();
        player2Animation = RotateBitmap(player2Animation, 180);
        LayerViewport layerViewport = mScreen.getLayerViewport();
        float pictureWidth = layerViewport.getWidth() * SCALE;
        float pictureHeight = layerViewport.getHeight() * SCALE;
        this.player1 = new MovingAnimation(0, layerViewport.getBottom() - pictureHeight / 2,
                pictureWidth, pictureHeight, player1Animation, this.mScreen.getGameScreen());
        this.player2 = new MovingAnimation(0, layerViewport.getTop() + pictureHeight / 2,
                pictureWidth, pictureHeight, player2Animation, this.mScreen.getGameScreen());

        this.player1.setDestination(mScreen.getLayerViewport().x, mScreen.getLayerViewport().y - layerViewport.halfHeight / 2);
        this.player2.setDestination(mScreen.getLayerViewport().x, mScreen.getLayerViewport().y + layerViewport.halfHeight / 2);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        this.player1.update(elapsedTime);
        this.player2.update(elapsedTime);
        this.isFinished = this.player1.isFinished() && this.player2.isFinished();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        this.player1.draw(elapsedTime, graphics2D, mScreen.getLayerViewport(), mScreen.getScreenViewPort());

        this.player2.draw(elapsedTime, graphics2D, mScreen.getLayerViewport(), mScreen.getScreenViewPort());
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }


    @Override
    public boolean start(long movingTimeInMillis) {
        boolean p1 = this.player1.start(movingTimeInMillis);
        boolean p2 = this.player2.start(movingTimeInMillis);
        return p1 & p2;
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
