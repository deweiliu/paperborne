package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Deck;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie on 28/01/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class DeckTests {

    @Mock
    Game game;

    @Mock
    GameScreen cardDemoScreen = Mockito.mock(GameScreen.class);

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
    public void testDeck() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        Deck deck = new Deck(cardDemoScreen, game);
    }

    @Test
    public void testCardRemoval() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        int sizeBefore = hero.getDeck().getCardsInDeck().size();
        hero.getDeck().drawCard();
        int sizeAfter = hero.getDeck().getCardsInDeck().size();
        assertTrue(sizeAfter == (sizeBefore - 1));
    }

    @Test
    public void testDeckIsEmpty(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        hero.getDeck().getCardsInDeck().clear();

        assertTrue(hero.getDeck().isDeckEmpty());


    }


}
