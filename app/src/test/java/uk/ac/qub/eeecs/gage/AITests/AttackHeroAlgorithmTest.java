package uk.ac.qub.eeecs.gage.AITests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.AttackHeroAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class AttackHeroAlgorithmTest {
    private AttackHeroAlgorithm algorithm;
    private Hero AI;

    @Before
    public void setUp() {
        AI = Mockito.mock(Hero.class);
        Hero human = Mockito.mock(Hero.class);

        algorithm = new AttackHeroAlgorithm(human, AI);
    }

    @Test
    public void checkActionNumber() {
        assertEquals(AIDecision.ATTACK_HERO, algorithm.actionNumber());
    }

    @Test
    public void checkResultIsLegal() {
        //Check it 5 times to ensure everything goes OK
        for (int i = 0; i < 5; i++) {
            boolean flag = false;
            if (algorithm.isValid()) {
                Card attacker = algorithm.getAttacker();
                for (Card each : AI.getActiveCards()) {
                    if (each.equals(attacker)) {
                        flag = true;
                    }
                }
            } else {
                try {
                    algorithm.getAttacker();
                } catch (IllegalStateException e) {
                    flag = true;
                }
            }
            assertTrue(flag);
        }
    }


}
