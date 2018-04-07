package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm;

import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 Dewei Liu on 12/02/2018.
 */

public class AIDecision {
    public int getAction() {
        return action;
    }

    /************************************************************************************************/
    public final static int END_TURN = 101;

    /************************************************************************************************/
    public final static int ATTACK_ACTIVE_CARD = 102;

    public Card getSourceCard() {
        exception_ATTACK_ACTIVE_CARD();
        return this.sourceCard;
    }

    public Card getTargetCard() {
        exception_ATTACK_ACTIVE_CARD();
        return this.targetCard;
    }

    private void exception_ATTACK_ACTIVE_CARD() {
        if (this.action != ATTACK_ACTIVE_CARD) {
            throw new IllegalStateException(exceptionString + "ATTACK_ACTIVE_CARD.");
        }
    }

    /************************************************************************************************/
    public final static int ATTACK_HERO = 103;

    public Card getAttackerCard() {
        exception_ATTACK_HERO();
        return this.sourceCard;
    }

    private void exception_ATTACK_HERO() {
        if (this.action != ATTACK_HERO) {
            throw new IllegalStateException(exceptionString + "ATTACK_HERO.");
        }
    }

    /************************************************************************************************/
    public final static int PLAY_CARD = 104;

    public Card getCardPlayed() {
        exception_PLAY_CARD();
        return this.sourceCard;
    }

    private void exception_PLAY_CARD() {
        if (this.action != PLAY_CARD) {
            throw new IllegalStateException(exceptionString + "PLAY_CARD.");
        }
    }

    /************************************************************************************************/

    //The animation which is attacked (attackee)
    private Card targetCard = null;

    //The animation which attacks other (attacker)
    private Card sourceCard = null;

    private int action = -1;

    private String exceptionString = "You cannot call this function unless the player's Action is ";

    public AIDecision(int action, Card source, Card target) {
        switch (action) {
            case ATTACK_ACTIVE_CARD:
                this.targetCard = target;
            case PLAY_CARD:
            case ATTACK_HERO:
                this.sourceCard = source;
            case END_TURN:
                this.action = action;
                break;
            default:
                throw new IllegalStateException("Illegal value of Action");
        }
    }
}
