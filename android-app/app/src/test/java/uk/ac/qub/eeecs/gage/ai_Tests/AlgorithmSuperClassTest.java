package uk.ac.qub.eeecs.gage.ai_Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.cardDemo.Cards.Deck;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Hand;
import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.AlgorithmSuperclass;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by 40216004 Dewei Liu on 09/04/2018.
 * <p>
 * 10 unit tests in this class
 */

@RunWith(MockitoJUnitRunner.class)
public class AlgorithmSuperClassTest extends AlgorithmSuperclass {
    private Hero human;
    private Hero AI;

    public AlgorithmSuperClassTest() {
        this(Mockito.mock(Hero.class), Mockito.mock(Hero.class));
    }

    private AlgorithmSuperClassTest(Hero humanPlayer, Hero AIPlayer) {
        super(humanPlayer, AIPlayer);
        this.human = humanPlayer;
        this.AI = AIPlayer;
    }

    @Before
    public void setUp() {
//All set up needed have been done in the constructor
    }


    /**************************************************************************************************************/
    //Tests for the AI side
    @Test
    public void getMyMaxActiveCard_Test() {
        assertEquals(AI.getMaxActiveCards(), super.getMyMaxActiveCard());
    }

    @Test
    public void getMyDeckCardsNumber_Test() {
        Deck deck = Mockito.mock(Deck.class);
        when(deck.getCardsInDeck()).thenReturn(Mockito.mock(ArrayList.class));
        when(AI.getDeck()).thenReturn(deck);
        assertEquals(AI.getDeck().getCardsInDeck().size(), super.getMyDeckCardsNumber());
    }

    @Test
    public void getMyBoardCards_Test() {
        when(AI.getActiveCards()).thenReturn(Mockito.mock(ArrayList.class));
        assertEquals(AI.getActiveCards(), super.getMyBoardCards());
    }

    @Test
    public void getMyHandCards_Test() {
        Hand hand = Mockito.mock(Hand.class);
        when(hand.getCards()).thenReturn(Mockito.mock(ArrayList.class));
        when(AI.getHand()).thenReturn(hand);
        assertEquals(AI.getHand().getCards(), super.getMyHandCards());
    }

    @Test
    public void getMyHealth_Test() {
        assertEquals(AI.getCurrentHealth(), super.getMyHealth());
    }

    @Test
    public void getMyMana_Test() {
        assertEquals(AI.getCurrentMana(), super.getMyMana());
    }

    /**************************************************************************************************************/
    //Tests for the human side
    @Test
    public void isPlayerDeckEmpty_Test() {
        when(human.getDeck()).thenReturn(Mockito.mock(Deck.class));
        assertEquals(human.getDeck().isDeckEmpty(), super.isPlayerDeckEmpty());
    }

    @Test
    public void getPlayerBoardCards_Test() {
        when(human.getActiveCards()).thenReturn(Mockito.mock(ArrayList.class));
        assertEquals(human.getActiveCards(), super.getPlayerBoardCards());
    }

    @Test
    public void getPlayerHealth_Test() {
        assertEquals(human.getCurrentHealth(), super.getPlayerHealth());
    }

    @Test
    public void getPlayerHandCardNumber_Test() {
        Hand humanHand = Mockito.mock(Hand.class);
        when(humanHand.getCards()).thenReturn(Mockito.mock(ArrayList.class));
        when(human.getHand()).thenReturn(humanHand);

        assertEquals(human.getHand().getCards().size(), super.getPlayerHandCardNumber());
    }

    /**************************************************************************************************************/
    //Mandatory override functions
    @Override
    public int actionNumber() {
        return 0;
    }

    @Override
    protected void algorithm() {

    }
}
