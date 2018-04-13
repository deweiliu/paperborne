package uk.ac.qub.eeecs.gage;

/**
 * Created by JC on 12/04/2018
 */

import android.content.res.AssetManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.performanceScreen.PerformanceScreen;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceScreenTest {

    // Screen width and height for touch events
    private final int SCREEN_WIDTH = 1280;
    private final int SCREEN_HEIGHT = 720;

    @Mock
    private Input input;

    @Mock
    Game game;

    @Mock
    ScreenManager screenManager;

    @Mock
    AssetStore assetManager;

    @Before
    public void setUp(){
        screenManager = new ScreenManager();
        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getScreenWidth()).thenReturn(SCREEN_WIDTH);
        when(game.getScreenHeight()).thenReturn(SCREEN_HEIGHT);
        when(game.getInput()).thenReturn(input);
    }

    @Test
    public void makeScreen() {
        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        assertNotNull(game.getScreenManager().getScreen(performanceScreen.getName()));
    }

    public void buttonsExist() {
        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        assertNotNull(performanceScreen.getmRectanglesDown());
        assertNotNull(performanceScreen.getmRectanglesUp());
    }

    @Test
    public void initialRects() {
        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        assertEquals(performanceScreen.getNumRectangles(), 100);
    }

    @Test
    public void layerViewportCreationTest() {
        //not a mistake - making width < height
        when(game.getScreenWidth()).thenReturn(SCREEN_HEIGHT);
        when(game.getScreenHeight()).thenReturn(SCREEN_WIDTH);

        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        assertTrue(performanceScreen.getmLayerViewport().x < performanceScreen.getmLayerViewport().y);
        assertTrue(performanceScreen.getmLayerViewport().halfWidth < performanceScreen.getmLayerViewport().halfHeight);
    }

    @Test
    public void lowerRectangles() {
        // Elapsed time for updates
        ElapsedTime elapsedTime = new ElapsedTime();

        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        // Create viewports to allow conversion from touch coordinates into screen coords
        ScreenViewport screenViewport = new ScreenViewport(0, 0, performanceScreen.getGame().getScreenWidth(), performanceScreen.getGame().getScreenHeight());

        LayerViewport layerViewport;

        if (screenViewport.width > screenViewport.height)
            layerViewport = new LayerViewport(240.0f, 240.0f
                    * screenViewport.height / screenViewport.width, 240,
                    240.0f * screenViewport.height / screenViewport.width);
        else
            layerViewport = new LayerViewport(240.0f * screenViewport.height
                    / screenViewport.width, 240.0f, 240.0f
                    * screenViewport.height / screenViewport.width, 240);

        // Create a touch position
        Vector2 touchPos = new Vector2(performanceScreen.getmRectanglesDown().position);

        // Set up the touch event
        TouchEvent touchPosition = new TouchEvent();
        touchPosition.x = touchPos.x;
        touchPosition.y = touchPos.y;
        touchPosition.type = TouchEvent.TOUCH_DOWN;
        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchPosition);

        // Convert the touch coordinates into screen coordinates
        Vector2 buttonPos = new Vector2();
        InputHelper.convertScreenPosIntoLayer(
                screenViewport,
                new Vector2(touchPos.x, touchPos.y),
                layerViewport, buttonPos);

        //move button
        performanceScreen.getmRectanglesDown().position = new Vector2(buttonPos);

        // Set up simulated return for touch events
        when(input.getTouchEvents()).thenReturn(touchEvents);

        //update screen
        performanceScreen.update(elapsedTime);

        //did the number of rectangles decrease as expected?
        assertTrue(performanceScreen.getNumRectangles() == 50);
    }

    @Test
    public void raiseRectangles() {
        // Elapsed time for updates
        ElapsedTime elapsedTime = new ElapsedTime();

        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        // Create viewports to allow conversion from touch coordinates into screen coords
        ScreenViewport screenViewport = new ScreenViewport(0, 0, performanceScreen.getGame().getScreenWidth(), performanceScreen.getGame().getScreenHeight());

        LayerViewport layerViewport;

        if (screenViewport.width > screenViewport.height)
            layerViewport = new LayerViewport(240.0f, 240.0f
                    * screenViewport.height / screenViewport.width, 240,
                    240.0f * screenViewport.height / screenViewport.width);
        else
            layerViewport = new LayerViewport(240.0f * screenViewport.height
                    / screenViewport.width, 240.0f, 240.0f
                    * screenViewport.height / screenViewport.width, 240);

        // Create a touch position
        Vector2 touchPos = new Vector2(performanceScreen.getmRectanglesUp().position);

        // Set up the touch event
        TouchEvent touchPosition = new TouchEvent();
        touchPosition.x = touchPos.x;
        touchPosition.y = touchPos.y;
        touchPosition.type = TouchEvent.TOUCH_DOWN;
        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchPosition);

        // Convert the touch coordinates into screen coordinates
        Vector2 buttonPos = new Vector2();
        InputHelper.convertScreenPosIntoLayer(
                screenViewport,
                new Vector2(touchPos.x, touchPos.y),
                layerViewport, buttonPos);

        //move button
        performanceScreen.getmRectanglesUp().position = new Vector2(buttonPos);

        // Set up simulated return for touch events
        when(input.getTouchEvents()).thenReturn(touchEvents);

        //update screen
        performanceScreen.update(elapsedTime);

        //did the number of rectangles increase as expected?
        assertTrue(performanceScreen.getNumRectangles() == 150);
    }

}
