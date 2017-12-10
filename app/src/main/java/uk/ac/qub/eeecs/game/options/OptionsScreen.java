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
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;


/**
 * Screen to display user options and allow users to edit them
 */
public class OptionsScreen extends GameScreen
{
	/**
	 * Minimum, maximum and default volumes the music and sound effects can be set to
	 */
	final private int MAX_VOLUME = 10;
	final private int MIN_VOLUME = 0;
	final private int DEFAULT_VOLUME = 7;
	
	/**
	 * Width and height of the toggle buttons
	 */
	final private float TOGGLE_WIDTH = 128.0f;
	final private float TOGGLE_HEIGHT = 128.0f;
	
	/**
	 * Width and height of the sliders
	 */
	final private float SLIDER_WIDTH = 768.0f;
	final private float SLIDER_HEIGHT = 128.0f;
	
	// Vertical separation between each option between each option
	final private float OPTION_SEPARATION = 192.0f;
	
	// The entire option width, space to leave to fit in text and the UI elements
	final private float OPTION_WIDTH = 768.0f;
	
	// Calling activity
	private Activity activity;
	
	// The background object
	private GameObject optionsBackground;
	
	// The options manager for setting and retrieving user options
	private OptionsManager manager;
	
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
	private Slider soundSlider;
	
	// Painter for text style
	private Paint textPainter;
	
	/**
	 * Currently loaded user options
	 */
	private boolean fpsCurrentVal = false;
	private boolean effectsCurrentVal = false;
	private int musicCurrentVal = DEFAULT_VOLUME;
	private int soundCurrentVal= DEFAULT_VOLUME;
	
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
		super("OptionsScreen", game);
		
		// Set up viewports
		mLayerViewport = new LayerViewport(240, 160, 240, 160);
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());
		GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);
		
		// Get calling activity
		activity = game.getActivity();
		
		// Create OptionsManager
		manager = new OptionsManager(game.getActivity());
		
		// Get any current user options values
		fpsCurrentVal = manager.getBoolOption(OptionsManager.FPS_COUNTER);
		effectsCurrentVal = manager.getBoolOption(OptionsManager.VISUAL_EFFECTS);
		musicCurrentVal = manager.getIntOption(OptionsManager.MUSIC_VOLUME, DEFAULT_VOLUME);
		soundCurrentVal = manager.getIntOption(OptionsManager.SOUND_VOLUME, DEFAULT_VOLUME);
		
		// Load in the assets used by this layer
		AssetStore assetManager = mGame.getAssetManager();
		
		// Load in bitmaps
		assetManager.loadAndAddBitmap("OptionsBackground", "img/OptionsScreenBackground.jpg");
		assetManager.loadAndAddBitmap("FPSChecked", "img/FPSChecked.jpg");
		assetManager.loadAndAddBitmap("FPSUnchecked", "img/FPSUnchecked.jpg");
		assetManager.loadAndAddBitmap("SliderBase", "img/SliderBase.png");
		assetManager.loadAndAddBitmap("SliderFill", "img/SliderFill.png");
		
		// Set up background
		optionsBackground = new GameObject(
				mLayerViewport.getWidth() / 2.0f,
				mLayerViewport.getHeight() / 2.0f,
				mLayerViewport.getWidth(),
				mLayerViewport.getHeight(),
				assetManager.getBitmap("OptionsBackground"),
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
				"FPSChecked",
				"FPSUnchecked",
				this
		);
		
		// Update toggle button with loaded value
		fpsToggle.setToggled(fpsCurrentVal);
		
		// Set up visual effects toggle button
		effectsToggle = new ToggleButton(
				optionXPos,
				(OPTION_SEPARATION *2),
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				"FPSChecked",
				"FPSUnchecked",
				this
		);
		
		// Update toggle button with loaded value
		effectsToggle.setToggled(effectsCurrentVal);
		
		// Set up music slider, as part of constructor supply loaded value
		musicSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				musicCurrentVal,
				textPainter,
				game.getScreenWidth()/2.0f,
				(OPTION_SEPARATION *3),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				"SliderBase",
				"SliderFill",
				this,
				false);
		
		// Set up sound slider, as part of constructor supply loaded value
		soundSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				soundCurrentVal,
				textPainter,
				game.getScreenWidth()/2.0f,
				(OPTION_SEPARATION *4),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				"SliderBase",
				"SliderFill",
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
			// Just check the first touch event that occurred in the frame.
			// It means pressing the screen with several fingers may not
			// trigger a 'button', but, hey, it's an exceedingly basic menu.
			TouchEvent touchEvent = touchEvents.get(0);
			
			// Update UI elements
			fpsToggle.update(elapsedTime);
			effectsToggle.update(elapsedTime);
			musicSlider.update(elapsedTime);
			soundSlider.update(elapsedTime);
		}
		if(fpsCurrentVal != fpsToggle.isToggledOn())
		{
			// If currently loaded value is not the same as the UI element value
			// Update the stored user options
			manager.setOption(OptionsManager.FPS_COUNTER, fpsToggle.isToggledOn());
			// Update currently loaded value
			fpsCurrentVal = fpsToggle.isToggledOn();
		}
		if(effectsCurrentVal != effectsToggle.isToggledOn())
		{
			manager.setOption(OptionsManager.VISUAL_EFFECTS, effectsToggle.isToggledOn());
			effectsCurrentVal = effectsToggle.isToggledOn();
		}
		if(musicCurrentVal != musicSlider.getVal())
		{
			manager.setOption(OptionsManager.MUSIC_VOLUME, musicSlider.getVal());
			musicCurrentVal = musicSlider.getVal();
		}
		if(soundCurrentVal != soundSlider.getVal())
		{
			manager.setOption(OptionsManager.SOUND_VOLUME, soundSlider.getVal());
			soundCurrentVal = soundSlider.getVal();
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
		optionsBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		// Draw UI elements
		fpsToggle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		effectsToggle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		musicSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		soundSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		// Draw options text
		graphics2D.drawText(activity.getString(R.string.fps_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION, textPainter);
		graphics2D.drawText(activity.getString(R.string.effects_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION *2, textPainter);
		graphics2D.drawText(activity.getString(R.string.music_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION *3, textPainter);
		graphics2D.drawText(activity.getString(R.string.sound_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPARATION *4, textPainter);
		
	}
	
	/**
	 * Remove the current game screen and then change to the specified screen
	 *
	 * @param screen game screen to become active
	 */
	private void changeToScreen(GameScreen screen)
	{
		mGame.getScreenManager().removeScreen(this.getName());
		mGame.getScreenManager().addScreen(screen);
	}
}
