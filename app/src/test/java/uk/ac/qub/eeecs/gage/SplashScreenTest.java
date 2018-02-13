package uk.ac.qub.eeecs.gage;

/**
 * Created by nshah on 08/12/2017.
 */

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.InputEvent;
import android.text.method.Touch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.SplashScreen;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameObject;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SplashScreenTest {

    String splashScreenName = "CardSplashScreen";
    String menuScreenName = "MenuScreen";

    @Mock
    Game game;

    @Mock
    ScreenManager screenManager;

    @Mock
    GameScreen splashScreen = Mockito.mock(GameScreen.class);

    @Mock
    GameScreen menuScreen = Mockito.mock(GameScreen.class);

    @Mock
    AssetStore assetManager;

    @Mock
    Bitmap bitmap;

    @Mock
    Input input;

    @Before
    public void setUp(){
        screenManager = new ScreenManager();
        when(game.getScreenManager()).thenReturn(screenManager);

        when(game.getInput()).thenReturn(input);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(splashScreen.getName()).thenReturn(splashScreenName);
        when(menuScreen.getName()).thenReturn(menuScreenName);

    }

    //This test checks that if touch event occurs when on the splash screen,
    //it should change to the menu screen
    @Test
    public void testScreenChange(){
        //Sets up test data
        SplashScreen splashScreen = new SplashScreen(game);
        game.getScreenManager().addScreen(splashScreen);
        MenuScreen menuScreen = new MenuScreen(game);

        //Create a touch event
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = 0;
        touchEvent.y = 0;

        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        ElapsedTime elapsedTime = new ElapsedTime();

        //Call the update method
        splashScreen.update(elapsedTime);

        //Check the return
        assertEquals(game.getScreenManager().getCurrentScreen().getName(), menuScreen.getName());
    }

}