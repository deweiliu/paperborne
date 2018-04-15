package uk.ac.qub.eeecs.game.worldScreen;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.SaveManager;
import uk.ac.qub.eeecs.game.cardDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.ui.PopUp;

/**
 * Created by Jamie T on 25/01/2018.
 * Handles all single player level selection and progression
 */

public class WorldScreen extends GameScreen {
    
    // Name of the game screen
    private static final String WORLD_SCREEN_NAME = "WorldScreen";
    
    // Bitmap paths and IDs
    private static final String WORLD_MAP_BITMAP_ID = "worldmap";
    private static final String WORLD_MAP_BITMAP_PATH = "img/WorldMap.png";

    private static final String LAUNCH_BUTTON_BITMAP_ID = "launchbutton";
    private static final String LAUNCH_BUTTON_BITMAP_PATH = "img/LaunchButton.png";

    private static final String CHARACTER_BITMAP_ID = "character";
    private static final String CHARACTER_BITMAP_PATH = "img/Hero/Character.png";

    // Level Sprite width/height
    private final int DEFAULT_LEVEL_HEIGHT = 128;

    // Player Character Sprite width/height
    private static final int PLAYER_WIDTH = 128;
    private static final int PLAYER_HEIGHT = 128;

    // Launch battle button width/height/distance from bottom of screen
    private static final int LAUNCH_BUTTON_WIDTH = 512;
    private static final int LAUNCH_BUTTON_HEIGHT = 128;
    private static final int LAUNCH_BUTTON_Y_SEPERATION = 128;

    // Pop up font size
    private static final int POPUP_FONT_SIZE = 72;
    // Pop up visibility duration
    private static final long POPUP_DURATION = 3;
    // Message to display to the user if they try to launch a locked level
    private static final String LEVEL_LOCKED_MESSAGE = "Level locked!";

    // Paint defining how text is to be drawn
    private Paint textPainter;
    // Button to launch a battle
    private PushButton launchButton;
    // World Map Background
    private GameObject worldBackground;
    // List of available levels for the player to choose from
    private List<Level> levels;
    // The player character sprite
    private GameObject worldPlayer;
    // The index in the available levels of the currently selected level
    private int currentLevel = 0;
    // The currently loaded save file
    private SaveGame loadedSave;
    // Game asset manager
    private AssetStore assetManager;
    // Pop up message to display to the user if there is a level they try to launch that is locked
    private PopUp popUp;
    // Game activity
    private Activity activity;

    /**
     * Handler and Runnable for toast message
     */
    private Handler toastHandler;
    private Runnable toastRunner;

