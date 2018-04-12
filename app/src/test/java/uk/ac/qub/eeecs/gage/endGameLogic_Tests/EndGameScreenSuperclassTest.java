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
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreenSuperclass;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by 40216004 on 10/04/2018.
 *
 * 10 unit tests in this class
 */

@RunWith(MockitoJUnitRunner.class)
public class EndGameScreenSuperclassTest {

    private EndGameController controller;
    private EndGameScreenSuperclass superClass;

    @Before
    public void setUp() {
/********************************************************************************************************/
//Set up the controller
        Random random = new Random();
        boolean isSinglePlayer = random.nextBoolean();
        boolean hasPlayer1Won = random.nextBoolean();

        //Set up game screen
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        AssetStore assetManager = Mockito.mock(AssetStore.class);
        when(assetManager.getBitmap("loadingToMainMenuFromEndGameLogic")).thenReturn(bitmap);
        when(assetManager.getBitmap("WinAnimation")).thenReturn(bitmap);
        when(assetManager.getBitmap("LoseAnimation")).thenReturn(bitmap);

        ScreenManager screenManager = Mockito.mock(ScreenManager.class);

        Game game = Mockito.mock(Game.class);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getScreenManager()).thenReturn(screenManager);
        GameScreen cardDemoScreen = new GameScreen("Hello testing", game) {
            @Override
            public void update(ElapsedTime elapsedTime) {

            }

            @Override
            public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

            }
        };
        controller = new EndGameController(cardDemoScreen, isSinglePlayer, hasPlayer1Won);

        /********************************************************************************************************/
//Set up the object of the super class
        superClass = new EndGameScreenSuperclass(controller) {
        };
    }

    @Test
    public void layerViewport_Test() {
        assertEquals(controller.getLayerViewport(), superClass.getLayerViewport());
    }

    @Test
    public void ScreenViewport_Test() {
        assertEquals(controller.getScreenViewport(), superClass.getScreenViewPort());
    }

    @Test
    public void getGameScreen_Test() {
        assertEquals(controller, superClass.getGameScreen());
    }

    @Test
    public void getGame_Test() {
        assertEquals(controller.getGame(), superClass.getGame());
    }

    @Test
    public void getAssetManager_Test() {
        assertEquals(controller.getGame().getAssetManager(), superClass.getAssetManager());
    }

    @Test
    public void getScreenWidth_Test() {
        assertEquals(controller.getGame().getScreenWidth(), superClass.getScreenWidth());
    }

    @Test
    public void getScreenHeight_Test() {
        assertEquals(controller.getGame().getScreenHeight(), superClass.getScreenHeight());
    }

    @Test
    public void isSinglePlayer_Test() {
        assertEquals(controller.isSinglePlayer(), superClass.isSinglePlayer());
    }

    @Test
    public void hasPlayer1Won_Test() {
        assertEquals(controller.hasPlayer1Won(), superClass.hasPlayer1Won());
    }

    @Test
    public void getBattleScreen_Test() {
        assertEquals(controller.getCardDemoScreen(), superClass.getCardDemoScreen());
    }


}
