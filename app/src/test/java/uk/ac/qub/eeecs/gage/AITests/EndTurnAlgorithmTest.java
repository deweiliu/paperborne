package uk.ac.qub.eeecs.gage.AITests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.EndTurnAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
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
