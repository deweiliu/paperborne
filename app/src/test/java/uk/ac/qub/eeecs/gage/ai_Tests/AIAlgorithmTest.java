package uk.ac.qub.eeecs.gage.ai_Tests;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 *
 * 1 unit test in this class
 */
@RunWith(MockitoJUnitRunner.class)
public class AIAlgorithmTest {
    private Hero human, AI;

    @Before
    public void setUp() {
        human = Mockito.mock(Hero.class);
        AI = Mockito.mock(Hero.class);
    }

    @Test
    public void algorithmTest() {
        AIAlgorithm algorithm = new AIAlgorithm(human, AI);
        algorithm.start();
        assertEquals(false, algorithm.isFinished());
        algorithm.notifyOverTime();
        assertEquals(true, algorithm.isFinished());
        AIDecision action = algorithm.getDecision();
        assertEquals(AIDecision.END_TURN, action.getAction());
    }
}
