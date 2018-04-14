package uk.ac.qub.eeecs.gage.ai_Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.EndTurnAlgorithm;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 *
 * 2 unit tests in this class
 */
@RunWith(MockitoJUnitRunner.class)

public class EndTurnAlgorithmTest {
    private EndTurnAlgorithm algorithm;

    @Before
    public void setUp() {
        Hero AI = Mockito.mock(Hero.class);
        Hero human = Mockito.mock(Hero.class);
        algorithm = new EndTurnAlgorithm(human, AI);
    }

    @Test
    public void checkValidity() {
        assertEquals(true, algorithm.isValid());
    }

    @Test
    public void checkActionNumber() {
        assertEquals(AIDecision.END_TURN, algorithm.actionNumber());
    }
}
