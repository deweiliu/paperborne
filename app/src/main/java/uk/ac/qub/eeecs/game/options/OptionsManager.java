package uk.ac.qub.eeecs.game.options;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jamie T on 08/12/2017.
 * Used to interact with Game Options
 */

public class OptionsManager
{
	/**
	 * Constants to define different types of options that can be set and changed
	 */
	public static final String PREFERENCES_STORAGE_KEY = "paperborne_prefs";
	public static final String FPS_COUNTER = "fps_counter";
	public static final String VISUAL_EFFECTS = "visual_effects";
	public static final String MUSIC_VOLUME = "music_volume";
	public static final String SOUND_VOLUME = "sound_volume";
	
	// Shared Preferences for interacting with Android's Key-Value storage
	private SharedPreferences mSharedPrefs;
	
	/**
	 * @param context calling context
	 */
	public OptionsManager(Context context)
	{
		// Set up shared preferences with the activity provided
		mSharedPrefs = context.getSharedPreferences(PREFERENCES_STORAGE_KEY, Context.MODE_PRIVATE);
	}
	
	/**
	 * Update a boolean user game option
	 * @param option the option to update (from the OptionsManager class)
	 * @param value the boolean value to set the option to
	 */
	public void setOption(String option, Boolean value)
	{
		// Open the shared preferences editor, update the value and apply the changes
		SharedPreferences.Editor editor = mSharedPrefs.edit();
		editor.putBoolean(option, value);
		editor.apply();
	}
	
	/**
	 * Update an integer user game option
	 * @param option the option to update (from the OptionsManager class)
	 * @param value the integer value to set the option to
	 */
	public void setOption(String option, Integer value)
	{
		// Open the shared preferences editor, update the value and apply the changes
		SharedPreferences.Editor editor = mSharedPrefs.edit();
		editor.putInt(option, value);
		editor.apply();
	}
	
	/**
	 * Retrieve an existing boolean option, default value is false
	 * @param option the option to retrieve
	 * @return the boolean option value
	 */
	public Boolean getBoolOption(String option)
	{
		return getBoolOption(option, false);
	}
	
	/**
	 * Retrieve an existing boolean option, default is provided as the def parameter
	 * @param option the option to retrieve
	 * @param def the default value if no existing option is found
	 * @return the boolean option value
	 */
	public Boolean getBoolOption(String option, Boolean def)
	{
		// Open shared preferences and return the value with the matching key
		return mSharedPrefs.getBoolean(option, def);
	}
	
	/**
	 * Retrieve an existing integer option, default is 0
	 * @param option the option to retrieve
	 * @return the integer option value
	 */
	public Integer getIntOption(String option)
	{
		return getIntOption(option, 0);
	}
	
	/**
	 * Retrieve an existing integer option, the default value is provided as the def parameter
	 * @param option the option to retrieve
	 * @param def the default value if no existing option is found
	 * @return the integer option value
	 */
	public Integer getIntOption(String option, Integer def)
	{
		return mSharedPrefs.getInt(option, def);
	}
}
