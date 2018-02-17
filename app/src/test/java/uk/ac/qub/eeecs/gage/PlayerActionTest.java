package uk.ac.qub.eeecs.gage;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by 40216004 Dewei Liu on 16/02/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerActionTest {
    private PlayerAction endTurn, attackActiveCard;

    private Card source, target;

    private Card temp;

    @Before
    public void setUp() {
        source = Mockito.mock(Card.class);
        target = Mockito.mock(Card.class);
        temp = Mockito.mock(Card.class);
        endTurn = new PlayerAction(PlayerAction.END_TURN, null, null);
        attackActiveCard = new PlayerAction(PlayerAction.ATTACK_ACTIVE_CARD, source, target);

    }

    @Test
    public void actionState() {
        assertEquals(PlayerAction.END_TURN, endTurn.getAction());
        assertEquals(PlayerAction.ATTACK_ACTIVE_CARD, attackActiveCard.getAction());

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
