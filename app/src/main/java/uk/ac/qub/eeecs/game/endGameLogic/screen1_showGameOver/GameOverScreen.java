package uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GameOverScreen implements EndGameScreen {
    private GameScreen mBattleScreen;

    //In million second
    private final static long ANIMATION_PERIOD = 3000;
    private Paint mPaint;
    private boolean isFinished;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private GameOverAnimation mGameOverAnimation;
    private EndGameController mEndGameControllerScreen;
    private final static int TEXT_SIZE = 100;

    public GameOverScreen(EndGameController endGameControllerScreen) {
        super();
        isFinished = false;
        mEndGameControllerScreen = endGameControllerScreen;
        this.mBattleScreen = endGameControllerScreen.getBattleScreen();

        mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setTextSize(TEXT_SIZE);

        mScreenViewport = mEndGameControllerScreen.getScreenViewport();
        mLayerViewport = mEndGameControllerScreen.getLayerViewport();

        AssetStore assetManager = getGame().getAssetManager();
        String winAnimationName = "WinAnimation";
        assetManager.loadAndAddBitmap(winAnimationName, "img/End Game Logic/you-win.png");
        Bitmap winAnimation = assetManager.getBitmap(winAnimationName);

        String loseAnimationName = "LoseAnimation";
        assetManager.loadAndAddBitmap(loseAnimationName, "img/End Game Logic/you-lose.png");
        Bitmap loseAnimation = assetManager.getBitmap(loseAnimationName);

        if (mEndGameControllerScreen.isSinglePlayer()) {
            if (mEndGameControllerScreen.isPlayer1Wins()) {

                mGameOverAnimation = new SinglePlayerGameOver(this, winAnimation);
            } else {
                mGameOverAnimation = new SinglePlayerGameOver(this, loseAnimation);
            }
        } else {
            if (mEndGameControllerScreen.isPlayer1Wins()) {
                mGameOverAnimation = new TwoPlayerGameOver(this, winAnimation, loseAnimation);
            } else {
                mGameOverAnimation = new TwoPlayerGameOver(this, loseAnimation, winAnimation);

            }

        }
        mGameOverAnimation.start(ANIMATION_PERIOD);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        Input input = getGame().getInput();
        mGameOverAnimation.update(elapsedTime);
        if (mGameOverAnimation.isFinished()) {
            if (input.existsTouch(0)) {
                this.isFinished = true;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLUE);
        this.mBattleScreen.draw(elapsedTime, graphics2D);
        mGameOverAnimation.draw(elapsedTime, graphics2D);
        if (mGameOverAnimation.isFinished()) {

            final float textPositionX = getGame().getScreenWidth() / 5;
            final String text = "Touch Screen to Continue...";

            if (mEndGameControllerScreen.isSinglePlayer()) {
                graphics2D.drawText(text, textPositionX, getGame().getScreenHeight() * 4 / 5, this.mPaint);
            } else {
                graphics2D.drawText(text, textPositionX, (getGame().getScreenHeight() - TEXT_SIZE) / 2, this.mPaint);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }


    @Override
    public GameScreen getGameScreen() {
        return mEndGameControllerScreen;
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
        if (mLayerViewport == null) {
            Log.d("Game over Screen", "getlayerview port is null");
        }
        return mLayerViewport;

    }

    @Override
    public ScreenViewport getScreenViewPort() {
        return mScreenViewport;
    }


}


