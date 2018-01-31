package uk.ac.qub.eeecs.game.worldScreen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;

/**
 * Created by Jamie T on 25/01/2018.
 * Handles all single player level selection and progression
 */

public class WorldScreen extends GameScreen
{
	
	private final String LEVEL_LIST = "levels";
	private final String LEVEL_ID = "id";
	private final String LEVEL_NAME = "name";
	private final String LEVEL_BITMAP = "bitmap";
	private final String LEVEL_BITMAP_PATH = "bitmapPath";
	private final String LEVEL_X_PERCENT = "xPercent";
	private final String LEVEL_Y_PERCENT = "yPercent";
	private final String LEVEL_WIDTH = "width";
	private final String LEVEL_HEIGHT = "height";
	
	// Game levels filename, in the future could be dynamic to allow for different campaigns
	private final String LEVEL_FILE = "levels/game_levels.json";
	
	// Level Sprite width/height
	private final int DEFAULT_LEVEL_WIDTH = 128;
	private final int DEFAULT_LEVEL_HEIGHT = 128;
	
	// Player Character Sprite width/height
	private final int PLAYER_WIDTH = 128;
	private final int PLAYER_HEIGHT = 128;
	
	// Launch battle button width/height/distance from bottom of screen
	private final int LAUNCH_BUTTON_WIDTH = 512;
	private final int LAUNCH_BUTTON_HEIGHT = 128;
	private final int LAUNCH_BUTTON_Y_SEPERATION = 128;
	
	// Paint defining how text is to be drawn
	private Paint mTextPainter;
	// Button to launch a battle
	private PushButton mLaunchButton;
	// World Map Background
	private GameObject mWorldBackground;
	// List of available levels for the player to choose from
	private List<GameLevel> mLevels;
	// The player character sprite
	private GameObject mWorldPlayer;
	// The index in the available levels of the currently selected level
	private int mCurrentLevel = 0;
	
	private AssetStore assetManager;
	
	public WorldScreen(Game game)
	{
		super("WorldScreen", game);
		// Initialise level list
		mLevels = new ArrayList<>();
		// Get asset manager
		assetManager = game.getAssetManager();
		// Load in any bitmaps used in the world screen
		assetManager.loadAndAddBitmap("WorldMap", "img/WorldMap.png");
		assetManager.loadAndAddBitmap("Character", "img/Character.png");
		assetManager.loadAndAddBitmap("LaunchButton", "img/BlueButton.png");
		
		// Create the world map background
		mWorldBackground = new GameObject(
				game.getScreenWidth() / 2,
				game.getScreenHeight() / 2,
				mGame.getScreenWidth(),
				mGame.getScreenHeight(),
				assetManager.getBitmap("WorldMap"),
				this
		);
		
		// Load in levels from JSON campaign definition
		mLevels = loadLevels(LEVEL_FILE);
		
		if(mLevels.size() < 1)
		{
			// If there are no loaded levels
			// Display an error and return to the main menu
			Toast.makeText(game.getActivity(), game.getContext().getString(R.string.broken_level_file), Toast.LENGTH_LONG).show();
			mGame.getScreenManager().removeScreen(this.getName());
			mGame.getScreenManager().addScreen(new MenuScreen(mGame));
		}
		
		// Create the world player sprite, with their X and Y aligned to current/default level
		mWorldPlayer = new GameObject(
				mLevels.get(mCurrentLevel).getButton().position.x,
				mLevels.get(mCurrentLevel).getButton().position.y - DEFAULT_LEVEL_HEIGHT,
				PLAYER_WIDTH,
				PLAYER_HEIGHT,
				assetManager.getBitmap("Character"),
				this
		);
		
		// Create the launch battle button
		mLaunchButton = new PushButton(
				game.getScreenWidth() / 2,
				game.getScreenHeight() - LAUNCH_BUTTON_Y_SEPERATION,
				LAUNCH_BUTTON_WIDTH,
				LAUNCH_BUTTON_HEIGHT,
				"LaunchButton",
				this
		);
		
		// Set up text painter with styles
		mTextPainter = new Paint();
		mTextPainter.setTextSize(60);
		mTextPainter.setColor(Color.BLACK);
		mTextPainter.setTextAlign(Paint.Align.CENTER);
	}
	
