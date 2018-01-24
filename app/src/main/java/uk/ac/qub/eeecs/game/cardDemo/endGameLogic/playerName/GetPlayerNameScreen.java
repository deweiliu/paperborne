package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.playerName;

import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameLogic;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetPlayerNameScreen implements EndGameScreen {
    private PlayerName mPlayerName;
    private boolean isFinished = false;

    public GetPlayerNameScreen(EndGameLogic endGameLogic) {
        super();
        this.mPlayerName = new PlayerName(endGameLogic.isSinglePlayer());
        mPlayerName.setWinnerName("I am winner.");
        mPlayerName.setLoserName("I am loser.");
        endGameLogic.setmPlayerName(mPlayerName);
        isFinished = true;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.GREEN);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public GameScreen getGemeScreen() {
        return null;
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public int getScreenWidth() {
        return 0;
    }

    @Override
    public int getScreenHeight() {
        return 0;
    }

    @Override
    public LayerViewport getLayerViewport() {
        return null;
    }

    @Override
    public ScreenViewport getScreenViewPort() {
        return null;
    }

    @Override
    public AssetStore getAssetManager() {
        return null;
    }
}
