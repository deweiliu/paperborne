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
	
	@Test
	public void makeScreen() throws Exception {
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
		
		// Check that with no stored values the returned value is 0
		assertEquals(0, (long)manager.getIntOption(OptionsManager.MUSIC_VOLUME));
		// Check that when an invalid option is requested the value returned is 0
		assertEquals(0, (long)manager.getIntOption("invalid"));
		// Check that with no stored values and a default provided the default is returned
		assertEquals(5, (long)manager.getIntOption(OptionsManager.MUSIC_VOLUME), 5);
		
		// Check that when an option is set the right value is returned
		manager.setOption(OptionsManager.FPS_COUNTER, true);
		assertEquals(true, manager.getBoolOption(OptionsManager.FPS_COUNTER));
		
		// Check that when an option is set the right value is returned
		manager.setOption(OptionsManager.MUSIC_VOLUME, 5);
		assertEquals(5, (long)manager.getIntOption(OptionsManager.MUSIC_VOLUME));
		
	}
	
	private void clearSharedPrefs(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(OptionsManager.PREFERENCES_STORAGE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
}