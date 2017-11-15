package uk.ac.qub.eeecs.game.options;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import android.graphics.Color;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;


/**
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */
public class OptionsScreen extends GameScreen {


    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////
    private PushButton stopShifting;
    private PushButton startShifting;
    private boolean isShifting=true;
    private int count=0;
    /**
     * Width and height of the level
     */
    private final float LEVEL_WIDTH = 500.0f;
    private final float LEVEL_HEIGHT = 1000.0f;
    /**
     * Define the background image
     */
    private GameObject mOptionsScreenBackground;

    /**
     * Define viewports for this layer and the associated screen projection
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;


    /**
     * Define the colour of background.
     */
    private int green=0;
    private int blue=0;
    private int red=0;
    private boolean flag=false;
    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Options screen
     *
     * @param game Game to which this screen belongs
     */
    public OptionsScreen(Game game) {
        super("OptionsScreen", game);

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    // Create the view ports

        mLayerViewport = new LayerViewport(240, 160, 240, 160);
        mScreenViewport = new ScreenViewport();
        GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);



    // Load in the assets used by this layer
    AssetStore assetManager = mGame.getAssetManager();
    assetManager.loadAndAddBitmap("OptionsScreenBackgroud1", "img/OptionsScreenBackgroud1.png");
        assetManager.loadAndAddBitmap("StartShifting", "img/StartShifting.png");
        assetManager.loadAndAddBitmap("StopShifting", "img/StopShifting.png");

        // Create the space background
        mOptionsScreenBackground = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
                .getAssetManager().getBitmap("OptionsScreenBackgroud1"), this);



        // Define the spacing that will be used to position the buttons
        int spacingX = game.getScreenWidth() / 5;
        int spacingY = game.getScreenHeight() / 3;

        // Create the push button
        stopShifting = new PushButton(
                spacingX * 4.0f, spacingY * 0.5f, spacingX, spacingY, "StopShifting", this);
        startShifting= new PushButton(
                spacingX * 1.0f, spacingY * 0.5f, spacingX, spacingY, "StartShifting", this);
    }
    /**
     * Update the options screen
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

            stopShifting.update(elapsedTime);
            startShifting.update(elapsedTime);

        }
        if (stopShifting.isPushTriggered()){
            isShifting=false;
        }else if(startShifting.isPushTriggered()){
            isShifting=true;
        }
        if(isShifting==true ){
            if(flag==false){
                if(red<255)red++;
                else if(green<255)green++;
                else if(blue<255)blue++;
                else flag=true;
            }else{
                if(red>0)red--;
                else if(green>0)green--;
                else if(blue>0)blue--;
                else flag=false;
            }
        }

    }

    /**
     * Draw the options screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Draw the background first of all
      graphics2D.clear(Color.WHITE);


      if(isShifting==true ){

          graphics2D.clear(Color.rgb(red,green,blue));
          stopShifting.draw(elapsedTime, graphics2D, null, null);
      }
      else{
          mOptionsScreenBackground.draw(elapsedTime, graphics2D, mLayerViewport,  mScreenViewport);
          startShifting.draw(elapsedTime, graphics2D, null, null);
      }
    }
}
