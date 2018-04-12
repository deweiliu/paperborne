package uk.ac.qub.eeecs.game.ui;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.BasicEndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 25/01/2018.
 * This GameObject is shown at the centre of screen and rotates a certain degree according to time
 * This can be used as a loading animation at the centre of screen
 */

public final class Rotating extends GameObject implements BasicEndGameStuff {
    private long PERIOD;
    private boolean isFinished;
    private boolean isStarted;
    private long rotatingTimePerCycle;
    private Bitmap originalBitmap;
    private float ratioForWidth;
    private float ratioForHeight;

    //The time that  void start(); called
    private long startTime;

    // the object rotates or not
    private boolean isRotate;

    /**
     * @param rotatingTimeInMillisPerCycle The period that the picture rotates one round
     * @param size                         The size of the picture
     * @param bitmap                       The originalBitmap you want to draw
     * @param gameScreen                   The game screen for the game object
     * @param animationPeriod              The time you want to display
     */
    public Rotating(long rotatingTimeInMillisPerCycle, float size, Bitmap bitmap, GameScreen gameScreen, long animationPeriod) {
        super(gameScreen.getGame().getScreenWidth() / 2, gameScreen.getGame().getScreenHeight() / 2, size, size, bitmap, gameScreen);

        this.originalBitmap = bitmap;
        PERIOD = animationPeriod;
        isFinished = false;
        isStarted = false;
        this.rotatingTimePerCycle = rotatingTimeInMillisPerCycle;
        ratioForWidth = size / bitmap.getWidth();
        ratioForHeight = size / bitmap.getHeight();

        isRotate = true;
    }

    public void reset() {
        isStarted = false;
        isFinished = false;
        super.mBitmap = originalBitmap;
        super.mBound.halfWidth = (ratioForWidth * super.mBitmap.getWidth() / 2);
        super.mBound.halfHeight = (ratioForHeight * super.mBitmap.getHeight() / 2);
    }

    public void start() {
        isStarted = true;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (!isStarted) {
            this.start();
        } else {
            long timePassed = System.currentTimeMillis() - startTime;
            if (timePassed > PERIOD) {
                this.isFinished = true;
            } else {

                //if the object rotates, rotate the picture
                if (isRotate) {
                    int degrees = ((int) (((double) timePassed / (double) rotatingTimePerCycle)
                            * 360)) % 360;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degrees);
                    Bitmap rotated = Bitmap.createBitmap(this.originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

                    //because the picture size may be changed after rotation, we need to readjust its bound
                    super.mBound.halfWidth = (ratioForWidth * rotated.getWidth() / 2);
                    super.mBound.halfHeight = (ratioForHeight * rotated.getHeight() / 2);

                    //commit the new picture
                    super.mBitmap = rotated;
                    super.update(elapsedTime);
                }
            }
        }
    }

    public void setIsRotate(boolean isRotate) {
        this.isRotate = isRotate;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        try {
            super.draw(elapsedTime, graphics2D);
        } catch (Exception e) {
            //The originalBitmap may be to large to draw.
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
