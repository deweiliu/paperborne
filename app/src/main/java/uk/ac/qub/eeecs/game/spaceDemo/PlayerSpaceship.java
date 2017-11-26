package uk.ac.qub.eeecs.game.spaceDemo;

import uk.ac.qub.eeecs.gage.ai.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Player controlled spaceship
 *
 * @version 1.0
 */

public class PlayerSpaceship extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Centre of the screen (used to determine the offset of touch events)
     */
    private Vector2 screenCentre = new Vector2();

    /**
     * Acceleration vector based on the player's touch input
     */
    private Vector2 playerTouchAcceleration = new Vector2();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a player controlled spaceship
     *
     * @param startX     x location of the player spaceship
     * @param startY     y location of the player spaceship
     * @param gameScreen Gamescreen to which spaceship belongs
     */
    public PlayerSpaceship(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, 50.0f, 50.0f, gameScreen.getGame()
                .getAssetManager().getBitmap("Spaceship1"), gameScreen);

        // Store the centre of the screen
        screenCentre.x = gameScreen.getGame().getScreenWidth() / 2;
        screenCentre.y = gameScreen.getGame().getScreenHeight() / 2;

        // Define the maximum velocities and accelerations of the spaceship
        maxAcceleration = 600.0f;
        maxVelocity = 100.0f;
        maxAngularVelocity = 1400.0f;
        maxAngularAcceleration = 1400.0f;

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the player spaceship
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Consider any touch events occurring since the update
        Input input = mGameScreen.getGame().getInput();

        if (input.existsTouch(0)) {
            // Get the primary touch event
            playerTouchAcceleration.x = (input.getTouchX(0) - screenCentre.x)
                    / screenCentre.x;
            playerTouchAcceleration.y = (screenCentre.y - input.getTouchY(0))
                    / screenCentre.y; // Invert the for y axis

            // Convert into an input acceleration
            acceleration.x = playerTouchAcceleration.x * maxAcceleration;
            acceleration.y = playerTouchAcceleration.y * maxAcceleration;

        }

        // Ensure that the ships points in the direction of movement
        angularAcceleration = SteeringBehaviours.alignWithMovement(this);

        // Dampen the linear and angular acceleration and velocity
        angularAcceleration *= 0.95f;
        angularVelocity *= 0.75f;
        acceleration.multiply(0.75f);
        velocity.multiply(0.95f);

        // Apply the determined accelerations
        super.update(elapsedTime);
    }
}
