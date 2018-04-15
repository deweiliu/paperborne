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
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreenSuperclass;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.SinglePlayerGameOver;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.interface_superclass.GameOverSuperclass;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by 40216004 Dewei Liu on 10/04/2018.
 * <p>
 * 3 unit tests in this class
 */

@RunWith(MockitoJUnitRunner.class)
public class SinglePlayerGameOverTest {
    private GameOverSuperclass singlePlayer;
    private final static int PERIOD = 5000; //5s
    private ElapsedTime elapsedTime;

    @Before
    public void setUp() {
        elapsedTime = Mockito.mock(ElapsedTime.class);

        /********************************************************************************************************/
        //Set up the controller
        Random random = new Random();
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
        EndGameController controller = new EndGameController(cardDemoScreen, true, hasPlayer1Won);

        /********************************************************************************************************/
        //Set up the singlePlayer of the super class
        EndGameScreen endGameScreen = new EndGameScreenSuperclass(controller) {
        };

        /**************************************************************************************************************/
        //Set up the GameOverSuperclass
        singlePlayer = new SinglePlayerGameOver(endGameScreen, Mockito.mock(Bitmap.class));
    }

    @Test
    public void Test1NotFinished() {
        singlePlayer.start(PERIOD);
        sleep(PERIOD / 2, singlePlayer);
        assertEquals(false, singlePlayer.isFinished());
    }

    @Test
    public void Test2NotFinished() {
        singlePlayer.start(PERIOD);
        sleep(PERIOD / 5 * 4, singlePlayer);
        assertEquals(false, singlePlayer.isFinished());
    }

    @Test
    public void finalTest() {
        singlePlayer.start(PERIOD);
        long startTime = System.currentTimeMillis();
        while (startTime + PERIOD < System.currentTimeMillis()) {
            assertEquals(false, singlePlayer.isFinished());
            sleep(PERIOD / 10, singlePlayer);
        }
    }

    private void sleep(long millis, GameOverSuperclass object) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        object.update(elapsedTime);
    }
}