	@Override
	public void update(ElapsedTime elapsedTime)
	{
		// For each available level
		for (int i = 0; i < mLevels.size(); i++)
		{
			// Get the button for the level
			PushButton button = mLevels.get(i).getButton();
			// Update the button
			button.update(elapsedTime);
			if (button.isPushTriggered())
			{
				// If the button for the level has been pressed
				// Set the currently selected level index to the index of the pressed level
				mCurrentLevel = i;
				// Move the player sprite to above the level sprite
				mWorldPlayer.setPosition(button.position.x, button.position.y - DEFAULT_LEVEL_HEIGHT);
			}
		}
		// Update launch button
		mLaunchButton.update(elapsedTime);
		if (mLaunchButton.isPushTriggered())
		{
			// If the launch button has been pressed load the selected level's battle
			loadBattle();
		}
	}
	
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D)
	{
		// Draw the world map background
		mWorldBackground.draw(elapsedTime, graphics2D);
		// For each level loaded
		for (int i = 0; i < mLevels.size(); i++)
		{
			// Draw the level's button
			mLevels.get(i).getButton().draw(elapsedTime, graphics2D);
		}
		// Draw the player sprite and launch button
		mWorldPlayer.draw(elapsedTime, graphics2D);
		mLaunchButton.draw(elapsedTime, graphics2D);
		// Draw the text stating which level is currently selected above the launch button
		graphics2D.drawText(
				mLevels.get(mCurrentLevel).getName(),
				mGame.getScreenWidth() / 2,
				mLaunchButton.position.y - LAUNCH_BUTTON_HEIGHT,
				mTextPainter
		);
	}
	
	/**
	 * Loads a game level's battle
	 */
	private void loadBattle()
	{
		//TODO: Add level specific details for launching a battle, e.g. deck, opponent etc.
		mGame.getScreenManager().removeScreen(this.getName());
		mGame.getScreenManager().addScreen(new CardDemoScreen(mGame));
	}
	
	/**
	 * Loads a world's levels from a level file
	 * @param levelFileName level file path, root folder is assets/
	 * @return The loaded levels
	 */
	private List<GameLevel> loadLevels(String levelFileName)
	{
		try
		{
			// Gets the  JSON in string form form the file
			JSONObject jsonData = new JSONObject(getJSONString(mGame.getContext(), levelFileName));
			// Gets the levels as a JSON array
			JSONArray jsonLevels = jsonData.getJSONArray(LEVEL_LIST);
			// Sets up return list containing all parsed levels
			List<GameLevel> levels = new ArrayList<>();
			// For each JSON level
			for(int i = 0 ; i < jsonLevels.length(); i++)
			{
				// Extract the individual level
				JSONObject level = jsonLevels.getJSONObject(i);
				
				// Load any required bitmaps for the level
				assetManager.loadAndAddBitmap(level.getString(LEVEL_BITMAP), level.getString(LEVEL_BITMAP_PATH));
				
				// Create a new game level and add it to the list of levels
				levels.add(
						new GameLevel(
								level.getString(LEVEL_ID), // Level ID
								level.getString(LEVEL_NAME), // Level Name
								new PushButton( // Create the game level's button
										mGame.getScreenWidth() * (float)level.getDouble(LEVEL_X_PERCENT), // Calculate button's X position from X percentage supplied
										mGame.getScreenHeight() * (float)level.getDouble(LEVEL_Y_PERCENT),
										level.getInt(LEVEL_WIDTH), // Button width
										level.getInt(LEVEL_HEIGHT), // Button height
										level.getString(LEVEL_BITMAP), // Button bitmap
										this
								)));
			}
			//TODO: Add in prerequisite system to have required levels be completed before a level can be played
			return levels;
			
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	
	//TODO: Move this method to a common util class
	/**
	 * Reads a JSON file from assets
	 * @param context calling context
	 * @param filename JSON filename to read, root folder is assets/
	 * @return
	 */
	private String getJSONString(Context context, String filename)
	{
		// JSON charset
		final String CHAR_SET = "UTF-8";
		// Set up json string output
		String json;
		try
		{
			// Open the JSON file with an input stream
			InputStream is = context.getAssets().open(filename);
			// Gets the estimated size of the file
			int size = is.available();
			// Create a byte buffer to read into
			byte[] buffer = new byte[size];
			// Read from the input stream into the buffer
			is.read(buffer);
			// Close the input stream
			is.close();
			// Convert the buffer into a JSON string in the specified charset
			json = new String(buffer, CHAR_SET);
		}
		catch (IOException ex)
		{
			// If there is an error print the stack trace
			ex.printStackTrace();
			return null;
		}
		return json;
	}
	
	public List<GameLevel> getLevels()
	{
		return mLevels;
	}
	
	public int getCurrentLevel()
	{
		return mCurrentLevel;
	}
}
