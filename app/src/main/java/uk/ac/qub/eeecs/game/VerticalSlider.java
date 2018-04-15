package uk.ac.qub.eeecs.game;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.Button;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;


public class VerticalSlider extends Button {
    /**
     * Minimum, maximum and current slider values
     */
    private int mMax;
    private int mMin;
    private int mVal;


    private boolean mProcessInLayerSpace;

    // Slider text style
    private Paint mTextStyle;

    // Graphical asset used to represent the slider
    private Bitmap mSliderBitmap;

    // Graphical asset used to represent the slider fill
    private Bitmap mSliderFillBitmap;

    // Sound played whenever the slider is pressed
    private Sound mTriggerSound;

    /**
     * Create new slider with sound
     *
     * @param x             Centre y location of the slider
     * @param y             Centre x location of the slider
     * @param width         Width of the slider
     * @param height        Height of the slider
     * @param defaultBitmap Bitmap used to represent this control
     * @param triggerSound  Sound to play once per touch
     *                      space (screen by default)
     * @param gameScreen    Gamescreen to which this control belongs
     */
    public VerticalSlider(int min, int max, int val, Paint textStyle, float x, float y, float width, float height,
                          String defaultBitmap, String sliderFillBitmap, String triggerSound, GameScreen gameScreen,
                          Boolean processInLayerSpace) {
        super(x, y, width, height, defaultBitmap, processInLayerSpace, gameScreen);
        mProcessInLayerSpace = processInLayerSpace;
        // Load in slider fill bitmap
        this.mSliderFillBitmap = gameScreen.getGame().getAssetManager().getBitmap(sliderFillBitmap);
        this.mTextStyle = textStyle;
        this.mMin = min;
        this.mMax = max;
        this.mVal = val;
        // Retrieve the assets used by this slider
        AssetStore assetStore = gameScreen.getGame().getAssetManager();
        mSliderBitmap = assetStore.getBitmap(defaultBitmap);
        mTriggerSound = (triggerSound == null)
                ? null : assetStore.getSound(triggerSound);
    }

    /**
     * Create a new slider without sound
     *
     * @param x          Centre y location of the slider
     * @param y          Centre x location of the slider
     * @param width      Width of the slider
     * @param height     Height of the slider
     * @param bitmap     Bitmap used to represent this control
     * @param gameScreen Gamescreen to which this control belongs
     */
    public VerticalSlider(int min, int max, int val, Paint textStyle, float x, float y, float width, float height,
                          String bitmap, String sliderFillBitmap, GameScreen gameScreen, Boolean processInLayerSpace) {
        this(min, max, val, textStyle, x, y, width, height, bitmap, sliderFillBitmap, null, gameScreen, processInLayerSpace);
    }

    @Override
    protected void updateTriggerActions(TouchEvent touchEvent, Vector2 touchLocation) {
    }


    @Override
    protected void updateTouchActions(Vector2 touchLocation) {

    }

    /**
     * Undertake default actions for the untouched button, reverting
     * to the default bitmap and state.
     */
    @Override
    protected void updateDefaultActions() {
        mBitmap = mSliderBitmap;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        if (mProcessInLayerSpace) {
            // If in layer space, then determine an appropriate screen space bound
            if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {
                Rect fillRect = new Rect(
                        drawScreenRect.left,
                        drawScreenRect.bottom - (int) ((float) drawScreenRect.height() * ((float) mVal / (float) mMax)),
                        drawScreenRect.right,
                        drawScreenRect.bottom);

                // Draw slider base and fill bitmaps
                graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
                graphics2D.drawBitmap(mSliderFillBitmap, fillRect, fillRect, null);
                // Draw the slider axis text

                graphics2D.drawText(String.valueOf(mMin), (float) drawScreenRect.right, drawScreenRect.bottom, mTextStyle);
                graphics2D.drawText(String.valueOf(mMax), (float) drawScreenRect.right, drawScreenRect.top, mTextStyle);
                graphics2D.drawText(String.valueOf(mVal), drawScreenRect.exactCenterX(), drawScreenRect.centerY(), mTextStyle);
            }
        } else {

            Rect fillRect = new Rect(
                    drawScreenRect.left,
                    drawScreenRect.bottom - (int) ((float) drawScreenRect.height() * ((float) mVal / (float) mMax)),
                    drawScreenRect.right,
                    drawScreenRect.bottom);

            draw(elapsedTime, graphics2D);
            graphics2D.drawBitmap(mSliderFillBitmap, fillRect, fillRect, null);
            // Draw the slider axis text
            graphics2D.drawText(String.valueOf(mMin), (float) drawScreenRect.right + 20f, drawScreenRect.exactCenterY() + 230f, mTextStyle);
            graphics2D.drawText(String.valueOf(mMax), (float) drawScreenRect.right + 20f, drawScreenRect.exactCenterY() - 230f, mTextStyle);
            graphics2D.drawText(String.valueOf(mVal), drawScreenRect.right + 20f, drawScreenRect.exactCenterY(), mTextStyle);
        }
    }

    /**
     * Get the current slider value
     *
     * @return the slider value
     */
    public int getVal() {
        return mVal;
    }

    /**
     * Set the slider value
     *
     * @param val the value to set the slider to
     */
    public void setVal(int val) {
        if (val > mMax) {
            // If the value is greater than the maximum, set the value to the maximum
            this.mVal = mMax;
        } else if (val < mMin) {
            // If the value is less than the minimum, set the value to the minimum
            this.mVal = mMin;
        } else {
            // If the value is within the min-max range, set it to the value provided
            this.mVal = val;
        }
    }
}
