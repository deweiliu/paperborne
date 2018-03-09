package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class AttackHeroAlgorithm extends AlgorithmSuperClass {
    public AttackHeroAlgorithm(Board board) {
        super(board);
    }

    private Card attacker;


    @Override
    public final int actionNumber() {
        return PlayerAction.ATTACK_HERO;
    }

    @Override
    protected void AIAlgorithm() {

        ArrayList<Card> myBoardCards = super.getMyBoardCards();
        ArrayList<Card2> cards = new ArrayList<Card2>();
        for (Card each : myBoardCards) {
            if (each.isFinishedMove() == false) {
                //If the card is valid to attack hero, add it into the list for later decision
                Card2 card = new Card2(each);
                cards.add(card);
            }
        }

        //If no cards is valid to attack
        if (cards.isEmpty()) {
            super.isValid = false;
        } else {

            //The computer has the possibility of 20% to make a mistake accidentally
            if (random.nextInt(5) == 0) {
                super.isValid = false;
            } else {
                MyComparator comparator = new MyComparator();

                //Sort in descendant order by the attack value
                cards.sort(comparator);

                //So I decide to attack with the most powerful card
                attacker = cards.get(0).getCard();
                super.isValid = true;
            }
        }
    }

    public Card getAttacker() {

        super.checkValid_ThrowException();
        return this.attacker;
    }

    private class MyComparator implements Comparator<AttackHeroAlgorithm.Card2> {

        @Override
        public int compare(Card2 card2, Card2 t1) {
            if (card2.getAttackValue() < t1.getAttackValue()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private class Card2 {
        private int attackValue;
        private Card card;

        public Card2(Card card) {
            this.card = card;
            this.attackValue = card.getAttackValue();
        }

        public int getAttackValue() {
            return attackValue;
        }

        public Card getCard() {
            return card;
        }
    }

}
