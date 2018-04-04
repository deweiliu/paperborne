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
    private long timeMark;
    private boolean isFinished;
    private Matrix matrix;
    private boolean isStarted = false;
    private long timePast;
    private int sourceWidth, sourceHeight;
    private long rotatingTimePerCycle;
    private Bitmap bitmap;
    private float ratioForWidth, ratioForHeight;
    private boolean isRotate;

    public Rotating(long rotatingTimeInMillisPerCycle, float size, Bitmap bitmap, GameScreen gameScreen, long animationPeriod) {
        super(gameScreen.getGame().getScreenWidth() / 2, gameScreen.getGame().getScreenHeight() / 2, size, size, bitmap, gameScreen);

        this.bitmap = bitmap;
        sourceWidth = bitmap.getWidth();
        sourceHeight = bitmap.getHeight();
        PERIOD = animationPeriod;
        isFinished = false;
        this.rotatingTimePerCycle = rotatingTimeInMillisPerCycle;
        ratioForWidth = size / bitmap.getWidth();
        ratioForHeight = size / bitmap.getHeight();
        matrix = new Matrix();
        isRotate = true;
    }

    public void reset() {
        isStarted = false;
        isFinished = false;
        super.mBitmap = bitmap;
        super.mBound.halfWidth = (ratioForWidth * super.mBitmap.getWidth() / 2);
        super.mBound.halfHeight = (ratioForHeight * super.mBitmap.getHeight() / 2);
    }

    public void start() {
        isStarted = true;
        timeMark = System.currentTimeMillis();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (isStarted == false) {
            this.start();
        } else {
            timePast = System.currentTimeMillis() - timeMark;
            if (timePast > PERIOD) {
                this.isFinished = true;
            } else {
                if (isRotate) {
                    int rotatingDegrees = ((int) (((double) timePast / (double) rotatingTimePerCycle)
                            * 360)) % 360;
                    matrix.reset();
                    matrix.setRotate(rotatingDegrees);
                    Bitmap rotated = Bitmap.createBitmap(this.bitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
                    super.mBound.halfWidth = (ratioForWidth * rotated.getWidth() / 2);
                    super.mBound.halfHeight = (ratioForHeight * rotated.getHeight() / 2);
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
            //The bitmap may be to large to draw.
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
