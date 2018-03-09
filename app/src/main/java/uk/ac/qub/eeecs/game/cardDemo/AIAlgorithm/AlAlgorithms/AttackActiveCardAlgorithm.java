package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class AttackActiveCardAlgorithm extends AlgorithmSuperClass {
    public AttackActiveCardAlgorithm(Board board) {
        super(board);
    }

    private Card attacker, attackee;


    @Override
    public final int actionNumber() {
        return PlayerAction.ATTACK_ACTIVE_CARD;
    }

    @Override
    protected void AIAlgorithm() {

        //If can win within two turns, then don't attack any active card, and attack the hero directly
        if (canWinWithin2Turns() == true) {
            super.isValid = false;
        } else {
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
            if (actions.isEmpty()) {
                super.isValid = false;

            } else {
                double maxScore = Double.MIN_VALUE;
                Action bestAction = null;
                for (Action each : actions) {
                    if (each.getScore() > maxScore) {
                        bestAction = each;
                        maxScore = each.getScore();
                    }
                }
                this.attacker = bestAction.getAttacker();
                this.attackee = bestAction.getAttackee();
                super.isValid = true;
            }
        }


        //TODO
    }

    public Card getAttacker() {
        super.checkValid_ThrowException();
        return this.attacker;
    }

    public Card getAttackee() {
        super.checkValid_ThrowException();
        return this.attackee;
    }

    private int achievementScore() {
        return random.nextInt(100);
    }

    /*The reason for choosing "2 turns" is:
    Turn1: For this turn, I know what card I can use to attack human player's hero, so I know exactly the attack value I can make this turn
    For the next player's turn, now I know what card can he use to attack my hero (maybe s/he add new cards in the turn, but they cannot attack immediately).
    So I know if I will loose in next turn.
    Turn 2: For the next of my turn, now, I know what card I can use for attack in the turn, so I know exactly the attack value I can make this turn
    For the player's turn after the next player's turn, I don't know what card s/he is going add in the next player's turn, so I cannot predict so far.         */
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

    private class Action {
        private double score;
        private Card attacker, attackee;

        public Action(Card attacker, Card attackee) {
            this.attacker = attacker;
            this.attackee = attackee;
            evaluateAction();
        }

        public double getScore() {
            return score;
        }

        public Card getAttacker() {
            return attacker;
        }

        public Card getAttackee() {
            return attackee;
        }

        private double evaluateAction() {

            double myScore = 0;
            ArrayList<Card> myCards = AttackActiveCardAlgorithm.super.getMyBoardCards();

            for (Card each : myCards) {
                score += this.getCardValue(each, 0);
            }


            double playerScore = 0;
            ArrayList<Card> playerCards = AttackActiveCardAlgorithm.super.getPlayerBoardCards();

            for (Card each : playerCards) {
                if (each.equals(attackee)) {
                    playerScore += this.getCardValue(each, attacker.getAttackValue());

                } else {
                    playerScore += this.getCardValue(each, 0);
                }
            }

            this.score = myScore - playerScore;
            return score;
        }

        private double getCardValue(Card card, int reducedHealth) {
            int healthScore;
            if (card.getHealthValue() > reducedHealth) {
                healthScore = card.getHealthValue() + 2;
            } else {
                healthScore = 0;
            }
            return card.getAttackValue() * healthScore;
        }


    }

}
