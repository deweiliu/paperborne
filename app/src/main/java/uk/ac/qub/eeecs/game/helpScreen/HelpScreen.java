package uk.ac.qub.eeecs.game.helpScreen;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Vector;


import uk.ac.qub.eeecs.gage.world.GameObject;

import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Starter class for Card game stories in the 3rd sprint
 * Created by Dewei on 05/12/2017.
 * Modified by Dewei on 07/12/2017.
 * Modified by Dewei on 08/12/2017.
 */


public class HelpScreen extends GameScreen {

    /**
     * Define the background star scape
     */
    private GameObject mHelpScreenBackground;
    private GameObject mHelpScreenLogo;

    /**
     * Define the backIconHeight and backIconWidth of screen
     */
    private float SCREEN_WIDTH;
    private float SCREEN_HEIGHT;

    /**
     * Define viewports  for this layer and the associated screen projection
     * 1 is for the background and text
     * 2 is for the logo on the top of the screen
     */
    private ScreenViewport mScreenViewport1;
    private LayerViewport mLayerViewport1;


    private ScreenViewport mScreenViewport2;
    private LayerViewport mLayerViewport2;
    /**
     * Define viewports' CentreXLocation and CentreYLocation
     */
    private float CentreXLocation;
    private float CentreYLocation;
    private float yFactor;
    private float xFactor;
    /**
     * Define textual game rules
     */
    private Vector<String> gameRules;
    //private String gameRules = "A  <br>B<br/>  C<br/> D</br> E F G H Igame ruleThis is our game ruleThis is our \ngame ruleThis is our game ruleThis is our game ruleThis is our game ruleThis is our game rules";

    /**
     * Define text paint format
     */
    private Paint mTextFormat;
    final private int textSize = 80;
    final private int textLineSpacing = textSize / 5;
    /**
     * Define the position of the textual game rules
     */
    private float firstLineTextPositionX;
    private float firstLineTextPositionY;
    /**
     * Define the touch event of the last touch
     */

    private float lastTouchPositionX;
    private float lastTouchPositionY;
    /**
     * Define the touch event of the this touch
     */

    private float currentTouchPositionX;
    private float currentTouchPositionY;

    /**
     * Decide if the touch event is scroll
     */
    private boolean scroll = false;


    /**
     * Define a back icon to go back to the card demo screen
     */
    private PushButton mBackIcon;

    /**
     * The backIconWidth and backIconHeight of the back icon
     */
    private float backIconHeight;
    private float backIconWidth;
    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Help screen
     *
     * @param game Game to which this screen belongs
     */
    public HelpScreen(Game game) {
        super("HelpScreen", game);

        //Get screen backIconWidth and backIconHeight
        SCREEN_HEIGHT = game.getScreenHeight();
        SCREEN_WIDTH = game.getScreenWidth();
        xFactor = 1.0f;
        yFactor = 2.5f;
        CentreXLocation = (xFactor - 1) * SCREEN_WIDTH / 2;
        CentreYLocation = (yFactor - 1) * SCREEN_HEIGHT / 2;

        // Create the screen viewport
        mScreenViewport1 = new ScreenViewport(0, (int) (SCREEN_HEIGHT / 5), (int) SCREEN_WIDTH, (int) SCREEN_HEIGHT);

        mLayerViewport1 = new LayerViewport(CentreXLocation, CentreYLocation, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);


        mScreenViewport2 = new ScreenViewport(0, 0, (int) SCREEN_WIDTH, (int) (SCREEN_HEIGHT / 5));
        mLayerViewport2 = new LayerViewport(0, 0, (int) SCREEN_WIDTH / 2, (int) (SCREEN_HEIGHT / 5) / 2);

        // Load in the assets used by the steering demo
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("HelpBackground", "img/HelpBackground.png");
        assetManager.loadAndAddBitmap("HelpScreenLogo", "img/HelpScreenLogo.png");
        assetManager.loadAndAddBitmap("BackIcon", "img/LeftArrow.png");

        // Create the back button
        backIconHeight = SCREEN_HEIGHT / 10;
        backIconWidth = SCREEN_WIDTH / 10;
        mBackIcon = new PushButton(backIconWidth / 2, SCREEN_HEIGHT - backIconHeight / 2, backIconWidth, backIconHeight, "BackIcon", this);

        // Create help screen background
        mHelpScreenBackground = new GameObject(0, 0, SCREEN_WIDTH * xFactor, SCREEN_HEIGHT * yFactor,
                getGame().getAssetManager().getBitmap("HelpBackground"), this);

        // Create the header
        mHelpScreenLogo = new GameObject(0, 0, (int) SCREEN_WIDTH, (int) (SCREEN_HEIGHT / 5),
                getGame().getAssetManager().getBitmap("HelpScreenLogo"), this);


        //Set text painting format
        mTextFormat = new Paint();
        mTextFormat.setTextSize(textSize);
        mTextFormat.setColor(Color.WHITE);

        //Define the text in help screen
        gameRules = new Vector<String>();
        gameRules.add("Hello, welcome to check out our game rules!");
        gameRules.add("");
        gameRules.add("First, press the \"PLAY\" button.");
        gameRules.add("");
        gameRules.add("/*Here is the details of the first step.*/");
        gameRules.add("");
        gameRules.add("Second, choose the hero you want to use.");
        gameRules.add("");
        gameRules.add("/*Here is the details of the second step.*/");
        gameRules.add("");
        gameRules.add("Third, play cards to beat your component!");
        gameRules.add("");
        gameRules.add("/*Here is the details of the third step.*/");
        gameRules.add("");
        gameRules.add("Copyrights 2017");
        gameRules.add("");
        gameRules.add("Cam Stevenson, Dewei Liu, Jamie Caldwell,");
        gameRules.add("Jamie Thompson and Nameer Shahzad.");
        gameRules.add("");
        gameRules.add("All Rights Reserved.");

    }


