package uk.ac.qub.eeecs.gage.OptionsTests;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.TestUtil;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.game.options.OptionsScreen;
import uk.ac.qub.eeecs.game.ui.Slider;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 11/04/2018.
 * Used to check that the options screen is operating correctly
 */

@RunWith(MockitoJUnitRunner.class)
public class OptionsScreenTest {

    // Screen dimensions
    private final static int TEST_SCREEN_WIDTH = 1200;
    private final static int TEST_SCREEN_HEIGHT = 800;

    // Mock values for the options screen tests
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
    private Activity activity;
    @Mock
    private AssetManager assets;
    @Mock
    private Music music;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private IGraphics2D iGraphics2D;
    @Mock
    private SharedPreferences.Editor editor;

    // Options screen to test on
    private OptionsScreen optionsScreen;

    /**
     * Set up for the options screen tests
     */
    @Before
    public void setup() {
        // Mock assets and input
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
        when(context.getAssets()).thenReturn(assets);
        when(game.getInput()).thenReturn(input);
        // Mock context
        when(game.getContext()).thenReturn(context);
        // Mock activity
        when(game.getActivity()).thenReturn(activity);
        // Mock screen dimensions
        when(game.getScreenWidth()).thenReturn(TEST_SCREEN_WIDTH);
        when(game.getScreenHeight()).thenReturn(TEST_SCREEN_HEIGHT);
        // Mock shared preferences
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        // Mock shared preferences value retrieval
        when(sharedPreferences.getBoolean(any(String.class), any(Boolean.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                // Return whatever the second parameter was
                return (Boolean) invocation.getArguments()[1];
            }
        });
        when(sharedPreferences.getInt(any(String.class), any(Integer.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                // Return whatever the second parameter was
                return (Integer) invocation.getArguments()[1];
            }
        });
        // Set up options screen for testing
        optionsScreen = new OptionsScreen(game);
    }

    /**
     * Used to check the FPS toggle operates correctly, when the toggle is pressed it's value
     * should flip (true -> false or false -> true)
     */
    @Test
    public void fpsToggleTest() {
        // Get FPS toggle
        ToggleButton fpsToggle = optionsScreen.getFpsToggle();
        // Get initial value
        Boolean toggled = fpsToggle.isToggledOn();
        // Get touch events on the object and mock them
        List<TouchEvent> touchEvents = TestUtil.touchObject(fpsToggle, iGraphics2D);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        // Update options screen
        optionsScreen.update(new ElapsedTime());
        // Check the value is flipped from the initial value
        assertEquals(!toggled, fpsToggle.isToggledOn());
    }

    /**
     * Used to check the effects toggle operates correctly, when the toggle is pressed it's value
     * should flip (true -> false or false -> true)
     */
    @Test
    public void effectsToggleTest() {
        // Get effects toggle
        ToggleButton effectsToggle = optionsScreen.getEffectsToggle();
        // Get initial value
        Boolean toggled = effectsToggle.isToggledOn();
        // Get touch events on the object and mock them
        List<TouchEvent> touchEvents = TestUtil.touchObject(effectsToggle, iGraphics2D);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        // Update options screen
        optionsScreen.update(new ElapsedTime());
        // Check the value is flipped from the initial value
        assertEquals(!toggled, effectsToggle.isToggledOn());
    }

    /**
     * Used to check the music slider operates correctly, when the slider is pressed it's value
     * should be incremented by one
     */
    @Test
    public void musicSliderTest() {
        // Get music slider
        Slider musicSlider = optionsScreen.getMusicSlider();
        // Get initial value
        int value = musicSlider.getVal();
        // Get touch events on the object and mock them
        List<TouchEvent> touchEvents = TestUtil.touchObject(musicSlider, iGraphics2D);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        // Update options screen
        optionsScreen.update(new ElapsedTime());
        // Check the value has been incremented by one
        assertEquals(value + 1, musicSlider.getVal());
    }

    /**
     * Used to check the sound slider operates correctly, when the slider is pressed it's value
     * should be incremented by one
     */
    @Test
    public void soundSliderTest() {
        // Get sounds slider
        Slider soundsSlider = optionsScreen.getSoundsSlider();
        // get initial value
        int value = soundsSlider.getVal();
        // Get touch events on the object and mock them
        List<TouchEvent> touchEvents = TestUtil.touchObject(soundsSlider, iGraphics2D);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        // Update options screen
        optionsScreen.update(new ElapsedTime());
        // Check the value has been incremented by one
        assertEquals(value + 1, soundsSlider.getVal());
    }
}

