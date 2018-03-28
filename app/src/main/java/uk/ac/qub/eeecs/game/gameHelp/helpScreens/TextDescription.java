package uk.ac.qub.eeecs.game.gameHelp.helpScreens;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Vector;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.gameHelp.HelpScreenController;

/**
 * Created by 40216004 Dewei Liu on 05/12/2017.
 */


public class TextDescription extends HelpScreenSuperClass {


    /**
     * Define the backIconHeight and backIconWidth of screen
     */
    private float SCREEN_WIDTH;
    private float SCREEN_HEIGHT;

    /**
     * Define viewports  for this layer and the associated screen projection
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;

    /* * Define viewports' CentreXLocation and CentreYLocation
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


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Help screen
     *
     * @param game Game to which this screen belongs
     */
    public TextDescription(Game game, HelpScreenController controller) {
        super("TextDescription", game, controller);

        //Get screen backIconWidth and backIconHeight
        SCREEN_HEIGHT = game.getScreenHeight();
        SCREEN_WIDTH = game.getScreenWidth();
        xFactor = 1.0f;
        yFactor = 2.5f;
        CentreXLocation = (xFactor - 1) * SCREEN_WIDTH / 2;
        CentreYLocation = (yFactor - 1) * SCREEN_HEIGHT / 2;

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, (int) (SCREEN_HEIGHT / 5), (int) SCREEN_WIDTH, (int) SCREEN_HEIGHT);

        mLayerViewport = new LayerViewport(CentreXLocation, CentreYLocation, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

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
        gameRules.add("Copyrights 2018");
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

        super.update(elapsedTime);

        // Process any touch events occurring since the last update
        Input input = mGame.getInput();


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

            CentreXLocation = mLayerViewport.x;
            CentreYLocation = mLayerViewport.y;

            if (mLayerViewport.getTop() > SCREEN_HEIGHT * yFactor / 2) {
                standarCentreLocationY = (SCREEN_HEIGHT * yFactor / 2 - mLayerViewport.halfHeight);
                CentreYLocation = (mLayerViewport.y - standarCentreLocationY) / (Acceleration + 1) + standarCentreLocationY;
            } else {
                if (mLayerViewport.getBottom() < -SCREEN_HEIGHT * yFactor / 2) {

                    standarCentreLocationY = mLayerViewport.halfHeight - SCREEN_HEIGHT * yFactor / 2;
                    CentreYLocation = (mLayerViewport.y - standarCentreLocationY) / (Acceleration + 1) + standarCentreLocationY;
                }
            }

            if (mLayerViewport.getLeft() < -SCREEN_WIDTH * xFactor / 2) {
                standarCentreLocationX = (mLayerViewport.halfWidth - SCREEN_WIDTH * xFactor / 2);
                CentreXLocation = (mLayerViewport.x - standarCentreLocationX) / (Acceleration + 1) + standarCentreLocationX;

            } else {
                if (mLayerViewport.getRight() > SCREEN_WIDTH * xFactor / 2) {
                    standarCentreLocationX = -mLayerViewport.halfWidth + SCREEN_WIDTH * xFactor / 2;
                    CentreXLocation = (mLayerViewport.x - standarCentreLocationX) / (Acceleration + 1) + standarCentreLocationX;
                }
            }
        }
        //mHelpScreenLogo.setPosition(CentreXLocation, CentreYLocation + SCREEN_HEIGHT / 2 - SCREEN_HEIGHT / 10);
        mLayerViewport.set(CentreXLocation, CentreYLocation, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);


        //Update the position of textual game rules
        firstLineTextPositionX = (SCREEN_WIDTH * xFactor / 2) - mLayerViewport.getRight() + textSize * 3;
        firstLineTextPositionY = mLayerViewport.getTop() - SCREEN_HEIGHT * yFactor / 2 + SCREEN_HEIGHT / 5 + textSize;


    }


    /**
     * Draw the help screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Draw the textual game rules
        for (int i = 0; i < gameRules.size(); i++) {
            float currentLineTextPositionX, currentLineTextPositionY;
            currentLineTextPositionX = firstLineTextPositionX;
            currentLineTextPositionY = firstLineTextPositionY + i * (textSize + textLineSpacing);
            graphics2D.drawText(gameRules.get(i), currentLineTextPositionX, currentLineTextPositionY, mTextFormat);
        }

    }


}
