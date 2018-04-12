package uk.ac.qub.eeecs.gage.ui_Tests;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.ui.PopUp;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 09/04/2018.
 * Used for unit testing the PopUp UI element
 */

@RunWith(MockitoJUnitRunner.class)
public class PopUpTest
{
	// Test name for the game screen
	private final static String TEST_GAME_SCREEN = "test_game_screen";
	
	// Test values for the pop up
	private final static String TEST_MESSAGE = "test_message";
	private final static long TEST_DURATION = 2;
	private final static float TEST_FONT_SIZE = 20.0f;
	
	// Mock values for creating the pop up and game screen
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
	
	// Test game screen for creating pop up in
	private GameScreen testGameScreen;
	
	@Before
	public void setup() {
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
	
	@Test
	public void popUpVisibleTest()
	{
		// Elapsed time required for update calls
		ElapsedTime elapsedTime = new ElapsedTime();
		
		// Create a new pop up
		PopUp testPopUp = new PopUp(
				TEST_MESSAGE,
				TEST_DURATION,
				TEST_FONT_SIZE,
				bitmap,
				testGameScreen
		);
		
		// Check the pop up's starting state is not visible
		assertEquals(false, testPopUp.isVisible());
		
		// Show the pop up
		testPopUp.show();
		
		// Update pop up
		testPopUp.update(elapsedTime);
		
		// Check the pop up's state is visible after it is shown
		assertEquals(true, testPopUp.isVisible());
		
		try
		{
			// Wait until 1 second after the pop up is supposed to disappear
			Thread.sleep((TEST_DURATION + 1) * 1000);
		}
		catch (InterruptedException e)
		{
			// If there is an exception print the stack trace and fail the test
			e.printStackTrace();
			fail();
		}
		
		// Update pop up
		testPopUp.update(elapsedTime);
		
		// Check the pop up's state after waiting longer than the duration is not visible
		assertEquals(false, testPopUp.isVisible());
		
	}
	
}