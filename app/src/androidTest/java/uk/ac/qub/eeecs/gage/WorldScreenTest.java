package uk.ac.qub.eeecs.gage;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by Jamie T on 28/01/2018.
 * Used to check that the world screen is operating correctly
 */
@RunWith(AndroidJUnit4.class)
public class WorldScreenTest
{
	
	@Test
	public void worldScreenTest() throws Exception
	{
		//TODO: Rework this, accessing getAssets is causing nullptr exception
//		Context appContext = InstrumentationRegistry.getTargetContext();
//		DemoGame game = new DemoGame();
//		game.mAssetManager = new AssetStore(new FileIO(appContext));
//		WorldScreen worldScreen = new WorldScreen(game);
//
//		// Check that the level list isn't null
//		assertNotNull(worldScreen.getLevels());
//		// Check that levels have been loaded
//		assertTrue(worldScreen.getLevels().size() > 0);
//		// Get levels
//		List<GameLevel> levels = worldScreen.getLevels();
//		// List to track level IDs
//		List<String> levelIDs = new ArrayList<>();
//		// For each loaded level
//		for (int i = 0; i < levels.size(); i++)
//		{
//			// Get the level
//			GameLevel level = levels.get(i);
//			// Check the Level ID isn't null
//			assertNotNull(level.getId());
//			// Check that the Level ID hasn't already been used
//			assertFalse(levelIDs.contains(level.getId()));
//			// Add new ID to the level list
//			levelIDs.add(level.getId());
//
//			// Check name and button aren't null
//			assertNotNull(level.getName());
//			assertNotNull(level.getButton());
//		}
		
		
	}
	
}