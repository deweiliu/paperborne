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
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie C on 28/01/2018.
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
    ArrayList<LevelCard> playerDeck, opponentDeck;

    @Mock
    Level level;

    @Mock
    Context context;

    @Mock
    Music music;

    @Mock
    SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        screenManager = new ScreenManager();
        int screenWidth = 1920, screenHeight = 1080;

        when(game.getScreenWidth()).thenReturn(screenWidth);
        when(game.getScreenHeight()).thenReturn(screenHeight);

        when(game.getInput()).thenReturn(input);
        when(game.getScreenManager()).thenReturn(screenManager);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
        when(game.getContext()).thenReturn(context);
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
    }

    //JC
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

    //JC
    @Test
    public void damageTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 10);

        card.takeDamage(1);
        assertTrue(card.getHealthValue() == 9);
    }

    //JC
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
    //NS
    @Test
    public void cardActiveTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        ScreenViewport mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        LayerViewport mLayerViewport;

        if (mScreenViewport.width > mScreenViewport.height) {
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        } else {
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
        }

        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        Card card = new Card(1, "TestCard", 10, 10, bitmap, cardDemoScreen, 0, 0, 0);
        ElapsedTime elapsedTime = new ElapsedTime();

        Vector2 touchPos = new Vector2();
        touchPos.x = 10;
        touchPos.y = 10;

        TestUtil.convertLayerPosIntoScreen(mScreenViewport, touchPos, mLayerViewport, card.getPosition());

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = touchPos.x;
        touchEvent.y = touchPos.y;

        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        card.update(elapsedTime, mScreenViewport, mLayerViewport, hero);
        assertTrue(card.isCardIsActive());
    }

    //Tests If the card remains not active when a touch event takes place outside the card
    //NS
    @Test
    public void cardNotActiveTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        ScreenViewport mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        LayerViewport mLayerViewport;

        if (mScreenViewport.width > mScreenViewport.height) {
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        } else {
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
        }


        card = new Card(1, "TestCard", 10, 10, bitmap, cardDemoScreen, 10, 5, 10);
        ElapsedTime elapsedTime = new ElapsedTime();

        Vector2 touchPos = new Vector2();
        touchPos.x = 100;
        touchPos.y = 100;
        TestUtil.convertLayerPosIntoScreen(mScreenViewport, touchPos, mLayerViewport, card.position);

        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_UP;
        touchEvent.x = touchPos.x;
        touchEvent.y = touchPos.y;

        List<TouchEvent> touchEvents = new ArrayList<>();

        touchEvents.add(touchEvent);

        when(input.getTouchEvents()).thenReturn(touchEvents);

        card.update(elapsedTime, mScreenViewport, mLayerViewport, hero);
        assertFalse(card.isCardIsActive());
    }

    //Tests a card is played when it has been dropped in the correct area
    //NS
    @Test
    public void cardPlayedTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);
        hero.incrementManaLimit();
        hero.refillMana();

        ScreenViewport mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        LayerViewport mLayerViewport;

        if (mScreenViewport.width > mScreenViewport.height) {
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        } else {
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
        }


        hero.incrementManaLimit();
        hero.refillMana();

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setCardState(Card.CardState.CARD_IN_HAND);

        ElapsedTime elapsedTime = new ElapsedTime();
        Vector2 touchPos = new Vector2();
        touchPos.x = 60;
        touchPos.y = 1000;
        InputHelper.convertScreenPosIntoLayer(mScreenViewport, touchPos, mLayerViewport, card.getPosition());
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_UP;
        touchEvent.x = touchPos.x;
        touchEvent.y = touchPos.y;
        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchEvent);
        card.setCardPressedDown(true);
        card.setPosition(touchPos.x, touchPos.y);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        card.update(elapsedTime, mScreenViewport, mLayerViewport, hero);
        assertTrue(card.getCardState() == Card.CardState.CARD_ON_BOARD);

        //Test a card remains in hand when dropped in the right area
        Card card2 = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card2.setCardState(Card.CardState.CARD_IN_HAND);
        touchPos = new Vector2();
        touchPos.x = 60;
        touchPos.y = 10;

        InputHelper.convertScreenPosIntoLayer(mScreenViewport, touchPos, mLayerViewport, card2.getPosition());
        TouchEvent touchEvent2 = new TouchEvent();
        touchEvent2.type = TouchEvent.TOUCH_UP;
        touchEvent2.x = touchPos.x;
        touchEvent2.y = touchPos.y;
        List<TouchEvent> touchEvents2 = new ArrayList<>();
        touchEvents.add(touchEvent2);
        card2.setCardPressedDown(true);
        card2.setPosition(touchPos.x, touchPos.y);
        when(input.getTouchEvents()).thenReturn(touchEvents2);
        card2.update(elapsedTime, mScreenViewport, mLayerViewport, hero);
        assertTrue(card2.getCardState() == Card.CardState.CARD_IN_HAND);
    }

    //The following tests test various setter methods
    //NS
    @Test
    public void cardSetPositionTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);

        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setPosition(100, 100);

        assertTrue(card.getPosition().x == 100 && card.getPosition().y == 100);

    }

    //NS
    @Test
    public void cardSetAnchorTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setAnchor(50, 50);

        assertTrue(card.getAnchor().x == 50 && card.getAnchor().y == 50);
        card.setAnchor(10, 10);
        assertTrue(card.getAnchor().x == 10 && card.getAnchor().y == 10);
    }

    //NS
    @Test
    public void cardSetCardStateTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        card.setCardState(Card.CardState.CARD_ON_BOARD);
        assertTrue(card.getCardState() == Card.CardState.CARD_ON_BOARD);
        card.setCardState(Card.CardState.CARD_IN_DECK);
        assertTrue(card.getCardState() == Card.CardState.CARD_IN_DECK);
        card.setCardState(Card.CardState.CARD_IN_HAND);
        assertTrue(card.getCardState() == Card.CardState.CARD_IN_HAND);
    }

    //NS
    @Test
    public void cardSetLastPosition() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);
        Vector2 lastPosition = new Vector2();
        lastPosition.x = 10;
        lastPosition.y = 10;

        card.setLastPosition(lastPosition);
        assertTrue(card.getLastPosition() == lastPosition);

        lastPosition.x = 100;
        lastPosition.y = 200;
        card.setLastPosition(lastPosition);
        assertTrue(card.getLastPosition() == lastPosition);
    }

    //NS
    @Test
    public void cardSetManaCost() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 1, 1, 1);

        card.setManaCost(50);
        assertTrue(card.getManaCost() == 50);

        card.setManaCost(10);
        assertTrue(card.getManaCost() == 10);
    }

    //CS
    @Test
    public void cardSetAttackValue() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 0, 0, 0);

        card.setAttackValue(20);
        assertTrue(card.getAttackValue() == 20);
        card.setAttackValue(5);
        assertTrue(card.getAttackValue() == 5);
    }

    //CS
    @Test
    public void cardSetHealthValue() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 0, 0, 0);

        card.setHealthValue(20);
        assertTrue(card.getHealthValue() == 20);
    }

    //CS
    @Test
    public void cardSetCardID() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 0, 0, 0);

        card.setCardID(20);
        assertTrue(card.getCardID() == 20);
    }

    //CS
    @Test
    public void cardSetCardName() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 0, 0, 0);

        card.setCardName("New Card Name");
        assertTrue(card.getCardName().equals("New Card Name"));
    }

    //CS
    @Test
    public void cardSetFinishedMove() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        card = new Card(0, "Test", 0, 0, bitmap, cardDemoScreen, 0, 0, 0);

        card.setFinishedMove(true);
        assertTrue(card.isFinishedMove());
    }

    //JC
    @Test
    public void heroesWithDeck() {

        //create levelcard lists

        String bitmapString = "";

        LevelCard oneCostCard = new LevelCard("Weak Man", bitmapString, 1, 1, 1);
        LevelCard twoCostCard = new LevelCard("Dog", bitmapString, 2, 2, 2);
        LevelCard threeCostCard = new LevelCard("Fat Man", bitmapString, 3, 2, 3);
        LevelCard fourCostCard = new LevelCard("Sword", bitmapString, 4, 1, 4);
        LevelCard fiveCostCard = new LevelCard("Dragon", bitmapString, 5, 7, 5);

        playerDeck = new ArrayList<>();
        opponentDeck = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            playerDeck.add(new LevelCard(oneCostCard));
            opponentDeck.add(new LevelCard(oneCostCard));
        }
        for (int i = 0; i < 4; i++) {
            playerDeck.add(new LevelCard(twoCostCard));
            opponentDeck.add(new LevelCard(twoCostCard));
        }
        for (int i = 0; i < 3; i++) {
            playerDeck.add(new LevelCard(threeCostCard));
            opponentDeck.add(new LevelCard(threeCostCard));
        }
        for (int i = 0; i < 2; i++) {
            playerDeck.add(new LevelCard(fourCostCard));
            opponentDeck.add(new LevelCard(fourCostCard));
        }
        playerDeck.add(new LevelCard(fiveCostCard));
        opponentDeck.add(new LevelCard(fiveCostCard));

        CardDemoScreen cardDemoScreen = new CardDemoScreen(game, opponentDeck, playerDeck, level);
        game.getScreenManager().addScreen(cardDemoScreen);

        int plCount1 = 0, plCount2 = 0, plCount3 = 0, plCount4 = 0, plCount5 = 0;
        int opCount1 = 0, opCount2 = 0, opCount3 = 0, opCount4 = 0, opCount5 = 0;


        for (Card card : cardDemoScreen.getPlayer().getDeck().getCardsInDeck()) {
            switch (card.getManaCost()) {
                case 1:
                    plCount1++;
                    break;
                case 2:
                    plCount2++;
                    break;
                case 3:
                    plCount3++;
                    break;
                case 4:
                    plCount4++;
                    break;
                case 5:
                    plCount5++;
                    break;
                default:
                    fail(); //really shouldn't ever see this
                    break;
            }
        }
        for (Card card : cardDemoScreen.getPlayer().getHand().getCards()) {
            switch (card.getManaCost()) {
                case 1:
                    plCount1++;
                    break;
                case 2:
                    plCount2++;
                    break;
                case 3:
                    plCount3++;
                    break;
                case 4:
                    plCount4++;
                    break;
                case 5:
                    plCount5++;
                    break;
                default:
                    fail(); //really shouldn't ever see this
                    break;
            }
        }

        for (Card card : cardDemoScreen.getOpponent().getDeck().getCardsInDeck()) {
            switch (card.getManaCost()) {
                case 1:
                    opCount1++;
                    break;
                case 2:
                    opCount2++;
                    break;
                case 3:
                    opCount3++;
                    break;
                case 4:
                    opCount4++;
                    break;
                case 5:
                    opCount5++;
                    break;
                default:
                    fail(); //really shouldn't ever see this
                    break;
            }
        }

        for (Card card : cardDemoScreen.getOpponent().getHand().getCards()) {
            switch (card.getManaCost()) {
                case 1:
                    opCount1++;
                    break;
                case 2:
                    opCount2++;
                    break;
                case 3:
                    opCount3++;
                    break;
                case 4:
                    opCount4++;
                    break;
                case 5:
                    opCount5++;
                    break;
                default:
                    fail(); //really shouldn't ever see this
                    break;
            }
        }


        //check number of cards are correct

        assertEquals(5, plCount1);
        assertEquals(4, plCount2);
        assertEquals(3, plCount3);
        assertEquals(2, plCount4);
        assertEquals(1, plCount5);

        assertEquals(5, opCount1);
        assertEquals(4, opCount2);
        assertEquals(3, opCount3);
        assertEquals(2, opCount4);
        assertEquals(1, opCount5);
    }

    //Tests if a card acknowledges when a touch event takes place on it
    //NS
    @Test
    public void cardIsPressedTest() {
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
        ElapsedTime elapsedTime = new ElapsedTime();


        ScreenViewport mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        LayerViewport mLayerViewport;

        if (mScreenViewport.width > mScreenViewport.height) {
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        } else {
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);
        }

        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);

        card = new Card(0, "Test", 10, 10, bitmap, cardDemoScreen, 0, 0, 0);
        card.setCardState(Card.CardState.CARD_IN_HAND);

        Vector2 touchPos = new Vector2();
        touchPos.x = 10;
        touchPos.y = 10;

        TestUtil.convertLayerPosIntoScreen(mScreenViewport, touchPos, mLayerViewport, card.getPosition());

        List<TouchEvent> touchEvents = new ArrayList<>();
        TouchEvent touchEvent = new TouchEvent();
        touchEvent.type = TouchEvent.TOUCH_DOWN;
        touchEvent.x = touchPos.x;
        touchEvent.y = touchPos.y;

        touchEvents.add(touchEvent);
        when(input.getTouchEvents()).thenReturn(touchEvents);
        card.setFinishedMove(false);
        card.update(elapsedTime, mScreenViewport, mLayerViewport, hero);

        assertTrue(card.isCardPressedDown());
    }
}