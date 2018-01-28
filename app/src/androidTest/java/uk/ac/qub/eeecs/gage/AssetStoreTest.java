package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example AssetStore Unit Tests.
 * <p>
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AssetStoreTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess() {
        // Build asset store
        AssetStore assetStore = new AssetStore(new FileIO(context));
        // Check for successful load
        String assetName = "Spaceship1", assetPath = "img/Spaceship1.png";
        assertTrue(assetStore.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddBitmap_InvalidData_TestErrorReport() {
        // Build asset store
        AssetStore assetStore = new AssetStore(new FileIO(context));
        // Check for an unsuccessful load
        String assetName = "Spaceship1", assetPath = "img/Doesnotexist.png";
        assertFalse(assetStore.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddBitmap_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetStore assetStore = new AssetStore(new FileIO(context));
        // Load a bitmap and test it cannot be added a second time
        String assetName = "Spaceship1", assetPath = "img/Spaceship1.png";
        assetStore.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetStore.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void getBitmap_ValidGet_TestIsSuccess() {
        // Build asset store
        AssetStore assetStore = new AssetStore(new FileIO(context));
        // Load a bitmap and test it can be extracted
        String assetName = "Spaceship1", assetPath = "img/Spaceship1.png";
        assetStore.loadAndAddBitmap(assetName, assetPath);
        assertNotNull(assetStore.getBitmap(assetName));
    }
}
