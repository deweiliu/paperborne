package uk.ac.qub.eeecs.gage;


import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
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
            hero.playCard(hero.getDeck().drawCard());
        }
        for(Card card : hero.getActiveCards())
        {
            assertTrue(card.getCardState() == Card.CardState.CARD_ON_BOARD);
        }
    }
    
    /**
     * Checks if when a card is tapped it is selected
     */
    @Test
    public void checkCardSelection()
    {
        // Elapsed time for updates
        ElapsedTime elapsedTime = new ElapsedTime();
        // Set up screen
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        game.getScreenManager().addScreen(cardDemoScreen);
    
        // Create viewports to allow conversion from touch coordinates into screen coords
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
        
        // Create a touch position
        Vector2 touchPos = new Vector2(50.0f, 50.0f);
    
        // Create a hero, deck card etc.
        Hero hero = new Hero(0, 0, bitmap, cardDemoScreen, game);
        
        // Play a randomly drawn card
        hero.playCard(hero.getDeck().drawCard());
        
        // Pick a card from the board
        Card boardCard = hero.getActiveCards().get(0);
        
        // Set up the touch event
        TouchEvent touchPosition = new TouchEvent();
        touchPosition.x = touchPos.x;
        touchPosition.y = touchPos.y;
        touchPosition.type = TouchEvent.TOUCH_DOWN;
        List<TouchEvent> touchEvents = new ArrayList<>();
        touchEvents.add(touchPosition);
    
        // Convert the touch coordinates into screen coordinates
        Vector2 cardPos = new Vector2();
        InputHelper.convertScreenPosIntoLayer(
                screenViewport,
                new Vector2(touchPos.x, touchPos.y),
                layerViewport, cardPos);
        
        // Place the card in the exact position of the touch
        boardCard.position = new Vector2(cardPos.x, cardPos.y);
        
        // Set up simulated return for touch events
        when(input.getTouchEvents()).thenReturn(touchEvents);
        
        // Update the card
        cardDemoScreen.update(elapsedTime);
        boardCard.update(elapsedTime, screenViewport, layerViewport);
        
        // Check it is selected, it is pressed and the cards move hasn't finished
        assertTrue(boardCard.isCardIsActive());
        assertTrue(boardCard.isCardPressedDown());
        assertTrue(!boardCard.isFinishedMove());
    
        // Set up the touch up event
        touchPosition = new TouchEvent();
        touchPosition.type = TouchEvent.TOUCH_UP;
        touchEvents.add(touchPosition);
    
        // Update card
        cardDemoScreen.update(elapsedTime);
        boardCard.update(elapsedTime, screenViewport, layerViewport);
        
        // Check that the card is selected, it isn't pressed and the cards move hasn't finished
        assertTrue(boardCard.isCardIsActive());
        assertTrue(!boardCard.isCardPressedDown());
        assertTrue(!boardCard.isFinishedMove());
        
    }
    

}