    public WorldScreen(Game game) {
        super(WORLD_SCREEN_NAME, game);
        activity = game.getActivity();
        // Initialise level list
        levels = new ArrayList<>();
        // Get asset manager
        assetManager = game.getAssetManager();
        // Load in any bitmaps used in the world screen
        assetManager.loadAndAddBitmap(WORLD_MAP_BITMAP_ID, WORLD_MAP_BITMAP_PATH);
        assetManager.loadAndAddBitmap(CHARACTER_BITMAP_ID, CHARACTER_BITMAP_PATH);
        assetManager.loadAndAddBitmap(LAUNCH_BUTTON_BITMAP_ID, LAUNCH_BUTTON_BITMAP_PATH);
        assetManager.loadAndAddBitmap(PopUp.POPUP_BITMAP_ID, PopUp.POPUP_BITMAP_PATH);

        // Create the world map background
        worldBackground = new GameObject(
                game.getScreenWidth() / 2,
                game.getScreenHeight() / 2,
                mGame.getScreenWidth(),
                mGame.getScreenHeight(),
                assetManager.getBitmap(WORLD_MAP_BITMAP_ID),
                this
        );

        // Load in levels from JSON campaign definition
        levels = SaveManager.loadLevels(SaveManager.LEVEL_FILE, game.getContext());

        for (Level level : levels) {
            // Load any required bitmaps for the level
            assetManager.loadAndAddBitmap(level.getBitmapID(), level.getBitmapPath());
            level.setButton(new PushButton( // Create the game level's button
                    getGame().getScreenWidth() * (float) level.getxPercent(), // Calculate button's X position from X percentage supplied
                    getGame().getScreenHeight() * (float) level.getyPercent(),
                    (float) level.getWidth(), // Button width
                    (float) level.getHeight(), // Button height
                    level.getBitmapID(), // Button bitmap
                    this
            ));
        }
        // Load the default save slot save, if none exists create a new one
        loadedSave = SaveManager.loadSavedGame(SaveManager.DEFAULT_SAVE_SLOT, game.getContext());

        if (levels.size() < 1) {
            // If there are no loaded levels
            // Display an error and return to the main menu
            toastHandler = new Handler(Looper.getMainLooper());
            toastRunner = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getGame().getContext(), getGame().getContext().getString(R.string.broken_level_file), Toast.LENGTH_SHORT).show();
                }
            };
            toastHandler.post(toastRunner);
            mGame.getScreenManager().removeScreen(this.getName());
            mGame.getScreenManager().addScreen(new MenuScreen(mGame));
        }

        // Create the world player sprite, with their X and Y aligned to current/default level
        worldPlayer = new GameObject(
                levels.get(currentLevel).getButton().position.x,
                levels.get(currentLevel).getButton().position.y - DEFAULT_LEVEL_HEIGHT,
                PLAYER_WIDTH,
                PLAYER_HEIGHT,
                assetManager.getBitmap(CHARACTER_BITMAP_ID),
                this
        );

        // Create the launch battle button
        launchButton = new PushButton(
                game.getScreenWidth() / 2,
                game.getScreenHeight() - LAUNCH_BUTTON_Y_SEPERATION,
                LAUNCH_BUTTON_WIDTH,
                LAUNCH_BUTTON_HEIGHT,
                LAUNCH_BUTTON_BITMAP_ID,
                this
        );

        // Set up text painter with styles
        textPainter = new Paint();
        textPainter.setTextSize(mGame.getScreenWidth() / 48);
        textPainter.setColor(Color.BLACK);
        textPainter.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // For each available level
        for (int i = 0; i < levels.size(); i++) {
            // Get the button for the level
            PushButton button = levels.get(i).getButton();
            // Update the button
            button.update(elapsedTime);
            if (button.isPushTriggered()) {
                // If the button for the level has been pressed
                // Set the currently selected level index to the index of the pressed level
                currentLevel = i;
                // Move the player sprite to above the level sprite
                worldPlayer.setPosition(button.position.x, button.position.y - DEFAULT_LEVEL_HEIGHT);
            }
        }
        // Update launch button
        launchButton.update(elapsedTime);
        if (launchButton.isPushTriggered()) {
            // If the launch button has been pressed load the selected level's battle
            loadBattle();
        }
        if (popUp != null) {
            popUp.update(elapsedTime);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Draw the world map background
        worldBackground.draw(elapsedTime, graphics2D);
        // For each level loaded
        for (int i = 0; i < levels.size(); i++) {
            // Draw the level's button
            levels.get(i).getButton().draw(elapsedTime, graphics2D);
        }
        // Draw the player sprite and launch button
        worldPlayer.draw(elapsedTime, graphics2D);
        launchButton.draw(elapsedTime, graphics2D);
        // Draw the text stating which level is currently selected above the launch button
        graphics2D.drawText(
                levels.get(currentLevel).getName(),
                mGame.getScreenWidth() / 2,
                launchButton.position.y - LAUNCH_BUTTON_HEIGHT,
                textPainter
        );
        if (popUp != null) {
            if (popUp.isVisible()) {
                // If the pop up should be visible, draw it
                popUp.draw(elapsedTime, graphics2D);
            }
        }
    }

    /**
     * Loads a game level's battle if the level is unlocked to the user
     */
    private void loadBattle() {
        boolean locked = true;
        // Get the level attempting to be loaded
        Level level = levels.get(currentLevel);
        if (level == null) {
            // If the level isn't loaded correctly
            // load the first level instead
            level = levels.get(0);
        }
        if (level.getPrerequisites().isEmpty()) {
            // If the level has no prerequisite levels, the level is unlocked to the user
            locked = false;
        } else {
            // Otherwise loop through each of the level's prerequisites
            for (int i = 0; i < level.getPrerequisites().size(); i++) {
                // If the loaded save game has the level ID in it's completed levels list
                // The level is unlocked to the user
                // otherwise the level is locked to the user
                locked = !loadedSave.getCompleted().contains(level.getPrerequisites().get(i));
            }
        }
        if (locked) {
            // Can't play level, haven't completed previous levels
            // Create new popup with error message
            popUp = new PopUp(
                    String.format("%s", LEVEL_LOCKED_MESSAGE),
                    POPUP_DURATION,
                    POPUP_FONT_SIZE,
                    mGame.getAssetManager().getBitmap(PopUp.POPUP_BITMAP_ID),
                    this
            );
            // Display the popup
            popUp.show();
        } else {
            // Load the battle screen
            mGame.getScreenManager().removeScreen(this.getName());
            // Load in level specific information, such as opponent and player decks
            mGame.getScreenManager().addScreen(new CardDemoScreen(
                    mGame,
                    level.getDeck(),
                    loadedSave.getPlayerDeck(),
                    level
            ));
        }
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

}
