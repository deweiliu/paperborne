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

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.GameUtil;
import uk.ac.qub.eeecs.game.options.OptionsManager;
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;

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

    @Mock
    IGraphics2D iGraphics2D;

    @Mock
    List<LevelCard> playerDeck, opponentDeck;

    @Mock
    Level level;

    @Mock
    Context context;

    @Mock
    Music music;

    @Mock
    SharedPreferences sharedPreferences;

    @Mock
    OptionsManager optionsManager;

    @Before
    public void setUp() {
        screenManager = new ScreenManager();


        when(game.getInput()).thenReturn(input);
        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(game.getContext()).thenReturn(context);
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
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


    //Tests if the card is active when touched
    @Test
    public void cardActiveTest(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(1, "TestCard", 10, 10, bitmap, cardDemoScreen, 10, 5, 10);
        ElapsedTime elapsedTime = new ElapsedTime();

        card.draw(elapsedTime, iGraphics2D);

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = 10;
        touchEvent.y = 10;

        List<TouchEvent> touchEvents = new ArrayList<>();

        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        card.update(elapsedTime);
        assertTrue(card.isCardIsActive());
    }

    @Test
    public void cardNotActiveTest(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(1, "TestCard", 10, 10, bitmap, cardDemoScreen, 10, 5, 10);
        ElapsedTime elapsedTime = new ElapsedTime();

        card.draw(elapsedTime, iGraphics2D);

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_UP;
        touchEvent.x = 10;
        touchEvent.y = 10;

        List<TouchEvent> touchEvents = new ArrayList<>();

        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        card.update(elapsedTime);
        assertFalse(card.isCardIsActive());
    }

    @Test
    public void cardIsPressedTest(){
        ElapsedTime elapsedTime = new ElapsedTime();
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        ScreenViewport screenViewport = new ScreenViewport(0, 0, cardDemoScreen.getGame().getScreenWidth(), cardDemoScreen.getGame().getScreenHeight());

        LayerViewport layerViewport;

        if (screenViewport.width > screenViewport.height)
            layerViewport = new LayerViewport(240.0f, 240.0f
                    * screenViewport.height / screenViewport.width, 240,
                    240.0f * screenViewport.height / screenViewport.width);
        else
            layerViewport = new LayerViewport(240.0f * screenViewport.height
                    / screenViewport.width, 240.0f, 240.0f
                    * screenViewport.height / screenViewport.width, 240);



        //Creates new Hero
        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        // Play a randomly drawn card
        hero.playCard(hero.getDeck().drawCard());

        // Pick a card from the board
        Card card = hero.getActiveCards().get(0);



        // Create a touch position
        Vector2 touchPos = new Vector2(50.0f, 50.0f);

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = touchPos.x;
        touchEvent.y = touchPos.y;
        List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        // Convert the touch coordinates into screen coordinates
        GameUtil.convertLayerPosIntoScreen(screenViewport, touchPos, layerViewport, card.getPosition());

        //Call update method
        cardDemoScreen.update(elapsedTime);
        card.update(elapsedTime, screenViewport, layerViewport, hero);

        assertTrue(card.isCardPressedDown());
    }

    @Test
    public void cardPlayedTest(){
        int ScreenWidth = 1920;
        int ScreenHeight = 1080;
        when(game.getScreenWidth()).thenReturn(ScreenWidth);
        when(game.getScreenHeight()).thenReturn(ScreenHeight);

        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        ScreenViewport mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        LayerViewport mLayerViewport;

        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);


        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);
        hero.incrementManaLimit();
        hero.refillMana();

        Card card = new Card(0, "Test", ScreenWidth / 5.0f, (ScreenHeight / 3.0f) * 1.5f, bitmap, cardDemoScreen, 1, 1, 1);
        card.setCardState(Card.CardState.CARD_IN_HAND);

        ElapsedTime elapsedTime = new ElapsedTime();

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_UP;
        touchEvent.x = card.getPosition().x;
        touchEvent.y = card.getPosition().y;
        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchEvent);

        card.setCardPressedDown(true);
        Vector2 tempPos = new Vector2();
        InputHelper.convertScreenPosIntoLayer(mScreenViewport, new Vector2(touchEvent.x, touchEvent.y),
                mLayerViewport, tempPos);

        card.setPosition(tempPos.x, tempPos.y);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        card.update(elapsedTime, mScreenViewport, mLayerViewport, hero);

        assertTrue(card.getCardState() == Card.CardState.CARD_ON_BOARD);
    }

    @Test
    public void cardSetPositionTest(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setPosition(100, 100);

        assertTrue(card.getPosition().x == 100 && card.getPosition().y == 100);

    }

    @Test
    public void cardSetAnchorTest(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setAnchor(50, 50);

        assertTrue(card.getAnchor().x == 50 && card.getAnchor().y == 50);
    }


    @Test
    public void cardSetCardStateTest(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setCardState(Card.CardState.CARD_ON_BOARD);
        assertTrue(card.getCardState() == Card.CardState.CARD_ON_BOARD);
    }


    @Test
    public void cardSetLastPosition(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        Vector2 lastPosition = new Vector2();
        lastPosition.x = 10;
        lastPosition.y = 10;

        card.setLastPosition(lastPosition);
        assertTrue(card.getLastPosition() == lastPosition);
    }

    @Test
    public void cardSetManaCost(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);

        card.setManaCost(50);
        assertTrue(card.getManaCost() == 50);
    }

    //CS
    @Test
    public void cardSetAttackValue(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0,"Test",0,0,bitmap,cardDemoScreen,0,0,0);

        card.setAttackValue(20);
        assertTrue(card.getAttackValue() == 20);
    }

    //CS
    @Test
    public void cardSetHealthValue(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0,"Test",0,0,bitmap,cardDemoScreen,0,0,0);

        card.setHealthValue(20);
        assertTrue(card.getHealthValue() == 20);
    }

    //CS
    @Test
    public void cardSetCardID(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0,"Test",0,0,bitmap,cardDemoScreen,0,0,0);

        card.setCardID(20);
        assertTrue(card.getCardID() == 20);
    }

    //CS
    @Test
    public void cardSetCardName(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0,"Test",0,0,bitmap,cardDemoScreen,0,0,0);

        card.setCardName("New Card Name");
        assertTrue(card.getCardName() == "New Card Name");
    }

    //CS
    @Test
    public void cardSetFinishedMove(){
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0,"Test",0,0,bitmap,cardDemoScreen,0,0,0);

        card.setFinishedMove(true);
        assertTrue(card.isFinishedMove() == true);
    }
}