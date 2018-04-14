package uk.ac.qub.eeecs.gage;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CardsOnBoardTest
{
    // Screen width and height for touch events
    private final int SCREEN_WIDTH = 1280;
    private final int SCREEN_HEIGHT = 720;
    
    // Mocked input
    @Mock
    private Input input;
    
    @Mock
    private SharedPreferences sharedPreferences;
    
    @Mock
    private Music music;
    
    @Mock
    private Context context;
    
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
        when(game.getScreenWidth()).thenReturn(SCREEN_WIDTH);
        when(game.getScreenHeight()).thenReturn(SCREEN_HEIGHT);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
        when(game.getContext()).thenReturn(context);
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
    }
    
    /**
     * Checks that cards that are played from the hand to the board have the correct cardstate
     */
    @Test
    public void checkCardPlayedState()
    {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
    
        // Check that each card in the deck is the expected cardstate
        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);
        // Draw 5 cards and play them on the board
        for(int i = 0; i < 3; i++)
        {
            hero.playCard(hero.getHand().getCards().get(i));
        }
        for(Card card : hero.getActiveCards())
        {
            assertTrue(card.getCardState() == Card.CardState.CARD_ON_BOARD);
        }
    }
    
    @Test
    public void checkCardRemovedOnDeath() {

        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        for(int i = 0; i < 9; i++) {
            hero.incrementManaLimit();
        }

        for(Card card : hero.getHand().getCards()) { // a card of mana cost 1 must be played first for some reason
            if(card.getManaCost() == 1) {
                hero.playCard(card);
                hero.refillMana();
                break;
            }
        }

        //play five cards total
        for(int i = 0; i < 4; i++) {
            hero.playCard(hero.getHand().getCards().get(0));
            hero.refillMana();
        }

        //damage first card for max health
        hero.getActiveCards().get(0).takeDamage(hero.getActiveCards().get(0).getHealthValue());

        //remove it
        hero.clearDeadCards();

        assertTrue(hero.getActiveCards().size() == 4);
    }

}
