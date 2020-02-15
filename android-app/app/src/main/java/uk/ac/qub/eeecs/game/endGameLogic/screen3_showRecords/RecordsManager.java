package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dewei Liu 40216004 on 01/04/2018
 */
public class RecordsManager {

    public static final String PREFERENCES_KEY = "Paperborne Players records";
    private final String PLAYER_NAMES_KEY = "Player names";
    private final String TIMES_OF_WIN_KEY = "win";
    private final String TIMES_OF_LOSE_KEY = "lose";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * @param context calling context
     */
    public RecordsManager(Context context) {
        // Set up shared preferences with the activity provided
        preferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public Set<String> getNames() {
        return preferences.getStringSet(PLAYER_NAMES_KEY, null);
    }

    public int getTimesOfWin(String playerName) {
        return preferences.getInt(playerName + TIMES_OF_WIN_KEY, 0);
    }

    public int getTimesOfLose(String playerName) {
        return preferences.getInt(playerName + TIMES_OF_LOSE_KEY, 0);

    }

    public void addWinner(String playerName) {
        this.addRecord(playerName, true);
    }

    public void addLoser(String playerName) {
        this.addRecord(playerName, false);
    }

    private void addRecord(String playerName, boolean isWin) {
        //Set up the player name in record
        Set<String> names = getNames();
        if ((names) == null) {
            names = new HashSet<>();

        }
        boolean newPlayer = true;
        for (String each : names) {
            if (each.equals(playerName)) {
                newPlayer = false;
            }
        }
        if (newPlayer) {
            names.add(playerName);
        }
        editor.putStringSet(PLAYER_NAMES_KEY, names);

        /***********************************************************************/
        //Set up the win & lost records
        String key = playerName;
        int times;
        if (isWin) {
            times = this.getTimesOfWin(playerName);
            key = playerName + TIMES_OF_WIN_KEY;

        } else {
            times = this.getTimesOfLose(playerName);
            key = playerName + TIMES_OF_LOSE_KEY;
        }
        times += 1;
        editor.putInt(key, times);
        editor.apply();
    }

}
