package uk.ac.qub.eeecs.gage.engine;

import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * The screen manager stores the available screens defined within the game.
 * Screens can be added or remove to reflect the evolution of the game. Within
 * the central game loop, the current game screen will be retrieved and
 * updated/rendered
 *
 * @version 1.0
 */
public class ScreenManager {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Collection of available game screens
     */
    private Map<String, GameScreen> mGameScreens;

    /**
     * Current game screen
     */
    private GameScreen mCurrentScreen;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new screen manager
     */
    public ScreenManager() {
        mGameScreens = new HashMap<String, GameScreen>();
        mCurrentScreen = null;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Add the specified game screen to the manager.
     * <p>
     * Note: If this is the first game screen to be added to the manager then it
     * will automatically become the current game screen.
     *
     * @param screen GameScreen instance to be added
     * @return Boolean true if the screen was added, false if the screen could
     * not be added (a screen with the specified name already exists).
     */
    public boolean addScreen(GameScreen screen) {
        // Add the game screen if the specified name isn't already added
        if (mGameScreens.containsKey(screen.getName()))
            return false;
        mGameScreens.put(screen.getName(), screen);

        // If this is the first added screen then automatically set it as
        // the default game screen
        if (mGameScreens.size() == 1)
            mCurrentScreen = screen;

        return true;
    }

    /**
     * Set the named game screen as the current screen.
     *
     * @param name String name reference for the target screen
     * @return Boolean true if the screen could be set as the current game
     * screen, otherwise false (no screen found with the specified
     * name).
     */
    public boolean setAsCurrentScreen(String name) {
        GameScreen currentScreen = mGameScreens.get(name);
        if (currentScreen != null) {
            mCurrentScreen = currentScreen;
            return true;
        } else
            return false;
    }

    /**
     * Return the current game screen.
     *
     * @return Current game instance instance, or null if no current game screen
     * has been defined.
     */
    public GameScreen getCurrentScreen() {
        return mCurrentScreen;
    }

    /**
     * Return the named game screen.
     *
     * @param name String name reference for the target screen.
     * @return Current game instance instance, or null if no the specified game
     * screen could not be found.
     */
    public GameScreen getScreen(String name) {
        return mGameScreens.get(name);
    }

    /**
     * Remove the specified game screen from the manager.
     * <p>
     * Note: Remove a screen from the manager will not result in dispose being
     * automatically called on the removed screen.
     *
     * @param name String name reference for the screen to remove.
     * @return Boolean true if the screen was removed, false otherwise (the
     * specified screen could not be found).
     */
    public boolean removeScreen(String name) {
        GameScreen gameScreen = mGameScreens.remove(name);
        return (gameScreen != null);
    }

    /**
     * Dispose of the manager and all game screens stored within the manager.
     */
    public void dispose() {
        for (GameScreen gameScreen : mGameScreens.values())
            gameScreen.dispose();
    }
}
