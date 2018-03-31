package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;


import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public abstract class AllGameRecords implements EndGameStuff {
    protected PushButton switchButton;
    protected EndGameScreen mEndGameScreen;
    protected String SWITCH_BUTTON_NAME;
    protected Bitmap switchBitmap;
    protected float SWITCH_BUTTON_SIZE;
    protected LayerViewport mLayerViewport;
    protected boolean isFinished=false;

    public AllGameRecords(EndGameScreen mEndGameScreen) {
        this.mEndGameScreen = mEndGameScreen;
        mLayerViewport = mEndGameScreen.getLayerViewport();
        SWITCH_BUTTON_SIZE = mLayerViewport.getHeight() / 4;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
    }
}
