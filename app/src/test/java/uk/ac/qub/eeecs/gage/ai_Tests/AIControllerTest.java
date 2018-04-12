package uk.ac.qub.eeecs.gage.ai_Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIController;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;

/**
 * Created by 40216004 Dewei Liu on 16/02/2018.
 *
 * 2 unit tests in this class
 * <p>
 * Although there may are some exceptions occurred in this rotatingObject_Test1NotFinished, but the rotatingObject_Test1NotFinished can pass successfully
 * because the AI mController runs in another thread and uses many features of the mock object of board which may occur NullPointerExceptions
 */
@RunWith(MockitoJUnitRunner.class)
public class AIControllerTest {
    private AIController controller;
    private final static long THINKING_TIME_LIMIT = 5000; //5s

    @Before
    public void setUp() {
        Hero human = Mockito.mock(Hero.class);
        Hero AI = Mockito.mock(Hero.class);
        controller = new AIController(human, AI);
    }

    @Test
    public void waitUntilAIFinishes() {
        boolean exception;
        long startTime = System.currentTimeMillis();
        controller.startThinking(THINKING_TIME_LIMIT);

        //The algorithm just started, and it should not be finished so fast
        assertEquals(false, controller.isFinished());
        exception = false;
        try {
            //The algorithm doesn't finish so far, and we cannot call the getDecision() function or it throws exception
            controller.getDecision();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);

        /************************************************************************************************/

        //Wait until the time limit to pass
        while (System.currentTimeMillis() < startTime + THINKING_TIME_LIMIT) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //now, the time limit has passed, and it should be finished
        assertEquals(true, controller.isFinished());

        exception = false;
        try {
            //The algorithm does finish now, and we can call the getDecision() function or it won't throw exception
            controller.getDecision();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(false, exception);
    }

    @Test
    public void notifyOverTime_ToStopAI() {
        boolean exception;
        controller.startThinking(THINKING_TIME_LIMIT);

        //The algorithm just started, and it should not be finished so fast
        assertEquals(false, controller.isFinished());
        exception = false;
        try {
            //The algorithm doesn't finish so far, and we cannot call the getDecision() function or it throws exception
            controller.getDecision();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);

        /************************************************************************************************/

        //Tell the AI mController that deadline has passed
        controller.notifyOverTime();

        //now, the time limit has passed, and it should be finished
        assertEquals(true, controller.isFinished());

        exception = false;
        try {
            //The algorithm does finish now, and we can call the getDecision() function or it won't throw exception
            controller.getDecision();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(false, exception);
    }
}
