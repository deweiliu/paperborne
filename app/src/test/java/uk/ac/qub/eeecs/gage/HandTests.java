package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie on 28/01/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class HandTests
{

    @Mock
    private Game game;

    @Mock
    private Bitmap bitmap;

    @Mock
    private AssetStore assetManager;

    @Mock
    private ScreenManager screenManager;

    @Before
    public void setUp() {
        screenManager = new ScreenManager();

        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
    }
    
    
    @Test
    public void testCardDrawnStates()
    {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        
        // Check that each card in the deck is the expected cardstate
        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);
        for(Card card : hero.getHand().getCards())
        {
            assertTrue(card.getCardState() == Card.CardState.CARD_IN_HAND);
        }
    }


}
