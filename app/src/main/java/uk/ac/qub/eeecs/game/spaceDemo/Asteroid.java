package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Simple asteroid
 *
 * @version 1.0
 */
public class Asteroid extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Private Random instance used to create the asteroids
     */
    private static Random random = new Random();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an asteroid
     *
     * @param startX     x location of the asteroid
     * @param startY     y location of the asteroid
     * @param gameScreen Gamescreen to which asteroid belongs
     */
    public Asteroid(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, 200.0f, 200.0f, null, gameScreen);

        mBitmap = gameScreen.getGame().getAssetManager()
                .getBitmap(random.nextBoolean() ? "Asteroid1" : "Asteroid2");


        /*User story 2 : Asteroid Size Variety
        Dewei
        mBound.halfHeight and mBound.halfWidth were changed
         */
        mBound.halfWidth = random.nextInt(30)+10.0f;//20.0f former
        mBound.halfHeight =  mBound.halfWidth;      //20.0f former

        angularVelocity = random.nextFloat() * 240.0f - 20.0f;
    }
}
