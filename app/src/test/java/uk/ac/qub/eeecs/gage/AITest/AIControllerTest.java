package uk.ac.qub.eeecs.gage.AITest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AIController;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.Board;

import static junit.framework.Assert.assertEquals;

/**
 * Created by 40216004 Dewei Liu on 16/02/2018.
 *
 * Although there are some exceptions occur in this test, but the test can pass successfully
 * because the AI controller runs in another thread and uses many features of the mock object of board which may occur NullPointerExceptions
 */
@RunWith(MockitoJUnitRunner.class)
public class AIControllerTest {
    private AIController controller;
    private final static long THINKING_TIME_LIMIT = 5000; //5s

    @Before
    public void setUp() {

        Board board=Mockito.mock(Board.class);
        controller = new AIController(board);
    }


    @Test
    public void waitUnitlAIFinishes() {
        boolean exception;
        long startTime = System.currentTimeMillis();

        controller.startThinking(THINKING_TIME_LIMIT);

        //The algorithm just started, and it should not be finished so fast
        assertEquals(false, controller.isFinished());
        exception = false;
        try {
            //The algorithm doesn't finish so far, and we cannot call the getAction() function or it throws exception
            controller.getAction();
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
            //The algorithm does finish now, and we can call the getAction() function or it won't throw exception
            controller.getAction();
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
            //The algorithm doesn't finish so far, and we cannot call the getAction() function or it throws exception
            controller.getAction();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);

        /************************************************************************************************/

        //Tell the AI controller that deadline has passed
        controller.notifyOverTime();


        //now, the time limit has passed, and it should be finished
        assertEquals(true, controller.isFinished());

        exception = false;
        try {
            //The algorithm does finish now, and we can call the getAction() function or it won't throw exception
            controller.getAction();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(false, exception);
    }


}
