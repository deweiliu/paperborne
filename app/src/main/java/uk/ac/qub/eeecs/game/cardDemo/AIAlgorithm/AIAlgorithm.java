package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm;

import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.AlgorithmSuperClass;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.AttackActiveCardAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.AttackHeroAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.EndTurnAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms.PlayCardAlgorithm;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by 40216004 Dewei Liu on 15/02/2018.
 */

public class AIAlgorithm implements Runnable {

    private boolean isFinished;
    Thread thread;

    //Board information
    private Board mBoard;

    //For PlayerAction class
    private int actionState = -1;
    private Card targetCard = null;
    private Card sourceCard = null;
    private PlayerAction action;

    public AIAlgorithm(Board board) {
        isFinished = false;
        this.mBoard = board;


    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    //AI function
    @Override
    public void run() {
        //PlayCardAlgorithm
        AlgorithmSuperClass algorithm = new PlayCardAlgorithm(mBoard);
        if (algorithm.isValid()) {
            this.sourceCard = ((PlayCardAlgorithm) algorithm).getPlayedCard();
        }

        /*******************************************************************************************/

        //AttackActiveCardAlgorithm
        else {
            algorithm = new AttackActiveCardAlgorithm(mBoard);
            if (algorithm.isValid()) {
                this.sourceCard = ((AttackActiveCardAlgorithm) algorithm).getAttacker();
                this.targetCard = ((AttackActiveCardAlgorithm) algorithm).getAttackee();
            }

            /*******************************************************************************************/

            //AttackHeroAlgorithm
            else {
                algorithm = new AttackHeroAlgorithm(mBoard);
                if (algorithm.isValid()) {
                    this.sourceCard = ((AttackHeroAlgorithm) algorithm).getAttacker();
                }

                /*******************************************************************************************/

                //EndTurnAlgorithm
                else {
                    algorithm = new EndTurnAlgorithm(mBoard);
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

    public PlayerAction getAction() {
        return action;
    }

    public void notifyOverTime() {
        if (this.isFinished == false) {
            this.actionState = PlayerAction.END_TURN;
            this.finishFunction();
        }
    }

    private void finishFunction() {
        if (actionState != -1) {
            this.action = new PlayerAction(actionState, sourceCard, targetCard);
            this.isFinished = true;
        }


    }


}
