package uk.ac.qub.eeecs.game.options;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.ui.Slider;


/**
 * Created by Jamie T on 08/12/2017.
 * Screen to display user options and allow users to edit them
 */
public class OptionsScreen extends GameScreen
{
	// Name of the game screen
	private static final String OPTIONS_SCREEN_NAME = "OptionsScreen";
	
	/**
	 * Asset ID's and paths for retrieval from asset storage
	 */
	private static final String OPTIONS_BACKGROUND_BITMAP_ID = "OptionsBackground";
	private static final String OPTIONS_BACKGROUND_BITMAP_PATH = "img/OptionsScreenBackground.jpg";
	private static final String UNCHECKED_BITMAP_ID = "Unchecked";
	private static final String UNCHECKED_BITMAP_PATH = "img/Unchecked.jpg";
	private static final String CHECKED_BITMAP_ID = "Checked";
	private static final String CHECKED_BITMAP_PATH = "img/Checked.jpg";
	private static final String SLIDER_BASE_BITMAP_ID = "SliderBase";
	private static final String SLIDER_BASE_BITMAP_PATH = "img/SliderBase.png";
	private static final String SLIDER_FILL_BITMAP_ID = "SliderFill";
	private static final String SLIDER_FILL_BITMAP_PATH = "img/SliderFill.png";
	
	/**
	 * Minimum, maximum and default volumes the music and sound effects can be set to
	 */
	private static final int MAX_VOLUME = 10;
	private static final int MIN_VOLUME = 0;
	private static final int DEFAULT_VOLUME = 7;
	
	/**
	 * Width and height of the toggle buttons
	 */
	private static final float TOGGLE_WIDTH = 128.0f;
	private static final float TOGGLE_HEIGHT = 128.0f;
	
	/**
	 * Width and height of the sliders
	 */
	private static final float SLIDER_WIDTH = 768.0f;
	private static final float SLIDER_HEIGHT = 128.0f;
	
	// Vertical separation between each option between each option
	private static final float OPTION_SEPARATION = 192.0f;
	
	// The entire option width, space to leave to fit in text and the UI elements
	private static final float OPTION_WIDTH = 768.0f;
	
	// Calling activity
	private Activity mActivity;
	
	// The background object
	private GameObject mOptionsBackground;
	
	// The options manager for setting and retrieving user options
	private OptionsManager mManager;
	
	// Each option's leftmost horizontal position
	private float mOptionXPos;
	
	/**
	 * Toggle buttons for user options
	 */
	private ToggleButton mFpsToggle;
	private ToggleButton mEffectsToggle;
	
	/**
	 * Sliders for user options
	 */
	private Slider mMusicSlider;
	private Slider mSoundsSlider;
	
	// Painter for text style
	private Paint mTextPainter;
	
	/**
	 * Currently loaded user options
	 */
	private boolean mFpsCurrentVal = false;
	private boolean mEffectsCurrentVal = false;
	private int mMusicCurrentVal = DEFAULT_VOLUME;
	private int mSoundCurrentVal = DEFAULT_VOLUME;
	
	/**
	 * Define viewports for this layer and the associated screen projection
	 */
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;
	
	/**
	 * Create the Options screen
	 *
	 * @param game Game to which this screen belongs
	 */
	public OptionsScreen(Game game)
	{
		super(OPTIONS_SCREEN_NAME, game);
		
		// Set up viewports
		// Create the screen viewport
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());

