package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class ShowRecordsScreen implements EndGameScreen {
    private Paint mPaint;
private RecordsManager manager;
    private LayerViewport mLayerViewport;
    private EndGameController controller;
    private ScreenViewport mScreenViewport;
    private boolean isFinished;
    private PushButton mainMenuButton;
    private RecordsSuperClass showRecords;
    private User playerName;


    public ShowRecordsScreen(EndGameController gameScreen, User playerName) {
        controller = gameScreen;

        this.playerName = playerName;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(150);
        mScreenViewport = gameScreen.getScreenViewport();
        mLayerViewport = gameScreen.getLayerViewport();

        isFinished = false;

        //Set up return to main menu button
        final String MAIN_MENU_BUTTON_NAME = "ReturnMainMenuButton";
        getAssetManager().loadAndAddBitmap(MAIN_MENU_BUTTON_NAME, "img/End Game Logic/return-menu.png");
        final float MAIN_MENU_BUTTON_SIZE = mLayerViewport.getHeight() / 5;
        this.mainMenuButton = new PushButton(mLayerViewport.getLeft() + MAIN_MENU_BUTTON_SIZE / 2,
                mLayerViewport.getTop() - MAIN_MENU_BUTTON_SIZE / 2,
                MAIN_MENU_BUTTON_SIZE, MAIN_MENU_BUTTON_SIZE, MAIN_MENU_BUTTON_NAME, this.controller);
        this.mainMenuButton.processInLayerSpace(true);


         RecordsManager manager=new RecordsManager(gameScreen.getGame().getContext());
       if(  playerName.isSinglePlayer()){
           if(playerName.isWinnerPlayer1()){
               manager.addWinner(playerName.getWinner());
           }else{
               manager.addLoser(playerName.getLoser());
           }
       }else{
           manager.addLoser(playerName.getLoser());
           manager.addWinner(playerName.getWinner());
       }
       this.manager=manager;
        showRecords = new CurrentPlayerRecord(this, playerName,manager);

    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        mainMenuButton.update(elapsedTime, mLayerViewport, mScreenViewport);
        if (mainMenuButton.isPushTriggered()) {
            this.isFinished = true;
        }
        showRecords.update(elapsedTime);
        if (showRecords.isFinished()) {
            if (showRecords instanceof CurrentPlayerRecord) {
                showRecords = new HistoricalPlayersRecords(this,playerName,manager);
            } else if (showRecords instanceof HistoricalPlayersRecords) {
                showRecords = new CurrentPlayerRecord(this,playerName,manager);
            } else {
                Log.d("Some thing goes wrong.", "");
            }
        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        showRecords.draw(elapsedTime, graphics2D);
        mainMenuButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);


    }



    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public GameScreen getGameScreen() {
        return controller;
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
