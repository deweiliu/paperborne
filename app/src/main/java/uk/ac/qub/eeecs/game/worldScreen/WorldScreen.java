package uk.ac.qub.eeecs.game.worldScreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	// Identifier for the list of levels in the JSON level file
	private static final String LEVEL_LIST = "levels";
	// Identifier for the list of cards that make up the player starting deck in the JSON level file
	private static final String PLAYER_STARTING_DECK = "startingDeck";
	// Game levels filename, in the future could be dynamic to allow for different campaigns
	private static final String LEVEL_FILE = "levels/game_levels.json";
	// Identifier for the list of save games in the JSON save file
	private static final String SAVE_LIST = "saves";
	// Save games filename
	private static final String SAVE_FILE = "save_games.json";
	
	// Maximum number of save slots
	private static final int MAX_SAVE_SLOTS = 3;
	// Default save slot
	// TODO: Allow the user to choose a save slot rather than just using the default
	private static final int DEFAULT_SAVE_SLOT = 1;
	
	// Level Sprite width/height
	private final int DEFAULT_LEVEL_HEIGHT = 128;
	
	// Player Character Sprite width/height
	private static final int PLAYER_WIDTH = 128;
	private static final int PLAYER_HEIGHT = 128;
	
	// Launch battle button width/height/distance from bottom of screen
	private static final int LAUNCH_BUTTON_WIDTH = 512;
	private static final int LAUNCH_BUTTON_HEIGHT = 128;
	private static final int LAUNCH_BUTTON_Y_SEPERATION = 128;
	
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
	// The player's starting deck for the world, will be set to this if there is no existing
	// save game and the player is starting a new game, defined in the levels file
	private List<LevelCard> mStartingDeck;
	// The currently loaded save file
	private SaveGame mLoadedSave;
	
	// App activity
	private Activity mActivity;
	// Game asset manager
	private AssetStore mAssetManager;
	
	public WorldScreen(Game game)
	{
		super("WorldScreen", game);
		mActivity = game.getActivity();
		// Initialise level list
		mLevels = new ArrayList<>();
		// Initialise starting deck list
		mStartingDeck = new ArrayList<>();
		// Get asset manager
		mAssetManager = game.getAssetManager();
		// Load in any bitmaps used in the world screen
		mAssetManager.loadAndAddBitmap("WorldMap", "img/WorldMap.png");
		mAssetManager.loadAndAddBitmap("Character", "img/Character.png");
		mAssetManager.loadAndAddBitmap("LaunchButton", "img/BlueButton.png");
		
		// Create the world map background
		mWorldBackground = new GameObject(
				game.getScreenWidth() / 2,
				game.getScreenHeight() / 2,
				mGame.getScreenWidth(),
				mGame.getScreenHeight(),
				mAssetManager.getBitmap("WorldMap"),
				this
		);
		
		// Load in levels from JSON campaign definition
		mLevels = loadLevels(LEVEL_FILE);
		// Load the default save slot save, if none exists create a new one
		mLoadedSave = loadSavedGame(DEFAULT_SAVE_SLOT);
		
		if (mLevels.size() < 1)
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
				mAssetManager.getBitmap("Character"),
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
	 * Loads a game level's battle if the level is unlocked to the user
	 */
	private void loadBattle()
	{
		boolean locked = true;
		// Get the level attempting to be loaded
		GameLevel level = mLevels.get(mCurrentLevel);
		if(level.getPrerequisites().isEmpty())
		{
			// If the level has no prerequisite levels, the level is unlocked to the user
			locked = false;
		}
		else
		{
			// Otherwise loop through each of the level's prerequisites
			for(int i = 0; i < level.getPrerequisites().size(); i++)
			{
				// If the loaded save game has the level ID in it's completed levels list
				if(mLoadedSave.getCompleted().contains(level.getPrerequisites().get(i)))
				{
					// The level is unlocked to the user
					locked = false;
				}
			}
		}
		if(locked)
		{
			// Can't play level, haven't completed previous levels
			//TODO: Display a  message to the user informing them that the level is locked
		}
		else
		{
			// Load the battle screen
			mGame.getScreenManager().removeScreen(this.getName());
			// Load in level specific information, such as opponent and player decks
			mGame.getScreenManager().addScreen(new CardDemoScreen(
					mGame,
					mLevels.get(mCurrentLevel).getDeck(),
					mLoadedSave.getPlayerDeck()
			));
		}
	}
	
	/**
	 * Loads a world's levels from a level file
	 *
	 * @param levelFileName level file path, root folder is assets/
	 * @return The loaded levels
	 */
	private List<GameLevel> loadLevels(String levelFileName)
	{
		try
		{
			// Gets the  JSON in string form form the file
			JSONObject jsonData = new JSONObject(getJSONAsset(mGame.getContext(), levelFileName));
			// Gets the levels as a JSON array
			JSONArray jsonLevels = jsonData.getJSONArray(LEVEL_LIST);
			// Sets up return list containing all parsed levels
			List<GameLevel> levels = new ArrayList<>();
			
			// Starting deck is the deck that the user will be supplied at the start of a save game,
			// if the user has already played this deck will not be used, instead the deck stored
			// with the save game will be used
			
			// Load the player's starting deck list
			JSONArray jsonPlayerDeck = jsonData.getJSONArray(PLAYER_STARTING_DECK);
			
			// Add each card into the player starting deck list
			for (int j = 0; j < jsonPlayerDeck.length(); j++)
			{
				JSONObject card = jsonPlayerDeck.getJSONObject(j);
				mStartingDeck.add(new LevelCard(
						card.getString(LevelCard.CARD_NAME),
						card.getString(LevelCard.CARD_BITMAP),
						card.getInt(LevelCard.CARD_MANA_COST),
						card.getInt(LevelCard.CARD_HEALTH),
						card.getInt(LevelCard.CARD_ATTACK))
				);
			}
			
			// For each JSON level
			for (int i = 0; i < jsonLevels.length(); i++)
			{
				// Extract the individual level
				JSONObject level = jsonLevels.getJSONObject(i);
				
				// Load any required bitmaps for the level
				mAssetManager.loadAndAddBitmap(level.getString(GameLevel.LEVEL_BITMAP), level.getString(GameLevel.LEVEL_BITMAP_PATH));
				
				// Load the level's deck
				JSONArray jsonDeck = level.getJSONArray(GameLevel.LEVEL_DECK);
				
				// Set up deck card objects
				List<LevelCard> deck = new ArrayList<>();
				
				// Add each card into the new deck list
				for (int j = 0; j < jsonDeck.length(); j++)
				{
					JSONObject card = jsonDeck.getJSONObject(j);
					deck.add(new LevelCard(
							card.getString(LevelCard.CARD_NAME),
							card.getString(LevelCard.CARD_BITMAP),
							card.getInt(LevelCard.CARD_MANA_COST),
							card.getInt(LevelCard.CARD_HEALTH),
							card.getInt(LevelCard.CARD_ATTACK))
					);
				}
				
				if (level.has(GameLevel.LEVEL_PREREQUISITES))
				{
					// If the level has prerequisites
					// Load the level's prerequisites
					JSONArray jsonPrerequisites = level.getJSONArray(GameLevel.LEVEL_DECK);
					
					// Set up prerequisite ID objects
					List<String> prerequisites = new ArrayList<>();
					
					// Add each card into the new deck list
					for (int j = 0; j < jsonPrerequisites.length(); j++)
					{
						prerequisites.add(jsonPrerequisites.getString(j));
					}
					// Create a new game level and add it to the list of levels with prerequisite
					// level IDs
					levels.add(
							new GameLevel(
									level.getString(GameLevel.LEVEL_ID), // Level ID
									level.getString(GameLevel.LEVEL_NAME), // Level Name
									new PushButton( // Create the game level's button
											mGame.getScreenWidth() * (float) level.getDouble(GameLevel.LEVEL_X_PERCENT), // Calculate button's X position from X percentage supplied
											mGame.getScreenHeight() * (float) level.getDouble(GameLevel.LEVEL_Y_PERCENT),
											level.getInt(GameLevel.LEVEL_WIDTH), // Button width
											level.getInt(GameLevel.LEVEL_HEIGHT), // Button height
											level.getString(GameLevel.LEVEL_BITMAP), // Button bitmap
											this
									),
									deck,
									prerequisites
							)
					);
				}
				else
				{
					// If the level doesn't have prerequisites
					// Create a new game level and add it to the list of levels without any
					// prerequisites
					levels.add(
							new GameLevel(
									level.getString(GameLevel.LEVEL_ID), // Level ID
									level.getString(GameLevel.LEVEL_NAME), // Level Name
									new PushButton( // Create the game level's button
											mGame.getScreenWidth() * (float) level.getDouble(GameLevel.LEVEL_X_PERCENT), // Calculate button's X position from X percentage supplied
											mGame.getScreenHeight() * (float) level.getDouble(GameLevel.LEVEL_Y_PERCENT),
											level.getInt(GameLevel.LEVEL_WIDTH), // Button width
											level.getInt(GameLevel.LEVEL_HEIGHT), // Button height
											level.getString(GameLevel.LEVEL_BITMAP), // Button bitmap
											this
									),
									deck
							)
					);
				}
			}
			return levels;
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	/**
	 * Loads a save game, specified by save slot, if one doesn't exists it creates a new one
	 * @param saveSlot the save slot of the save game to load
	 * @return the loaded save game
	 */
	public SaveGame loadSavedGame(int saveSlot)
	{
		// Get any existing save games
		List<SaveGame> saveGames = readSaveFile();
		if(!saveGames.isEmpty())
		{
			// If there are existing save games
			for(int i = 0; i < saveGames.size(); i++)
			{
				// Loop through each save game
				if(saveGames.get(i).getSlot() == saveSlot)
				{
					// If a save game has a matching save slot, return that save game
					return saveGames.get(i);
				}
			}
		}
		// If there is no save game with a matching save slot, or no existing saves, create a new
		// save file
		writeSaveFile();
		// Load the newly created save file again and return its response
		return loadSavedGame(saveSlot);
	}
	
	/**
	 * Used to attempt to load in the save game file which can contain multiple save games,
	 * loads from JSON string and parses into Java objects
	 * @return List of save games that have been loaded, if no saves exists will be empty list
	 */
	private List<SaveGame> readSaveFile()
	{
		try
		{
			try
			{
				// Open save file stream
				InputStream inputStream = mActivity.openFileInput(SAVE_FILE);
				if (inputStream != null)
				{
					// Get stream reader
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					// Get buffered reader
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					// Set up read string
					String readString;
					// Create string builder
					StringBuilder stringBuilder = new StringBuilder();
					while ((readString = bufferedReader.readLine()) != null)
					{
						// Loop through reading each line and appending to the string builder until
						// the end of the file
						stringBuilder.append(readString);
					}
					// Close the stream
					inputStream.close();
					// Get the final string from the string builder
					String saveString = stringBuilder.toString();
					try
					{
						// Convert string that has been read into a JSON object
						JSONObject jsonSaveData = new JSONObject(saveString);
						// Get the JSON array containing save games from the JSON object
						JSONArray jsonSaveList = jsonSaveData.getJSONArray(SAVE_LIST);
						// Set up save game list to add the loaded save games into
						List<SaveGame> saveGames = new ArrayList<>();
						for(int i = 0; i < jsonSaveList.length(); i++)
						{
							// Iterate through each save game
							JSONObject jsonSave = jsonSaveList.getJSONObject(i);
							// Get the JSON list of completed levels for the save game
							JSONArray jsonCompleted = jsonSave.getJSONArray(SaveGame.COMPLETED);
							// Set up list of completed level IDs
							List<String> completed = new ArrayList<>();
							for(int j = 0; j < jsonCompleted.length(); j++)
							{
								// Add each completed level to the list from the JSON
								completed.add(jsonCompleted.getString(i));
							}
							// Get the JSON list of the player's deck
							JSONArray jsonPlayerDeck = jsonSave.getJSONArray(SaveGame.PLAYER_DECK);
							// Set up list of player cards
							List<LevelCard> playerDeck = new ArrayList<>();
							for (int j = 0; j < jsonPlayerDeck.length(); j++)
							{
								// Add each player card in the deck to the list from the JSON
								JSONObject jsonCard = jsonPlayerDeck.getJSONObject(j);
								playerDeck.add(new LevelCard(
										jsonCard.getString(LevelCard.CARD_NAME),
										jsonCard.getString(LevelCard.CARD_BITMAP),
										jsonCard.getInt(LevelCard.CARD_MANA_COST),
										jsonCard.getInt(LevelCard.CARD_HEALTH),
										jsonCard.getInt(LevelCard.CARD_ATTACK))
								);
							}
							// Add each save game to the save game list from the JSON and parsed
							// lists
							saveGames.add(new SaveGame(
									jsonSave.getInt(SaveGame.SLOT),
									playerDeck,
									completed
							));
						}
						return saveGames;
					}
					catch(JSONException e)
					{
						e.printStackTrace();
						return new ArrayList<>();
					}
				}
				else
				{
					// If input stream is null, return an empty list
					return new ArrayList<>();
				}
			}
			catch(FileNotFoundException e)
			{
				// If there is no file, return an empty list
				return new ArrayList<>();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	/**
	 * Updates the save file with the current save, if no saves exists creates a new save file with
	 * starting save states
	 */
	private void writeSaveFile()
	{
		// Read in any existing save file
		List<SaveGame> saveList = readSaveFile();
		if(!saveList.isEmpty())
		{
			// If there are existing save games
			for (int i = 0; i < saveList.size(); i++)
			{
				// Loop through each save game
				if (saveList.get(i).getSlot() == mLoadedSave.getSlot())
				{
					// If the one that is currently loaded has the same slot as an existing one
					// replace the existing one with the currently loaded one
					saveList.set(i, mLoadedSave);
				}
			}
		}
		else
		{
			// If there is no existing save games
			for(int i = 0; i < MAX_SAVE_SLOTS; i++)
			{
				// Set up starting data for the number of save slots available, with the starting
				// deck and no levels completed
				saveList.add(new SaveGame(i, mStartingDeck, new ArrayList<String>()));
			}
		}
		try
		{
			// Set up JSON Object for the save file
			JSONObject jsonSaveObject = new JSONObject();
			// Set up JSON array for the list of save games
			JSONArray jsonSaveList = new JSONArray();
			for(int i = 0; i < saveList.size(); i++)
			{
				// Loop through each save game and add it to the JSON list
				jsonSaveList.put(new JSONObject(saveList.get(i).toJSON()));
			}
			// Add the list of JSON save games to the JSON save object
			jsonSaveObject.put(SAVE_LIST, jsonSaveList);
			// Convert the JSON save object to a JSON string
			String saveString = jsonSaveObject.toString();
			try
			{
				// Create output stream
				FileOutputStream outputStream;
				// Open save file for writing to
				outputStream = mActivity.openFileOutput(SAVE_FILE, Context.MODE_PRIVATE);
				// Write to the save file
				outputStream.write(saveString.getBytes());
				// Close stream
				outputStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	
	//TODO: Move this method to a common util class
	
	/**
	 * Reads a JSON file from assets
	 *
	 * @param context  calling context
	 * @param filename JSON filename to read, root folder is assets/
	 * @return
	 */
	private String getJSONAsset(Context context, String filename)
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
