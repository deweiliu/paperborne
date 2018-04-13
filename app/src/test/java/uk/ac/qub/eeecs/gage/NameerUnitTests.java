package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Deck;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Hand;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NameerUnitTests {
    @Mock
    Game game;
    
    @Mock
    private SharedPreferences sharedPreferences;
    
    @Mock
    private Music music;
    
    @Mock
    private Context context;

    @Mock
    GameScreen cardDemoScreen = Mockito.mock(GameScreen.class);

    @Mock
    Hero hero;

    @Mock
    Deck deck;

    @Mock
    Hand hand;

    @Mock
    Bitmap bitmap;

    @Mock
    AssetStore assetManager;

    @Mock
    ScreenManager screenManager;

    @Before
    public void setUp(){
        //hero = new Hero(0, 0, bitmap, cardGameScreen, game);

        screenManager = new ScreenManager();

        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
        when(game.getContext()).thenReturn(context);
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
    }

    //This tests that when a Hand is created, the size of the hand is as should be expected
    //NS
    @Test
    public void testHandCreation(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        Hero hero = new Hero(0,0, bitmap, cardDemoScreen, game);

        assertEquals(hero.getHand().getCards().size(), 10);

    }
}
