package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie on 28/01/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class CardTests {

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

    @Mock
    Input input;

    @Before
    public void setUp() {
        screenManager = new ScreenManager();

        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);

    }

    @Test
    public void cardCopyTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        Card newCard = new Card(card);

        //not equals in the "not literal same object sense"
        assertTrue(newCard != card);

        //but true in every other way
        assertTrue(newCard.getCardID() == card.getCardID());
        assertTrue(newCard.getCardName() == card.getCardName());
        assertTrue(newCard.getPosition().x == card.getPosition().x);
        assertTrue(newCard.getPosition().y == card.getPosition().y);
        assertTrue(newCard.getBitmap() == card.getBitmap());
        assertTrue(newCard.getManaCost() == card.getManaCost());
        assertTrue(newCard.getAttackValue() == card.getAttackValue());
        assertTrue(newCard.getHealthValue() == card.getHealthValue());
    }

    @Test
    public void damageTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 10);

        card.takeDamage(1);
        assertTrue(card.getHealthValue() == 9);
    }

    @Test
    public void deathTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.takeDamage(1);
        assertTrue(card.getCardIsDead());

        Card card2 = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 10);
        card2.takeDamage(1);
        assertFalse(card2.getCardIsDead());
    }

    @Test
    public void cardIsPressedTest(){

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = card.position.x;
        touchEvent.y = card.position.y;
        List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        ElapsedTime elapsedTime = new ElapsedTime();

        //Creates new Card
        card = new Card(0, "Test", 10.0f, 10.0f, bitmap, cardDemoScreen, 1, 1, 1);

        //Call update method
        cardDemoScreen.update(elapsedTime);

        assertTrue(card.isCardPressedDown());
    }



}
