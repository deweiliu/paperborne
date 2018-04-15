package uk.ac.qub.eeecs.gage.WorldScreenTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Jamie T on 07/04/2018.
 * Used for unit testing the Level class
 */

@RunWith(MockitoJUnitRunner.class)
public class LevelTest {
    // Test values for the card deck
    private static final int TEST_CARD_ATTACK = 1;
    private static final int TEST_CARD_HEALTH = 1;
    private static final int TEST_CARD_MANA = 1;
    private static final String TEST_CARD_BITMAP = "test_bitmap";
    private static final String TEST_CARD_NAME = "test_card";
    // Test values for the level
    private static final String TEST_PREREQ_ID = "level_one";
    private static final String TEST_ID = "level_two";
    private static final String TEST_NAME = "Level Two";
    private static final String TEST_BITMAP_PATH = "bitmap";
    private static final String TEST_BITMAP_ID = "bitmap";
    private static final double TEST_X_PERCENT = 0.80;
    private static final double TEST_Y_PERCENT = 0.80;
    private static final double TEST_WIDTH = 50;
    private static final double TEST_HEIGHT = 50;


    @Test
    public void levelPrerequisiteInitTest() {
        // Test deck values
        List<LevelCard> testDeck = new ArrayList<>();
        testDeck.add(
                new LevelCard(
                        TEST_CARD_NAME,
                        TEST_CARD_BITMAP,
                        TEST_CARD_MANA,
                        TEST_CARD_HEALTH,
                        TEST_CARD_ATTACK
                )
        );

        // Test prerequisites values
        List<String> testPrerequisites = new ArrayList<>();
        testPrerequisites.add(
                TEST_PREREQ_ID
        );

        // Set up card by constructor
        Level level = new Level(
                TEST_ID,
                TEST_NAME,
                TEST_X_PERCENT,
                TEST_Y_PERCENT,
                TEST_WIDTH,
                TEST_HEIGHT,
                testDeck,
                TEST_BITMAP_PATH,
                TEST_BITMAP_ID,
                testPrerequisites
        );

        // Check values are as expected
        assertEquals(level.getId(), TEST_ID);
        assertEquals(level.getName(), TEST_NAME);
        assertEquals(level.getxPercent(), TEST_X_PERCENT);
        assertEquals(level.getyPercent(), TEST_Y_PERCENT);
        assertEquals(level.getWidth(), TEST_WIDTH);
        assertEquals(level.getHeight(), TEST_HEIGHT);
        assertEquals(level.getDeck(), testDeck);
        assertEquals(level.getBitmapPath(), TEST_BITMAP_PATH);
        assertEquals(level.getBitmapID(), TEST_BITMAP_ID);
        assertEquals(level.getPrerequisites(), testPrerequisites);

        // Set up card by setter methods
        Level setLevelCard = new Level(
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null
        );
        setLevelCard.setId(TEST_ID);
        setLevelCard.setName(TEST_NAME);
        setLevelCard.setxPercent(TEST_X_PERCENT);
        setLevelCard.setyPercent(TEST_Y_PERCENT);
        setLevelCard.setWidth(TEST_WIDTH);
        setLevelCard.setHeight(TEST_HEIGHT);
        setLevelCard.setDeck(testDeck);
        setLevelCard.setBitmapPath(TEST_BITMAP_PATH);
        setLevelCard.setBitmapID(TEST_BITMAP_ID);
        setLevelCard.setPrerequisites(testPrerequisites);

        // Check values are as expected
        assertEquals(level.getId(), TEST_ID);
        assertEquals(level.getName(), TEST_NAME);
        assertEquals(level.getxPercent(), TEST_X_PERCENT);
        assertEquals(level.getyPercent(), TEST_Y_PERCENT);
        assertEquals(level.getWidth(), TEST_WIDTH);
        assertEquals(level.getHeight(), TEST_HEIGHT);
        assertEquals(level.getDeck(), testDeck);
        assertEquals(level.getBitmapPath(), TEST_BITMAP_PATH);
        assertEquals(level.getBitmapID(), TEST_BITMAP_ID);
        assertEquals(level.getPrerequisites(), testPrerequisites);
    }

    @Test
    public void levelNoPrerequisiteInitTest() {
        // Test deck values
        List<LevelCard> testDeck = new ArrayList<>();
        testDeck.add(
                new LevelCard(
                        TEST_CARD_NAME,
                        TEST_CARD_BITMAP,
                        TEST_CARD_MANA,
                        TEST_CARD_HEALTH,
                        TEST_CARD_ATTACK
                )
        );

        // Set up card by constructor
        Level level = new Level(
                TEST_ID,
                TEST_NAME,
                TEST_X_PERCENT,
                TEST_Y_PERCENT,
                TEST_WIDTH,
                TEST_HEIGHT,
                testDeck,
                TEST_BITMAP_PATH,
                TEST_BITMAP_ID
        );

        // Check values are as expected
        assertEquals(level.getId(), TEST_ID);
        assertEquals(level.getName(), TEST_NAME);
        assertEquals(level.getxPercent(), TEST_X_PERCENT);
        assertEquals(level.getyPercent(), TEST_Y_PERCENT);
        assertEquals(level.getWidth(), TEST_WIDTH);
        assertEquals(level.getHeight(), TEST_HEIGHT);
        assertEquals(level.getDeck(), testDeck);
        assertEquals(level.getBitmapPath(), TEST_BITMAP_PATH);
        assertEquals(level.getBitmapID(), TEST_BITMAP_ID);

        // Set up card by setter methods
        Level setLevelCard = new Level(
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null
        );
        setLevelCard.setId(TEST_ID);
        setLevelCard.setName(TEST_NAME);
        setLevelCard.setxPercent(TEST_X_PERCENT);
        setLevelCard.setyPercent(TEST_Y_PERCENT);
        setLevelCard.setWidth(TEST_WIDTH);
        setLevelCard.setHeight(TEST_HEIGHT);
        setLevelCard.setDeck(testDeck);
        setLevelCard.setBitmapPath(TEST_BITMAP_PATH);
        setLevelCard.setBitmapID(TEST_BITMAP_ID);

        // Check values are as expected
        assertEquals(level.getId(), TEST_ID);
        assertEquals(level.getName(), TEST_NAME);
        assertEquals(level.getxPercent(), TEST_X_PERCENT);
        assertEquals(level.getyPercent(), TEST_Y_PERCENT);
        assertEquals(level.getWidth(), TEST_WIDTH);
        assertEquals(level.getHeight(), TEST_HEIGHT);
        assertEquals(level.getDeck(), testDeck);
        assertEquals(level.getBitmapPath(), TEST_BITMAP_PATH);
        assertEquals(level.getBitmapID(), TEST_BITMAP_ID);
    }
}