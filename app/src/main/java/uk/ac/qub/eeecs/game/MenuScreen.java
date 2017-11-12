package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.performanceScreen.PerformanceScreen;
import uk.ac.qub.eeecs.game.platformDemo.PlatformDemoScreen;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;

/**
 * An exceedingly basic menu screen with a couple of touch buttons
 *
 * @version 1.0
 */
public class MenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the buttons for playing the 'games'
     */
    private PushButton mSpaceshipDemoButton;
    private PushButton mPlatformDemoButton;
    private PushButton mCardDemoButton;
    private PushButton mPerformanceButton;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple menu screen
     *
     * @param game Game to which this screen belongs
     */
    public MenuScreen(Game game) {
        super("MenuScreen", game);

        // Load in the bitmaps used on the main menu screen
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("SpaceshipDemoIcon", "img/Spaceship1.png");
        assetManager.loadAndAddBitmap("CardDemoIcon", "img/CardBackground1.png");
        assetManager.loadAndAddBitmap("PlatformDemoIcon", "img/Platform1.png");
        assetManager.loadAndAddBitmap("PerformanceIcon", "img/Performance.png");

        // Define the spacing that will be used to position the buttons
        int spacingX = game.getScreenWidth() / 5;
        int spacingY = game.getScreenHeight() / 3;

        // Create the trigger buttons
        mPerformanceButton = new PushButton(
                spacingX * 0.25f, game.getScreenHeight() - (spacingY/4), spacingX/2, spacingY/2, "PerformanceIcon", this);
        mSpaceshipDemoButton = new PushButton(
                spacingX * 1.0f, spacingY * 1.5f, spacingX, spacingY, "SpaceshipDemoIcon", this);
        mCardDemoButton = new PushButton(
                spacingX * 2.5f, spacingY * 1.5f, spacingX, spacingY, "CardDemoIcon", this);
        mPlatformDemoButton = new PushButton(
                spacingX * 4.0f, spacingY * 1.5f, spacingX, spacingY, "PlatformDemoIcon", this);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the menu screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Just check the first touch event that occurred in the frame.
            // It means pressing the screen with several fingers may not
            // trigger a 'button', but, hey, it's an exceedingly basic menu.
            TouchEvent touchEvent = touchEvents.get(0);

            // Update each button and transition if needed

            mPerformanceButton.update(elapsedTime);
            mSpaceshipDemoButton.update(elapsedTime);
            mCardDemoButton.update(elapsedTime);
            mPlatformDemoButton.update(elapsedTime);


            if(mPerformanceButton.isPushTriggered())
                changeToScreen(new PerformanceScreen(mGame));
            if (mSpaceshipDemoButton.isPushTriggered())
                changeToScreen(new SpaceshipDemoScreen(mGame));
            else if (mCardDemoButton.isPushTriggered())
                changeToScreen(new CardDemoScreen(mGame));
            else if (mPlatformDemoButton.isPushTriggered())
                changeToScreen(new PlatformDemoScreen(mGame));
        }
    }

    /**
     * Remove the current game screen and then change to the specified screen
     *
     * @param screen game screen to become active
     */
    private void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
    }

    /**
     * Draw the menu screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear the screen and draw the buttons
        graphics2D.clear(Color.WHITE);

        mPerformanceButton.draw(elapsedTime, graphics2D, null, null);
        mSpaceshipDemoButton.draw(elapsedTime, graphics2D, null, null);
        mCardDemoButton.draw(elapsedTime, graphics2D, null, null);
        mPlatformDemoButton.draw(elapsedTime, graphics2D, null, null);

    }
}
