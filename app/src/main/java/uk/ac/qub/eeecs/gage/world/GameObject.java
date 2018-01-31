package uk.ac.qub.eeecs.gage.world;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;

/**
 * Game object superclass.
 *
 * @version 1.0
 */
public class GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Game screen to which this game object belongs
     */
    protected GameScreen mGameScreen;

    /**
     * Bitmap used to render this game object
     */
    protected Bitmap mBitmap;

    /**
     * Position of this game object
     */
    public Vector2 position = new Vector2();

    /**
     * Bounding box for this game object
     */
    protected BoundingBox mBound = new BoundingBox();

    public static Vector2 screenDimensions = new Vector2();

    /**
     * Reusable Rect's used to draw this game object
     */
    protected Rect drawSourceRect = new Rect();
    protected Rect drawScreenRect = new Rect();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new game object
     *
     * @param gameScreen Gamescreen to which this object belongs
     */
    public GameObject(GameScreen gameScreen) {
        mGameScreen = gameScreen;
    }

    /**
     * Create a new game object
     *
     * @param x          x location of the object
     * @param y          y location of the object
     * @param bitmap     Bitmap used to represent this object
     * @param gameScreen Gamescreen to which this object belongs
     */
    public GameObject(float x, float y, Bitmap bitmap, GameScreen gameScreen) {
        mGameScreen = gameScreen;

        position.x = x;
        position.y = y;
        mBitmap = bitmap;

        mBound.x = x;
        mBound.y = y;
        mBound.halfWidth = bitmap.getWidth() / 2.0f;
        mBound.halfHeight = bitmap.getHeight() / 2.0f;


    }

    /**
     * Create a new game object
     *
     * @param x          x location of the object
     * @param y          y location of the object
     * @param width      width of the object
     * @param height     height of the object
     * @param bitmap     Bitmap used to represent this object
     * @param gameScreen Gamescreen to which this object belongs
     */
    public GameObject(float x, float y, float width, float height,
                      Bitmap bitmap, GameScreen gameScreen) {
        mGameScreen = gameScreen;

        position.x = x;
        position.y = y;
        mBitmap = bitmap;

        mBound.x = x;
        mBound.y = y;
        mBound.halfWidth = width / 2.0f;
        mBound.halfHeight = height / 2.0f;
        screenDimensions.x = gameScreen.getGame().getScreenWidth();
        screenDimensions.y = gameScreen.getGame().getScreenHeight();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Return the bounding box for this game object.
     * <p>
     * Note: The values within the bounding box should not be modified.
     *
     * @return
     */
    public BoundingBox getBound() {
        // Ensure the bound is centred on the sprite's current location
        mBound.x = position.x;
        mBound.y = position.y;
        return mBound;
    }

    /**
     * Return the bitmap used for this game object.
     *
     * @return Bitmap associated with this sprite.
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Set the position of the game object
     *
     * @param x x-location of the game object
     * @param y y-location of the game object
     */
    public void setPosition(float x, float y) {

        mBound.x = position.x = x;
        mBound.y = position.y = y;
    }

    /**
     * Update the game object
     *
     * @param elapsedTime Elapsed time information
     */
    public void update(ElapsedTime elapsedTime) {

    }

    /**
     * Draw the game object
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        drawScreenRect.set((int) (position.x - mBound.halfWidth),
                (int) (position.y - mBound.halfHeight),
                (int) (position.x + mBound.halfWidth),
                (int) (position.y + mBound.halfHeight));
        graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);
    }

    /**
     * Draw the game object
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport,
                screenViewport, drawSourceRect, drawScreenRect)) {
            graphics2D
                    .drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
        }
    }

    /**
     * Draw the game object
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     * @param paint          Paint instance
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport,
                     Paint paint) {
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport,
                screenViewport, drawSourceRect, drawScreenRect)) {
            graphics2D
                    .drawBitmap(mBitmap, drawSourceRect, drawScreenRect, paint);
        }
    }
}