    /**
     * Update the help screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();


        //Go back to the card demo screen
        mBackIcon.update(elapsedTime);
        if (mBackIcon.isPushTriggered()) {
            changeToScreen(new CardDemoScreen(mGame));
        }

        // Consider any touch events occurring since the update
        if (input.existsTouch(0)) {
            // Get the primary touch event
            currentTouchPositionX = input.getTouchX(0);
            currentTouchPositionY = input.getTouchY(0);
            if (scroll == false) {
                scroll = true;
                lastTouchPositionX = currentTouchPositionX;
                lastTouchPositionY = currentTouchPositionY;
            } else {

                //Do not move for horizontal direction
                //CentreXLocation = CentreXLocation + (lastTouchPositionX - currentTouchPositionX);
                //Just add the above statement if you want to add firstLineTextPositionX feature that you can drag horizontally

                //Do move for vertical direction
                CentreYLocation = CentreYLocation + (currentTouchPositionY - lastTouchPositionY);
                lastTouchPositionX = currentTouchPositionX;
                lastTouchPositionY = currentTouchPositionY;
            }

        } else {
            scroll = false;

            //Acceleration must greater than 0
            final float Acceleration = 0.5f;
            float standarCentreLocationX, standarCentreLocationY;

            CentreXLocation = mLayerViewport1.x;
            CentreYLocation = mLayerViewport1.y;

            if (mLayerViewport1.getTop() > SCREEN_HEIGHT * yFactor / 2) {
                standarCentreLocationY = (SCREEN_HEIGHT * yFactor / 2 - mLayerViewport1.halfHeight);
                CentreYLocation = (mLayerViewport1.y - standarCentreLocationY) / (Acceleration + 1) + standarCentreLocationY;
            } else {
                if (mLayerViewport1.getBottom() < -SCREEN_HEIGHT * yFactor / 2) {

                    standarCentreLocationY = mLayerViewport1.halfHeight - SCREEN_HEIGHT * yFactor / 2;
                    CentreYLocation = (mLayerViewport1.y - standarCentreLocationY) / (Acceleration + 1) + standarCentreLocationY;
                }
            }

            if (mLayerViewport1.getLeft() < -SCREEN_WIDTH * xFactor / 2) {
                standarCentreLocationX = (mLayerViewport1.halfWidth - SCREEN_WIDTH * xFactor / 2);
                CentreXLocation = (mLayerViewport1.x - standarCentreLocationX) / (Acceleration + 1) + standarCentreLocationX;

            } else {
                if (mLayerViewport1.getRight() > SCREEN_WIDTH * xFactor / 2) {
                    standarCentreLocationX = -mLayerViewport1.halfWidth + SCREEN_WIDTH * xFactor / 2;
                    CentreXLocation = (mLayerViewport1.x - standarCentreLocationX) / (Acceleration + 1) + standarCentreLocationX;
                }
            }
        }
        //mHelpScreenLogo.setPosition(CentreXLocation, CentreYLocation + SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 10);
        mLayerViewport1.set(CentreXLocation, CentreYLocation, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);


        //Update the position of textual game rules
        firstLineTextPositionX = (SCREEN_WIDTH * xFactor / 2) - mLayerViewport1.getRight() + textSize * 3;
        firstLineTextPositionY = mLayerViewport1.getTop() - SCREEN_HEIGHT * yFactor / 2 + SCREEN_HEIGHT / 5 + textSize;


        //Update the position of back arrow icon
        mBackIcon.setPosition(backIconWidth / 2, SCREEN_HEIGHT - backIconHeight / 2);


    }


    /**
     * Draw the help screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the help screen background first of all
        mHelpScreenBackground.draw(elapsedTime, graphics2D, mLayerViewport1, mScreenViewport1);


        // Draw the textual game rules
        for (int i = 0; i < gameRules.size(); i++) {
            float currentLineTextPositionX, currentLineTextPositionY;
            currentLineTextPositionX = firstLineTextPositionX;
            currentLineTextPositionY = firstLineTextPositionY + i * (textSize + textLineSpacing);
            graphics2D.drawText(gameRules.get(i), currentLineTextPositionX, currentLineTextPositionY, mTextFormat);
        }


        //Draw the help screen logo
        mHelpScreenLogo.draw(elapsedTime, graphics2D, mLayerViewport2, mScreenViewport2);

        //Draw the back icon
        mBackIcon.draw(elapsedTime, graphics2D, null, null);
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

}
