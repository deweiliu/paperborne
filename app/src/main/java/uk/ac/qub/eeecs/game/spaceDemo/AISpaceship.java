package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.Random;

import uk.ac.qub.eeecs.gage.ai.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * AI controlled spaceship
 *
 * @version 1.0
 */
public class AISpaceship extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * AI control behaviour
     */
    public enum ShipBehaviour {
        Turret, Seeker
    }

    private ShipBehaviour mShipBehaviour;

    /**
     * Distance at which the spaceship should avoid other game objects
     */
    private float separateThresholdShip = 75.0f;
    private float separateThresholdAsteroid = 125.0f;

    /**
     * Accumulators used to build up the net steering outcome
     */
    private Vector2 accAccumulator = new Vector2();
    private Vector2 accComponent = new Vector2();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a AI controlled spaceship
     *
     * @param startX        x location of the AI spaceship
     * @param startY        y location of the AI spaceship
     * @param shipBehaviour Steering behaviour to be used by the AI ship
     * @param gameScreen    Gamescreen to which AI belongs
     */
    public AISpaceship(float startX, float startY, ShipBehaviour shipBehaviour,
                       SpaceshipDemoScreen gameScreen) {
        super(startX, startY, 50.0f, 50.0f, null, gameScreen);

        mShipBehaviour = shipBehaviour;

        //Added code
        //Generates a random number, if the number is even choose 1 image, if odd choose another
        Random randomImage = new Random();
        /*
        int chosenImage = 0;
        if (randomImage.nextInt() % 2 == 0) {
            chosenImage = 1;
        }*/

        switch (mShipBehaviour) {
            case Turret:
                maxAcceleration = 0.0f;
                maxVelocity = 0.0f;
                maxAngularVelocity = 50.0f;
                maxAngularAcceleration = 50.0f;
                /*if (chosenImage == 1) {
                    mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Turret2");
                } else {
                    mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Turret1");
                }*/
                mBitmap = gameScreen.getGame().getAssetManager().getBitmap(randomImage.nextBoolean() ? "Turret2" : "Turret1");

                break;
            case Seeker:
                maxAcceleration = 30.0f;
                maxVelocity = 50.0f;
                maxAngularVelocity = 150.0f;
                maxAngularAcceleration = 300.0f;
                /*if (chosenImage == 1) {
                    mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship3");
                } else {

                    mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship4");
                }*/

                //User Story 5
                mBitmap = gameScreen.getGame().getAssetManager()
                        .getBitmap(randomImage.nextBoolean() ? "Spaceship4" : "Spaceship3");

                break;
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the AI Spaceship
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        switch (mShipBehaviour) {
            case Turret:
                // Turn towards the player
                angularAcceleration =
                        SteeringBehaviours.lookAt(this,
                                ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship().position);
                break;
            case Seeker:
                // Seek towards the player
                SteeringBehaviours.seek(this,
                        ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship().position,
                        acceleration);

                // Try to avoid a collision with the player ship
                SteeringBehaviours.separate(this,
                        ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship(),
                        separateThresholdShip, 1.0f, accComponent);
                accAccumulator.set(accComponent);

                // Try to avoid a collision with the other spaceships
                SteeringBehaviours.separate(this,
                        ((SpaceshipDemoScreen) mGameScreen).getAISpaceships(),
                        separateThresholdShip, 1.0f, accComponent);
                accAccumulator.add(accComponent);

                // Try to avoid a collision with the asteroids
                SteeringBehaviours.separate(this,
                        ((SpaceshipDemoScreen) mGameScreen).getAsteroids(),
                        separateThresholdAsteroid, 1.0f, accComponent);
                accAccumulator.add(accComponent);

                // If we are trying to avoid a collision then combine
                // it with the seek behaviour, placing more emphasis on
                // avoiding a collision.
                if (!accAccumulator.isZero()) {
                    acceleration.x = 0.1f * acceleration.x + 0.9f * accAccumulator.x;
                    acceleration.y = 0.1f * acceleration.y + 0.9f * accAccumulator.y;
                }

                // Make sure we point in the direction of travel.
                angularAcceleration = SteeringBehaviours.alignWithMovement(this);

                break;
        }

        // Call the sprite's superclass to apply the determined accelerations
        super.update(elapsedTime);
    }
}
