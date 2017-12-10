package uk.ac.qub.eeecs.gage;

import android.content.Context;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.performanceScreen.PerformanceScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class PerformanceScreenTest {

    @Test
    public void makeScreen() throws Exception {

        Context appContext = InstrumentationRegistry.getTargetContext();

        DemoGame game = new DemoGame();
        AssetStore assetStore = new AssetStore(new FileIO(appContext));
        game.mAssetManager = assetStore;
        PerformanceScreen performanceScreen = new PerformanceScreen(game);

        assertNotNull(performanceScreen);
        assertEquals(performanceScreen.getNumRectangles(), 100);
        assertNotNull(performanceScreen.getmRectanglesDown());
        assertNotNull(performanceScreen.getmRectanglesUp());
    }
}