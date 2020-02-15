package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by nshah on 12/11/2017.
 */
public class SplashScreen extends GameScreen {
    private double timeFromLoading;
    private GameObject SplashBackground;

    private LayerViewport mLayerViewport;
    private ScreenViewport mScreenViewport;

    private float viewportYPos = 400.0f;

    public SplashScreen(Game game) {
        super("CardSplashScreen", game);
        timeFromLoading = 0;
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());
        //starts the viewport from below the screen
        if (mScreenViewport.width > mScreenViewport.height) {
            mLayerViewport = new LayerViewport(240.0f, 400.0f, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        } else {
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
        }


        AssetStore assetManager = mGame.getAssetManager();

        assetManager.loadAndAddBitmap("SplashBackground", "img/SplashScreen.png");

        SplashBackground = new GameObject(mLayerViewport.getWidth() / 2.0f,
                mLayerViewport.getHeight() / 2.0f,
                mLayerViewport.getWidth(),
                mLayerViewport.getHeight(),
                getGame()
                        .getAssetManager().getBitmap("SplashBackground"), this);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        SplashBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        //Moves to the menu screen if the user touches the screen or the time limit has passed
        if (timeFromLoading > 10.0 || touchEvents.size() > 0) {
            changeToScreen(new MenuScreen(mGame));
        }

        //brings the screen up into place
        viewportYPos -= 15;
        if (viewportYPos <= 240.0f
                * mScreenViewport.height / mScreenViewport.width) {
            viewportYPos = 240.0f
                    * mScreenViewport.height / mScreenViewport.width;
        }
        mLayerViewport.set(240.0f, viewportYPos, mLayerViewport.halfWidth, mLayerViewport.halfHeight);

        timeFromLoading += 0.1;
    }

    private void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
    }
}
