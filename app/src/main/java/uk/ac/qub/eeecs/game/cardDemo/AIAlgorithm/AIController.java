package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm;

import java.util.Random;

import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by 40216004 Dewei Liu on 12/02/2018.
 */

public class AIController {
    private TimeController mTimeController;
    private AIAlgorithm mAlgorithm;

    //If the AI algorithm finishes before the earliestFinishTime, this mController responses as unfinished until that time
    //so it makes the player interpret that the AI is thinking
    private long earliestFinishTime;
    private boolean isFinished;
    private Board mBoard;
    private Random random;

    /**
     * @param board pass Board information as parameter
     */
    public AIController(Board board) {
        this.mBoard = board;
        random = new Random();
    }

    public AIController(Hero user, Hero AI){
        this(new Board(user,AI));
    }

    private void reset() {
        mAlgorithm = new AIAlgorithm(mBoard);
        mTimeController = new TimeController();
        isFinished = false;
    }

    /**
     * When this function is called, it creates a new thread for the AI algorithm
     * instead of doing the algorithm in the current thread.     *
     *
     * @param timeLimitInMillis how much mTimeController in millisecond can be used for the thinking for this action
     */
    public void startThinking(long timeLimitInMillis) {
        long currentTime = System.currentTimeMillis();

        //Finish the algorithm 1 second before the real deadline
        long timeForAIAlgorithm = timeLimitInMillis - 700;

        long deadLine = currentTime + timeForAIAlgorithm;


        this.earliestFinishTime = currentTime + Math.abs(random.nextInt((int) timeForAIAlgorithm - 100)) + 100;

        reset();
        mAlgorithm.start();
        mTimeController.start(deadLine);
    }

    /**
     * @return whether the AI function is finished
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Please make sure the AI function is finished before you get AI player's action
     *
     * @return
     */
    public PlayerAction getAction() {
        if (this.isFinished() == false) {
            throw new IllegalStateException();
        }
        return mAlgorithm.getAction();
    }

    /**
     * If the mTimeController limit has passed, call this function to stop AI algorithm
     * after this function is called, call
     */
    public void notifyOverTime() {
        mTimeController.stop();
        if (mAlgorithm.isFinished() == false) {
            mAlgorithm.notifyOverTime();
        }
        this.isFinished = true;
    }

    private void update() {
        if (System.currentTimeMillis() > earliestFinishTime && mAlgorithm.isFinished()) {
            mTimeController.stop();
            this.isFinished = true;
        }
    }


    private class TimeController implements Runnable {
        private long deadLine;
        private volatile boolean running;
        private Thread thread;

        public TimeController() {

            running = false;
            thread = null;
        }


        /**
         * Check if the mTimeController limit has passed
         */
        @Override
        public void run() {
            while (running) {
                if (System.currentTimeMillis() > this.deadLine) {
                    AIController.this.notifyOverTime();
                }
                AIController.this.update();

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void start(long deadLine) {
            this.deadLine = deadLine;
            if (thread != null) {
                this.stop();
            }
            running = true;
            thread = new Thread(this);
            thread.start();
        }

        public void stop() {

            running = false;

            thread = null;

        }
    }
}



