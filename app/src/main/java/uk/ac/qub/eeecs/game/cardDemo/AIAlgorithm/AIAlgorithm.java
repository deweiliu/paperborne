package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm;

import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by 40216004 Dewei Liu on 15/02/2018.
 */

public class AIAlgorithm implements Runnable {

    private boolean isFinished;
    Thread thread;

    //Board information
    private Hero human, AI;
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
        human = mBoard.getUserHero();
        AI = mBoard.getAIHero();
        //TODO: decide which action should be taken

//Default action
        this.actionState = PlayerAction.END_TURN;


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
