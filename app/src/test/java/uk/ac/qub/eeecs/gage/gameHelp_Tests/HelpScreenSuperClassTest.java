package uk.ac.qub.eeecs.gage.gameHelp_Tests;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.gameHelp.GameHelpController;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HelpScreenSuperClass;
import uk.ac.qub.eeecs.game.ui.PopUp;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

/**
 * Created by 40216004 Dewei Liu on 09/04/2018.
 * <p>
 * 8 unit tests in this class
 */

@RunWith(MockitoJUnitRunner.class)
public class HelpScreenSuperClassTest {

    private HelpScreenSuperClass screen;
    private Bitmap bitmap;
    private ElapsedTime elapsedTime;

    @Before
    public void setUp() {
        AssetStore assetManager = Mockito.mock(AssetStore.class);
        Game game = Mockito.mock(Game.class);
        when(game.getAssetManager()).thenReturn(assetManager);
        bitmap = Mockito.mock(Bitmap.class);
        elapsedTime = Mockito.mock(ElapsedTime.class);
        screen = new HelpScreenSuperClass("Test class", game, Mockito.mock(GameHelpController.class)) {
            protected void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
                //Draw nothing
            }
        };
    }

    @Test
    public void setPopUpMessage_TestNull() {
        assertEquals(null, screen.getPopUpMessage());
    }

    @Test
    public void setPopUpMessage_TestNotNull() {
        screen.setPopUpMessage("Hello, testing", 2, bitmap);
        assertNotEquals(null, screen.getPopUpMessage());
    }

    @Test
    public void setPopUpMessage_Test1_WithinThePeriod() {
        final int PERIOD = 3000;//3s for pop up message
        screen.setPopUpMessage("Hello, testing", PERIOD / 1000, bitmap);
        PopUp popUp = screen.getPopUpMessage();
        assertEquals(true, popUp.isVisible());

        sleep(PERIOD / 3, popUp);//1 second
        assertEquals(true, popUp.isVisible());

    }

    @Test
    public void setPopUpMessage_Test2_WithinThePeriod() {
        final int PERIOD = 4000;//4s for pop up message
        screen.setPopUpMessage("Hello, testing", PERIOD / 1000, bitmap);
        PopUp popUp = screen.getPopUpMessage();
        assertEquals(true, popUp.isVisible());

        sleep(PERIOD / 4 * 3, popUp);//3 seconds
        assertEquals(true, popUp.isVisible());

    }

    @Test
    public void setPopUpMessage_Test3_WithinThePeriod() {
        final int PERIOD = 8000;//10s for pop up message
        screen.setPopUpMessage("Hello, testing", PERIOD / 1000, bitmap);
        PopUp popUp = screen.getPopUpMessage();
        assertEquals(true, popUp.isVisible());

        sleep(PERIOD / 4, popUp);//2 second
        assertEquals(true, popUp.isVisible());

        sleep(PERIOD / 4, popUp);//2 seconds + 2 seconds = 4 seconds
        assertEquals(true, popUp.isVisible());

        sleep(PERIOD / 4, popUp);//2 seconds + 4 seconds = 6 seconds
        assertEquals(true, popUp.isVisible());
    }


    @Test
    public void setPopUpMessage_Test1_WithoutThePeriod() {
        final int PERIOD = 3000;//3s for pop up message
        screen.setPopUpMessage("Hello, testing", PERIOD / 1000, bitmap);
        PopUp popUp = screen.getPopUpMessage();

        sleep(PERIOD + 1000, popUp);//3 seconds + 1 seconds = 4 seconds
        assertEquals(false, popUp.isVisible());
    }

    @Test
    public void setPopUpMessage_Test2_WithoutThePeriod() {
        final int PERIOD = 5000;//5s for pop up message
        screen.setPopUpMessage("Hello, testing", PERIOD / 1000, bitmap);
        PopUp popUp = screen.getPopUpMessage();

        sleep(PERIOD + 1000, popUp);//5 seconds + 1 seconds = 6 seconds
        assertEquals(false, popUp.isVisible());
    }

    @Test
    public void setPopUpMessage_FinalTest() {
        final int PERIOD = 10000;//10s for pop up message
        long startTime = System.currentTimeMillis();
        screen.setPopUpMessage("Hello, testing", PERIOD / 1000, bitmap);
        PopUp popUp = screen.getPopUpMessage();
        while (true) {
            sleep(800, popUp);
            if (System.currentTimeMillis() - PERIOD < startTime) {
                assertEquals(true, popUp.isVisible());
            } else {
                assertEquals(false, popUp.isVisible());
                break;
            }
        }


    }

    private void sleep(long millis, PopUp object) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        object.update(elapsedTime);
    }
}
