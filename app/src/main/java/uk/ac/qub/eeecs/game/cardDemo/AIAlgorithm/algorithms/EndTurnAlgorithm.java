package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AIDecision;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class EndTurnAlgorithm extends AlgorithmSuperClass {
public EndTurnAlgorithm(Hero humanPlayer, Hero AIPlayer){
    super(humanPlayer,AIPlayer);
}

    @Override
    public final int actionNumber() {
        return AIDecision.END_TURN;
    }

    @Override
      protected final void algorithm() {
        //Make this action valid without thinking anymore
        super.isValid = true;
    }
}
