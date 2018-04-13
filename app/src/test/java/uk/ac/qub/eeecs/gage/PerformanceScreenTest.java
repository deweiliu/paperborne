package uk.ac.qub.eeecs.gage;

/**
 * Created by JC on 12/04/2018
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.game.performanceScreen.PerformanceScreen;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

    @Mock
    private IGraphics2D graphics2D;

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

        //get button
        PushButton rectsDownButton = performanceScreen.getmRectanglesDown();

        List<TouchEvent> touchEvents = TestUtil.touchObject(rectsDownButton, graphics2D);

        when(input.getTouchEvents()).thenReturn(touchEvents);
        performanceScreen.update(elapsedTime);

        assertEquals(50, performanceScreen.getNumRectangles());


    }

    @Test
    public void raiseRectangles() {
        // Elapsed time for updates
        ElapsedTime elapsedTime = new ElapsedTime();

        PerformanceScreen performanceScreen = new PerformanceScreen(game);
        game.getScreenManager().addScreen(performanceScreen);

        //get button
        PushButton rectsDownButton = performanceScreen.getmRectanglesUp();

        List<TouchEvent> touchEvents = TestUtil.touchObject(rectsDownButton, graphics2D);

        when(input.getTouchEvents()).thenReturn(touchEvents);
        performanceScreen.update(elapsedTime);

        assertEquals(150, performanceScreen.getNumRectangles());
    }

}
