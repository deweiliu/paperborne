package uk.ac.qub.eeecs.gage.endGameLogicTest;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.ui.Rotating;

import static org.junit.Assert.assertEquals;

/**
 * Created by 40216004 on 26/01/2018.
 * Test for Rotating.java
 */

public class RotatingTest {
    private Rotating rotatingObject;
    private final static long CYCLE_PERIOD = 3000;
    private final static int SIZE = 100;
    private final static long ANIMATION_PERIOD = 5000; //5s
    private GameScreen gameScreen;
    @Mock
    private ElapsedTime elapsedTime = Mockito.mock(ElapsedTime.class);
    @Mock
    private Bitmap bitmap = Mockito.mock(Bitmap.class);

    @Mock
    private Game mGame = Mockito.mock(Game.class);

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
        rotatingObject = new Rotating(CYCLE_PERIOD, SIZE, bitmap, gameScreen, ANIMATION_PERIOD);
        rotatingObject.setIsRotate(false);
    }

    @Test
    public void test() {

        rotatingObject.start();
        assertEquals(false, isFinished_AfterSleep(1100));//Now is 1.1s < 5s and it is not finished
        assertEquals(false, isFinished_AfterSleep(1100));//Now is (1.1 + 1.1) = 2.2s < 5s and it is not finished
        assertEquals(false, isFinished_AfterSleep(1100));//Now is (2.2 + 1.1) = 3.3s < 5s and it is not finished
        assertEquals(false, isFinished_AfterSleep(1100));//Now is (3.3 + 1.1) = 4.4s < 5s and it is not finished


        assertEquals(true, isFinished_AfterSleep(1100));//Now is (4.4 + 1.1) = 5.5s < 5s and it is finished
        assertEquals(true, isFinished_AfterSleep(1100));//Now is (5.5 + 1.1) = 6.6s < 6s and it is finished

        //reset the object and its statue of isFinished turns to false
        rotatingObject.reset();
        assertEquals(false, rotatingObject.isFinished());

        //If you run this test with a very very low performance device, it may fail.

    }

    private boolean isFinished_AfterSleep(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rotatingObject.update(this.elapsedTime);
        return rotatingObject.isFinished();
    }
}