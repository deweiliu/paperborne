package uk.ac.qub.eeecs.gage.AITest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.AttackHeroAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class AttackHeroAlgorithmTest {
    private AttackHeroAlgorithm algorithm;
    private Hero AI, human;

    @Before
    public void setUp() {
        AI = Mockito.mock(Hero.class);
        human = Mockito.mock(Hero.class);
        Board board = Mockito.mock(Board.class);
        when(board.getOpponentHero()).thenReturn(human);
        when(board.getAIHero()).thenReturn(AI);

        algorithm = new AttackHeroAlgorithm(board);
    }

    @Test
    public void checkActionNumber() {
        assertEquals(PlayerAction.ATTACK_HERO, algorithm.actionNumber());
    }

    @Test
    public void checkLegalResult() {
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
