package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.res.AssetManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;

import uk.ac.qub.eeecs.game.GameUtil;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Jamie C on 28/01/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class GameUtilTest {

    @Mock
    private Context context;

    @Mock
    AssetManager assetManager;

    @Mock
    InputStream inputStream;

    //TODO - fix all the GameUtil.getJSONAsset tests
    @Before
    public void setUp() {
        String expected = "expected";
        when(context.getAssets()).thenReturn(assetManager);
    }

    @Test
    public void getJSONAssetCorrect() {
        String filename = "filename";
        assertTrue(GameUtil.getJSONAsset(context, filename).isEmpty());
    }

    @Test
    public void getJSONAssetWrong() {

    }

    @Test
    public void getJSONAssetTest() {
        String filename = "";
        GameUtil.getJSONAsset(context, filename);
    }


    @Test
    public void invalidJSONTest() {
        String testString = "<>";

        assertFalse(GameUtil.isJSONValid(testString));
    }

    @Test
    public void validJSONTest() {
        String validJSON = "{age:18}";

        assertTrue(GameUtil.isJSONValid(validJSON));
    }
}
