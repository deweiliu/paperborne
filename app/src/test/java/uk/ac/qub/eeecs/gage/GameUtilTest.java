package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.GameUtil;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 10/04/2018.
 * Used for unit testing the GameUtil class
 */

@RunWith(MockitoJUnitRunner.class)
public class GameUtilTest
{
	// Test layer coordinates
	private static final float TEST_X = 232.0f;
	private static final float TEST_Y = 422.0f;
	
	// Test layer viewport values
	private static final float TEST_LAYER_VIEWPORT_X = 0;
	private static final float TEST_LAYER_VIEWPORT_Y = 0;
	private static final float TEST_LAYER_VIEWPORT_WIDTH = 1000;
	private static final float TEST_LAYER_VIEWPORT_HEIGHT = 500;
	
	// Test screen viewport values
	private static final int TEST_SCREEN_VIEWPORT_X = 0;
	private static final int TEST_SCREEN_VIEWPORT_Y = 0;
	
	// Test screen dimensions
	private static final int TEST_SCREEN_WIDTH = 2000;
	private static final int TEST_SCREEN_HEIGHT = 400;
	
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
	
	@Before
	public void setup() {
		when(game.getScreenWidth()).thenReturn(TEST_SCREEN_WIDTH);
		when(game.getScreenHeight()).thenReturn(TEST_SCREEN_HEIGHT);
		when(game.getAssetManager()).thenReturn(assetManager);
		when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
		when(game.getInput()).thenReturn(input);
		// Mock Context
		when(game.getContext()).thenReturn(context);
		// Mock Android Asset Manager
		when(context.getAssets()).thenReturn(assets);
	}
	
	/**
	 * Checks that the GameUtil.convertLayerPosIntoScreen functions correctly, converts a known
	 * layer coordinate to screen coordinates and back to layer then compares the two values
	 */
	@Test
	public void layerCoordinateConversionTest()
	{
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
				TEST_LAYER_VIEWPORT_WIDTH/2,
				TEST_LAYER_VIEWPORT_HEIGHT/2
		);
		// Set up initial layer position
		Vector2 layerPos = new Vector2(TEST_X, TEST_Y);
		// Set up empty screen position
		Vector2 screenPos = new Vector2();
		
		// Convert the layer position to screen coordinates
		GameUtil.convertLayerPosIntoScreen(screenViewport, screenPos, layerViewport, layerPos);
		
		// Set up empty comparison layer position
		Vector2 comparisonLayerPos = new Vector2();
		
		// Convert the screen coordinates back to layer coordinates
		InputHelper.convertScreenPosIntoLayer(screenViewport, screenPos, layerViewport, comparisonLayerPos);
		
		// Compare the start and end layer coordinates
		// Rounded due to slight loss of accuracy in conversion between floats and ints
		assertEquals(Math.round(comparisonLayerPos.x), Math.round(layerPos.x));
		assertEquals(Math.round(comparisonLayerPos.y), Math.round(layerPos.y));
		
	}
}