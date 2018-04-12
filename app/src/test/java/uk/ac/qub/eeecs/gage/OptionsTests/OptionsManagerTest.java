package uk.ac.qub.eeecs.gage.OptionsTests;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.game.options.OptionsManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie T on 11/04/2018.
 * Used to check that the options manager is operating correctly
 */

@RunWith(MockitoJUnitRunner.class)
public class OptionsManagerTest
{
	
	// Test values for integer testing
	private static final int TEST_FIRST_VAL = 6;
	private static final int TEST_SECOND_VAL = 12;
	
	// Mock values for the options manager tests
	@Mock
	private Game game;
	@Mock
	private Context context;
	@Mock
	private SharedPreferences sharedPreferences;
	
	/**
	 * Set up for the options manager tests
	 */
	@Before
	public void setup() {
		// Mock Context
		when(game.getContext()).thenReturn(context);
		// Mock shared preferences
		when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
		// Mock when shared preferences is called, return the default value passed as second parameter
		when(sharedPreferences.getBoolean(any(String.class), any(Boolean.class))).thenAnswer(new Answer<Boolean>()
		{
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
				// Return whatever the second parameter was
				return (Boolean)invocation.getArguments()[1];
			}
		});
		when(sharedPreferences.getInt(any(String.class), any(Integer.class))).thenAnswer(new Answer<Integer>()
		{
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable
			{
				// Return whatever the second parameter was
				return (Integer)invocation.getArguments()[1];
			}
		});
	}
	
	/**
	 * Tests that the OptionsManager.getBoolOption(key, default) returns the default value if
	 * no value is found
	 */
	@Test
	public void defaultBooleanTest()
	{
		OptionsManager optionsManager = new OptionsManager(context);
		
		assertTrue(optionsManager.getBoolOption(OptionsManager.FPS_COUNTER, true));
		
		assertFalse(optionsManager.getBoolOption(OptionsManager.FPS_COUNTER, false));
		
		assertTrue(optionsManager.getBoolOption(OptionsManager.VISUAL_EFFECTS, true));
		
		assertFalse(optionsManager.getBoolOption(OptionsManager.VISUAL_EFFECTS, false));
		
		assertTrue(optionsManager.getBoolOption(OptionsManager.MUSIC_MUTED, true));
		
		assertFalse(optionsManager.getBoolOption(OptionsManager.MUSIC_MUTED, false));
	}
	
	/**
	 * Tests that the OptionsManager.getIntOption(key, default) returns the default value if
	 * no value is found
	 */
	@Test
	public void defaultIntegerTest()
	{
		OptionsManager optionsManager = new OptionsManager(context);
		
		assertEquals(TEST_FIRST_VAL, (int)optionsManager.getIntOption(OptionsManager.MUSIC_VOLUME, TEST_FIRST_VAL));
		
		assertEquals(TEST_SECOND_VAL, (int)optionsManager.getIntOption(OptionsManager.MUSIC_VOLUME, TEST_SECOND_VAL));
		
		assertEquals(TEST_FIRST_VAL, (int)optionsManager.getIntOption(OptionsManager.SOUND_VOLUME, TEST_FIRST_VAL));
		
		assertEquals(TEST_SECOND_VAL, (int)optionsManager.getIntOption(OptionsManager.SOUND_VOLUME, TEST_SECOND_VAL));
	}
	
}
