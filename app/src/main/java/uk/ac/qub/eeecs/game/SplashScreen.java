package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;

/**
 * Created by nshah on 12/11/2017.
 */

public class SplashScreen extends GameScreen {
    public double timeFromLoading;

    public SplashScreen(Game game){
        super("CardSplashScreen", game);
        timeFromLoading = 0;


        AssetStore assetManager = mGame.getAssetManager();


    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.RED);
       // graphics2D.drawBitmap(getGame().getAssetManager().getBitmap("SplashScreen"), , );
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (timeFromLoading > 8.0 || touchEvents.size() > 0) {
            changeToScreen(new MenuScreen(mGame));
        }

        timeFromLoading += 0.1;
    }

    private void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
    }

}
