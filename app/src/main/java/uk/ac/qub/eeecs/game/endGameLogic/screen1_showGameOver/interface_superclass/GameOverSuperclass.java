package uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.interface_superclass;

import java.util.ArrayList;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.ui.Moving;

public abstract class GameOverSuperclass implements GameOverInterface {
    protected boolean isFinished;
    protected ArrayList<Moving> animations;
    protected EndGameScreen mScreen;

    public GameOverSuperclass(EndGameScreen screen) {
        animations = new ArrayList<>();
        mScreen = screen;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        boolean finished = true;
        for (Moving each : animations) {
            each.update(elapsedTime);

            //If there is at least one which has not been finished, it is not finished
            if (!each.isFinished()) {
                finished = false;
            }
        }
        this.isFinished = finished;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (Moving each : animations) {
            each.draw(elapsedTime, graphics2D, mScreen.getLayerViewport(), mScreen.getScreenViewPort());
        }
    }

    @Override
    public boolean start(long period) {
        boolean success = true;
        for (Moving each : animations) {

            //If there is at least one which has not been success, it fails
            if (!each.start(period)) {
                success = false;
            }
        }
        return success;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

}
