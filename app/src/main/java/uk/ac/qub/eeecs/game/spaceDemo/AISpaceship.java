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
    /*
    User story 7 (1/2) : AI Spaceship avoidance Behaviour
    Dewei
    Changed the value of separateThresholdShip from 75 to 180.
    Changed the value of separateThresholdAsteroid from 125 to 200.
    Define repulsion DecayFactor with value of 3.0f.
     */
    private float repulsionDecayFactor=3.0f;            //1.0f former
    private float separateThresholdShip = 180.0f;       //75.0f former
    private float separateThresholdAsteroid = 200.0f;   //125.0f former

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
    /*User story 4 (3/3): AI Spaceship Size Variety
     Dewei
     Change the class constructor of AISpaceship
      */
    public AISpaceship(float startX, float startY,float AISpaceshipWidth,float AISpaceshipHeight,
                       ShipBehaviour shipBehaviour, SpaceshipDemoScreen gameScreen) {
        super(startX, startY, AISpaceshipWidth, AISpaceshipHeight, null, gameScreen);

        mShipBehaviour = shipBehaviour;

        switch (mShipBehaviour) {
            case Turret:
                maxAcceleration = 0.0f;
                maxVelocity = 0.0f;
                maxAngularVelocity = 50.0f;
                maxAngularAcceleration = 50.0f;
                mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Turret");
                break;
            case Seeker:
                maxAcceleration = 30.0f;
                maxVelocity = 50.0f;
                maxAngularVelocity = 150.0f;
                maxAngularAcceleration = 300.0f;
                mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship2");
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

                /*User story 7 (2/2) : AI Space ship Avoidance Behaviour
                Dewei
                Change the 4th argument of function SteeringBehaviours.separate from 1.0 to the valuable repulsionDecayFactor.
                 */
                // Try to avoid a collision with the player ship
                SteeringBehaviours.separate(this,
                        ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship(),
                        separateThresholdShip, repulsionDecayFactor, accComponent);
                accAccumulator.set(accComponent);

                // Try to avoid a collision with the other spaceships
                SteeringBehaviours.separate(this,
                        ((SpaceshipDemoScreen) mGameScreen).getAISpaceships(),
                        separateThresholdShip, repulsionDecayFactor, accComponent);
                accAccumulator.add(accComponent);

                // Try to avoid a collision with the asteroids
                SteeringBehaviours.separate(this,
                        ((SpaceshipDemoScreen) mGameScreen).getAsteroids(),
                        separateThresholdAsteroid, repulsionDecayFactor, accComponent);
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
