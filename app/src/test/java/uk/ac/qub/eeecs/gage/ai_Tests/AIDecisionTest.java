package uk.ac.qub.eeecs.gage.ai_Tests;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by 40216004 Dewei Liu on 16/02/2018.
 *
 * 3 unit tests in this class
 */
@RunWith(MockitoJUnitRunner.class)
public class AIDecisionTest {
    private AIDecision endTurn, attackActiveCard;

    private Card source, target;

    private Card temp;

    @Before
    public void setUp() {
        source = Mockito.mock(Card.class);
        target = Mockito.mock(Card.class);
        temp = Mockito.mock(Card.class);
        endTurn = new AIDecision(AIDecision.END_TURN, null, null);
        attackActiveCard = new AIDecision(AIDecision.ATTACK_ACTIVE_CARD, source, target);

    }

    @Test
    public void actionState() {
        assertEquals(AIDecision.END_TURN, endTurn.getAction());
        assertEquals(AIDecision.ATTACK_ACTIVE_CARD, attackActiveCard.getAction());

    }

    @Test
    public void illegalFunction_ThrowException() {
        boolean exception;
        /************************************************************************************************/

        exception = false;
        try {

            //Cannot take this function unless the action state is ATTACK_HERO
            endTurn.getAttackerCard();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);
        /************************************************************************************************/


        exception = false;
        try {
            //Cannot take this function unless the action state is ATTACK_ACTIVE_CARD
            endTurn.getSourceCard();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);

        /************************************************************************************************/

        exception = false;
        try {

            //Cannot take this function unless the action state is ATTACK_HERO
            attackActiveCard.getAttackerCard();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);

        /************************************************************************************************/

        exception = false;
        try {
            //Cannot take this function unless the action state is PLAY_CARD
            attackActiveCard.getCardPlayed();
        } catch (IllegalStateException e) {
            exception = true;
        }
        assertEquals(true, exception);


    }

    @Test
    public void legalFunction_GetCorrectCord() {
        assertEquals(source, attackActiveCard.getSourceCard());
        assertEquals(target, attackActiveCard.getTargetCard());

        assertNotEquals(temp, attackActiveCard.getSourceCard());
        assertNotEquals(source, attackActiveCard.getTargetCard());
    }


}
