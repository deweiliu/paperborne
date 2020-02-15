package uk.ac.qub.eeecs.gage.ui_Tests;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.TestUtil;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.ui.Slider;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 09/04/2018.
 * Used for unit testing the Slider UI element
 */

@RunWith(MockitoJUnitRunner.class)
public class SliderTest {
    // Test name for the game screen
    private final static String TEST_GAME_SCREEN = "test_game_screen";

    // Screen dimensions
    private final static int TEST_SCREEN_WIDTH = 1200;
    private final static int TEST_SCREEN_HEIGHT = 800;

    // Layer viewport values
    private static final float TEST_LAYER_VIEWPORT_X = 0;
    private static final float TEST_LAYER_VIEWPORT_Y = 0;
    private static final float TEST_LAYER_VIEWPORT_WIDTH = 1000;
    private static final float TEST_LAYER_VIEWPORT_HEIGHT = 500;

    // Screen viewport values
    private static final int TEST_SCREEN_VIEWPORT_X = 0;
    private static final int TEST_SCREEN_VIEWPORT_Y = 0;

    // Test values for the slider
    private final static int TEST_MIN = 0;
    private final static int TEST_MAX = 5;
    private final static int TEST_VAL = 2;
    private final static int TEST_SET_VAL_IN_RANGE = 3;
    private final static int TEST_SET_VAL_ABOVE_RANGE = 6;
    private final static int TEST_SET_VAL_BELOW_RANGE = -1;
    private final static float TEST_X = 50.0f;
    private final static float TEST_Y = 50.0f;
    private final static float TEST_WIDTH = 10.0f;
    private final static float TEST_HEIGHT = 30.0f;
    private final static String TEST_BITMAP = "bitmap";
    private final static String TEST_SOUND = "sound";

    // Mock values for creating the slider and game screen
    @Mock
    private Game game;
    @Mock
    private AssetStore assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private Input input;
    @Mock
    private Context context;
    @Mock
    private AssetManager assets;
    @Mock
    private ScreenManager screenManager;
    @Mock
    private IGraphics2D graphics2D;
    @Mock
    private Sound sound;

    // Test game screen for creating sliders in
    private GameScreen testGameScreen;

    /**
     * Setup for Slider tests
     */
    @Before
    public void setup() {
        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(assetManager.getSound(any(String.class))).thenReturn(sound);
        when(game.getInput()).thenReturn(input);
        // Mock Context
        when(game.getContext()).thenReturn(context);
        // Mock Android Asset Manager
        when(context.getAssets()).thenReturn(assets);
        when(game.getScreenHeight()).thenReturn(TEST_SCREEN_HEIGHT);
        when(game.getScreenWidth()).thenReturn(TEST_SCREEN_WIDTH);
        // Test game screen
        testGameScreen = new GameScreen(TEST_GAME_SCREEN, game) {
            @Override
            public void update(ElapsedTime elapsedTime) {

            }

            @Override
            public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

            }
        };
    }

    /**
     * Tests the slider's value is set by the constructor
     */
    @Test
    public void createSliderValTest() {
        // Create test slider
        Slider testSlider = new Slider(
                TEST_MIN,
                TEST_MAX,
                TEST_VAL,
                null,
                TEST_X,
                TEST_Y,
                TEST_WIDTH,
                TEST_HEIGHT,
                TEST_BITMAP,
                TEST_BITMAP,
                testGameScreen,
                true
        );
        // Check the value provided in the constructor is the value returned by getVal
        assertEquals(TEST_VAL, testSlider.getVal());
    }

    /**
     * Tests the slider's value can be set with the setVal method
     */
    @Test
    public void setSliderValTest() {
        // Create test slider
        Slider testSlider = new Slider(
                TEST_MIN,
                TEST_MAX,
                TEST_VAL,
                null,
                TEST_X,
                TEST_Y,
                TEST_WIDTH,
                TEST_HEIGHT,
                TEST_BITMAP,
                TEST_BITMAP,
                testGameScreen,
                true
        );

        // Set the slider value to one inside the min-max range
        testSlider.setVal(TEST_SET_VAL_IN_RANGE);

        // Check that the value has been set correctly
        assertEquals(TEST_SET_VAL_IN_RANGE, testSlider.getVal());

        // Set the slider value to one above the max range
        testSlider.setVal(TEST_SET_VAL_ABOVE_RANGE);

        // Check the value has been set to the max value
        assertEquals(TEST_MAX, testSlider.getVal());

        // Set the slider value to one below the min range
        testSlider.setVal(TEST_SET_VAL_BELOW_RANGE);

        // Check the value has been set to the min value
        assertEquals(TEST_MIN, testSlider.getVal());
    }

    /**
     * Tests the slider's value is incremented after it is touched
     */
    @Test
    public void touchSliderValTest() {
        // Set up viewports
        ScreenViewport screenViewport = new ScreenViewport(
                TEST_SCREEN_VIEWPORT_X,
                TEST_SCREEN_VIEWPORT_Y,
                game.getScreenWidth(),
                game.getScreenHeight()
        );
        LayerViewport layerViewport = new LayerViewport(
                TEST_LAYER_VIEWPORT_X,
                TEST_LAYER_VIEWPORT_Y,
                TEST_LAYER_VIEWPORT_WIDTH / 2,
                TEST_LAYER_VIEWPORT_HEIGHT / 2
        );

        // Create test slider
        Slider testSlider = new Slider(
                TEST_MIN,
                TEST_MAX,
                TEST_VAL,
                null,
                TEST_X,
                TEST_Y,
                TEST_WIDTH,
                TEST_HEIGHT,
                TEST_BITMAP,
                TEST_BITMAP,
                testGameScreen,
                true
        );

        // Calculate the expected value after the slider is touched
        int expectedValue = TEST_VAL + 1;

        // Set up the game screen
        game.getScreenManager().addScreen(testGameScreen);

        // Elapsed time for update methods
        ElapsedTime elapsedTime = new ElapsedTime();

        List<TouchEvent> touchEvents = TestUtil.touchObject(testSlider, layerViewport, screenViewport, graphics2D);
        when(input.getTouchEvents()).thenReturn(touchEvents);

        // Update slider
        testSlider.update(elapsedTime, layerViewport, screenViewport);

        // Check value is as expected
        assertEquals(expectedValue, testSlider.getVal());
    }

    /**
     * Tests the slider is created correctly with the constructor with no sound provided
     */
    @Test
    public void createSliderTest() {
        // Create test slider
        Slider testSlider = new Slider(
                TEST_MIN,
                TEST_MAX,
                TEST_VAL,
                null,
                TEST_X,
                TEST_Y,
                TEST_WIDTH,
                TEST_HEIGHT,
                TEST_BITMAP,
                TEST_BITMAP,
                testGameScreen,
                true
        );
        // Test the slider is created with no sound
        assertEquals(null, testSlider.getTriggerSound());
    }

    /**
     * Tests the slider is created correctly with the constructor with a sound provided
     */
    @Test
    public void createSliderTestWithSound() {
        // Create test slider
        Slider testSlider = new Slider(
                TEST_MIN,
                TEST_MAX,
                TEST_VAL,
                null,
                TEST_X,
                TEST_Y,
                TEST_WIDTH,
                TEST_HEIGHT,
                TEST_BITMAP,
                TEST_BITMAP,
                TEST_SOUND,
                testGameScreen,
                true
        );
        // Test the slider is created with the sound provided
        assertEquals(sound, testSlider.getTriggerSound());
    }
}