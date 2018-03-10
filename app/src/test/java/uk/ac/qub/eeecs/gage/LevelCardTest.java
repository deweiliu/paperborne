package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.GameUtil;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Jamie T on 10/03/2018.
 * Used for unit testing the LevelCard class
 */

@RunWith(MockitoJUnitRunner.class)
public class LevelCardTest
{
    // Test values for the card
    private static final int TEST_ATTACK = 1;
    private static final int TEST_HEALTH = 1;
    private static final int TEST_MANA = 1;
    private static final String TEST_BITMAP = "test_bitmap";
    private static final String TEST_NAME = "test_card";
    
    @Test
    public void levelCardInitTest()
    {
        // Set up card by constructor
        LevelCard levelCard = new LevelCard(
                TEST_NAME,
                TEST_BITMAP,
                TEST_MANA,
                TEST_HEALTH,
                TEST_ATTACK
        );
        
        // Check values are as expected
        assertEquals(levelCard.getName(), TEST_NAME);
        assertEquals(levelCard.getBitmap(), TEST_BITMAP);
        assertEquals(levelCard.getManaCost(), TEST_MANA);
        assertEquals(levelCard.getHealthValue(), TEST_HEALTH);
        assertEquals(levelCard.getAttackValue(), TEST_ATTACK);
    
        // Set up card by setter methods
        LevelCard setLevelCard = new LevelCard(null, null, 0, 0 , 0);
        setLevelCard.setName(TEST_NAME);
        setLevelCard.setBitmap(TEST_BITMAP);
        setLevelCard.setAttackValue(TEST_ATTACK);
        setLevelCard.setHealthValue(TEST_HEALTH);
        setLevelCard.setManaCost(TEST_MANA);
        
        // Check values are as expected
        assertEquals(setLevelCard.getName(), TEST_NAME);
        assertEquals(setLevelCard.getBitmap(), TEST_BITMAP);
        assertEquals(setLevelCard.getManaCost(), TEST_MANA);
        assertEquals(setLevelCard.getHealthValue(), TEST_HEALTH);
        assertEquals(setLevelCard.getAttackValue(), TEST_ATTACK);
        
    }
    
    @Test
    public void saveGameJSONTest()
    {
        // Set up test card
        LevelCard levelCard = new LevelCard(
                TEST_NAME,
                TEST_BITMAP,
                TEST_MANA,
                TEST_HEALTH,
                TEST_ATTACK
        );
        
        // Check the JSON output is valid
        assertTrue(GameUtil.isJSONValid(levelCard.toJSON()));
    }
}