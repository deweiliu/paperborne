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
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */
public class OptionsScreen extends GameScreen
{
	
	final private int MAX_VOLUME = 10;
	final private int MIN_VOLUME = 0;
	final private int DEFAULT_VOLUME = 7;
	
	final private float TOGGLE_WIDTH = 128.0f;
	final private float TOGGLE_HEIGHT = 128.0f;
	
	final private float SLIDER_WIDTH = 768.0f;
	final private float SLIDER_HEIGHT = 128.0f;
	
	final private float OPTION_SEPERATION = 192.0f;
	final private float OPTION_WIDTH = 768.0f;
	
	private Activity activity;
	
	private GameObject optionsBackground;
	
	private OptionsManager manager;
	
	private float optionXPos;
	
	/**
	 * Define viewports for this layer and the associated screen projection
	 */
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;
	
	private ToggleButton fpsToggle;
	private ToggleButton effectsToggle;
	
	private Slider musicSlider;
	private Slider soundSlider;
	
	private Paint textPainter;
	
	private boolean fpsCurrentVal = false;
	private boolean effectsCurrentVal = false;
	private int musicCurrentVal = DEFAULT_VOLUME;
	private int soundCurrentVal= DEFAULT_VOLUME;
	
	/**
	 * Create the Options screen
	 *
	 * @param game Game to which this screen belongs
	 */
	public OptionsScreen(Game game)
	{
		super("OptionsScreen", game);
		
		// /////////////////////////////////////////////////////////////////////////
		// Methods
		// /////////////////////////////////////////////////////////////////////////
		// Create the view ports

		mLayerViewport = new LayerViewport(240, 160, 240, 160);
		mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
				game.getScreenHeight());
		GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);
		
		activity = game.getActivity();
		
		manager = new OptionsManager(game.getActivity());
		
		fpsCurrentVal = manager.getBoolOption(OptionsManager.FPS_COUNTER);
		effectsCurrentVal = manager.getBoolOption(OptionsManager.VISUAL_EFFECTS);
		musicCurrentVal = manager.getIntOption(OptionsManager.MUSIC_VOLUME, DEFAULT_VOLUME);
		soundCurrentVal = manager.getIntOption(OptionsManager.SOUND_VOLUME, DEFAULT_VOLUME);
		
		// Load in the assets used by this layer
		AssetStore assetManager = mGame.getAssetManager();
		
		assetManager.loadAndAddBitmap("OptionsBackground", "img/OptionsScreenBackground.jpg");
		
		assetManager.loadAndAddBitmap("FPSChecked", "img/FPSChecked.jpg");
		assetManager.loadAndAddBitmap("FPSUnchecked", "img/FPSUnchecked.jpg");
		assetManager.loadAndAddBitmap("SliderBase", "img/SliderBase.png");
		assetManager.loadAndAddBitmap("SliderFill", "img/SliderFill.png");
		
		optionsBackground = new GameObject(
				mLayerViewport.getWidth() / 2.0f,
				mLayerViewport.getHeight() / 2.0f,
				mLayerViewport.getWidth(),
				mLayerViewport.getHeight(),
				assetManager.getBitmap("OptionsBackground"),
				this);
		
		textPainter = new Paint();
		textPainter.setTextSize(60);
		textPainter.setColor(Color.BLACK);
		
		optionXPos = game.getScreenWidth() / 2.0f - OPTION_WIDTH / 2.0f;
		
		fpsToggle = new ToggleButton(
				optionXPos,
				OPTION_SEPERATION,
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				"FPSChecked",
				"FPSUnchecked",
				this
		);
		
		fpsToggle.setToggled(fpsCurrentVal);
		
		effectsToggle = new ToggleButton(
				optionXPos,
				(OPTION_SEPERATION*2),
				TOGGLE_WIDTH,
				TOGGLE_HEIGHT,
				"FPSChecked",
				"FPSUnchecked",
				this
		);
		
		effectsToggle.setToggled(effectsCurrentVal);
		
		musicSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				musicCurrentVal,
				textPainter,
				game.getScreenWidth()/2.0f,
				(OPTION_SEPERATION*3),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				"SliderBase",
				"SliderFill",
				this);
		
		
		soundSlider = new Slider(
				MIN_VOLUME,
				MAX_VOLUME,
				soundCurrentVal,
				textPainter,
				game.getScreenWidth()/2.0f,
				(OPTION_SEPERATION*4),
				SLIDER_WIDTH,
				SLIDER_HEIGHT,
				"SliderBase",
				"SliderFill",
				this);
		
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
			
			fpsToggle.update(elapsedTime);
			effectsToggle.update(elapsedTime);
			musicSlider.update(elapsedTime);
			soundSlider.update(elapsedTime);
		}
		if(fpsCurrentVal != fpsToggle.isToggledOn())
		{
			manager.setOption(OptionsManager.FPS_COUNTER, fpsToggle.isToggledOn());
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
		optionsBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		fpsToggle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		effectsToggle.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		musicSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		soundSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		graphics2D.drawText(activity.getString(R.string.fps_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPERATION, textPainter);
		graphics2D.drawText(activity.getString(R.string.effects_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPERATION*2, textPainter);
		graphics2D.drawText(activity.getString(R.string.music_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPERATION*3, textPainter);
		graphics2D.drawText(activity.getString(R.string.sound_description),optionXPos + TOGGLE_WIDTH, OPTION_SEPERATION*4, textPainter);
		
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
