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
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.cardDemo.TurnController;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie C on 14/04/2018
 */

@RunWith(MockitoJUnitRunner.class)
public class TurnControllerTest {

    @Mock
    Game game;

    @Mock
    Bitmap bitmap;

    @Mock
    AssetStore assetManager;

    @Mock
    ScreenManager screenManager;

    @Mock
    Context context;

    @Mock
    Music music;

    @Mock
    SharedPreferences sharedPreferences;

    @Mock
    TurnController turnController;

    @Mock
    CardDemoScreen cardDemoScreen;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getAssetManager().getMusic(any(String.class))).thenReturn(music);
        when(game.getContext()).thenReturn(context);
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);
    }

    //JC
    @Test
    public void testSwitchTurn() {
        cardDemoScreen = new CardDemoScreen(game);
        turnController = cardDemoScreen.getTurnController();
        boolean turn = turnController.isPlayerTurn();
        turnController.switchTurn();
        assertFalse(turn == turnController.isPlayerTurn());
    }

    //JC
    @Test
    public void testReturnTurn() {
        cardDemoScreen = new CardDemoScreen(game);
        turnController = cardDemoScreen.getTurnController();
        assertTrue(turnController.isPlayerTurn()); // game begins on player's turn
        turnController.switchTurn(); // change turn
        assertFalse(turnController.isPlayerTurn());
    }

    //JC
    @Test
    public void testEndTurn() {
        cardDemoScreen = new CardDemoScreen(game);
        turnController = cardDemoScreen.getTurnController();
        boolean turn = turnController.isPlayerTurn();
        turnController.doEndTurn(); //should swap turns
        assertFalse(turn == turnController.isPlayerTurn());
    }
}
