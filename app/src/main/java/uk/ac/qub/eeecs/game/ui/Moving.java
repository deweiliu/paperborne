package uk.ac.qub.eeecs.game.ui;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 23/01/2018.
 */

public final class Moving extends GameObject implements EndGameStuff {
    private boolean isFinished;
    private boolean isResumed;
    private boolean isStarted;
    private Position destination;
    private Position startingPoint;

    private long period;
    private Position distancePerMillis;
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

    public Moving(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, width, height, bitmap, gameScreen);
        initialize(x, y);
    }

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

    public boolean start(long movingTimeInMillionSecond) {
        if (destination != null) {
            if (startingPoint != null) {
                this.period = movingTimeInMillionSecond;

                float x = destination.x - startingPoint.x;
                float y = destination.y - startingPoint.y;
                distancePerMillis = new Position(x / period, y / period);


                super.setPosition(startingPoint.x, startingPoint.y);
                isStarted = true;
                return resume();
            } else {
                //Log.d("The starting point is null", "Hello");
                return false;

            }
        } else {
            // Log.d("The moving destination is null", "Please set call setDestination(float x, float y); first");
            return false;
        }
    }

    public void stop() {
        pause();
        isStarted = false;

    }

    public boolean resume() {
        if (isStarted == true) {
            this.isFinished = false;
            isResumed = true;
            lastTime = System.currentTimeMillis();
            startTime = lastTime;
            return true;
        }
        return false;
    }

    public void pause() {
        if (isResumed == true) {
            isResumed = false;
        }

    }

    private long startTime;

    @Override
    public void update(ElapsedTime elapsedTime) {

        if (isResumed) {
            long currentTime = System.currentTimeMillis();
            float movingX = (currentTime - this.lastTime) * distancePerMillis.x;
            float movingY = (currentTime - this.lastTime) * distancePerMillis.y;

            lastTime = System.currentTimeMillis();
            float x = super.position.x + movingX;
            float y = super.position.y + movingY;
            super.setPosition(x, y);

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
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
