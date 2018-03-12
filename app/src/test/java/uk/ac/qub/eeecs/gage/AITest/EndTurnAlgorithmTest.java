package uk.ac.qub.eeecs.gage.AITest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.EndTurnAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.Board;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)

public class EndTurnAlgorithmTest {
    private EndTurnAlgorithm algorithm;

    @Before
    public void setUp() {
        Board board = Mockito.mock(Board.class);
        algorithm = new EndTurnAlgorithm(board);
    }

    @Test
    public void checkValidity() {
        assertEquals(true, algorithm.isValid());
    }

    @Test
    public void checkActionNumber() {
        assertEquals(PlayerAction.END_TURN, algorithm.actionNumber());
    }
}
