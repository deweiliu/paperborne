package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by user on 08/12/2017.
 */

public class CamUnitTest {

    String cardDemoScreenName = "CardDemo";

    @Mock
    Game game;
    @Mock
    GameScreen cardDemoScreen = Mockito.mock(GameScreen.class);
    @Mock
    AssetStore assetManager;
    @Mock
    Bitmap bitmap;
    @Mock
    Input input;

    @Before
    public void setUp() {
        when(cardDemoScreen.getName()).thenReturn(cardDemoScreenName);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
    }

    @Test
    public void cardConstruction(){
        //Define expected properties
        int expectedID = 1;
        String expectedName = "Test";
        float expectedX = 100.0f;
        float expectedY = 100.0f;
        String expectedBitmap = "Card";
        float expectedWidth = 100.0f;
        float expectedHeight = 100.0f;
        int expectedMana = 1;
        int expectedAttack = 1;
        int expectedHealth = 1;

        //Test data
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);

        //Creates Card
        Card card = new Card(expectedID, expectedName, expectedX, expectedY
        ,bitmap,cardDemoScreen,expectedMana,expectedAttack,expectedHealth);


    }
}
