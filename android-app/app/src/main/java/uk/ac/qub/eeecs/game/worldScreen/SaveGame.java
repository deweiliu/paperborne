package uk.ac.qub.eeecs.game.worldScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jamie T on 09/03/2018.
 * Class used to represent a save game, contains information about the slot the save game is in
 * the player's deck in the save game and a list of levels the player has completed, represented by
 * level IDs
 */

public class SaveGame {
    // JSON String identifiers for save games
    public static final String SLOT = "id";
    public static final String PLAYER_DECK = "deck";
    public static final String COMPLETED = "completed";

    // The save slot the save is in, only 1 save game per slot
    private int slot;
    // The player's deck in the save game
    private List<LevelCard> playerDeck;
    // A list of levels the player has completed, represented by level IDs
    private List<String> completed;

    /**
     * Constructor for save game
     *
     * @param slot       save slot, only 1 save game per slot
     * @param playerDeck the player's deck state in the save game
     * @param completed  the list of levels the user has completed, represented by level IDs
     */
    public SaveGame(int slot, List<LevelCard> playerDeck, List<String> completed) {
        this.slot = slot;
        this.playerDeck = playerDeck;
        this.completed = completed;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public List<LevelCard> getPlayerDeck() {
        return playerDeck;
    }

    public void setPlayerDeck(List<LevelCard> playerDeck) {
        this.playerDeck = playerDeck;
    }

    public List<String> getCompleted() {
        return completed;
    }

    public void setCompleted(List<String> completed) {
        this.completed = completed;
    }

    /**
     * Converts the save game to a JSON string form for writing to a file
     *
     * @return JSON String form of this save game
     */
    public String toJSON() {
        try {
            // Set up overall JSON Object
            JSONObject jsonObject = new JSONObject();
            // Add the integer save slot
            jsonObject.put(SLOT, slot);
            // Create a JSON Array for storing the player's deck state
            JSONArray jsonDeck = new JSONArray();
            for (int i = 0; i < playerDeck.size(); i++) {
                // Loop through each of the player's deck cards and add them to the JSON array
                jsonDeck.put(new JSONObject(playerDeck.get(i).toJSON()));
            }
            // Add the deck JSON array to the overall JSON object
            jsonObject.put(PLAYER_DECK, jsonDeck);
            // Create a JSON Array for storing the levels the player has completed
            JSONArray jsonCompleted = new JSONArray();
            for (int i = 0; i < completed.size(); i++) {
                // Loop through each of the player's complete levels and add them to the JSON array
                jsonCompleted.put(completed.get(i));
            }
            // Add the list of complete levels tot he overall JSON object
            jsonObject.put(COMPLETED, jsonCompleted);
            // Convert the overall JSON object will all the information to a JSON string and return
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
