package uk.ac.qub.eeecs.game;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jamie T on 10/03/2018.
 * General Utility methods to be used across the game
 */

public class GameUtil {
    /**
     * Reads a JSON file from assets
     *
     * @param context  calling context
     * @param filename JSON filename to read, root folder is assets/
     * @return JSON string of the asset that has been read
     */
    public static String getJSONAsset(Context context, String filename) {
        // JSON charset
        final String CHAR_SET = "UTF-8";
        // Set up json string output
        String json;
        try {
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
        } catch (IOException ex) {
            // If there is an error print the stack trace
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Taken from StackOverflow - https://stackoverflow.com/a/10174938/6052295
     *
     * @param test JSON string to test
     * @return true = valid JSON string, false = invalid JSON string
     */
    public static boolean isJSONValid(String test) {
        try {
            // Try to parse the string into a JSON Object
            new JSONObject(test);
        } catch (JSONException ex) {
            // If there is a JSON exception
            try {
                // Try to parse the string into a JSON Array
                new JSONArray(test);
            } catch (JSONException ex1) {
                // If there is another exception, the object is invalid JSON, return false
                return false;
            }
        }
        // If both exceptions aren't triggered, it must be valid JSON, return true
        return true;
    }
}
