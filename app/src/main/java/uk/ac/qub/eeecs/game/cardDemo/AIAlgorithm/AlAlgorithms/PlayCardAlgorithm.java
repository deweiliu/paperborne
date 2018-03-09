package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class PlayCardAlgorithm extends AlgorithmSuperClass {
    private Card playedCard;

    public PlayCardAlgorithm(Board board) {
        super(board);

    }


    @Override
    public final int actionNumber() {
        return PlayerAction.PLAY_CARD;
    }


    //This algorithm is using the idea of Knapsack problem
    // weight is the capacity of knapsack, and value of card is the value of stuff
    //All algorithm is created for this project. NO copy-paste code from any old project
    @Override
    protected void AIAlgorithm() {

        //Get all information needed for cards
        ArrayList<Card> myHandCards = super.getMyHandCards();
        ArrayList<Card2> cards = new ArrayList<Card2>();
        for (Card each : myHandCards) {
            //Check the card is usable
            if (each.isFinishedMove() == false) {
                Card2 card2 = new Card2(each);
                cards.add(card2);
            } else {
                // Cannot use this card
            }
        }

        //Sort cards with ascent order by weight
        Comparator<Card2> comparator = new MyComparator();
        cards.sort(comparator);
////////////////////////////////////////////////////////////////////////////////////////////////////
        int capacity = super.getMyMana();

        if (cards.isEmpty()) {
            super.isValid = false;
            return;
        }

        //if the mana cannot afford the card with the least mana, then cannot play any card
        if (capacity < cards.get(0).getWeight()) {
            super.isValid = false;
            return;
        }
        playedCard = cards.get(KnapsackAlgorithm(cards, capacity)).getCard();
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
            //Card2 card=cards.get(i);
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


    public Card getPlayedCard() {
        super.checkValid_ThrowException();
        return this.playedCard;
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

            //Define the how to calculate the value of a card
            this.value = card.getAttackValue() * card.getHealthValue();

            //Define the how to calculate the weight of a card
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
