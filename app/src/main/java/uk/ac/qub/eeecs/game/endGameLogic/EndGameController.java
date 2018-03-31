package uk.ac.qub.eeecs.game.endGameLogic;

import android.graphics.Bitmap;
import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.ui.Rotating;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.GameOverScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.*;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.GetNameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.RecordsScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 * <p>
 * To use the package of endGameLogic
 * simply create an instance of EndGameController with parameters of current screen
 */

public class EndGameController extends GameScreen {
    private static final int GAME_OVER_ANIMATIONS = 0;
    private static final int PLAYERS_NAME = 1;
    private static final int RECORDS_SCREEN = 2;

    private int statue = -1;
    private boolean isSinglePlayer;
    private boolean isPlayer1Wins;
    private EndGameScreen mEndGameScreen = null;
    private GameScreen mBattleScreen;
    private User mPlayerName;
    private boolean stopDraw = false;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private boolean readyToUpdate;
    private Bitmap backGround;
    private Rotating loading;
    private GameObject background, title;

    public EndGameController(GameScreen cardDemoScreen, boolean isSinglePlayer, boolean isPlayer1Wins) {
        super("EndGameController", cardDemoScreen.getGame());
        this.mBattleScreen = cardDemoScreen;
        this.isSinglePlayer = isSinglePlayer;
        this.isPlayer1Wins = isPlayer1Wins;
        this.statue = GAME_OVER_ANIMATIONS;
        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);

        //Set up background
        final String BACKGROUND_NAME = "All End Game Screens Background";
        mGame.getAssetManager().loadAndAddBitmap(BACKGROUND_NAME, "img/Lined-Paper.png");
        backGround = mGame.getAssetManager().getBitmap(BACKGROUND_NAME);
        background = new GameObject(mLayerViewport.x, mLayerViewport.y, mLayerViewport.getWidth(), mLayerViewport.getHeight(), backGround, this);

        final String TITLE_NAME = "End Game Logic Title";
        mGame.getAssetManager().loadAndAddBitmap(TITLE_NAME, "img/Title.png");
        title = new GameObject(mLayerViewport.x, mLayerViewport.getTop() - mLayerViewport.getHeight() / 10, mLayerViewport.getWidth(), mLayerViewport.getHeight() / 5
                , mGame.getAssetManager().getBitmap(TITLE_NAME), this);


        //Set up loading animation
        final String LOADING_NAME = "loadingToMainMenuFromEndGameLogic";
        mGame.getAssetManager().loadAndAddBitmap(LOADING_NAME, "img/End Game Logic/loading.png");
        Bitmap loadingBitmap = mGame.getAssetManager().getBitmap(LOADING_NAME);
        loading = new Rotating(2000, mLayerViewport.getHeight() / 4, loadingBitmap, this, 5000);

        //This 5 statements must be at the end of this constructor
        this.mEndGameScreen = new GameOverScreen(this);
        mGame.getScreenManager().removeScreen(mBattleScreen.getName());
        mGame.getScreenManager().addScreen(this);
        mGame.getScreenManager().setAsCurrentScreen(this.getName());
        readyToUpdate = true;
        //Don't put any statement here or the app crashes
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (readyToUpdate) {
            if (mEndGameScreen.isFinished()) {
                if (loading.isFinished()) {
                    changeToNextScreen();
                } else {
                    loading.update(elapsedTime);
                }
            } else {
                mEndGameScreen.update(elapsedTime);

            }
        }
        background.update(elapsedTime);
        title.update(elapsedTime);
    }

    private void changeToNextScreen() {
        switch (this.statue) {
            //From game over animations to get player name
            case GAME_OVER_ANIMATIONS:
                this.mEndGameScreen = new GetNameScreen(this);
                this.statue = PLAYERS_NAME;
                break;

            //From get player name to records screen
            case PLAYERS_NAME:
                User user = ((GetNameScreen) mEndGameScreen).getUserName();

                this.mEndGameScreen = new RecordsScreen(this, user);
                this.statue = RECORDS_SCREEN;
                break;

            //From records screen to the main menu.
            case RECORDS_SCREEN:
                readyToUpdate = false;
                stopDraw = true;
                MenuScreen menuScreen = new MenuScreen(super.mGame);
                mGame.getScreenManager().removeScreen(this.getName());
                mGame.getScreenManager().addScreen(menuScreen);
                mGame.getScreenManager().setAsCurrentScreen(menuScreen.getName());
                break;

            //The current screen is null. Create a game over animations
            default:
                this.mEndGameScreen = new GameOverScreen(this);
                this.statue = GAME_OVER_ANIMATIONS;
                break;
        }
        loading.reset();
        this.stopDraw = true;

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (stopDraw) {
            stopDraw = false;
        } else {


            graphics2D.clear(Color.WHITE);
            background.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            title.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            mEndGameScreen.draw(elapsedTime, graphics2D);
            if (mEndGameScreen.isFinished()) {
                loading.draw(elapsedTime, graphics2D);
            }
        }
    }


    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public boolean isPlayer1Wins() {
        return isPlayer1Wins;
    }

    public GameScreen getBattleScreen() {
        return mBattleScreen;
    }

    public ScreenViewport getScreenViewport() {
        return mScreenViewport;
    }

    public LayerViewport getLayerViewport() {
        return mLayerViewport;
    }


}

