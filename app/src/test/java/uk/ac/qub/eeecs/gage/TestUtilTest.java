package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 10/04/2018.
 * Used for unit testing the TestUtil class
 */

@RunWith(MockitoJUnitRunner.class)
public class TestUtilTest
{
	// Test name for the game screen
	private final static String TEST_GAME_SCREEN = "test_game_screen";
	
	// Test game object properties
	private static final float TEST_GAMEOBJECT_X = 202.0f;
	private static final float TEST_GAMEOBJECT_Y = 123.0f;
	private static final float TEST_GAMEOBJECT_WIDTH = 50.0f;
	private static final float TEST_GAMEOBJECT_HEIGHT = 50.0f;
	
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
	
	private static final int EXPECTED_TOUCH_EVENTS = 2;
	
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
	private IGraphics2D graphics2D;
	
	private GameScreen testGameScreen;
	
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
		// Test game screen
		testGameScreen = new GameScreen(TEST_GAME_SCREEN, game)
		{
			@Override
			public void update(ElapsedTime elapsedTime)
			{
			
			}
			
			@Override
			public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D)
			{
			
			}
		};
	}
	
	/**
	 * Checks that the TestUtil.convertLayerPosIntoScreen functions correctly, converts a known
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
		TestUtil.convertLayerPosIntoScreen(screenViewport, screenPos, layerViewport, layerPos);
		
		// Set up empty comparison layer position
		Vector2 comparisonLayerPos = new Vector2();
		
		// Convert the screen coordinates back to layer coordinates
		InputHelper.convertScreenPosIntoLayer(screenViewport, screenPos, layerViewport, comparisonLayerPos);
		
		// Compare the start and end layer coordinates
		// Rounded due to slight loss of accuracy in conversion between floats and ints
		assertEquals(Math.round(comparisonLayerPos.x), Math.round(layerPos.x));
		assertEquals(Math.round(comparisonLayerPos.y), Math.round(layerPos.y));
	}
	
	/**
	 * Checks that TestUtil.touchObject(object, layerViewport, screenViewport, graphics2D) is
	 * operating correctly, it should return a list of two touch events, first touch down then
	 * touch up on the object supplied's position in screen space
	 */
	@Test
	public void touchObjectWithViewports()
	{
		// Set up game object
		GameObject gameObject = new GameObject(
				TEST_GAMEOBJECT_X,
				TEST_GAMEOBJECT_Y,
				TEST_GAMEOBJECT_WIDTH,
				TEST_GAMEOBJECT_HEIGHT,
				bitmap,
				testGameScreen
		);
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
		
		// Calculate expected screen touch position for the object
		Vector2 expectedTouchPosition = new Vector2();
		TestUtil.convertLayerPosIntoScreen(screenViewport, expectedTouchPosition, layerViewport, gameObject.position);
		
		// Get the touch events from touchObject
		List<TouchEvent> touchEvents = TestUtil.touchObject(gameObject, layerViewport, screenViewport, graphics2D);
		// Check that there are two touch events returned
		assertTrue(touchEvents.size() == EXPECTED_TOUCH_EVENTS);
		
		// Get the two events
		TouchEvent firstEvent = touchEvents.get(0);
		TouchEvent secondEvent = touchEvents.get(1);
		
		// Check the first event is touch down on the object position in screen space
		assertEquals(firstEvent.type, TouchEvent.TOUCH_DOWN);
		assertEquals(firstEvent.x, expectedTouchPosition.x);
		assertEquals(firstEvent.y, expectedTouchPosition.y);
		
		// Check the second event is touch up on the object position in screen space
		assertEquals(secondEvent.type, TouchEvent.TOUCH_UP);
		assertEquals(secondEvent.x, expectedTouchPosition.x);
		assertEquals(secondEvent.y, expectedTouchPosition.y);
	}
	
	/**
	 * Checks that TestUtil.touchObject(object, layerViewport, screenViewport, graphics2D) is
	 * operating correctly, it should return a list of two touch events, first touch down then
	 * touch up on the object supplied's position
	 */
	@Test
	public void touchObject()
	{
		// Set up game object
		GameObject gameObject = new GameObject(
				TEST_GAMEOBJECT_X,
				TEST_GAMEOBJECT_Y,
				TEST_GAMEOBJECT_WIDTH,
				TEST_GAMEOBJECT_HEIGHT,
				bitmap,
				testGameScreen
		);
		
		// Get the touch events from touchObject
		List<TouchEvent> touchEvents = TestUtil.touchObject(gameObject, graphics2D);
		// Check that there are two touch events returned
		assertTrue(touchEvents.size() == EXPECTED_TOUCH_EVENTS);
		
		// Get the two events
		TouchEvent firstEvent = touchEvents.get(0);
		TouchEvent secondEvent = touchEvents.get(1);
		
		// Check the first event is touch down on the object position
		assertEquals(firstEvent.type, TouchEvent.TOUCH_DOWN);
		assertEquals(firstEvent.x, gameObject.position.x);
		assertEquals(firstEvent.y, gameObject.position.y);
		
		// Check the second event is touch up on the object position
		assertEquals(secondEvent.type, TouchEvent.TOUCH_UP);
		assertEquals(secondEvent.x, gameObject.position.x);
		assertEquals(secondEvent.y, gameObject.position.y);
	}
}