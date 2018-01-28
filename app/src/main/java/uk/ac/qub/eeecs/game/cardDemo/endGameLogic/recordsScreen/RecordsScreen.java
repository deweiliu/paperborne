package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.recordsScreen;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameLogic;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen.GetName;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class RecordsScreen implements EndGameScreen {
    private Paint mPaint;

    private LayerViewport mLayerViewport;
    private EndGameLogic mEndGameLogic;
    private ScreenViewport mScreenViewport;
    private boolean isFinished;
    private PushButton mainMenuButton;
    private GameObject backGround;
    private AllGameRecords showRecords;



    public RecordsScreen(EndGameLogic gameScreen, GetName playerName) {
        mEndGameLogic = gameScreen;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(100);
        mScreenViewport = gameScreen.getScreenViewport();
        mLayerViewport = gameScreen.getLayerViewport();

        isFinished = false;

        //Set up return to main menu button
        final String MAIN_MENU_BUTTON_NAME = "ReturnMainMenuButton";
        getAssetManager().loadAndAddBitmap(MAIN_MENU_BUTTON_NAME, "img/End Game Logic/return-menu.png");
        final float MAIN_MENU_BUTTON_SIZE = mLayerViewport.getHeight() / 3;
        this.mainMenuButton = new PushButton(mLayerViewport.getLeft() + MAIN_MENU_BUTTON_SIZE / 2,
                mLayerViewport.getBottom() + MAIN_MENU_BUTTON_SIZE / 2,
                MAIN_MENU_BUTTON_SIZE, MAIN_MENU_BUTTON_SIZE, MAIN_MENU_BUTTON_NAME, this.mEndGameLogic);
        this.mainMenuButton.processInLayerSpace(true);

        //Add Background to records screen
        backGround = new GameObject(0, 0, mLayerViewport.getWidth(), mLayerViewport.getHeight(),
                mEndGameLogic.getBackGround(), this.mEndGameLogic);
        showRecords = new CurrentPlayers(this);

    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        mainMenuButton.update(elapsedTime, mLayerViewport, mScreenViewport);
        backGround.update(elapsedTime);
        if (mainMenuButton.isPushTriggered()) {
            this.isFinished = true;
        }
        showRecords.update(elapsedTime);
        if (showRecords.isFinished()) {
            if (showRecords instanceof CurrentPlayers) {
                showRecords = new HistoryPlayers(this);
            } else if (showRecords instanceof HistoryPlayers) {
                showRecords = new CurrentPlayers(this);
            } else {
                Log.d("Some thing goes wrong.", "");
            }
        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        backGround.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        showRecords.draw(elapsedTime, graphics2D);
        mainMenuButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public GameScreen getGameScreen() {
        return mEndGameLogic;
    }

    @Override
    public AssetStore getAssetManager() {
        return this.getGame().getAssetManager();
    }

    @Override
    public Game getGame() {
        return this.getGameScreen().getGame();
    }

    @Override
    public int getScreenWidth() {
        return getGame().getScreenWidth();
    }

    @Override
    public int getScreenHeight() {
        return getGame().getScreenHeight();
    }

    @Override
    public LayerViewport getLayerViewport() {
        return mLayerViewport;
    }

    @Override
    public ScreenViewport getScreenViewPort() {
        return mScreenViewport;
    }

}
