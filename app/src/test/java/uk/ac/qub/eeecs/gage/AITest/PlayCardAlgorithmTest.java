package uk.ac.qub.eeecs.gage.AITest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.PlayCardAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Hand;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayCardAlgorithmTest {
    private Hero AI, human;
    private PlayCardAlgorithm algorithm;
    @Before
    public void setUp() {

        AI = Mockito.mock(Hero.class);
        human = Mockito.mock(Hero.class);
        Board board = Mockito.mock(Board.class);
        when(board.getOpponentHero()).thenReturn(human);
        when(board.getAIHero()).thenReturn(AI);

        algorithm = new PlayCardAlgorithm(board);
    }

    @Test
    public void manaIsZero() {

        boolean flag = false;
        try {
            algorithm.getPlayedCard();
        } catch (IllegalStateException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void checkActionNumber() {
        assertEquals(PlayerAction.PLAY_CARD, algorithm.actionNumber());
    }
}
