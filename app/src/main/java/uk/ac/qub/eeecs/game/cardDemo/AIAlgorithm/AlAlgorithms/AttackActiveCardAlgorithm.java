package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 on 08/03/2018.
 */

public class AttackActiveCardAlgorithm extends AlgorithmSuperClass {
    public AttackActiveCardAlgorithm(Board board) {
        super(board);
    }

    private Card attacker,attackee;


    @Override
    public final int actionNumber() {
        return PlayerAction.ATTACK_ACTIVE_CARD;
    }

    @Override
    protected void AIAlgorithm() {
        super.isValid=false;

        //TODO
    }

    public Card getAttacker(){
        return this.attacker;
    }public Card getAttackee(){
        return this.attackee;
    }
}
