package uk.ac.qub.eeecs.game.performanceScreen;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;



/**
 * A screen for measuring performance.
 *
 * @version 1.0
 */
public class PerformanceScreen extends GameScreen{

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Make the buttons
     */
    private PushButton mRectanglesUp;
    private PushButton mRectanglesDown;

    /**
     * Create paint for FPS display
     */

    private Paint paintFPS;

    /**
     * Define viewports for this layer and the associated screen projection
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;

    /**
     * Number of rectangles to draw
     */
    private int numRectangles = 100;
    private int rectangleStepChange = 50;

    /**
     *  Define rectangles
     */
    private List<Sprite> mRectangles;

    /**
     * rectangle max attributes
     */

    private final float maxRectWidth = 100f;
    private final float maxRectHeight = 100f;

    /**
     * Width and height of the level
     */
    private final float LEVEL_WIDTH = 1000.0f;
    private final float LEVEL_HEIGHT = 1000.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Make a simple performance screen
     *
     * @param game Game to which this screen belongs
     */
    public PerformanceScreen(Game game) {
        super("PerformanceScreen", game);

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        // Create the layer viewport, taking into account the orientation
        // and aspect ratio of the screen.
        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240.0f,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.width
                    / mScreenViewport.height, 240.0f, 240.0f
                    * mScreenViewport.width / mScreenViewport.height, 240.0f);

        // init paint params
        paintFPS = new Paint();
        paintFPS.setTextSize(48);

        // load assets
        AssetStore assetManager = mGame.getAssetManager();

        assetManager.loadAndAddBitmap("Rectangle", "img/Hearthstone_Card_Template.png");
        assetManager.loadAndAddBitmap("PlusButton", "img/plus.png");
        assetManager.loadAndAddBitmap("MinusButton", "img/minus.png");

        Bitmap rect = assetManager.getBitmap("Rectangle");

        // create rectangles
        Random random = new Random();
        mRectangles = new ArrayList<Sprite>(numRectangles);
        for (int i = 0; i < numRectangles; ++i) {
            mRectangles.add(new Sprite(random.nextFloat() * game.getScreenWidth()/4, random.nextFloat() * game.getScreenHeight()/4,
                    random.nextFloat() * maxRectWidth, random.nextFloat() * maxRectHeight, rect, this));
        }

        // make buttons
        int spacingX = game.getScreenWidth() / 10;
        int spacingY = game.getScreenHeight() / 6;
        mRectanglesUp = new PushButton(
                spacingX * 0.5f, spacingY * 5.5f, spacingX, spacingY, "PlusButton", this);
        mRectanglesDown = new PushButton(
                spacingX * 1.5f, spacingY * 5.5f, spacingX, spacingY, "MinusButton", this);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the performance demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);

            mRectanglesUp.update(elapsedTime);
            mRectanglesDown.update(elapsedTime);

            if(mRectanglesUp.isPushTriggered())
                numRectangles += rectangleStepChange;
            else if (mRectanglesDown.isPushTriggered())
                numRectangles -=rectangleStepChange;
        }

        // Ensure the viewport cannot leave the confines of the world
        if (mLayerViewport.getLeft() < 0)
            mLayerViewport.x -= mLayerViewport.getLeft();
        else if (mLayerViewport.getRight() > LEVEL_WIDTH)
            mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

        if (mLayerViewport.getBottom() < 0)
            mLayerViewport.y -= mLayerViewport.getBottom();
        else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
            mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);

        mRectangles.clear();
        Random random = new Random();
        Bitmap rect = mGame.getAssetManager().getBitmap("Rectangle");
        for (int i = 0; i < numRectangles; ++i) {
            mRectangles.add(new Sprite(random.nextFloat() * mGame.getScreenWidth()/4, random.nextFloat() * mGame.getScreenHeight()/4,
                    random.nextFloat() * maxRectWidth, random.nextFloat() * maxRectHeight, rect, this));
        }

        for (Sprite rectangle : mRectangles)
            rectangle.update(elapsedTime);

    }

    /**
     * Draw the performance demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        for (Sprite rect : mRectangles)
            rect.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        String fps = "FPS: " + this.getGame().getAverageFramesPerSecond();
        graphics2D.drawText(fps, 10, 50, paintFPS);
        mRectanglesUp.draw(elapsedTime, graphics2D, null, null);
        mRectanglesDown.draw(elapsedTime, graphics2D, null, null);

    }

    public int getNumRectangles() {
        return numRectangles;
    }

    public PushButton getmRectanglesUp() {
        return mRectanglesUp;
    }

    public PushButton getmRectanglesDown() {
        return mRectanglesDown;
    }

    public LayerViewport getmLayerViewport() {
        return mLayerViewport;
    }

    public ScreenViewport getmScreenViewport() {
        return mScreenViewport;
    }
}
