package uk.ac.qub.eeecs.gage.WorldScreenTests;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;
import uk.ac.qub.eeecs.game.worldScreen.SaveGame;
import uk.ac.qub.eeecs.game.worldScreen.WorldScreen;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorldScreenTest {
	
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
	
	private ScreenManager screenManager;
	
	private SaveGame saveGame;
	
	final private static int DEFAULT_SAVE_SLOT = 1;
	
	final private static  int EXPECTED_LEVELS = 3;
	
	final private static  String MOCK_DATA = "{\"startingDeck\":[{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1}],\"levels\":[{\"id\":\"level_one\",\"name\":\"Level One\",\"bitmap\":\"LevelOne\",\"bitmapPath\":\"img/Level.png\",\"xPercent\":0.25,\"yPercent\":0.5,\"width\":128,\"height\":128,\"deck\":[{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1},{\"name\":\"Weak Man\",\"bitmap\":\"Card\",\"attackValue\":1,\"healthValue\":1,\"manaCost\":1}]},{\"id\":\"level_two\",\"name\":\"Level Two\",\"bitmap\":\"LevelTwo\",\"bitmapPath\":\"img/Level.png\",\"xPercent\":0.5,\"yPercent\":0.5,\"width\":128,\"height\":128,\"deck\":[{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1}]},{\"id\":\"level_three\",\"name\":\"Level Three\",\"bitmap\":\"LevelThree\",\"bitmapPath\":\"img/Level.png\",\"xPercent\":0.75,\"yPercent\":0.5,\"width\":128,\"height\":128,\"deck\":[{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1},{\"name\":\"Dragon\",\"bitmap\":\"Card\",\"attackValue\":5,\"healthValue\":5,\"manaCost\":1}]}]}";
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
	
	
	@Before
	public void setup() {
		screenManager = new ScreenManager();
		when(game.getScreenManager()).thenReturn(screenManager);
		when(game.getAssetManager()).thenReturn(assetManager);
		when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
		when(game.getInput()).thenReturn(input);
		// Mock Context
		when(game.getContext()).thenReturn(context);
		// Mock Android Asset Manager
		when(context.getAssets()).thenReturn(assets);
		try
		{
			// Whenever a level file is requested to be opened, return inputstream of mock JSON data
			when(assets.open(any(String.class))).thenReturn(new ByteArrayInputStream(MOCK_DATA.getBytes()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		saveGame = new SaveGame(1, new ArrayList<LevelCard>(), new ArrayList<String>());
	}
	
	//TODO: Fix this failing unit test
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
		assertTrue(worldScreen.getLevels().size() == EXPECTED_LEVELS);
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
	
	//TODO: Fix this failing unit test
	@Test
	public void gameLevelCreationTest()
	{
		WorldScreen worldScreen = new WorldScreen(game);
		// Unit test for a game level with no prerequisites
		Level noPrerequisiteLevel = new Level(
				"test_one",
				"Test One",
				1,
				1,
				2,
				2,
				new ArrayList<LevelCard>(),
				"BitMap",
				"BitMap"
		);
		// Check each field isn't null
		assertNotNull(noPrerequisiteLevel.getId());
		assertNotNull(noPrerequisiteLevel.getName());
		assertNotNull(noPrerequisiteLevel.getButton());
		assertNotNull(noPrerequisiteLevel.getPrerequisites());
		// Check there are no prerequisites
		assertTrue(noPrerequisiteLevel.getPrerequisites().size() == 0);
		
		// Set up prerequisite list
		List<String> prerequisites = new ArrayList<>();
		prerequisites.add(noPrerequisiteLevel.getId());
		
		// Unit test for a game level with some prerequisites
		Level prerequisiteLevel = new Level(
				"test_two",
				"Test Two",
				1,
				1,
				2,
				2,
				new ArrayList<LevelCard>(),
				"BitMap",
				"BitMap",
				prerequisites
		);
		// Check each field isn't null
		assertNotNull(prerequisiteLevel.getId());
		assertNotNull(prerequisiteLevel.getName());
		assertNotNull(prerequisiteLevel.getButton());
		assertNotNull(prerequisiteLevel.getPrerequisites());
		// Check there is 1 prerequisite
		assertTrue(prerequisiteLevel.getPrerequisites().size() == 1);
		// Check that the ID of the prerequisite is what is expected
		// Only 1 item so the first index 0 should reference the only item
		assertTrue(prerequisiteLevel.getPrerequisites().get(0).equals(noPrerequisiteLevel.getId()));
	}
	
	@Test
	public void worldScreenTest() {
//		WorldScreen worldScreen = new WorldScreen(game);
//		List<Level> levels = worldScreen.getLevels();
		
		// TODO: Fix this test below, is supposed to click each level and check the level updates
//		for(int i = 0; i < levels.size(); i++)
//		{
//			ElapsedTime elapsedTime = new ElapsedTime();
//			PushButton levelButton = levels.get(i).getButton();
//			levelButton.setPosition(i*20, 50);
//			TouchEvent touchPosition = new TouchEvent();
//			touchPosition.x = levelButton.position.x;
//			touchPosition.y = levelButton.position.y;
//			touchPosition.type = TouchEvent.TOUCH_DOWN;
//			List<TouchEvent> touchEvents = new ArrayList<>();
//			touchEvents.add(touchPosition);
//			when(input.getTouchEvents()).thenReturn(touchEvents);
//
//			worldScreen.update(elapsedTime);
//
//			assertEquals(worldScreen.getLevels().get(worldScreen.getCurrentLevel()).getId(), levels.get(i).getId());
//		}
	}
	
	
	@Test
	public void writeSaveFileTest()
	{
	
	}
	
	@Test
	public void readSaveFileTest()
	{
	
	}
	
	@Test
	public void loadSaveGameTest()
	{
	
	}
}

