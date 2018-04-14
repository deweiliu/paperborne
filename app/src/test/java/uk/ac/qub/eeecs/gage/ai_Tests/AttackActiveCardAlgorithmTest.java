package uk.ac.qub.eeecs.gage.ai_Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.AttackActiveCardAlgorithm;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Dewei Liu 40216004 on 10/03/2018.
 *
 * 5 unit tests in this class
 */
@RunWith(MockitoJUnitRunner.class)
public class AttackActiveCardAlgorithmTest {
    private AttackActiveCardAlgorithm algorithm;
    private Card attacker, attackee;
    private AttackActiveCardAlgorithm.Action action;
    private final int atkatker = 4;
    private final int hpatker = 5;
    private final int atkatkee = 5;
    private final int hpaktee = 3;

    @Before
    public void setUp() {
        //Set up mock board
        Hero human = Mockito.mock(Hero.class);
        Hero AI = Mockito.mock(Hero.class);

        //Create new object with mock board
        algorithm = new AttackActiveCardAlgorithm(human, AI);

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
        assertEquals(AIDecision.ATTACK_ACTIVE_CARD, algorithm.actionNumber());
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
