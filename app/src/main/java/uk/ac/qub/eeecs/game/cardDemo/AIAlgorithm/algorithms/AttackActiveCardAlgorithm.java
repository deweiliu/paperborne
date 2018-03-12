package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class AttackActiveCardAlgorithm extends AlgorithmSuperClass {
    private Card attacker, attackee;

    public AttackActiveCardAlgorithm(Board board) {
        super(board);
    }

    @Override
    public final int actionNumber() {
        return PlayerAction.ATTACK_ACTIVE_CARD;
    }

    public Card getAttacker() {
        super.checkValid_ThrowException();
        return this.attacker;
    }

    public Card getAttackee() {
        super.checkValid_ThrowException();
        return this.attackee;
    }

    /*******************************************************************************************/


    @Override
    protected void algorithm() {

        //If can win within two turns, then don't attack any active card, and attack the hero directly
        if (canWinWithin2Turns() == true) {
            super.isValid = false;
            return;
        }


        //Simulate all possible actions
        ArrayList<Card> attackers = super.getMyBoardCards(), attackees = super.getPlayerBoardCards();
        ArrayList<Action> actions = new ArrayList<Action>();
        for (Card eachAttacker : attackers) {
            if (eachAttacker.isFinishedMove() == false) {
                for (Card eachAttackee : attackees) {
                    Action action = new Action(eachAttacker, eachAttackee);
                    actions.add(action);
                }
            }
        }

        //Check if there is any available action
        if (actions.isEmpty()) {
            super.isValid = false;
            return;

        }


        //Calculate the max value among all actions
        double maxScore = Double.MIN_VALUE + 1;
        Action bestAction = null;
        for (Action each : actions) {
            if (each.getScore() > maxScore) {
                bestAction = each;
                maxScore = each.getScore();
            }
        }

        //If there are no actions with an acceptable score
        if (bestAction == null) {

            //Decide not to attack any active card
            super.isValid = false;
        }

        //else, choose the best solution (action)
        else {
            this.attacker = bestAction.getAttacker();
            this.attackee = bestAction.getAttackee();
            super.isValid = true;
        }
    }


    /*The reason for choosing "2 turns" is:
    Turn1: For this turn, I know what card I can use to attack human player's hero, so I know exactly the attack value I can make this turn
    For the next player's turn, now I know what card can he use to attack my hero (maybe s/he add new cards in the turn, but they cannot attack immediately).
    So I know if I will loose in next turn.
    Turn 2: For the next of my turn, now, I know what card I can use for attack in the turn, so I know exactly the attack value I can make this turn
    For the player's turn after the next player's turn, I don't know what card s/he is going add in the next player's turn, so I cannot predict so far.
           */
    private boolean canWinWithin2Turns() {
        boolean flag;
        int totalAttackValue = 0;
        ArrayList<Card> myCards = super.getMyBoardCards();
        ArrayList<Card> playerCards = super.getPlayerBoardCards();


        //check if I will loose in next turn.
        for (Card each : playerCards) {
            totalAttackValue += each.getAttackValue();
        }
        if (totalAttackValue >= super.getMyHealth()) {
            flag = true;
        } else {
            flag = false;
        }


        totalAttackValue = 0;
        for (Card each : myCards) {
            if (each.isFinishedMove() == false) {
                totalAttackValue += each.getAttackValue();
            }
            if (flag == false) {
                totalAttackValue += each.getAttackValue();
            }
        }

        //If the computer can win within 2 turns
        if (totalAttackValue >= super.getPlayerHealth()) {

            //But some time computer is going to make some mistakes
            if (random.nextInt(5) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            if (random.nextInt(5) == 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    public class Action {
        private Card attacker, attackee;

        public Action(Card attacker, Card attackee) {
            this.attacker = attacker;
            this.attackee = attackee;
        }

        public double getScore() {

            double myScore = 0;
            ArrayList<Card> myCards = AttackActiveCardAlgorithm.super.getMyBoardCards();

            for (Card each : myCards) {
                if (each.equals(attacker)) {
                    myScore += this.getMyCardValue(each, true);
                } else {
                    myScore += this.getMyCardValue(each, false);
                }
            }


            /****************************************************************************************/


            double playerScore = 0;
            ArrayList<Card> playerCards = AttackActiveCardAlgorithm.super.getPlayerBoardCards();

            for (Card each : playerCards) {
                if (each.equals(attackee)) {
                    playerScore += this.getPlayerCardValue(each, attacker.getAttackValue());

                } else {
                    playerScore += this.getPlayerCardValue(each, 0);
                }
            }

            /****************************************************************************************/

            double score = myScore - playerScore;


            //If the  attack value of my active card is too much higher than the health value of opponent's active card
            if (attacker.getAttackValue() / (double) attackee.getHealthValue() > 1.4) {

                //It does not worth to attack to opponent's active cards (Maybe attack his/her Hero directly)
                score = Double.MIN_VALUE;
            }
            return score;
        }

        public Card getAttacker() {
            return attacker;
        }

        public Card getAttackee() {
            return attackee;
        }


        /****************************************************************************************/

        public final static int BONUS_HEALTH_AFTER_ATTACK = 1;
        public final static int BONUS_HEALTH_BEFORE_ATTACK = 4;

        public double getMyCardValue(Card card, boolean aboutToAttack) {
            int healthScore;
            if (aboutToAttack || card.isFinishedMove()) {
                healthScore = card.getHealthValue() + BONUS_HEALTH_AFTER_ATTACK;

            } else {
                healthScore = card.getHealthValue() + BONUS_HEALTH_BEFORE_ATTACK;
            }
            return card.getAttackValue() * healthScore;
        }

        /****************************************************************************************/

        public final static int BONUS_HEALTH_AS_ATTACKEE = 2;

        public double getPlayerCardValue(Card card, int reducedHealth) {
            int healthScore;
            if (card.getHealthValue() > reducedHealth) {
                healthScore = card.getHealthValue() - reducedHealth + BONUS_HEALTH_AS_ATTACKEE;
            } else {
                healthScore = 0;
            }
            return card.getAttackValue() * healthScore;
        }


    }

}
