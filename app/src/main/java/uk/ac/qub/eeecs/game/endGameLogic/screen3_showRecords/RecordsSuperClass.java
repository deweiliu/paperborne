package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameStuff;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public abstract class RecordsSuperClass implements EndGameStuff {
    protected PushButton switchButton;
    protected EndGameScreen mScreen;
    protected String SWITCH_BUTTON_NAME;
    protected Bitmap switchBitmap;
    protected float SWITCH_BUTTON_SIZE;
    protected Paint mPaint;

    protected LayerViewport mLayerViewport;
    protected boolean isFinished = false;
    protected User user;
    protected RecordsManager manager;

    public RecordsSuperClass(EndGameScreen mEndGameScreen, User user, RecordsManager manager) {
        this.mScreen = mEndGameScreen;
        this.user = user;
        mLayerViewport = mEndGameScreen.getLayerViewport();
        SWITCH_BUTTON_SIZE = mLayerViewport.getHeight() / 5;
        this.manager = manager;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(100);
        SWITCH_BUTTON_NAME = "switchToHistoryPlayersRecordsScreen";
        mEndGameScreen.getAssetManager().loadAndAddBitmap(SWITCH_BUTTON_NAME, "img/End Game Logic/players-records-switch.jpg");
        switchBitmap = mEndGameScreen.getAssetManager().getBitmap(SWITCH_BUTTON_NAME);
        switchButton = new PushButton(mLayerViewport.getRight() - SWITCH_BUTTON_SIZE / 2,
                mLayerViewport.getTop() - SWITCH_BUTTON_SIZE / 2,
                SWITCH_BUTTON_SIZE, SWITCH_BUTTON_SIZE, SWITCH_BUTTON_NAME, mEndGameScreen.getGameScreen());
        this.switchButton.processInLayerSpace(true);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        switchButton.update(elapsedTime, mLayerViewport, mScreen.getScreenViewPort());
        if (switchButton.isPushTriggered()) {
            isFinished = true;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        switchButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreen.getScreenViewPort());
    }
}
