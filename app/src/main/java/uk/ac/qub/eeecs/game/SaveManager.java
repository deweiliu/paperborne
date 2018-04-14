package uk.ac.qub.eeecs.game;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;
import uk.ac.qub.eeecs.game.worldScreen.SaveGame;

/**
 * Created by Jamie on 04/04/2018.
 * Manager for handling all save games and level files
 */

public class SaveManager
{
	// Identifier for the list of levels in the JSON level file
	private static final String LEVEL_LIST = "levels";
	// Identifier for the list of cards that make up the player starting deck in the JSON level file
	private static final String PLAYER_STARTING_DECK = "startingDeck";
	// Game levels filename, in the future could be dynamic to allow for different campaigns
	public static final String LEVEL_FILE = "levels/game_levels.json";
	// Identifier for the list of save games in the JSON save file
	private static final String SAVE_LIST = "saves";
	// Save games filename
	public static final String SAVE_FILE = "save_games.json";
	// Maximum number of save slots
	private static final int MAX_SAVE_SLOTS = 3;
	// Default save slot
	// TODO: Allow the user to choose a save slot rather than just using the default
	public static final int DEFAULT_SAVE_SLOT = 1;
	
	/**
	 * Loads and returns the default starting deck for a campaign, used if it is a new save and
	 * the player doesn't have their own deck yet
	 * @param levelFileName the level file to load
	 * @param game the game the levels are to be loaded into
	 * @return the starting deck for the specified level file in a list
	 */
	private static List<LevelCard> loadStartingDeck(String levelFileName, Game game)
	{
		try
		{
			// Starting deck is the deck that the user will be supplied at the start of a save game,
			// if the user has already played this deck will not be used, instead the deck stored
			// with the save game will be used
			
			// Gets the  JSON in string form form the file
			JSONObject jsonData = new JSONObject(GameUtil.getJSONAsset(game.getContext(), levelFileName));
			
			// Load the player's starting deck list
			JSONArray jsonPlayerDeck = jsonData.getJSONArray(PLAYER_STARTING_DECK);
			
			List<LevelCard> startingDeck = new ArrayList<>();
			
			// Add each card into the player starting deck list
			for (int j = 0; j < jsonPlayerDeck.length(); j++)
			{
				JSONObject card = jsonPlayerDeck.getJSONObject(j);
				startingDeck.add(new LevelCard(
						card.getString(LevelCard.CARD_NAME),
						card.getString(LevelCard.CARD_BITMAP),
						card.getInt(LevelCard.CARD_MANA_COST),
						card.getInt(LevelCard.CARD_HEALTH),
						card.getInt(LevelCard.CARD_ATTACK))
				);
			}
			return startingDeck;
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	/**
	 * Loads a world's levels from a level file
	 *
	 * @param levelFileName level file path, root folder is assets/
	 * @return The loaded levels
	 */
	public static List<Level> loadLevels(String levelFileName, GameScreen gameScreen)
	{
		try
		{
			// Gets the  JSON in string form form the file
			JSONObject jsonData = new JSONObject(GameUtil.getJSONAsset(gameScreen.getGame().getContext(), levelFileName));
			// Gets the levels as a JSON array
			JSONArray jsonLevels = jsonData.getJSONArray(LEVEL_LIST);
			// Sets up return list containing all parsed levels
			List<Level> levels = new ArrayList<>();
			
			// For each JSON level
			for (int i = 0; i < jsonLevels.length(); i++)
			{
				// Extract the individual level
				JSONObject level = jsonLevels.getJSONObject(i);
				
				// Load the level's deck
				JSONArray jsonDeck = level.getJSONArray(Level.LEVEL_DECK);
				
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
				
				if (level.has(Level.LEVEL_PREREQUISITES))
				{
					// If the level has prerequisites
					// Load the level's prerequisites
					JSONArray jsonPrerequisites = level.getJSONArray(Level.LEVEL_PREREQUISITES);
					
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
							new Level(
									level.getString(Level.LEVEL_ID), // Level ID
									level.getString(Level.LEVEL_NAME), // Level Name
									level.getDouble(Level.LEVEL_X_PERCENT),
									level.getDouble(Level.LEVEL_Y_PERCENT),
									level.getDouble(Level.LEVEL_WIDTH),
									level.getDouble(Level.LEVEL_HEIGHT),
									deck,
									level.getString(Level.LEVEL_BITMAP_PATH),
									level.getString(Level.LEVEL_BITMAP),
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
							new Level(
									level.getString(Level.LEVEL_ID), // Level ID
									level.getString(Level.LEVEL_NAME), // Level Name
									level.getDouble(Level.LEVEL_X_PERCENT),
									level.getDouble(Level.LEVEL_Y_PERCENT),
									level.getDouble(Level.LEVEL_WIDTH),
									level.getDouble(Level.LEVEL_HEIGHT),
									deck,
									level.getString(Level.LEVEL_BITMAP_PATH),
									level.getString(Level.LEVEL_BITMAP)
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
	public static SaveGame loadSavedGame(int saveSlot, Game game)
	{
		// Get any existing save games
		List<SaveGame> saveGames = readSaveFile(game.getActivity());
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
		// save file and return it
		SaveGame saveGame = new SaveGame(DEFAULT_SAVE_SLOT, loadStartingDeck(LEVEL_FILE, game), new ArrayList<String>());
		writeSaveFile(saveGame, game);
		return saveGame;
	}
	
	/**
	 * Used to attempt to load in the save game file which can contain multiple save games,
	 * loads from JSON string and parses into Java objects
	 * @return List of save games that have been loaded, if no saves exists will be empty list
	 */
	private static List<SaveGame> readSaveFile(Activity activity)
	{
		try
		{
			try
			{
				// Open save file stream
				InputStream inputStream = activity.openFileInput(SAVE_FILE);
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
					Log.d("debug", saveString);
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
								completed.add(jsonCompleted.getString(j));
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
	 * @param save the save game to update
	 * @param game the game the save is in
	 */
	public static void writeSaveFile(SaveGame save, Game game)
	{
		// Read in any existing save file
		List<SaveGame> saveList = readSaveFile(game.getActivity());
		if(!saveList.isEmpty())
		{
			// If there are existing save games
			for (int i = 0; i < saveList.size(); i++)
			{
				// Loop through each save game
				if (saveList.get(i).getSlot() == save.getSlot())
				{
					// If the one that is currently loaded has the same slot as an existing one
					// replace the existing one with the currently loaded one
					saveList.set(i, save);
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
				saveList.add(new SaveGame(i, save.getPlayerDeck(), new ArrayList<String>()));
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
				OutputStream outputStream;
				// Open save file for writing to
				outputStream = game.getActivity().openFileOutput(SAVE_FILE, Context.MODE_PRIVATE);
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
}
