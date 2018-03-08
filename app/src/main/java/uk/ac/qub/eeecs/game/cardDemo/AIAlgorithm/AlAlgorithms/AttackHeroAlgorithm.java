package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 on 08/03/2018.
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
        super.isValid=false;
        //TODO
    }

    public Card getAttacker(){
        return this.attacker;
    }

}
