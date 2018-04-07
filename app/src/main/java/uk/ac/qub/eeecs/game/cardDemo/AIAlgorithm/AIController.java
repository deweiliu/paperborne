package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm;

import java.util.Random;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * This class give the interface for the whole AI
 * To use the AI, create a new object of this class at the very beginning of the game,
 * repeat the following steps for every decision you need:
 * 1. call void startThinking(long timeLimitInMillis); once,
 * 2. then call  public boolean isFinished(); again and again until it return true
 * 3. then call  public AIDecision getDecision(); to get the decision you need
 * <p>
 * Created by 40216004 Dewei Liu on 12/02/2018.
 */

public class AIController {
    //If the AI algorithm finishes before the earliestFinishTime, mController responses as unfinished until that time
    //so it makes the human player interpret that the AI is thinking
    private long earliestFinishTime;

    private TimeController mTimeController;
    private AIAlgorithm mAlgorithm;
    private boolean isFinished;
    private Hero humanPlayer;
    private Hero AIPlayer;
    private Random random;

    public AIController(Hero user, Hero AI) {
        this.humanPlayer = user;
        this.AIPlayer = AI;
        random = new Random();
    }

    /**
     * When this function is called, it creates a new thread for the AI algorithm
     * instead of doing the algorithm in the current thread.
     *
     * @param timeLimitInMillis how much mTimeController in millisecond can be used for the thinking for this action
     */
    public void startThinking(long timeLimitInMillis) {
        //Set up every thing

        long currentTime = System.currentTimeMillis();

        //Finish the algorithm 1 second before the real deadline
        long timeForAIAlgorithm = timeLimitInMillis - 700;

        long deadLine = currentTime + timeForAIAlgorithm;
        this.earliestFinishTime = currentTime + Math.abs(random.nextInt((int) timeForAIAlgorithm - 100)) + 100;

        //Start working
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
     * Please make sure the AI function is finished before you call this function
     *
     * @return the decision from AIAlgorithm
     */
    public AIDecision getDecision() {
        if (this.isFinished()) {
            return mAlgorithm.getDecision();
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * If the mTimeController limit has passed, call this function to stop AI algorithm
     * after this function is called, call getDecision();
     */
    public void notifyOverTime() {
        mTimeController.stop();
        if (!mAlgorithm.isFinished()) {
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

    private void reset() {
        mAlgorithm = new AIAlgorithm(humanPlayer, AIPlayer);
        mTimeController = new TimeController();
        isFinished = false;
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



