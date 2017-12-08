package uk.ac.qub.eeecs.game.options;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jamie on 08/12/2017.
 */

public class OptionsManager
{
	public static final String FPS_COUNTER = "fps_counter";
	public static final String VISUAL_EFFECTS = "visual_effects";
	public static final String MUSIC_VOLUME = "music_volume";
	public static final String SOUND_VOLUME = "sound_volume";
	
	private SharedPreferences sharedPref;
	
	public OptionsManager(Activity activity)
	{
		sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
	}
	
	public void setOption(String option, Boolean value)
	{
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(option, value);
		editor.apply();
	}
	
	public void setOption(String option, Integer value)
	{
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(option, value);
		editor.apply();
	}
	
	public Boolean getBoolOption(String option)
	{
		return getBoolOption(option, false);
	}
	
	public Boolean getBoolOption(String option, Boolean def)
	{
		return sharedPref.getBoolean(option, def);
	}
	
	public Integer getIntOption(String option)
	{
		return getIntOption(option, 0);
	}
	
	public Integer getIntOption(String option, Integer def)
	{
		return sharedPref.getInt(option, def);
	}
}
