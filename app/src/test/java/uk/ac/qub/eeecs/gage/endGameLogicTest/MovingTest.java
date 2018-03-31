package uk.ac.qub.eeecs.gage.endGameLogicTest;

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

/**
 * Created by 40216004 Dewei Liu on 24/01/2018.
 * Test for Moving.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MovingTest {
    private Moving movingObject;
    @Mock
    private Bitmap bitmap = Mockito.mock(Bitmap.class);

    @Mock
    private GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    private Game mGame = Mockito.mock(Game.class);
    private final static float VALUE = 100;
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

        movingObject = new Moving(VALUE, VALUE, VALUE, VALUE, bitmap, gameScreen);
    }

    @Test
    public void setDestination() throws Exception {
        //Don't set destination
        assertEquals(false, movingObject.start(PERIOD));

        movingObject.setDestination(VALUE * 2, VALUE * 2);
        //Set destination and the moving object can start to play animation
        assertEquals(true, movingObject.start(PERIOD));
    }

    @Test
    public void movingObject_Test() throws Exception {
        movingObject.setDestination(VALUE * 2, VALUE * 2);

        movingObject.start(PERIOD);//Animation period is 4 seconds

        assertEquals(false, isFinished_AfterSleep(900));//Now is 0.9s < 4s and it is not finished

        assertEquals(false, isFinished_AfterSleep(900));//Now is (0.9 + 0.9)s = 1.8s < 4s and it is not finished

        assertEquals(false, isFinished_AfterSleep(900));//Now is (1.8 + 0.9)s = 2.7s < 4s and it is not finished

        assertEquals(false, isFinished_AfterSleep(900));//Now is (2.7 + 0.9)s = 3.6s < 4s and it is not finished

        assertEquals(true, isFinished_AfterSleep(900));//Now is (3.6 + 0.9)s = 4.5s > 4s and it is finished

        //If you run this test with a very very low performance device, it may fail.
    }

    private boolean isFinished_AfterSleep(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        movingObject.update(elapsedTime);
        return movingObject.isFinished();
    }
}
