package uk.ac.qub.eeecs.game.cardDemo.Cards;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Created by user on 05/12/2017.
 */

public class Hand {
    private final int MAX_HAND_SIZE = 10;
    private ArrayList<Card> cards;

    public Hand(Deck deck){
        //Draws random cards from the deck and adds them to the hand
        for (int i = 0; i < MAX_HAND_SIZE; i++){
            this.cards.add(deck.addCardToHand());
        }
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }


}
