package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.spaceDemo.PlayerSpaceship;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Example instrumentation tests, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // /////////////////////////////////////////////////////////////////////////
    // Example Instrumented Test
    // /////////////////////////////////////////////////////////////////////////

    @Test
    public void example_gameObjectCreation() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        // Build a demo game
        DemoGame demo = new DemoGame();

        // The demo game isn't properly constructed until the onCreate and
        // onCreateView methods are called. We are going to partially constrct
        // the class using a bit of this code to add a functional asset manager
        // which game objects can use to load assets.
        AssetStore assetStore = new AssetStore(new FileIO(appContext));
        demo.mAssetManager = assetStore;

        // Create the spaceship steering demo
        SpaceshipDemoScreen steeringDemoGameScreen = new SpaceshipDemoScreen(demo);

        // Test that we can extract the player spaceship from the demo
        PlayerSpaceship playerSpaceship = steeringDemoGameScreen.getPlayerSpaceship();
        assertNotNull(playerSpaceship);
    }
}
