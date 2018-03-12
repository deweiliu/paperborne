package uk.ac.qub.eeecs.gage.AITest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.AttackActiveCardAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class AttackActiveCardAlgorithmTest {
    private Hero AI, human;
    private AttackActiveCardAlgorithm algorithm;
    private Card attacker, attackee;
    private AttackActiveCardAlgorithm.Action action;
    int atkatker = 4;
    int hpatker = 5;
    int atkatkee = 5;
    int hpaktee = 3;

    @Before
    public void setUp() {

        // algorithm = Mockito.mock(AttackActiveCardAlgorithm.class);

        //Set up mock board
        human = Mockito.mock(Hero.class);
        AI = Mockito.mock(Hero.class);
        Board board = Mockito.mock(Board.class);
        when(board.getAIHero()).thenReturn(AI);
        when(board.getUserHero()).thenReturn(human);

        //Create new object with mock board
        algorithm = new AttackActiveCardAlgorithm(board);

        //For test 1
        /****************************************************************************************/
        //For test 2


        //Set up mock attacker and attackee
        attacker = Mockito.mock(Card.class);
        attackee = Mockito.mock(Card.class);

        when(attacker.getAttackValue()).thenReturn(atkatker);
        when(attacker.getHealthValue()).thenReturn(hpatker);
        when(attackee.getAttackValue()).thenReturn(atkatkee);
        when(attackee.getHealthValue()).thenReturn(hpaktee);

        //create the object with mock attacker and attackee
        action = algorithm.new Action(attacker, attackee);

    }

    @Test
    public void checkActionNumber() {
        assertEquals(PlayerAction.ATTACK_ACTIVE_CARD, algorithm.actionNumber());

    }


    @Test
    public void checkLegalResult() {
        boolean flag = false;
        if (algorithm.isValid()) {
        } else {
            try {
                algorithm.getAttackee();
            } catch (IllegalStateException e) {
                flag = true;
            }
            assertTrue(flag);
            flag = false;
            try {
                algorithm.getAttacker();
            } catch (IllegalStateException e) {
                flag = true;
            }
            assertTrue(flag);
        }
    }


    //For test 1
    /****************************************************************************************/
    //For test 2

    @Test
    public void setAttackerAndAttackee() {
        assertEquals(attacker, action.getAttacker());
        assertEquals(attackee, action.getAttackee());
    }

    @Test
    public void calculateAttackeeScore() {
        double score, expected;
        int reducedHealth;
        when(attackee.isFinishedMove()).thenReturn(false);

        //Test when the attackee will not die
        reducedHealth = hpaktee - 1;
        score = action.getPlayerCardValue(attackee, reducedHealth);
        expected = (hpaktee - reducedHealth + AttackActiveCardAlgorithm.Action.BONUS_HEALTH_AS_ATTACKEE) * atkatkee;
        assertEquals(expected, score);
        assertNotEquals(expected + 45, score);

        //Test when the attackee will die (the atk exactly equals the hp)
        reducedHealth = hpaktee;
        score = action.getPlayerCardValue(attackee, reducedHealth);
        expected = 0;
        assertEquals(expected, score);
        assertFalse(score > 0);

        //Test when the attackee will die (the atk is greater than the hp)
        reducedHealth = hpaktee + 2;
        score = action.getPlayerCardValue(attackee, reducedHealth);
        expected = 0;
        assertEquals(expected, score);
        assertFalse(score > 0);

    }


    @Test
    public void calculateAttackerScore() {
        double score, expected;

        //The score of the attacker when it is about to attack someone
        when(attacker.isFinishedMove()).thenReturn(false);
        score = action.getMyCardValue(attacker, true);

        expected = (hpatker + AttackActiveCardAlgorithm.Action.BONUS_HEALTH_AFTER_ATTACK) * atkatker;
        assertEquals(expected, score);
        assertNotEquals(expected + 11, score);

        //The score of the attacker before it attack someone
        score = action.getMyCardValue(attacker, false);

        expected = (hpatker + AttackActiveCardAlgorithm.Action.BONUS_HEALTH_BEFORE_ATTACK) * atkatker;
        assertEquals(expected, score);
        assertNotEquals(expected + 1, score);


        //The score of the attacker when it already attacks someone
        when(attacker.isFinishedMove()).thenReturn(false);
        score = action.getMyCardValue(attacker, true);

        expected = (hpatker + AttackActiveCardAlgorithm.Action.BONUS_HEALTH_AFTER_ATTACK) * atkatker;
        assertEquals(expected, score);
        assertNotEquals(expected + 91, score);
    }

}
