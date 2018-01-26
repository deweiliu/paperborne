package uk.ac.qub.eeecs.game.cardDemo.endGameLogic;

import android.graphics.Bitmap;
import android.graphics.Color;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.animationsOfGameObject.RotatingAnimation;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen.GetPlayerNameScreen;

import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen.GetName;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.gameOverScreen.GameOverScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.recordsScreen.RecordsScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class EndGameLogic extends GameScreen {
    private static final int GAME_OVER_ANIMATIONS = 0;
    private static final int PLAYERS_NAME = 1;
    private static final int RECORDS_SCREEN = 2;

    private int statue = -1;
    private boolean isSinglePlayer;
    private boolean isPlayer1Wins;
    private EndGameScreen mEndGameScreen = null;
    private GameScreen mBattleScreen;
    private GetName mPlayerName;
    private boolean stopDraw = false;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private boolean readyToUpdate;
    private Bitmap backGround;
    private RotatingAnimation loading;

    public EndGameLogic(GameScreen battleScreen, boolean isSinglePlayer, boolean isPlayer1Wins) {
        super("EndGameLogic", battleScreen.getGame());
        this.mBattleScreen = battleScreen;
        this.isSinglePlayer = isSinglePlayer;
        this.isPlayer1Wins = isPlayer1Wins;
        this.statue = GAME_OVER_ANIMATIONS;
        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);

        //Set up background
        final String BACKGROUND_NAME = "RecordsScreenBackground";
        mGame.getAssetManager().loadAndAddBitmap(BACKGROUND_NAME, "img/End Game Logic/end-game-logic-background.jpg");
        backGround = mGame.getAssetManager().getBitmap(BACKGROUND_NAME);

        //Set up loading animation
        final String LOADING_NAME = "loadingToMainMenuFromEndGameLogic";
        mGame.getAssetManager().loadAndAddBitmap(LOADING_NAME, "img/End Game Logic/loading.png");
        Bitmap loadingBitmap = mGame.getAssetManager().getBitmap(LOADING_NAME);
        loading = new RotatingAnimation(2000, mLayerViewport.getHeight() / 4, loadingBitmap, this, 5000);

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
    }

    private void changeToNextScreen() {
        switch (this.statue) {
            //From game over animations to get player name
            case GAME_OVER_ANIMATIONS:
                this.mEndGameScreen = new GetPlayerNameScreen(this);
                this.statue = PLAYERS_NAME;
                break;

            //From get player name to records screen
            case PLAYERS_NAME:
                this.mEndGameScreen = new RecordsScreen(this, this.mPlayerName);
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


            graphics2D.clear(Color.GREEN);
            mEndGameScreen.draw(elapsedTime, graphics2D);
            if (mEndGameScreen.isFinished()) {
                loading.draw(elapsedTime, graphics2D);
            }
        }
    }

    public void setPlayerName(GetName mPlayerName) {
        this.mPlayerName = mPlayerName;
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

    public Bitmap getBackGround() {
        return backGround;
    }

}

