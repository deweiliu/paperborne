package uk.ac.qub.eeecs.gage.WorldScreenTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.game.GameUtil;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;
import uk.ac.qub.eeecs.game.worldScreen.SaveGame;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Jamie T on 10/03/2018.
 * Used for unit testing the SaveGame class
 */

@RunWith(MockitoJUnitRunner.class)
public class SaveGameTest {
    // Test values for the save game
    private static final int TEST_SAVE_SLOT = 1;
    private static final int TEST_ATTACK = 1;
    private static final int TEST_HEALTH = 1;
    private static final int TEST_MANA = 1;
    private static final String TEST_BITMAP = "test_bitmap";
    private static final String TEST_NAME = "test_card";
    private static final String TEST_REQUIRED_LEVEL = "test_level";
    // Number of cards to create for the test
    private static final int TEST_NUM_OF_CARDS = 10;

    @Test
    public void saveGameInitTest() {
        // Create list of cards with test data
        List<LevelCard> testCards = new ArrayList<>();
        for (int i = 0; i < TEST_NUM_OF_CARDS; i++) {
            testCards.add(new LevelCard(
                    TEST_NAME,
                    TEST_BITMAP,
                    TEST_MANA,
                    TEST_HEALTH,
                    TEST_ATTACK
            ));
        }

        // Create list of levels with the test data
        List<String> testLevels = new ArrayList<>();
        testLevels.add(TEST_REQUIRED_LEVEL);

        // Set up save game by constructor
        SaveGame saveGame = new SaveGame(
                TEST_SAVE_SLOT,
                testCards,
                testLevels
        );

        // Check values are as expected
        assertEquals(saveGame.getSlot(), TEST_SAVE_SLOT);
        assertEquals(saveGame.getPlayerDeck(), testCards);
        assertEquals(saveGame.getCompleted(), testLevels);

        // Set up save game by setter methods
        SaveGame setSavegame = new SaveGame(0, null, null);
        setSavegame.setSlot(TEST_SAVE_SLOT);
        setSavegame.setCompleted(testLevels);
        setSavegame.setPlayerDeck(testCards);

        // Check values are as expected
        assertEquals(setSavegame.getSlot(), TEST_SAVE_SLOT);
        assertEquals(setSavegame.getPlayerDeck(), testCards);
        assertEquals(setSavegame.getCompleted(), testLevels);

    }

    @Test
    public void saveGameJSONTest() {
        // Create list of cards with test data
        List<LevelCard> testCards = new ArrayList<>();
        for (int i = 0; i < TEST_NUM_OF_CARDS; i++) {
            testCards.add(new LevelCard(
                    TEST_NAME,
                    TEST_BITMAP,
                    TEST_MANA,
                    TEST_HEALTH,
                    TEST_ATTACK
            ));
        }

        // Create list of levels with the test data
        List<String> testLevels = new ArrayList<>();
        testLevels.add(TEST_REQUIRED_LEVEL);

        // Set up save game by constructor
        SaveGame saveGame = new SaveGame(
                TEST_SAVE_SLOT,
                testCards,
                testLevels
        );

        // Check the JSON output is valid
        assertTrue(GameUtil.isJSONValid(saveGame.toJSON()));
    }

}