package uk.ac.qub.eeecs.gage.AITest;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AIAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.Board;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class AIAlgorithmTest {
    private Board board;
    private AIAlgorithm algorithm;

    @Before
    public void setUp() {
        Board board = Mockito.mock(Board.class);
        this.board = board;
    }

    @Test
    public void algorithmTest() {
        algorithm = new AIAlgorithm(board);
        algorithm.start();
        assertEquals(false, algorithm.isFinished());
        algorithm.notifyOverTime();
        assertEquals(true, algorithm.isFinished());
        PlayerAction action = algorithm.getAction();
        assertEquals(PlayerAction.END_TURN, action.getAction());
    }
}
