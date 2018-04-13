package uk.ac.qub.eeecs.gage.WorldScreenTests;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.TestUtil;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.game.SaveManager;
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.WorldScreen;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 28/01/2018.
 * Used to check that the world screen is operating correctly
 */

@RunWith(MockitoJUnitRunner.class)
public class WorldScreenTest {
	
	// Screen dimensions
	private final static int TEST_SCREEN_WIDTH = 1200;
	private final static int TEST_SCREEN_HEIGHT = 800;
	
	// The expected number of levels in the level data
	final private static int TEST_EXPECTED_LEVEL_COUNT = 3;
	
	// Mock values for the world screen tests
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
	private FileOutputStream outputStream;
	@Mock
	private IGraphics2D graphics2D;
	
	private ScreenManager screenManager;
	
	final private static String MOCK_LEVEL_DATA = "{\"startingDeck\":[{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1}],\"levels\":[{\"id\":\"level_one\",\"name\":\"Level One\",\"bitmap\":\"LevelOne\",\"bitmapPath\":\"img/Level.png\",\"xPercent\":0.25,\"yPercent\":0.5,\"width\":128,\"height\":128,\"deck\":[{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1}]},{\"id\":\"level_two\",\"name\":\"Level Two\",\"bitmap\":\"LevelTwo\",\"bitmapPath\":\"img/Level.png\",\"xPercent\":0.5,\"yPercent\":0.5,\"width\":128,\"height\":128,\"deck\":[{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1}]},{\"id\":\"level_three\",\"name\":\"Level Three\",\"bitmap\":\"LevelThree\",\"bitmapPath\":\"img/Level.png\",\"xPercent\":0.75,\"yPercent\":0.5,\"width\":128,\"height\":128,\"deck\":[{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1}]}]}";
	/*
	{
	  "startingDeck":
      [
		{
		  "name":"Weak Man",
		  "bitmap":"Card",
		  "attackValue":1,
		  "healthValue":1,
		  "manaCost":1
		},
		{
		  "name":"Weak Man",
		  "bitmap":"Card",
		  "attackValue":1,
		  "healthValue":1,
		  "manaCost":1
		},
		{
		  "name":"Weak Man",
		  "bitmap":"Card",
		  "attackValue":1,
		  "healthValue":1,
		  "manaCost":1
		},
		{
		  "name":"Weak Man",
		  "bitmap":"Card",
		  "attackValue":1,
		  "healthValue":1,
		  "manaCost":1
		},
		{
		  "name":"Weak Man",
		  "bitmap":"Card",
		  "attackValue":1,
		  "healthValue":1,
		  "manaCost":1
		}
	  ],
	  "levels":
	  [
		{
		  "id":"level_one",
		  "name": "Level One",
		  "bitmap":"LevelOne",
		  "bitmapPath":"img/Level.png",
		  "xPercent": 0.25,
		  "yPercent": 0.50,
		  "width": 128,
		  "height": 128,
		  "deck":
		  [
			{
			  "name":"Weak Man",
			  "bitmap":"Card",
			  "attackValue":1,
			  "healthValue":1,
			  "manaCost":1
			},
			{
			  "name":"Weak Man",
			  "bitmap":"Card",
			  "attackValue":1,
			  "healthValue":1,
			  "manaCost":1
			},
			{
			  "name":"Weak Man",
			  "bitmap":"Card",
			  "attackValue":1,
			  "healthValue":1,
			  "manaCost":1
			},
			{
			  "name":"Weak Man",
			  "bitmap":"Card",
			  "attackValue":1,
			  "healthValue":1,
			  "manaCost":1
			},
			{
			  "name":"Weak Man",
			  "bitmap":"Card",
			  "attackValue":1,
			  "healthValue":1,
			  "manaCost":1
			}
		  ]
		},
		{
		  "id":"level_two",
		  "name": "Level Two",
		  "bitmap":"LevelTwo",
		  "bitmapPath":"img/Level.png",
		  "xPercent": 0.50,
		  "yPercent": 0.50,
		  "width": 128,
		  "height": 128,
		  "deck":
		  [
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			}
		  ]
		},
		{
		  "id":"level_three",
		  "name": "Level Three",
		  "bitmap":"LevelThree",
		  "bitmapPath":"img/Level.png",
		  "xPercent": 0.75,
		  "yPercent": 0.50,
		  "width": 128,
		  "height": 128,
		  "deck":
		  [
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			},
			{
			  "name":"Dragon",
			  "bitmap":"Card",
			  "attackValue":5,
			  "healthValue":5,
			  "manaCost":1
			}
		  ]
		}
	  ]
	}
	*/
	
	
	final private static String MOCK_SAVE_DATA = "{\"saves\":[{\"id\":0,\"deck\":[{\"name\":\"Weak Man\",\"bitmap\":\"1 cost\",\"healthValue\":1,\"manaCost\":1,\"attackValue\":1},{\"name\":\"Weak Man\",\"bitmap\":\"1 cost\",\"healthValue\":1,\"manaCost\":1,\"attackValue\":1},{\"name\":\"Dog\",\"bitmap\":\"1 cost\",\"healthValue\":1,\"manaCost\":1,\"attackValue\":1}],\"completed\":[]},{\"id\":1,\"deck\":[{\"name\":\"Weak Man\",\"bitmap\":\"1 cost\",\"healthValue\":1,\"manaCost\":1,\"attackValue\":1}],\"completed\":[]},{\"id\":2,\"deck\":[{\"name\":\"Weak Man\",\"bitmap\":\"1 cost\",\"healthValue\":1,\"manaCost\":1,\"attackValue\":1},{\"name\":\"Weak Man\",\"bitmap\":\"1 cost\",\"healthValue\":1,\"manaCost\":1,\"attackValue\":1}],\"completed\":[]}]}";
	/*
	{
		"saves":[
		{
			"id":0,
				"deck":[
			{
				"name":"Weak Man",
					"bitmap":"1 cost",
					"healthValue":1,
					"manaCost":1,
					"attackValue":1
			},
			{
				"name":"Weak Man",
					"bitmap":"1 cost",
					"healthValue":1,
					"manaCost":1,
					"attackValue":1
			},
			{
				"name":"Dog",
					"bitmap":"1 cost",
					"healthValue":1,
					"manaCost":1,
					"attackValue":1
			}
         ],
			"completed":[

         ]
		},
		{
			"id":1,
				"deck":[
			{
				"name":"Weak Man",
					"bitmap":"1 cost",
					"healthValue":1,
					"manaCost":1,
					"attackValue":1
			}
         ],
			"completed":[

         ]
		},
		{
			"id":2,
				"deck":[
			{
				"name":"Weak Man",
				"bitmap":"1 cost",
				"healthValue":1,
				"manaCost":1,
				"attackValue":1
			},
			{
				"name":"Weak Man",
				"bitmap":"1 cost",
				"healthValue":1,
				"manaCost":1,
				"attackValue":1
			}
         ],
			"completed":[

         ]
		}
   	  ]
	}
	*/
	
