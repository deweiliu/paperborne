package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.recordsScreen;

import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class HistoryPlayers extends AllGameRecords {
    private Paint mPaint;

    public HistoryPlayers(EndGameScreen mEndGameScreen) {
        super(mEndGameScreen);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(100);
        SWITCH_BUTTON_NAME = "switchToCurrentPlayersRecordsScreen";
        mEndGameScreen.getAssetManager().loadAndAddBitmap(SWITCH_BUTTON_NAME, "img/End Game Logic/players-records-switch.jpg");
        switchBitmap = mEndGameScreen.getAssetManager().getBitmap(SWITCH_BUTTON_NAME);
        switchButton = new PushButton(mLayerViewport.getRight() - SWITCH_BUTTON_SIZE / 2,
                mLayerViewport.getBottom() + SWITCH_BUTTON_SIZE / 2,
                SWITCH_BUTTON_SIZE, SWITCH_BUTTON_SIZE, SWITCH_BUTTON_NAME, mEndGameScreen.getGameScreen());
        this.switchButton.processInLayerSpace(true);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        switchButton.update(elapsedTime, super.mLayerViewport, mEndGameScreen.getScreenViewPort());
        if (switchButton.isPushTriggered()) {
            super.isFinished = true;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawText("Hello, showing history players records", 0, mEndGameScreen.getScreenHeight() / 4, mPaint);
        switchButton.draw(elapsedTime, graphics2D, super.mLayerViewport, mEndGameScreen.getScreenViewPort());

    }

    @Override
    public boolean isFinished() {
        return super.isFinished;
    }
}
