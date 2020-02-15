package uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm;

import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.AlgorithmSuperclass;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.AttackActiveCardAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.AttackHeroAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.EndTurnAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.algorithms.PlayCardAlgorithm;

/**
 * Give an over view structure of the AI
 * <p>
 * Created by 40216004 Dewei Liu on 15/02/2018.
 */

public class AIAlgorithm implements Runnable {
    private boolean isFinished;
    private Thread thread;
    private Hero humanPlayer;
    private Hero AIPlayer;

    //For AIDecision class
    private int actionState = -1;
    private Card targetCard = null;
    private Card sourceCard = null;
    private AIDecision decision;


    public AIAlgorithm(Hero humanPlayer, Hero AIPlayer) {
        isFinished = false;
        this.humanPlayer = humanPlayer;
        this.AIPlayer = AIPlayer;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    //AI function
    @Override
    public void run() {
        /*******************************************************************************************/
        //PlayCardAlgorithm
        AlgorithmSuperclass algorithm = new PlayCardAlgorithm(humanPlayer, AIPlayer);
        if (algorithm.isValid()) {
            this.sourceCard = ((PlayCardAlgorithm) algorithm).getCardPlayed();
        }

        /*******************************************************************************************/
        //AttackActiveCardAlgorithm
        else {
            algorithm = new AttackActiveCardAlgorithm(humanPlayer, AIPlayer);
            if (algorithm.isValid()) {
                this.sourceCard = ((AttackActiveCardAlgorithm) algorithm).getAttacker();
                this.targetCard = ((AttackActiveCardAlgorithm) algorithm).getAttackee();
            }

            /*******************************************************************************************/
            //AttackHeroAlgorithm
            else {
                algorithm = new AttackHeroAlgorithm(humanPlayer, AIPlayer);
                if (algorithm.isValid()) {
                    this.sourceCard = ((AttackHeroAlgorithm) algorithm).getAttacker();
                }

                /*******************************************************************************************/
                //EndTurnAlgorithm
                else {
                    algorithm = new EndTurnAlgorithm(humanPlayer, AIPlayer);
                    if (algorithm.isValid()) {

                    } else {
                        //No Action selected. Some thing went wrong
                    }
                }

            }

        }
        /*******************************************************************************************/

        this.actionState = algorithm.actionNumber();
        finishFunction();
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public AIDecision getDecision() {
        return decision;
    }

    public void notifyOverTime() {
        if (!this.isFinished) {
            this.actionState = AIDecision.END_TURN;
            this.finishFunction();
        }
    }

    private void finishFunction() {
        if (actionState != -1) {
            this.decision = new AIDecision(actionState, sourceCard, targetCard);
            this.isFinished = true;
        }
    }
}
