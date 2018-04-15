package uk.ac.qub.eeecs.game.ui;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.BasicEndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public final class Moving extends GameObject implements BasicEndGameStuff {
    private boolean isFinished;
    private boolean isResumed;
    private boolean isStarted;
    private Position destination;
    private Position startingPoint;

    private Position distancePerMillis;

    //The time of the last call of void update(ElapsedTime elapsedTime);
    private long lastTime;

    public void setDestination(float x, float y) {
        if (destination == null) {
            destination = new Position(x, y);
        } else {
            destination.x = x;
            destination.y = y;
        }
    }

    private class Position {
        public Position(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float x;
        public float y;
    }

    /**
     * @param x          starting x location of the object
     * @param y          starting  y location of the object
     * @param width      width of the object
     * @param height     height of the object
     * @param bitmap     Bitmap used to represent this object
     * @param gameScreen Gamescreen to which this object belongs
     */
    public Moving(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, width, height, bitmap, gameScreen);
        initialize(x, y);
    }

    /**
     * @param x          starting x location of the object
     * @param y          starting  y location of the object
     * @param bitmap     Bitmap used to represent this object
     * @param gameScreen Gamescreen to which this object belongs
     */
    public Moving(float x, float y, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, bitmap, gameScreen);
        initialize(x, y);
    }

    private void initialize(float x, float y) {
        startingPoint = new Position(x, y);
        destination = null;
        distancePerMillis = null;
        isStarted = false;
        isFinished = false;
        isResumed = false;
    }

    /**
     * Please do call void setDestination(float x, float y); before calling this function
     *
     * @param period The period for moving in millisecond
     */
    public boolean start(long period) {
        if (destination != null) {
            float x = destination.x - startingPoint.x;
            float y = destination.y - startingPoint.y;
            distancePerMillis = new Position(x / period, y / period);

            super.setPosition(startingPoint.x, startingPoint.y);
            isStarted = true;
            return resume();
        } else {
            throw new IllegalStateException("The moving destination is null, Please set call setDestination(float x, float y); first");
        }
    }

    public void stop() {
        pause();
        isStarted = false;
    }

    /**
     * Please do call  boolean start(long period); before calling this function
     */
    public boolean resume() {
        if (isStarted) {
            this.isFinished = false;
            isResumed = true;
            lastTime = System.currentTimeMillis();
            return true;
        } else {
            throw new IllegalStateException("You have to start the animation before you call boolean resume();");
        }
    }

    public void pause() {
        if (isResumed) {
            isResumed = false;
        }
    }

    /**
     * Please do call  boolean start(long period); OR boolean resume(); before calling this function
     * Update the game object
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        if (isResumed) {
            //Compute the moving distance depending on time
            long currentTime = System.currentTimeMillis();
            float movingX = (currentTime - this.lastTime) * distancePerMillis.x;
            float movingY = (currentTime - this.lastTime) * distancePerMillis.y;

            lastTime = System.currentTimeMillis();
            //Update the new position in GameObject
            super.setPosition(position.x + movingX, position.y + movingY);

            //Check if it has reach the destination
            if (super.position.x > startingPoint.x && super.position.x > destination.x) {
                finish();
            } else if (super.position.x < startingPoint.x && super.position.x < destination.x) {
                finish();
            } else if (super.position.y > startingPoint.y && super.position.y > destination.y) {
                finish();
            } else if (super.position.y < startingPoint.y && super.position.y < destination.y) {
                finish();
            }
        }
        super.update(elapsedTime);
    }

    private void finish() {
        stop();
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
