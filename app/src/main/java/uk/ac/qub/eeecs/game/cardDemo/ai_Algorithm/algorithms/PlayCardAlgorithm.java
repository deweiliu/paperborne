package uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class PlayCardAlgorithm extends AlgorithmSuperclass {
    private Card cardPlayed;

    public PlayCardAlgorithm(Hero humanPlayer, Hero AIPlayer){
        super(humanPlayer,AIPlayer);
    }

    @Override
    public final int actionNumber() {
        return AIDecision.PLAY_CARD;
    }

    public Card getCardPlayed() {
        super.checkValid_ThrowException();
        return this.cardPlayed;
    }

    /*******************************************************************************************/

    //This algorithm is using the idea of Knapsack problem
    // weight is the capacity of knapsack, and value of card is the value of stuff
    @Override
    protected final void algorithm() {

        //According to the game rule, not able to play any card to the board
        if (super.getMyBoardCards().size() >= super.getMyMaxActiveCard()) {
            super.isValid = false;
            return;
        }

        /****************************************************************************************/

        //Get all information needed for cards
        ArrayList<Card> myHandCards = super.getMyHandCards();
        ArrayList<Card2> cards = new ArrayList<Card2>();
        for (Card each : myHandCards) {

            //Check the card is usable
            if (!each.isFinishedMove()) {
                Card2 card2 = new Card2(each);
                cards.add(card2);
            } else {
                // Cannot use this card
            }
        }

        //If no cards are usable
        if (cards.isEmpty()) {

            //The action is not valid
            super.isValid = false;
            return;
        }


        /****************************************************************************************/
        //Knapsack algorithm

        //Sort cards with ascendant order by weight
        Comparator<Card2> comparator = new MyComparator();
        cards.sort(comparator);

        //The weight of knapsack
        int capacity = super.getMyMana();


        //if the mana cannot afford the card with the least mana, then cannot play any card
        if (capacity < cards.get(0).getWeight()) {
            super.isValid = false;
            return;
        }
        cardPlayed = cards.get(KnapsackAlgorithm(cards, capacity)).getCard();
        super.isValid = true;
    }


    private int KnapsackAlgorithm(ArrayList<Card2> cards, int capacity) {
        int rows = cards.size();
        int columns = capacity + 1;
        int[][] table = new int[rows][columns];

        //Do the 0th row
        for (int i = 0, j = 0; j < columns; j++) {
            int notChoose = 0;
            if (j < cards.get(i).getWeight()) {
                table[i][j] = notChoose;
            } else {
                int choose = cards.get(i).getValue();

                table[i][j] = Math.max(choose, notChoose);
            }
        }

        //Do the left row(s)
        for (int i = 1; i < rows; i++) {
            //Card3 card=cards.get(i);
            for (int j = 0; j < columns; j++) {
                int notChoose = table[i - 1][j];
                if (j < cards.get(i).getWeight()) {
                    table[i][j] = notChoose;
                } else {
                    int choose = cards.get(i).getValue() + table[i - 1][j - cards.get(i).getWeight()];
                    table[i][j] = Math.max(choose, notChoose);
                }
            }
        }

        //Choose the card index
        int cardNumber = 0;
        for (int i = rows - 1, j = columns - 1; i > 0; i--) {
            if (table[i][j] > table[i - 1][j]) {
                cardNumber = i;
                break;
            }
        }
        return cardNumber;
    }

    private class MyComparator implements Comparator<Card2> {

        @Override
        public int compare(Card2 card2, Card2 t1) {
            if (card2.getWeight() < t1.getWeight()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private class Card2 {
        private int value, weight;
        private Card card;

        public Card2(Card card) {

            //Define how to calculate the value of a card
            this.value = card.getAttackValue() * card.getHealthValue();

            //Define how to calculate the weight of a card
            this.weight = card.getManaCost();

            this.card = card;
        }

        public int getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }

        public Card getCard() {
            return card;
        }
    }
}
