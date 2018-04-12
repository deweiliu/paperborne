package uk.ac.qub.eeecs.gage.gameHelp_Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.gameHelp.GameHelpController;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HelpScreenSuperClass;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

/**
 * Created by 40216004 Dewei Liu on 10/04/2018.
 *
 * 7 unit tests in this class
 */

@RunWith(MockitoJUnitRunner.class)
public class GameHelpControllerTest {
    private GameHelpController controller;
    private ArrayList<HelpScreenSuperClass> helpScreens;
    private final static int SCREEN_NUMBER = 5;

    @Before
    public void setUp() {
        //Set up game
        Game game = Mockito.mock(Game.class);
        when(game.getAssetManager()).thenReturn(Mockito.mock(AssetStore.class));
        ScreenManager screenManager = Mockito.mock(ScreenManager.class);
        when(screenManager.getCurrentScreen()).thenReturn(Mockito.mock(GameScreen.class));
        when(game.getScreenManager()).thenReturn(screenManager);

        //Set up help screens
        helpScreens = new ArrayList<>();
        for (int i = 0; i < SCREEN_NUMBER; i++) {
            helpScreens.add(Mockito.mock(HelpScreenSuperClass.class));
        }

        //Set up the controller
        controller = new GameHelpController(game, helpScreens);
    }

    @Test
    public void initialScreen_test() {
        assertEquals(helpScreens.get(0), controller.getCurrentScreen());

    }

    /*********************************************************************************************/
    //For the function of  boolean previousScreen();
    @Test
    public void previous_AtTheFirstScreen_testFail() {
        //It is the first screen, so it cannot turn to the previous screen
        assertNotEquals(true, controller.previousScreen());
    }

    @Test
    public void previous_BeforeTheFistScreen_TestSuccess() {
        this.turnToTheLastScreen();

        for (int i = 0; i < SCREEN_NUMBER - 1; i++) {
            assertEquals(true, controller.previousScreen());
        }
    }

    @Test
    public void previous_TestCurrentScreen() {
        this.turnToTheLastScreen();

        for (int i = helpScreens.size() - 1; i >= 0; i--) {
            assertEquals(helpScreens.get(i), controller.getCurrentScreen());
            controller.previousScreen();
        }

    }

    /*********************************************************************************************/
    //For the function of boolean nextScreen()
    @Test
    public void next_BeforeTheLastScreen_TestSuccess() {
        for (int i = 0; i < SCREEN_NUMBER - 1; i++) {
            assertEquals(true, controller.nextScreen());
        }
    }

    @Test
    public void next_AtTheLastScreen_TestFail() {
        this.turnToTheLastScreen();

        //It is the last screen, so it cannot turn to the next screen
        assertNotEquals(true, controller.nextScreen());
    }

    @Test
    public void next_TestCurrentScreen() {
        for (HelpScreenSuperClass each : helpScreens) {
            assertEquals(each, controller.getCurrentScreen());
            controller.nextScreen();
        }
    }

    private void turnToTheLastScreen() {
        while (!controller.getCurrentScreen().equals(helpScreens.get(helpScreens.size() - 1))) {
            controller.nextScreen();
        }
    }

}
