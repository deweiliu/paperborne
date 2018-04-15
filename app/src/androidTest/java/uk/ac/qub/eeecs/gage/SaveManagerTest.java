package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.game.SaveManager;
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;
import uk.ac.qub.eeecs.game.worldScreen.SaveGame;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jamie T on 15/04/2018.
 * Used to check that the save manager is operating correctly
 */
@RunWith(AndroidJUnit4.class)
public class SaveManagerTest {

    // Test values for the save games

    // Test value for the completed level
    private static final String TEST_COMPLETED_LEVEL = "completed_level";

    // Test values for the card
    private static final int TEST_ATTACK = 1;
    private static final int TEST_HEALTH = 1;
    private static final int TEST_MANA = 1;
    private static final String TEST_BITMAP = "test_bitmap";
    private static final String TEST_NAME = "test_card";

    /**
     * Tests writing a new save game to the save file
     */
    @Test
    public void writeSaveFileTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        deleteSaveFile(appContext);

        // Create list of test completed levels
        List<String> completedList = new ArrayList<>();
        completedList.add(TEST_COMPLETED_LEVEL);

        // Create list of test player cards
        List<LevelCard> playerCards = new ArrayList<>();
        playerCards.add(new LevelCard(
                TEST_NAME,
                TEST_BITMAP,
                TEST_MANA,
                TEST_HEALTH,
                TEST_ATTACK
        ));

        // Create the new save
        SaveGame newSave = new SaveGame(
                SaveManager.DEFAULT_SAVE_SLOT,
                playerCards,
                completedList
        );

        // Write the save file
        SaveManager.writeSaveFile(newSave, appContext);

        // Load the save again
        SaveGame loadedSave = SaveManager.loadSavedGame(SaveManager.DEFAULT_SAVE_SLOT, appContext);

        // Check the save is the same
        // Check the save slot is the same
        assertEquals(loadedSave.getSlot(), newSave.getSlot());
        // Check the player deck is valid
        assertNotNull(loadedSave.getPlayerDeck());
        for (int i = 0; i < loadedSave.getPlayerDeck().size(); i++) {
            // Get the level card
            LevelCard levelCard = loadedSave.getPlayerDeck().get(i);
            // Check the level cards are the same
            assertEquals(levelCard.getName(), playerCards.get(i).getName());
            assertEquals(levelCard.getAttackValue(), playerCards.get(i).getAttackValue());
            assertEquals(levelCard.getManaCost(), playerCards.get(i).getManaCost());
            assertEquals(levelCard.getHealthValue(), playerCards.get(i).getHealthValue());
            assertEquals(levelCard.getBitmap(), playerCards.get(i).getBitmap());
        }
        // Check the completed levels are valid
        assertNotNull(loadedSave.getCompleted());
        for (int i = 0; i < loadedSave.getCompleted().size(); i++) {
            // Get the completed level id
            String completed = loadedSave.getCompleted().get(i);
            // Check the completed level ids are the same
            Assert.assertEquals(completed, newSave.getCompleted().get(i));
        }
        // Delete the save file to stop loading test values in the game
        deleteSaveFile(appContext);
    }

    /**
     * Tests loading a save game from the save file
     */
    @Test
    public void loadSaveGameTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // Delete any existing saves
        deleteSaveFile(appContext);
        // Load the new save
        SaveGame loadedSave = SaveManager.loadSavedGame(SaveManager.DEFAULT_SAVE_SLOT, appContext);
        // Check it has loaded
        assertNotNull(loadedSave.getSlot());
        assertNotNull(loadedSave.getPlayerDeck());
        assertNotNull(loadedSave.getCompleted());
        // Delete the save file
        deleteSaveFile(appContext);
    }

    /**
     * Tests loading the levels from the level file
     */
    @Test
    public void loadLevelsTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        // Load the levels from the save manager
        List<Level> loadedLevels = SaveManager.loadLevels(SaveManager.LEVEL_FILE, appContext);
        // Check that the level list isn't null
        Assert.assertNotNull(loadedLevels);
        // Check that levels have been loaded
        Assert.assertTrue(loadedLevels.size() > 0);
        // List to track level IDs
        List<String> levelIDs = new ArrayList<>();
        // For each loaded level
        for (int i = 0; i < loadedLevels.size(); i++) {
            // Get the level
            Level level = loadedLevels.get(i);
            // Check the Level ID isn't null
            Assert.assertNotNull(level.getId());
            // Check that the Level ID hasn't already been used
            assertFalse(levelIDs.contains(level.getId()));
            // Add new ID to the level list
            levelIDs.add(level.getId());
        }
    }

    /**
     * Deletes the save file
     *
     * @param context context used to delete the save file
     */
    private void deleteSaveFile(Context context) {
        context.deleteFile(SaveManager.SAVE_FILE);
    }
}