package uk.ac.qub.eeecs.gage.ui;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Push button class. Providing controllable trigger events on button push
 * or button release.
 *
 * @version 1.0
 */
public class PushButton extends Button {

    // /////////////////////////////////////////////////////////////////////////
    // Properties: Button State and Trigger
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the available button states
     */
    protected enum ButtonState {
        DEFAULT, PUSHED
    }

    /**
     * Store the current button state
     */
    protected ButtonState mButtonState = ButtonState.DEFAULT;

    /**
     * Determine if this button is triggered on a touch press or a touch release
     */
    protected boolean mTriggerOnRelease = true;

    /**
     * Private variable used to store an unconsumed trigger event
     */
    private boolean mPushTriggered;

    // /////////////////////////////////////////////////////////////////////////
    // Properties: Button Appearance and Sound
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Graphical asset used to represent the default button state
     */
    protected Bitmap mDefaultBitmap;

    /**
     * Graphical asset used to represent the pushed button state
     */
    protected Bitmap mPushBitmap;

    /**
     * Name of the sound asset to be played whenever the button is triggered
     */
    protected Sound mTriggerSound;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new push button.
     *
     * @param x                   Centre y location of the button
     * @param y                   Centre x location of the button
     * @param width               Width of the button
     * @param height              Height of the button
     * @param defaultBitmap       Bitmap used to represent this control
     * @param pushBitmap          Bitmap used to represent this control
     * @param triggerSound        Sound to play once per button trigger
     * @param processInLayerSpace Specify if the button is to be processed in layer
     *                            space (screen by default)
     * @param gameScreen          Gamescreen to which this control belongs
     */
    public PushButton(float x, float y, float width, float height,
                      String defaultBitmap, String pushBitmap, String triggerSound,
                      boolean processInLayerSpace, GameScreen gameScreen) {
        super(x, y, width, height, defaultBitmap, processInLayerSpace, gameScreen);

        // Retrieve the assets used by this button
        AssetStore assetStore = gameScreen.getGame().getAssetManager();
        mDefaultBitmap = assetStore.getBitmap(defaultBitmap);
        mPushBitmap = (pushBitmap == null)
                ? mDefaultBitmap : assetStore.getBitmap(pushBitmap);
        mTriggerSound = (triggerSound == null)
                ? null : assetStore.getSound(triggerSound);
    }

    /**
     * Create a new push button.
     *
     * @param x          Centre y location of the button
     * @param y          Centre x location of the button
     * @param width      Width of the button
     * @param height     Height of the button
     * @param bitmap     Bitmap used to represent this control
     * @param gameScreen Gamescreen to which this control belongs
     */
    public PushButton(float x, float y, float width, float height,
                      String bitmap, GameScreen gameScreen) {
        this(x, y, width, height, bitmap, null, null, false, gameScreen);
    }

    /**
     * Create a new push button.
     *
     * @param x             Centre y location of the button
     * @param y             Centre x location of the button
     * @param width         Width of the button
     * @param height        Height of the button
     * @param defaultBitmap Bitmap used to represent this control
     * @param pushBitmap    Bitmap used to represent this control
     * @param gameScreen    Gamescreen to which this control belongs
     */
    public PushButton(float x, float y, float width, float height,
                      String defaultBitmap, String pushBitmap, GameScreen gameScreen) {
        this(x, y, width, height, defaultBitmap, pushBitmap, null, false, gameScreen);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Specific if the button trigger should occur on a push down or push up
     * touch event.
     *
     * @param triggerOnRelease True if the trigger should occur on button touch release
     */
    public void triggerOnRelease(boolean triggerOnRelease) {
        mTriggerOnRelease = triggerOnRelease;
    }

    /**
     * Check for and undertake trigger actions for the button.
     * <p>
     * If triggered play a sound and record that a button click has been recorded
     *
     * @param touchEvent    Touch event that gave rise to the trigger
     * @param touchLocation Touch location at which the trigger occurred
     */
    @Override
    protected void updateTriggerActions(TouchEvent touchEvent, Vector2 touchLocation) {
        // Trigger if the appropriate touch up or touch down has occurred
        if ((!mTriggerOnRelease && touchEvent.type == TouchEvent.TOUCH_DOWN) ||
                (mTriggerOnRelease && touchEvent.type == TouchEvent.TOUCH_UP)) {
            // Record the trigger
            mPushTriggered = true;

            // Play a trigger sound if needed
            if (mTriggerSound != null)
                mTriggerSound.play();
        }
    }

    /**
     * Undertake touch actions for the button, updating the state
     * and bitmap.
     *
     * @param touchLocation Touch location at which the trigger occurred
     */
    @Override
    protected void updateTouchActions(Vector2 touchLocation) {
        mBitmap = mPushBitmap;
        mButtonState = ButtonState.PUSHED;
    }

    /**
     * Undertake default actions for the untouched button, reverting
     * to the default bitmap and state.
     */
    @Override
    protected void updateDefaultActions() {
        mBitmap = mDefaultBitmap;
        mButtonState = ButtonState.DEFAULT;
    }

    /**
     * Determine if there is an unprocessed trigger event. Once returned
     * the trigger event will be consumed.
     *
     * @return True if there is an unconsumed trigger event for
     * this button, otherwise False.
     */
    public boolean isPushTriggered() {
        if (mPushTriggered) {
            mPushTriggered = false;
            return true;
        }
        return false;
    }

    /**
     * Determine if the button is currently pushed.
     *
     * @return True if the button is pushed, otherwise False.
     */
    public boolean isPushed() {
        return mButtonState == ButtonState.PUSHED;
    }
}
