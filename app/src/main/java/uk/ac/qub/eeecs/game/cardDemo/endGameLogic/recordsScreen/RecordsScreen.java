package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.recordsScreen;

import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameLogic;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.playerName.PlayerName;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class RecordsScreen implements EndGameScreen {
    private Paint mPaint;

    private LayerViewport mLayerViewport;
    private EndGameLogic mEndGameLogic;
    private ScreenViewport mScreenViewport;

    public RecordsScreen(EndGameLogic gameScreen, PlayerName playerName) {
        mEndGameLogic = gameScreen;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(100);
        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.drawText("Here is RecordsScreen.", 0, getGame().getScreenHeight() / 2, mPaint);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public GameScreen getGemeScreen() {
        return mEndGameLogic;
    }

    @Override
    public AssetStore getAssetManager() {
        return this.getGame().getAssetManager();
    }

    @Override
    public Game getGame() {
        return this.getGemeScreen().getGame();
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
