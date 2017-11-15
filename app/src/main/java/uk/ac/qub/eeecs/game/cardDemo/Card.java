package uk.ac.qub.eeecs.game.cardDemo;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;

/**
 * Created by user on 14/11/2017.
 */


public class Card extends Sprite {

    private Vector2 screenCentre = new Vector2();
    private Vector2 playerTouchAcceleration = new Vector2();

    public Card (float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, 140.0f, 210.0f, gameScreen.getGame()
                .getAssetManager().getBitmap("Card"), gameScreen);

        maxAcceleration = 600.0f;
        maxVelocity = 100.0f;
        maxAngularVelocity = 1440.0f;
        maxAngularAcceleration = 1440.0f;
    }

    public void update(ElapsedTime elapsedTime) {
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
    }

}
