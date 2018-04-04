package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.interface_superclass;


import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.RecordsManager;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.ShowRecordsScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public abstract class RecordsSuperclass implements RecordsInterface {
    private PushButton switchButton;
    protected EndGameScreen mScreen;
    protected Paint mPaint;
    private PushButton homeButton;
    protected ArrayList<PushButton> buttons;

    protected boolean isFinished = false;
    protected User user;
    protected RecordsManager manager;

    public RecordsSuperclass(EndGameScreen mEndGameScreen, User user, RecordsManager manager) {
        this.mScreen = mEndGameScreen;
        this.user = user;
        LayerViewport layer = mEndGameScreen.getLayerViewport();
        this.manager = manager;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(100);


        //Set up buttons
        buttons = new ArrayList<>();
        float BUTTON_SIZE = layer.getHeight() / 5;

        //Set up switch button
        String SWITCH_BUTTON_NAME = "switch between current player and historical player";
        mEndGameScreen.getAssetManager().loadAndAddBitmap(SWITCH_BUTTON_NAME, "img/End Game Logic/players-records-switch.jpg");
        switchButton = new PushButton(layer.getRight() - BUTTON_SIZE / 2, layer.getTop() - BUTTON_SIZE / 2,
                BUTTON_SIZE, BUTTON_SIZE, SWITCH_BUTTON_NAME, mEndGameScreen.getGameScreen());
        buttons.add(switchButton);


        //Set up return to main menu button
        final String MAIN_MENU_BUTTON_NAME = "Return to Main Menu";
        mEndGameScreen.getAssetManager().loadAndAddBitmap(MAIN_MENU_BUTTON_NAME, "img/End Game Logic/return-menu.png");
        this.homeButton = new PushButton(layer.getLeft() + BUTTON_SIZE / 2, layer.getTop() - BUTTON_SIZE / 2,
                BUTTON_SIZE, BUTTON_SIZE, MAIN_MENU_BUTTON_NAME, mEndGameScreen.getGameScreen());
        buttons.add(homeButton);

        for (PushButton each : buttons) {
            each.processInLayerSpace(true);
        }

    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        for (PushButton each : buttons) {
            each.update(elapsedTime, mScreen.getLayerViewport(), mScreen.getScreenViewPort());
        }

        if (switchButton.isPushTriggered()) {
            isFinished = true;
        }

        if (homeButton.isPushTriggered()) {
            if (mScreen instanceof ShowRecordsScreen) {
                ((ShowRecordsScreen) mScreen).homeButtonPushed();
            }else{
                //Some thing goes wrong.
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (PushButton each : buttons) {
            each.draw(elapsedTime, graphics2D, mScreen.getLayerViewport(), mScreen.getScreenViewPort());
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

}
