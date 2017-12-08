package uk.ac.qub.eeecs.game.options;

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
 * Created by Jamie on 08/12/2017.
 */

public class Slider extends Button
{
	private int max;
	private int min;
	private int val;
	
	final private int TEXT_SEPERATION = 48;
	
	private boolean mProcessInLayerSpace = false;
	
	private Paint textStyle;
	
	/**
	 * Graphical asset used to represent the slider
	 */
	protected Bitmap sliderBitmap;
	
	protected Bitmap sliderFillBitmap;
	
	
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
	 * @param triggerSound        Sound to play once per button trigger
	 *                            space (screen by default)
	 * @param gameScreen          Gamescreen to which this control belongs
	 */
	public Slider(int min, int max, int val, Paint textStyle, float x, float y, float width, float height, String defaultBitmap, String sliderFillBitmap, String triggerSound, GameScreen gameScreen) {
		super(x, y, width, height, defaultBitmap, false, gameScreen);
		this.sliderFillBitmap = gameScreen.getGame().getAssetManager().getBitmap(sliderFillBitmap);
		this.textStyle = textStyle;
		this.min = min;
		this.max = max;
		this.val = val;
		// Retrieve the assets used by this button
		AssetStore assetStore = gameScreen.getGame().getAssetManager();
		sliderBitmap = assetStore.getBitmap(defaultBitmap);
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
	public Slider(int min, int max, int val, Paint textStyle, float x, float y, float width, float height, String bitmap, String sliderFillBitmap, GameScreen gameScreen) {
		this(min, max, val, textStyle, x, y, width, height, bitmap, sliderFillBitmap, null, gameScreen);
	}
	
	// /////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void updateTriggerActions(TouchEvent touchEvent, Vector2 touchLocation) {
	}
	
	/**
	 * Undertake touch actions for the button, updating the state
	 * and bitmap.
	 *
	 * @param touchLocation Touch location at which the trigger occurred
	 */
	@Override
	protected void updateTouchActions(Vector2 touchLocation) {
		val++;
		if(val > max)
		{
			val = min;
		}
	}
	
	/**
	 * Undertake default actions for the untouched button, reverting
	 * to the default bitmap and state.
	 */
	@Override
	protected void updateDefaultActions() {
		mBitmap = sliderBitmap;
	}
	
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport)
	{
		if (mProcessInLayerSpace)
		{
			// If in layer space, then determine an appropriate screen space bound
			if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect))
			{
				Rect fillRect = new Rect(drawScreenRect.left, drawScreenRect.top, (drawScreenRect.left + (int)((float)drawScreenRect.width() * ((float)val/(float)max))), drawScreenRect.bottom);
				float textY = drawScreenRect.exactCenterY() + (drawScreenRect.height()/2) + TEXT_SEPERATION;
				graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
				graphics2D.drawBitmap(sliderFillBitmap, fillRect, fillRect, null);
				graphics2D.drawText(String.valueOf(min), (float) drawScreenRect.left, textY, textStyle);
				graphics2D.drawText(String.valueOf(max), (float) drawScreenRect.right, textY, textStyle);
				graphics2D.drawText(String.valueOf(val), drawScreenRect.exactCenterX(), textY, textStyle);
			}
		}
		else
		{
			Rect fillRect = new Rect(drawScreenRect.left, drawScreenRect.top, (drawScreenRect.left + (int)((float)drawScreenRect.width() * ((float)val/(float)max))), drawScreenRect.bottom);
			float textY = drawScreenRect.exactCenterY() + (drawScreenRect.height()/2) + TEXT_SEPERATION;
			draw(elapsedTime, graphics2D);
			graphics2D.drawBitmap(sliderFillBitmap, fillRect, fillRect, null);
			graphics2D.drawText(String.valueOf(min), (float) drawScreenRect.left, textY, textStyle);
			graphics2D.drawText(String.valueOf(max), (float) drawScreenRect.right, textY, textStyle);
			graphics2D.drawText(String.valueOf(val), drawScreenRect.exactCenterX(), textY, textStyle);
		}
	}
	
	public int getVal()
	{
		return val;
	}
	
	public void setVal(int val)
	{
		if(val > max)
		{
			this.val = max;
		}
		else if(val < min)
		{
			this.val = min;
		}
		else
		{
			this.val = val;
		}
	}
}
