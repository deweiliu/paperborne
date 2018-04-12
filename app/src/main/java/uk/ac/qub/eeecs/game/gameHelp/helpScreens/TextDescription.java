package uk.ac.qub.eeecs.game.gameHelp.helpScreens;

import java.util.Vector;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.gameHelp.GameHelpController;

/**
 * Created by 40216004 Dewei Liu on 05/12/2017.
 */


public class TextDescription extends HelpScreenSuperClass {


    //Define the backIconHeight and backIconWidth of screen
    private float SCREEN_WIDTH;
    private float SCREEN_HEIGHT;

    //the centre position of texts
    private float centerX;
    private float centerY;
    private final float Y_FACTOR = 2.5f;
    private final float X_FACTOR = 1.0f;

    // Define textual game rules
    private Vector<String> gameRules;

    private final float textSize = 100;
    private float textLineSpacing;

    //the position of the textual game rules
    private float X_firstLine;
    private float Y_firstLine;

    private float lastTouchX;
    private float lastTouchY;

    // Decide if the touch event is scrolling
    private boolean scrolling = false;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Help screen
     *
     * @param game Game to which this screen belongs
     */
    public TextDescription(Game game, GameHelpController controller) {
        super("TextDescription", game, controller);

        //Get screen backIconWidth and backIconHeight
        SCREEN_HEIGHT = game.getScreenHeight();
        SCREEN_WIDTH = game.getScreenWidth();

        centerX = (X_FACTOR - 1) * SCREEN_WIDTH / 2;
        centerY = (Y_FACTOR - 1) * SCREEN_HEIGHT / 2;

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, (int) (SCREEN_HEIGHT / 5), (int) SCREEN_WIDTH, (int) SCREEN_HEIGHT);
        mLayerViewport = new LayerViewport(centerX, centerY, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

        mPaint.setTextSize(textSize);
        textLineSpacing = textSize / 5;

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

        /*************************************************************************************/
        //Belows are for calculate centerX & centerY

        // If screen is being touched
        if (input.existsTouch(0)) {
            float touchX = input.getTouchX(0);
            float touchY = input.getTouchY(0);

            //If this touch is the fist touch point (not drag)
            if (!scrolling) {
                scrolling = true;
                lastTouchX = touchX;
                lastTouchY = touchY;
            } else {

                /*************************************************************************************/
                //If want to move for horizontal direction, just uncomment the below statement
                //centerX += (lastTouchX - touchX);
                /*************************************************************************************/

                //Do move for vertical direction
                centerY += (touchY - lastTouchY);

                //Record the touch event
                lastTouchX = touchX;
                lastTouchY = touchY;
            }
        }

        //else there's no touch, so correct the position (the drag may be out of bound)
        else {
            scrolling = false;

            //Acceleration must greater than 0
            final float Acceleration = 0.5f;

            centerX = mLayerViewport.x;
            centerY = mLayerViewport.y;

            /*************************************************************************************/
            //For Y
            float standardCenterPositionY;
            if (mLayerViewport.getTop() > SCREEN_HEIGHT * Y_FACTOR / 2) {
                standardCenterPositionY = (SCREEN_HEIGHT * Y_FACTOR / 2 - mLayerViewport.halfHeight);
                centerY = (mLayerViewport.y - standardCenterPositionY) / (Acceleration + 1) + standardCenterPositionY;
            } else {
                if (mLayerViewport.getBottom() < -SCREEN_HEIGHT * Y_FACTOR / 2) {
                    standardCenterPositionY = mLayerViewport.halfHeight - SCREEN_HEIGHT * Y_FACTOR / 2;
                    centerY = (mLayerViewport.y - standardCenterPositionY) / (Acceleration + 1) + standardCenterPositionY;
                }
            }

            /*************************************************************************************/
            //for X
            float standardCenterPositionX;
            if (mLayerViewport.getLeft() < -SCREEN_WIDTH * X_FACTOR / 2) {
                standardCenterPositionX = (mLayerViewport.halfWidth - SCREEN_WIDTH * X_FACTOR / 2);
                centerX = (mLayerViewport.x - standardCenterPositionX) / (Acceleration + 1) + standardCenterPositionX;
            } else {
                if (mLayerViewport.getRight() > SCREEN_WIDTH * X_FACTOR / 2) {
                    standardCenterPositionX = -mLayerViewport.halfWidth + SCREEN_WIDTH * X_FACTOR / 2;
                    centerX = (mLayerViewport.x - standardCenterPositionX) / (Acceleration + 1) + standardCenterPositionX;
                }
            }
            /*************************************************************************************/
        }

        //Above are for calculate centerX & centerY
        /*************************************************************************************/

        //Save centerX & centerY to the layer viewport
        mLayerViewport.set(centerX, centerY, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

        //Calculate the texts position
        X_firstLine = (SCREEN_WIDTH * X_FACTOR / 2) - mLayerViewport.getRight() + textSize * 3;
        Y_firstLine = mLayerViewport.getTop() - SCREEN_HEIGHT * Y_FACTOR / 2 + SCREEN_HEIGHT / 5 + textSize;

        //Note: the reason why I don't calculate the X_firstLine & Y_firstLine directly,
        //instead calculate centerX & centerY fist is that
        //I original had a scrollable picture, and I can use centerX and centerY to set up the centre position of that picture.
        //Unfortunately, it has been deleted, because I want to extend the game help with other screens
        //and I don't think I need it any more.
    }

    /**
     * Draw the help screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    protected void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (int i = 0; i < gameRules.size(); i++) {
            float x = X_firstLine;
            float y = Y_firstLine + i * (textSize + textLineSpacing);
            graphics2D.drawText(gameRules.get(i), x, y, mPaint);
        }
    }
}
