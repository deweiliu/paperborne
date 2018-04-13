package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.res.AssetManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;

import uk.ac.qub.eeecs.game.GameUtil;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
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

    @Before
    public void setUp() {
        when(context.getAssets()).thenReturn(assetManager);
    }

    @Test
    public void getJSONAssetCorrect() {

        try {
            when(assetManager.open(any(String.class))).thenAnswer(
                    new Answer<InputStream>() {
                        public InputStream answer(InvocationOnMock invocation) {
                            return inputStream;
                        }
                    });
            when(inputStream.available()).thenReturn(8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "filename";
        assertFalse(GameUtil.getJSONAsset(context, filename).isEmpty());
    }

    @Test
    public void getJSONAssetWrong() {
        try {
            when(assetManager.open(any(String.class))).thenThrow(new IOException());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ignore any IOException here. It's meant to be here.");
        assertNull(GameUtil.getJSONAsset(context, ""));
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
