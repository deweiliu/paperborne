package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Example local unit tests, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    // /////////////////////////////////////////////////////////////////////////
    // Simple Unit Test
    // /////////////////////////////////////////////////////////////////////////

    @Test
    public void addition_isCorrect() throws Exception {
        // As basic as it gets!
        assertEquals(4, 2 + 2);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Example of mocking
    // /////////////////////////////////////////////////////////////////////////

    @Mock
    GameObject object1, object2;

    @Test
    public void determineCollisionType_CorrectDetection_TestIsSuccess()
    {
        BoundingBox bound1 = new BoundingBox(100, 100, 50, 50);
        Vector2 position1 = new Vector2(100,100);
        when(object1.getBound()).thenReturn(bound1);

        BoundingBox bound2 = new BoundingBox(180, 180, 50, 50);
        Vector2 position2 = new Vector2(180,180);
        when(object2.getBound()).thenReturn(bound2);

        CollisionDetector.CollisionType collisionType =
                CollisionDetector.determineCollisionType(bound1, bound2);

        assert(collisionType == CollisionDetector.CollisionType.Bottom);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Example of mocking setup and testing object construction
    // /////////////////////////////////////////////////////////////////////////

    @Mock
    private Game spaceShipGame;
    //@Mock
   // private SpaceshipDemoScreen spaceShipGameScreen;
    @Mock
    private AssetStore spaceShipAssetManager;
    @Mock
    private Bitmap spaceShipBitmap;

    @Before
    public void setUp() {
       // when(spaceShipGameScreen.getGame()).thenReturn(spaceShipGame);
        when(spaceShipGame.getAssetManager()).thenReturn(spaceShipAssetManager);
    }

    @Test

    public void aiSpaceShip_TurretConstruction_ExpectedProperties() {
        // Define expected properties
        float expectedXPosition = 100.0f;
        float expectedYPosition = 50.0f;
        float expectedMaxAcceleration = 0.0f;
        float expectedMaxVelocity = 0.0f;
        float expectedMaxAngularVelocity = 50.0f;
        float expectedMaxAngularAcceleration = 50.0f;

        /*User story 4 (2/3): AI Spaceship Size Variety
		Dewei
		Define 2 valuable.
		*/
        float expectedWidth=50.0f;
        float expectedHeight=50.0f;
        String expectedBitmap = "Turret";

        // Add in a mock that could not be defined as part of the setup as it relies
        // on the expected turret bitmap name
        when(spaceShipAssetManager.getBitmap(expectedBitmap)).thenReturn(spaceShipBitmap);

        // Create a new aiSpaceship turret instance
       // AISpaceship aiSpaceship = new AISpaceship(expectedXPosition, expectedYPosition, expectedWidth, expectedHeight,
              //  AISpaceship.ShipBehaviour.Turret, spaceShipGameScreen);

        // Test that the constructed values are as expected
      /*  assertTrue(aiSpaceship.position.x == expectedXPosition);
        assertTrue(aiSpaceship.position.y == expectedYPosition);
        assertTrue(aiSpaceship.maxAcceleration == expectedMaxAcceleration);
        assertTrue(aiSpaceship.maxVelocity == expectedMaxVelocity);
        assertTrue(aiSpaceship.maxAngularVelocity == expectedMaxAngularVelocity);
        assertTrue(aiSpaceship.maxAngularAcceleration == expectedMaxAngularAcceleration);
        assertEquals(aiSpaceship.getBitmap(), spaceShipBitmap);*/
    }
}





