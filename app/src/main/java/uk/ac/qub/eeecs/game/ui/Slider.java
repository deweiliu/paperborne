package uk.ac.qub.eeecs.game.ui;

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

/**
 * Created by Jamie T on 08/12/2017.
 * Slider UI element for changing values in a range
 */

public class Slider extends Button
{
	/**
	 * Minimum, maximum and current slider values
	 */
	private int mMax;
	private int mMin;
	private int mVal;
	
	// Separation between the size labels and the slider bar
	final private int TEXT_SEPARATION = 48;
	
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
	public Slider(int min, int max, int val, Paint textStyle, float x, float y, float width, float height,
				  String defaultBitmap, String sliderFillBitmap, String triggerSound, GameScreen gameScreen,
				  Boolean processInLayerSpace)
	{
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
	public Slider(int min, int max, int val, Paint textStyle, float x, float y, float width, float height,
				  String bitmap, String sliderFillBitmap, GameScreen gameScreen, Boolean processInLayerSpace)
	{
		this(min, max, val, textStyle, x, y, width, height, bitmap, sliderFillBitmap, null, gameScreen, processInLayerSpace);
	}
	
	@Override
	protected void updateTriggerActions(TouchEvent touchEvent, Vector2 touchLocation)
	{
		// Attempt to increment the slider
		increment();
	}
	
	/**
	 * Undertake touch actions for the slider, updating the current value
	 *
	 * @param touchLocation Touch location at which the trigger occurred
	 */
	@Override
	protected void updateTouchActions(Vector2 touchLocation)
	{
		// Attempt to increment the slider
		increment();
	}
	
	/**
	 * Increments the slider's value by one, if this puts it over the max it wraps around to
	 * the min value
	 */
	private void increment()
	{
		// If there is a sound provided play it
		if (mTriggerSound != null)
		{
			mTriggerSound.play();
		}
		// Increment the value
		mVal++;
		if (mVal > mMax)
		{
			// If the value is greater than the maximum, set the value to the minimum
			mVal = mMin;
		}
	}
	
	/**
	 * Undertake default actions for the untouched button, reverting
	 * to the default bitmap and state.
	 */
	@Override
	protected void updateDefaultActions()
	{
		mBitmap = mSliderBitmap;
	}
	
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport)
	{
		if (mProcessInLayerSpace)
		{
			// If in layer space, then determine an appropriate screen space bound
			if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect))
			{
				// Create a rect for the fill bitmap
				// the right position is calculated by:
				// SliderLeft + (SliderWidth * (SliderValue / SliderMax))
				Rect fillRect = new Rect(
						drawScreenRect.left,
						drawScreenRect.top,
						(drawScreenRect.left + (int) ((float) drawScreenRect.width() * ((float) mVal / (float) mMax))),
						drawScreenRect.bottom);
				// Draw slider base and fill bitmaps
				graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
				graphics2D.drawBitmap(mSliderFillBitmap, fillRect, fillRect, null);
				// Draw the slider axis text
				graphics2D.drawText(String.valueOf(mMin), (float) drawScreenRect.left, drawScreenRect.centerY(), mTextStyle);
				graphics2D.drawText(String.valueOf(mMax), (float) drawScreenRect.right, drawScreenRect.centerY(), mTextStyle);
				graphics2D.drawText(String.valueOf(mVal), drawScreenRect.exactCenterX(), drawScreenRect.centerY(), mTextStyle);
			}
		}
		else
		{
			// Create a rect for the fill bitmap
			// the right position is calculated by:
			// SliderLeft + (SliderWidth * (SliderValue / SliderMax))
			Rect fillRect = new Rect(
					drawScreenRect.left,
					drawScreenRect.top,
					(drawScreenRect.left + (int) ((float) drawScreenRect.width() * ((float) mVal / (float) mMax))),
					drawScreenRect.bottom);
			// Calculate the text Y position from the slider center Y position, slider height and text separation value
			float textY = drawScreenRect.exactCenterY() + (drawScreenRect.height() / 2) + TEXT_SEPARATION;
			// Draw slider base and fill bitmaps
			draw(elapsedTime, graphics2D);
			graphics2D.drawBitmap(mSliderFillBitmap, fillRect, fillRect, null);
			// Draw the slider axis text
			graphics2D.drawText(String.valueOf(mMin), (float) drawScreenRect.left, textY, mTextStyle);
			graphics2D.drawText(String.valueOf(mMax), (float) drawScreenRect.right, textY, mTextStyle);
			graphics2D.drawText(String.valueOf(mVal), drawScreenRect.exactCenterX(), textY, mTextStyle);
		}
	}
	
	/**
	 * Get the current slider value
	 *
	 * @return the slider value
	 */
	public int getVal()
	{
		return mVal;
	}
	
	/**
	 * Set the slider value
	 *
	 * @param val the value to set the slider to
	 */
	public void setVal(int val)
	{
		if (val > mMax)
		{
			// If the value is greater than the maximum, set the value to the maximum
			this.mVal = mMax;
		}
		else if (val < mMin)
		{
			// If the value is less than the minimum, set the value to the minimum
			this.mVal = mMin;
		}
		else
		{
			// If the value is within the min-max range, set it to the value provided
			this.mVal = val;
		}
	}
}
