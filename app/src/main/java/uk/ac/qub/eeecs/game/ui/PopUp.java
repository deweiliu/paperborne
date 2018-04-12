package uk.ac.qub.eeecs.game.ui;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Created by Jamie T on 10/03/2018.
 * Pop Up class for creating and displaying an in game pop up, auto sizes itself based on the
 * font size provided
 */

public class PopUp extends Sprite
{
	// Bitmap path and ID
	public static final String POPUP_BITMAP_PATH = "img/Pop_Up_Box.png";
	public static final String POPUP_BITMAP_ID = "popup";
	
	// Popup box padding around the text
	private static final float PADDING = 40f;
	
	// Message to display
	private String mMessage;
	// Time that the pop up is shown at
	private long mShowTime;
	// Number of seconds the pop up should be shown for
	private long mDuration;
	// If the pop up should be visible or not
	private boolean mVisible;
	// Text style of the text to be drawn in the pop up
	private Paint mTextStyle;
	
	/**
	 * Sets up the pop up with the desired settings
	 * @param message the message to show in the pop up
	 * @param duration the time in seconds to display the pop up for
	 * @param fontSize the size of the font to draw
	 * @param bitmap the bitmap background of the popup to draw
	 * @param gameScreen the game screen the pop up is to be drawn on
	 */
	public PopUp(String message, long duration, float fontSize, Bitmap bitmap, GameScreen gameScreen)
	{
		super(0, 0, bitmap, gameScreen);
		this.mMessage = message;
		this.mDuration = duration;
		// Set showtime to current time to avoid null pointer exceptions
		this.mShowTime = System.currentTimeMillis();
		// Set visibility to false as default
		mVisible = false;
		// Set up text styles
		mTextStyle = new Paint();
		mTextStyle.setTextSize(fontSize);
		mTextStyle.setTextAlign(Paint.Align.CENTER);
		// Create new bounds for the pop up box
		Rect textBounds = new Rect();
		// Calculate the new pop up box size based on the message and font size supplied
		mTextStyle.getTextBounds(mMessage, 0, mMessage.length(), textBounds);
		
		// Update the bounds with the new width and height
		mBound.halfWidth = textBounds.width() / 2.0f + (PADDING / 2);
		mBound.halfHeight = textBounds.height() / 2.0f + (PADDING / 2);
		
		// Update the bounds with the new x and y positions, sitting at the top of the screen
		mBound.x = gameScreen.getGame().getScreenWidth()/2;
		mBound.y = mBound.halfHeight + PADDING;
		
		// Update the position with the new x and y positions, sitting at the top of the screen
		position.x = gameScreen.getGame().getScreenWidth()/2;
		position.y = mBound.halfHeight + PADDING;
	}
	
	/**
	 * Starts to show the pop up, sets up the timer that makes sure the pop up is only shown for
	 * the desired duration
	 */
	public void show()
	{
		// Pop up should be visible
		mVisible = true;
		// Mark the current time as the time shown
		mShowTime = System.currentTimeMillis();
	}
	
	@Override
	public void update(ElapsedTime elapsedTime)
	{
		super.update(elapsedTime);
		// Get the current time
		long currentTime = System.currentTimeMillis();
		if(mShowTime + (mDuration * 1000) < currentTime)
		{
			// If the the duration has passed since the time this pop up was shown, mark the pop
			// up as not visible
			mVisible = false;
		}
	}
	
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D)
	{
		super.draw(elapsedTime, graphics2D);
		// Draw the pop up text
		graphics2D.drawText(mMessage, (float) drawScreenRect.centerX(), drawScreenRect.bottom - (PADDING/2.0f), mTextStyle);
	}
	
	/**
	 * Returns if the pop up should be drawn or not
	 * @return true = show pop up, false = hide pop up
	 */
	public boolean isVisible()
	{
		return mVisible;
	}
}
