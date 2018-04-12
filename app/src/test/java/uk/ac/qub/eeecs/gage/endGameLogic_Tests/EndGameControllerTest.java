package uk.ac.qub.eeecs.gage.endGameLogic_Tests;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

/**
 * Created by 40216004 Dewei Liu on 10/04/2018.
 *
 * 4 unit tests in this class
 */

@RunWith(MockitoJUnitRunner.class)
public class EndGameControllerTest {
    private EndGameController controller;
    private GameScreen cardDemoScreen;
    private boolean isSinglePlayer;
    private boolean hasPlayer1Won;

    @Before
    public void setUp() {
        Random random = new Random();
        isSinglePlayer = random.nextBoolean();
        hasPlayer1Won = random.nextBoolean();

        //Set up game screen
        Bitmap bitmap=Mockito.mock(Bitmap.class);
        AssetStore assetManager = Mockito.mock(AssetStore.class);
        when(assetManager.getBitmap("loadingToMainMenuFromEndGameLogic")).thenReturn(bitmap);
        when(assetManager.getBitmap("WinAnimation")).thenReturn(bitmap);
        when(assetManager.getBitmap("LoseAnimation")).thenReturn(bitmap);

        ScreenManager screenManager = Mockito.mock(ScreenManager.class);

        Game game = Mockito.mock(Game.class);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getScreenManager()).thenReturn(screenManager);
        cardDemoScreen = new GameScreen("Hello testing", game) {
            @Override
            public void update(ElapsedTime elapsedTime) {

            }

            @Override
            public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

            }
        };

        controller = new EndGameController(cardDemoScreen, isSinglePlayer, hasPlayer1Won);
    }

    @Test
    public void viewports() {
        assertNotEquals(null, controller.getLayerViewport());
        assertNotEquals(null, controller.getScreenViewport());
    }

    @Test
    public void isSinglePlayer_Test() {
        assertEquals(isSinglePlayer, controller.isSinglePlayer());
    }

    @Test
    public void sasPlayer1Won_Test() {
        assertEquals(hasPlayer1Won, controller.hasPlayer1Won());
    }

    @Test
    public void getCardDemoScreen_Test() {
        assertEquals(cardDemoScreen, controller.getCardDemoScreen());
    }

}
