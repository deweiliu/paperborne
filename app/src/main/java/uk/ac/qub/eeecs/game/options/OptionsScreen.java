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
	 * Default toggle values
	 */
	private static final boolean DEFAULT_EFFECTS_TOGGLE = true;
	private static final boolean DEFAULT_FPS_TOGGLE = false;
	
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
	private Activity activity;
	
	// The background object
	private GameObject optionsBackground;
	
	// The options manager for setting and retrieving user options
	private OptionsManager optionsManager;
	
	// Each option's leftmost horizontal position
	private float optionXPos;
	
	/**
	 * Toggle buttons for user options
	 */
	private ToggleButton fpsToggle;
	private ToggleButton effectsToggle;
	
	/**
	 * Sliders for user options
	 */
	private Slider musicSlider;
	private Slider soundsSlider;
	
	// Painter for text style
	private Paint textPainter;
	
	/**
	 * Currently loaded user options
	 */
	private boolean fpsCurrentVal = DEFAULT_FPS_TOGGLE;
	private boolean effectsCurrentVal = DEFAULT_EFFECTS_TOGGLE;
	private int musicCurrentVal = DEFAULT_VOLUME;
	private int soundCurrentVal = DEFAULT_VOLUME;
	
	/**
	 * Define viewports for this layer and the associated screen projection
	 */
	private ScreenViewport screenViewport;
	private LayerViewport layerViewport;
	
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
		screenViewport = new ScreenViewport(
				0,
				0,
				game.getScreenWidth(),
				game.getScreenHeight()
		);
		
		// Create the layer viewport, taking into account the orientation
		// and aspect ratio of the screen.
		if (screenViewport.width > screenViewport.height)
		{
			layerViewport = new LayerViewport(240.0f, 240.0f
					* screenViewport.height / screenViewport.width, 240,
					240.0f * screenViewport.height / screenViewport.width);
		}
		else
		{
			layerViewport = new LayerViewport(240.0f * screenViewport.height
					/ screenViewport.width, 240.0f, 240.0f
					* screenViewport.height / screenViewport.width, 240);
		}
		
		// Get calling activity
		activity = game.getActivity();
		
		// Create OptionsManager
		optionsManager = new OptionsManager(game.getContext());
		
		// Get any current user options values
		fpsCurrentVal = optionsManager.getBoolOption(OptionsManager.FPS_COUNTER, DEFAULT_FPS_TOGGLE);
		effectsCurrentVal = optionsManager.getBoolOption(OptionsManager.VISUAL_EFFECTS, DEFAULT_EFFECTS_TOGGLE);
		musicCurrentVal = optionsManager.getIntOption(OptionsManager.MUSIC_VOLUME, DEFAULT_VOLUME);
		soundCurrentVal = optionsManager.getIntOption(OptionsManager.SOUND_VOLUME, DEFAULT_VOLUME);
		
		// Load in the assets used by this layer
		AssetStore assetManager = mGame.getAssetManager();
		
		// Load in bitmaps
		assetManager.loadAndAddBitmap(OPTIONS_BACKGROUND_BITMAP_ID, OPTIONS_BACKGROUND_BITMAP_PATH);
		assetManager.loadAndAddBitmap(CHECKED_BITMAP_ID, CHECKED_BITMAP_PATH);
		assetManager.loadAndAddBitmap(UNCHECKED_BITMAP_ID, UNCHECKED_BITMAP_PATH);
		assetManager.loadAndAddBitmap(SLIDER_BASE_BITMAP_ID, SLIDER_BASE_BITMAP_PATH);
		assetManager.loadAndAddBitmap(SLIDER_FILL_BITMAP_ID, SLIDER_FILL_BITMAP_PATH);
		
		// Set up background
		optionsBackground = new GameObject(
				layerViewport.getWidth() / 2.0f,
				layerViewport.getHeight() / 2.0f,
				layerViewport.getWidth(),
				layerViewport.getHeight(),
				assetManager.getBitmap(OPTIONS_BACKGROUND_BITMAP_ID),
				this);
		
		// Set up text painter with styles
		textPainter = new Paint();
		textPainter.setTextSize(60);
		textPainter.setColor(Color.BLACK);
		
		// Calculate leftmost horizontal position of options
		optionXPos = game.getScreenWidth() / 2.0f - OPTION_WIDTH / 2.0f;
		
		// Set up FPS toggle button
		fpsToggle = new ToggleButton(
				optionXPos,
				OPTION_SEPARATION,
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				UNCHECKED_BITMAP_ID,
				CHECKED_BITMAP_ID,
				this
		);
		
		// Update toggle button with loaded value
		fpsToggle.setToggled(fpsCurrentVal);
		
		// Set up visual effects toggle button
		effectsToggle = new ToggleButton(
				optionXPos,
				(OPTION_SEPARATION * 2),
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				UNCHECKED_BITMAP_ID,
				CHECKED_BITMAP_ID,
				this
		);
		
		// Update toggle button with loaded value
		effectsToggle.setToggled(effectsCurrentVal);
		
		// Set up text painter with styles
		Paint sliderPainter = new Paint();
		sliderPainter.setTextSize(60);
		sliderPainter.setColor(Color.BLACK);
		sliderPainter.setTextAlign(Paint.Align.CENTER);
		
		// Set up music slider, as part of constructor supply loaded value
		musicSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				musicCurrentVal,
				sliderPainter,
				game.getScreenWidth() / 2.0f,
				(OPTION_SEPARATION * 3),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				SLIDER_BASE_BITMAP_ID,
				SLIDER_FILL_BITMAP_ID,
				this,
				false);
		
		// Set up sound slider, as part of constructor supply loaded value
		soundsSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				soundCurrentVal,
				sliderPainter,
				game.getScreenWidth() / 2.0f,
				(OPTION_SEPARATION * 4),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				SLIDER_BASE_BITMAP_ID,
				SLIDER_FILL_BITMAP_ID,
				this,
				false);
		
		optionsManager.setOption(OptionsManager.FPS_COUNTER, fpsCurrentVal);
		optionsManager.setOption(OptionsManager.VISUAL_EFFECTS, effectsCurrentVal);
		optionsManager.setOption(OptionsManager.MUSIC_VOLUME, musicCurrentVal);
		optionsManager.setOption(OptionsManager.SOUND_VOLUME, soundCurrentVal);
		
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
			fpsToggle.update(elapsedTime);
			effectsToggle.update(elapsedTime);
			musicSlider.update(elapsedTime);
			soundsSlider.update(elapsedTime);
		}
		if (fpsCurrentVal != fpsToggle.isToggledOn())
		{
			// If currently loaded value is not the same as the UI element value
			// Update the stored user options
			optionsManager.setOption(OptionsManager.FPS_COUNTER, fpsToggle.isToggledOn());
			// Update currently loaded value
			fpsCurrentVal = fpsToggle.isToggledOn();
		}
		if (effectsCurrentVal != effectsToggle.isToggledOn())
		{
			optionsManager.setOption(OptionsManager.VISUAL_EFFECTS, effectsToggle.isToggledOn());
			effectsCurrentVal = effectsToggle.isToggledOn();
		}
		if (musicCurrentVal != musicSlider.getVal())
		{
			optionsManager.setOption(OptionsManager.MUSIC_VOLUME, musicSlider.getVal());
			musicCurrentVal = musicSlider.getVal();
		}
		if (soundCurrentVal != soundsSlider.getVal())
		{
			optionsManager.setOption(OptionsManager.SOUND_VOLUME, soundsSlider.getVal());
			soundCurrentVal = soundsSlider.getVal();
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
		optionsBackground.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
		// Draw UI elements
		fpsToggle.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
		effectsToggle.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
		musicSlider.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
		soundsSlider.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
		// Draw options text
		graphics2D.drawText(activity.getString(R.string.fps_description), optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION, textPainter);
		graphics2D.drawText(activity.getString(R.string.effects_description), optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION * 2, textPainter);
		graphics2D.drawText(activity.getString(R.string.music_description), optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION * 3, textPainter);
		graphics2D.drawText(activity.getString(R.string.sound_description), optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION * 4, textPainter);
		
	}
	
	public ToggleButton getFpsToggle()
	{
		return fpsToggle;
	}
	
	public ToggleButton getEffectsToggle()
	{
		return effectsToggle;
	}
	
	public Slider getMusicSlider()
	{
		return musicSlider;
	}
	
	public Slider getSoundsSlider()
	{
		return soundsSlider;
	}
}
