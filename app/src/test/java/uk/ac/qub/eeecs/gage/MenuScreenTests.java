package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.MenuScreen;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by JC on 12/04/2018
 */


@RunWith(MockitoJUnitRunner.class)
public class MenuScreenTests {

    @Mock
    Game game;

    @Mock
    Input input;

    @Mock
    ScreenManager screenManager;

    @Mock
    AssetStore assetManager;

    @Mock
    Bitmap bitmap;

    @Mock
    private IGraphics2D graphics2D;

    @Before
    public void setUp() {
        screenManager = new ScreenManager();
        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
    }

    @Test
    public void menuScreenCreation(){
        MenuScreen menuScreen = new MenuScreen(game);
        game.getScreenManager().addScreen(menuScreen);
        assertNotNull(menuScreen);
        assertEquals(game.getScreenManager().getCurrentScreen(), menuScreen);
    }

    @Test
    public void drawMenuScreen() {
        ElapsedTime elapsedTime = new ElapsedTime();
        MenuScreen menuScreen = new MenuScreen(game);
        game.getScreenManager().addScreen(menuScreen);
        menuScreen.update(elapsedTime);
        menuScreen.draw(elapsedTime, graphics2D);
        //if we didn't crash we passed
    }
}
