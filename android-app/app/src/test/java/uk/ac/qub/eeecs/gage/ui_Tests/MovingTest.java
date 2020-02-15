package uk.ac.qub.eeecs.gage.ui_Tests;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.ui.Moving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by 40216004 Dewei Liu on 24/01/2018.
 * Test for Moving.java
 * <p>
 * 8 unit tests in this class
 */
@RunWith(MockitoJUnitRunner.class)
public class MovingTest {
    private Moving object;
    @Mock
    private Bitmap bitmap = Mockito.mock(Bitmap.class);

    @Mock
    private GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    private Game mGame = Mockito.mock(Game.class);
    private final static float VALUE = 100.f;
    private final static long PERIOD = 4000;//4s
    @Mock
    private ElapsedTime elapsedTime = Mockito.mock(ElapsedTime.class);


    @Before
    public void setUp() {
        gameScreen = new GameScreen("Hello", mGame) {
            @Override
            public void update(ElapsedTime elapsedTime) {
            }

            @Override
            public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
            }
        };

        object = new Moving(VALUE, VALUE, VALUE, VALUE, bitmap, gameScreen);
    }

    /******************************************************************************************************/
    //Tests for  boolean start(long movingTimeInMilliSecond)
    @Test
    public void setDestination_TestSuccess() throws Exception {
        object.setDestination(VALUE * 2, VALUE * 2);
        //Set destination and the moving object can start to play animation
        assertEquals(true, object.start(PERIOD));
    }

    @Test
    public void noDestination_TestException() {
        boolean exception = false;
        try {
            object.start(PERIOD);
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertNotEquals(false, exception);
    }

    /******************************************************************************************************/
    //Tests for boolean resume();
    @Test
    public void notStarted_TestExceptionFail() {
        boolean exception = false;
        object.setDestination(VALUE * 2, VALUE * 2);
        try {
            object.resume();
        } catch (IllegalStateException e) {
            exception = true;
        }

        //Exception occurred
        assertNotEquals(false, exception);
    }

    @Test
    public void started_TestExceptionSuccess() {
        boolean exception = false;
        object.setDestination(VALUE * 2, VALUE * 2);
        object.start(PERIOD);
        try {
            object.resume();
        } catch (IllegalStateException e) {
            exception = true;
        }

        //No exception
        assertEquals(false, exception);
    }


    /******************************************************************************************************/
    //The below tests are for testing finish
    @Test
    public void movingObject_Test1NotFinished() throws Exception {
        object.setDestination(VALUE * 2, VALUE * 2);
        object.start(PERIOD);//Animation period is 4 seconds
        sleep(PERIOD / 2);
        assertEquals(false, object.isFinished());
    }

    @Test
    public void movingObject_Test2NotFinished() throws Exception {
        object.setDestination(VALUE * 2, VALUE * 2);
        object.start(PERIOD);//Animation period is 4 seconds
        sleep(PERIOD / 4 * 3);
        assertEquals(false, object.isFinished());
    }

    @Test
    public void movingObject_Test3Finished() throws Exception {
        object.setDestination(VALUE * 2, VALUE * 2);
        object.start(PERIOD);//Animation period is 4 seconds
        sleep(PERIOD + 100);
        assertEquals(true, object.isFinished());
    }

    @Test
    public void movingObject_FinalTest() throws Exception {
        object.setDestination(VALUE * 2, VALUE * 2);
        object.start(PERIOD);
        long startTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - PERIOD < startTime) {
                assertEquals(false, object.isFinished());
                sleep(900);
            } else {
                object.update(elapsedTime);
                assertEquals(true, object.isFinished());
                break;
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        object.update(elapsedTime);
    }
}
