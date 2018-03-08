package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.PlayerAction;
import uk.ac.qub.eeecs.game.cardDemo.Board;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class EndTurnAlgorithm extends AlgorithmSuperClass{
    public EndTurnAlgorithm(Board board){
        super(board);
    }


    @Override
    public final int actionNumber() {
        return PlayerAction.END_TURN;
    }

    @Override
    protected void AIAlgorithm() {
        //Make this action valid without thinking anymore
        super.isValid=true;
    }

}
