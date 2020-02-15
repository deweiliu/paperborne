package uk.ac.qub.eeecs.gage.ui_Tests;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.ui.Rotating;

import static org.junit.Assert.assertEquals;

/**
 * Created by 40216004 Dewei Liu on 26/01/2018.
 * Test for Rotating.java
 * <p>
 * 4 unit tests in this class
 */

public class RotatingTest {
    private Rotating object;
    private final static long SPEED = 3000;
    private final static int SIZE = 100;
    private final static long ANIMATION_PERIOD = 4000; //4s
    @Mock
    private ElapsedTime elapsedTime;

    @Before
    public void setUp() {
        elapsedTime = Mockito.mock(ElapsedTime.class);
        GameScreen gameScreen = new GameScreen("Hello", Mockito.mock(Game.class)) {
            @Override
            public void update(ElapsedTime elapsedTime) {
            }

            @Override
            public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
            }
        };
        object = new Rotating(SPEED, SIZE, Mockito.mock(Bitmap.class), gameScreen, ANIMATION_PERIOD);
        object.setIsRotate(false);
    }

    @Test
    public void rotatingObject_Test1NotFinished() {
        object.start();
        sleep(ANIMATION_PERIOD / 2, object);
        assertEquals(false, object.isFinished());
    }

    @Test
    public void rotatingObject_Test2NotFinished() {
        object.start();
        sleep(ANIMATION_PERIOD / 4 * 3, object);
        assertEquals(false, object.isFinished());
    }

    @Test
    public void rotatingObject_Test3Finished() {
        object.start();
        sleep(ANIMATION_PERIOD + 100, object);
        assertEquals(true, object.isFinished());
    }

    @Test
    public void rotatingObject_FinalTestFinished() {
        long startTime = System.currentTimeMillis();
        object.start();
        Random random = new Random();
        while (true) {
            if (System.currentTimeMillis() < startTime + ANIMATION_PERIOD) {
                assertEquals(false, object.isFinished());
                sleep(random.nextInt((int) ANIMATION_PERIOD / 3), object);
            } else {
                assertEquals(true, object.isFinished());
                break;
            }
        }
    }

    private void sleep(long millis, Rotating rotatingObject) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rotatingObject.update(this.elapsedTime);
    }
}