		// Create the layer viewport, taking into account the orientation
		// and aspect ratio of the screen.
		if (mScreenViewport.width > mScreenViewport.height)
			mLayerViewport = new LayerViewport(240.0f, 240.0f
					* mScreenViewport.height / mScreenViewport.width, 240,
					240.0f * mScreenViewport.height / mScreenViewport.width);
		else
			mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
					/ mScreenViewport.width, 240.0f, 240.0f
					* mScreenViewport.height / mScreenViewport.width, 240);
		
		// Get calling activity
		mActivity = game.getActivity();
		
		// Create OptionsManager
		mManager = new OptionsManager(game.getContext());
		
		// Get any current user options values
		mFpsCurrentVal = mManager.getBoolOption(OptionsManager.FPS_COUNTER);
		mEffectsCurrentVal = mManager.getBoolOption(OptionsManager.VISUAL_EFFECTS);
		mMusicCurrentVal = mManager.getIntOption(OptionsManager.MUSIC_VOLUME, DEFAULT_VOLUME);
		mSoundCurrentVal = mManager.getIntOption(OptionsManager.SOUND_VOLUME, DEFAULT_VOLUME);
		
		// Load in the assets used by this layer
		AssetStore assetManager = mGame.getAssetManager();
		
		// Load in bitmaps
		assetManager.loadAndAddBitmap(OPTIONS_BACKGROUND_BITMAP_ID, OPTIONS_BACKGROUND_BITMAP_PATH);
		assetManager.loadAndAddBitmap(CHECKED_BITMAP_ID, CHECKED_BITMAP_PATH);
		assetManager.loadAndAddBitmap(UNCHECKED_BITMAP_ID, UNCHECKED_BITMAP_PATH);
		assetManager.loadAndAddBitmap(SLIDER_BASE_BITMAP_ID, SLIDER_BASE_BITMAP_PATH);
		assetManager.loadAndAddBitmap(SLIDER_FILL_BITMAP_ID, SLIDER_FILL_BITMAP_PATH);
		
		// Set up background
		mOptionsBackground = new GameObject(
				mLayerViewport.getWidth() / 2.0f,
				mLayerViewport.getHeight() / 2.0f,
				mLayerViewport.getWidth(),
				mLayerViewport.getHeight(),
				assetManager.getBitmap(OPTIONS_BACKGROUND_BITMAP_ID),
				this);
		
		// Set up text painter with styles
		mTextPainter = new Paint();
		mTextPainter.setTextSize(60);
		mTextPainter.setColor(Color.BLACK);
		
		// Calculate leftmost horizontal position of options
		mOptionXPos = game.getScreenWidth() / 2.0f - OPTION_WIDTH / 2.0f;
		
		// Set up FPS toggle button
		mFpsToggle = new ToggleButton(
				mOptionXPos,
				OPTION_SEPARATION,
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				CHECKED_BITMAP_ID,
				UNCHECKED_BITMAP_ID,
				this
		);
		
		// Update toggle button with loaded value
		mFpsToggle.setToggled(mFpsCurrentVal);
		
		// Set up visual effects toggle button
		mEffectsToggle = new ToggleButton(
				mOptionXPos,
				(OPTION_SEPARATION *2),
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				CHECKED_BITMAP_ID,
				UNCHECKED_BITMAP_ID,
				this
		);
		
		// Update toggle button with loaded value
		mEffectsToggle.setToggled(mEffectsCurrentVal);
		
		// Set up text painter with styles
		Paint sliderPainter = new Paint();
		sliderPainter.setTextSize(60);
		sliderPainter.setColor(Color.BLACK);
		sliderPainter.setTextAlign(Paint.Align.CENTER);
		
		// Set up music slider, as part of constructor supply loaded value
		mMusicSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				mMusicCurrentVal,
				sliderPainter,
				game.getScreenWidth()/2.0f,
				(OPTION_SEPARATION *3),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				SLIDER_BASE_BITMAP_ID,
				SLIDER_FILL_BITMAP_ID,
				this,
				false);
		
		// Set up sound slider, as part of constructor supply loaded value
		mSoundsSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				mSoundCurrentVal,
				sliderPainter,
				game.getScreenWidth()/2.0f,
				(OPTION_SEPARATION *4),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				SLIDER_BASE_BITMAP_ID,
				SLIDER_FILL_BITMAP_ID,
				this,
				false);
		
	}
	
	/**
	 * Update the options screen
	 *
	 * @param elapsedTime Elapsed time information
	 */
	@Override
	public void update(ElapsedTime elapsedTime)
	{
		Input input = mGame.getInput();
		
		List<TouchEvent> touchEvents = input.getTouchEvents();
		
		if (touchEvents.size() > 0)
		{
			// Update UI elements
			mFpsToggle.update(elapsedTime);
			mEffectsToggle.update(elapsedTime);
			mMusicSlider.update(elapsedTime);
			mSoundsSlider.update(elapsedTime);
		}
		if(mFpsCurrentVal != mFpsToggle.isToggledOn())
		{
			// If currently loaded value is not the same as the UI element value
			// Update the stored user options
			mManager.setOption(OptionsManager.FPS_COUNTER, mFpsToggle.isToggledOn());
			// Update currently loaded value
			mFpsCurrentVal = mFpsToggle.isToggledOn();
		}
		if(mEffectsCurrentVal != mEffectsToggle.isToggledOn())
		{
			mManager.setOption(OptionsManager.VISUAL_EFFECTS, mEffectsToggle.isToggledOn());
			mEffectsCurrentVal = mEffectsToggle.isToggledOn();
		}
		if(mMusicCurrentVal != mMusicSlider.getVal())
		{
			mManager.setOption(OptionsManager.MUSIC_VOLUME, mMusicSlider.getVal());
			mMusicCurrentVal = mMusicSlider.getVal();
		}
		if(mSoundCurrentVal != mSoundsSlider.getVal())
		{
			mManager.setOption(OptionsManager.SOUND_VOLUME, mSoundsSlider.getVal());
			mSoundCurrentVal = mSoundsSlider.getVal();
		}
	}
	
	/**
	 * Draw the options screen
	 *
	 * @param elapsedTime Elapsed time information
	 * @param graphics2D  Graphics instance
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D)
	{
		// Draw the background first of all
		graphics2D.clear(Color.WHITE);
		// Draw background image
		mOptionsBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		// Draw UI elements
		mFpsToggle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		mEffectsToggle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		mMusicSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		mSoundsSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		// Draw options text
		graphics2D.drawText(mActivity.getString(R.string.fps_description), mOptionXPos + TOGGLE_WIDTH, OPTION_SEPARATION, mTextPainter);
		graphics2D.drawText(mActivity.getString(R.string.effects_description), mOptionXPos + TOGGLE_WIDTH, OPTION_SEPARATION *2, mTextPainter);
		graphics2D.drawText(mActivity.getString(R.string.music_description), mOptionXPos + TOGGLE_WIDTH, OPTION_SEPARATION *3, mTextPainter);
		graphics2D.drawText(mActivity.getString(R.string.sound_description), mOptionXPos + TOGGLE_WIDTH, OPTION_SEPARATION *4, mTextPainter);
		
	}
	
	public ToggleButton getFpsToggle()
	{
		return mFpsToggle;
	}
	
	public ToggleButton getEffectsToggle()
	{
		return mEffectsToggle;
	}
	
	public Slider getMusicSlider()
	{
		return mMusicSlider;
	}
	
	public Slider getSoundsSlider()
	{
		return mSoundsSlider;
	}
}
