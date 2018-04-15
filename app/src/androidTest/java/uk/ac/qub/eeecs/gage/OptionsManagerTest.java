package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.game.options.OptionsManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jamie T on 10/12/2017.
 * Used to check that the options manager is operating correctly
 */
@RunWith(AndroidJUnit4.class)
public class OptionsManagerTest
{
	// Test values and keys
	private static final String TEST_BOOL_KEY = "test_bool";
	private static final String TEST_INT_KEY = "test_bool";
	private static final Boolean TEST_BOOL_VALUE = true;
	private static final int TEST_INT_VALUE = 7;
	
	/**
	 * Tests setting boolean value in options manager
	 */
	@Test
	public void setBool()
	{
		Context appContext = InstrumentationRegistry.getTargetContext();
		
		OptionsManager manager = new OptionsManager(appContext);
		
		// Clear any existing shared preferences
		clearSharedPrefs(appContext);
		
		// Check that when an option is set the right value is returned
		manager.setOption(OptionsManager.FPS_COUNTER, TEST_BOOL_VALUE);
		assertEquals(TEST_BOOL_VALUE, manager.getBoolOption(OptionsManager.FPS_COUNTER));
		
		// Check that when an option is set the right value is returned
		manager.setOption(TEST_BOOL_KEY, TEST_BOOL_VALUE);
		assertEquals(TEST_BOOL_VALUE, manager.getBoolOption(TEST_BOOL_KEY));
		clearSharedPrefs(appContext);
	}
	
	/**
	 * Tests getting boolean value int he options manager
	 */
	@Test
	public void getBool()
	{
		Context appContext = InstrumentationRegistry.getTargetContext();
		
		// Clear any existing shared preferences
		clearSharedPrefs(appContext);
		
		OptionsManager manager = new OptionsManager(appContext);
		
		// Check that with no stored values the returned value is false
		assertEquals(false, manager.getBoolOption(OptionsManager.FPS_COUNTER));
		// Check that when an invalid option is requested the value returned is false
		assertEquals(false, manager.getBoolOption("invalid"));
		// Check that with no stored values and a default provided the default is returned
		assertEquals(true, manager.getBoolOption(OptionsManager.FPS_COUNTER, true));
		clearSharedPrefs(appContext);
	}
	
	/**
	 * Tests setting an integer value in the options manager
	 */
	@Test
	public void setInt()
	{
		Context appContext = InstrumentationRegistry.getTargetContext();
		
		OptionsManager manager = new OptionsManager(appContext);
		
		// Clear any existing shared preferences
		clearSharedPrefs(appContext);
		
		// Check that when an option is set the right value is returned
		manager.setOption(OptionsManager.MUSIC_VOLUME, TEST_INT_VALUE);
		assertEquals(TEST_INT_VALUE, (long) manager.getIntOption(OptionsManager.MUSIC_VOLUME));
		
		// Check that when an option is set the right value is returned
		manager.setOption(TEST_INT_KEY, TEST_INT_VALUE);
		assertEquals(TEST_INT_VALUE, (long) manager.getIntOption(TEST_INT_KEY));
		clearSharedPrefs(appContext);
	}
	
	/**
	 * Tests getting an integer value in the options manager
	 */
	@Test
	public void getInt()
	{
		Context appContext = InstrumentationRegistry.getTargetContext();
		
		// Clear any existing shared preferences
		clearSharedPrefs(appContext);
		
		OptionsManager manager = new OptionsManager(appContext);
		
		// Check that with no stored values the returned value is 0
		assertEquals(0, (long) manager.getIntOption(OptionsManager.MUSIC_VOLUME));
		// Check that when an invalid option is requested the value returned is 0
		assertEquals(0, (long) manager.getIntOption("invalid"));
		// Check that with no stored values and a default provided the default is returned
		assertEquals(5, (long) manager.getIntOption(OptionsManager.MUSIC_VOLUME), 5);
		clearSharedPrefs(appContext);
	}
	
	/**
	 * Clears shared preferences of any existing key values
	 * @param context the context to use to clear the shared preferences
	 */
	private void clearSharedPrefs(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(OptionsManager.PREFERENCES_STORAGE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
}