	/**
	 * Set up for the world screen tests
	 */
	@Before
	public void setup() {
		screenManager = new ScreenManager();
		when(game.getScreenManager()).thenReturn(screenManager);
		when(game.getAssetManager()).thenReturn(assetManager);
		when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
		when(game.getInput()).thenReturn(input);
		// Mock Context
		when(game.getContext()).thenReturn(context);
		when(game.getActivity()).thenReturn(activity);
		// Mock Android Asset Manager
		when(context.getAssets()).thenReturn(assets);
		when(game.getScreenWidth()).thenReturn(TEST_SCREEN_WIDTH);
		when(game.getScreenHeight()).thenReturn(TEST_SCREEN_HEIGHT);
		try
		{
			// Using thenAnswer rather than thenReturn in order to return a new stream object each
			// time it is called to allow for multiple uses after the stream is closed
			// Whenever a level file is requested to be opened, return input stream of mock JSON data
			when(assets.open(SaveManager.LEVEL_FILE)).thenAnswer(
					new Answer<ByteArrayInputStream>() {
						public ByteArrayInputStream answer(InvocationOnMock invocation) {
							return new ByteArrayInputStream(MOCK_LEVEL_DATA.getBytes());
						}
					});
			// Whenever a save file is requested to be opened, return null
			when(activity.openFileInput(SaveManager.SAVE_FILE)).thenReturn(null);
			// Whenever a save file is requested to be opened for writing, return a mocked output stream
			when(activity.openFileOutput(SaveManager.SAVE_FILE, Context.MODE_PRIVATE)).thenAnswer(
					new Answer<FileOutputStream>() {
						public FileOutputStream answer(InvocationOnMock invocation) {
							return outputStream;
							
						}
					});
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks that the levels are read in correctly
	 */
	@Test
	public void readLevelsTest()
	{
		// Setup test data
		WorldScreen worldScreen = new WorldScreen(game);
		game.getScreenManager().addScreen(worldScreen);
		
		// Check that the level list isn't null
		assertNotNull(worldScreen.getLevels());
		// Check that levels have been loaded
		assertTrue(worldScreen.getLevels().size() > 0);
		// Check that the expected number of levels have been loaded
		assertTrue(worldScreen.getLevels().size() == TEST_EXPECTED_LEVEL_COUNT);
		// Get levels
		List<Level> levels = worldScreen.getLevels();
		// List to track level IDs
		List<String> levelIDs = new ArrayList<>();
		// For each loaded level
		for (int i = 0; i < levels.size(); i++)
		{
			// Get the level
			Level level = levels.get(i);
			// Check the Level ID isn't null
			assertNotNull(level.getId());
			// Check that the Level ID hasn't already been used
			assertFalse(levelIDs.contains(level.getId()));
			// Add new ID to the level list
			levelIDs.add(level.getId());
			
			// Check name and button aren't null
			assertNotNull(level.getName());
			assertNotNull(level.getButton());
		}
	}
	
	/**
	 * Checks the correct level is selected when a level's button is pressed
	 */
	@Test
	public void levelButtonTest() {
		// Set up world screen
		WorldScreen worldScreen = new WorldScreen(game);
		// Get list of levels
		List<Level> levels = worldScreen.getLevels();
		
		// Elapsed time for update methods
		ElapsedTime elapsedTime = new ElapsedTime();
		
		// Go through each level and press it's button, then check the current level is updated
		// to the level that has been pressed
		for(int i = 0; i < levels.size(); i++)
		{
			// Get the level's button
			PushButton levelButton = levels.get(i).getButton();
			List<TouchEvent> touchEvents = TestUtil.touchObject(levelButton, graphics2D);
			when(input.getTouchEvents()).thenReturn(touchEvents);
			// Update world screen, which includes updating the buttons
			worldScreen.update(elapsedTime);
			
			// Check that the world screen's current level is the level that was just pressed
			assertEquals(worldScreen.getLevels().get(worldScreen.getCurrentLevel()).getId(), levels.get(i).getId());
		}
	}
	
}

