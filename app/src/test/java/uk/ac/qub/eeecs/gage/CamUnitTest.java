package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by user on 08/12/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class CamUnitTest {


    @Mock
    Game game;

    @Mock
    GameScreen cardDemoScreen = Mockito.mock(GameScreen.class);

    @Mock
    Card card;

    @Mock
    Bitmap bitmap;

    @Mock
    AssetStore assetManager;

    @Mock
    ScreenManager screenManager;

    @Before
    public void setUp() {
        screenManager = new ScreenManager();

        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
    }


    @Test
    public void cardConstructionTest(){
        //Define expected properties
        int expectedID = 1;
        String expectedName = "Test";
        float expectedX = 100.0f;
        float expectedY = 100.0f;
        Bitmap expectedBitmap = bitmap;
        int expectedMana = 1;
        int expectedAttack = 1;
        int expectedHealth = 1;

        //Test data
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game, new ArrayList<LevelCard>());

        //Creates Card1 and Card2 for comparison that the values get added to the repective vairables
        Card card1 = new Card(1, "Test", 100.0f, 100.0f
        ,bitmap,cardDemoScreen,1,1,1);

        Card card2 = new Card(expectedID,expectedName,expectedX,expectedY
                ,expectedBitmap,cardDemoScreen,expectedMana,expectedAttack,expectedHealth);

        //Checks if not equal in memory
        assertTrue(card1 != card);

        //Checks if equal in variables
        assertTrue(card1.getCardID() == card2.getCardID());
        assertTrue(card1.getCardName() == card2.getCardName());
        assertTrue(card1.getPosition().x == card2.getPosition().x);
        assertTrue(card1.getPosition().y == card2.getPosition().y);
        assertTrue(card1.getBitmap() == card2.getBitmap());
        assertTrue(card1.getManaCost() == card2.getManaCost());
        assertTrue(card1.getAttackValue() == card2.getAttackValue());
        assertTrue(card1.getHealthValue() == card2.getHealthValue());


    }
}