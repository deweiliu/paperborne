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
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Deck;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Hand;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by Jamie C on 28/01/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class HeroTests {

    @Mock
    Game game;
    
    @Mock
    private SharedPreferences sharedPreferences;
    
    @Mock
    private Context context;

    @Mock
    Hero hero;

    @Mock
    Bitmap bitmap;

    @Mock
    Music music;

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
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
        when(game.getContext()).thenReturn(context);
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
    }

    //JC
    @Test
    public void testHero() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0,0, bitmap, cardDemoScreen, game);
        assertTrue(hero.getDeck() != null);
        assertTrue(hero.getHand() != null);
        assertTrue(hero.getBitmap() == bitmap);
    }

    @Test
    public void testHeroTakingDamage(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0,0, bitmap, cardDemoScreen, game);

        //Hero health is set at 30
        hero.takeDamage(10);
        assertTrue(hero.getCurrentHealth() == 20);

        hero.takeDamage(10);
        assertTrue(hero.getCurrentHealth() == 10);

    }

    @Test
    public void testHeroIsDead(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0,0, bitmap, cardDemoScreen, game);

        //Hero health is set at 30, so deal 30 damage to give 0
        hero.takeDamage(30);
        assertTrue(hero.getHeroIsDead());
    }

    //NS
    @Test
    public void testManaIncrement(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0,0, bitmap, cardDemoScreen, game);
        hero.incrementManaLimit();
        assertTrue(hero.getManaLimit() == 2);

    }

    //NS
    @Test
    public void setHeroTouched(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0, 0, bitmap, cardDemoScreen, game);
        hero.setHeroTouched(true);
        assertTrue(hero.getHeroTouched());
    }

    @Test
    public void testManaRefill(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0,0,bitmap,cardDemoScreen,game);
        //set hero mana to 2, as hero starts with 1 mana
        hero.incrementManaLimit();
        assertTrue(hero.getManaLimit() == 2);

        //refills hero's current mana to the mana limit
        hero.refillMana();
        assertTrue(hero.getCurrentMana() == 2);
    }

    @Test
    public void testHeroUsingMana(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        hero = new Hero(0,0, bitmap, cardDemoScreen, game);
        //Creates a card with a Mana Cost of 5
        Card card = new Card(1, "TestCard", 10, 10, bitmap, cardDemoScreen, 5, 5, 10);

        //Increments players mana to 10
        for(int i=0;i<10;i++){ hero.incrementManaLimit(); }
        //checks if hero's mana limit is at 10
        assertTrue(hero.getManaLimit() == 10);

        //Plays card
        hero.refillMana();
        hero.playCard(card);

        assertTrue(hero.getCurrentMana() == 5);

    }

}
