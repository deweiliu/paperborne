package uk.ac.qub.eeecs.gage.ai_Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.PlayCardAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 *
 * 2 unit tests in this class
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayCardAlgorithmTest {
    private PlayCardAlgorithm algorithm;

    @Before
    public void setUp() {
        Hero AI = Mockito.mock(Hero.class);
        Hero human = Mockito.mock(Hero.class);
        algorithm = new PlayCardAlgorithm(human, AI);
    }

    @Test
    public void manaIsZero() {
        boolean flag = false;
        try {
            algorithm.getCardPlayed();
        } catch (IllegalStateException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void checkActionNumber() {
        assertEquals(AIDecision.PLAY_CARD, algorithm.actionNumber());
    }
}
