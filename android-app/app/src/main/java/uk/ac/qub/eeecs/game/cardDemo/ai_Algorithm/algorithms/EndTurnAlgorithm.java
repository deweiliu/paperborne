package uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms;

import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 */

public class EndTurnAlgorithm extends AlgorithmSuperclass {
    public EndTurnAlgorithm(Hero humanPlayer, Hero AIPlayer) {
        super(humanPlayer, AIPlayer);
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
