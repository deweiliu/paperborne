package uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public interface EndGameScreen extends BasicEndGameStuff {

    /**
     * @return GameScreen to which this object belongs
     */
    GameScreen getGameScreen();

    /**
     * Return the game to which this game screen is attached
     *
     * @return Game to which screen is attached
     */
    Game getGame();

    /**
     * @return the screen width
     */
    int getScreenWidth();

    /**
     * @return the screen height
     */
    int getScreenHeight();

    /**
     * @return Game layer viewport
     */
    LayerViewport getLayerViewport();

    /**
     * @return screen viewport
     */
    ScreenViewport getScreenViewPort();

    /**
     * Get the game's asset manager
     *
     * @return Asset manager
     */
    AssetStore getAssetManager();

    /**
     * @return if is single player mode or multiplayer mode
     */
    boolean isSinglePlayer();

    /**
     * in single player mode, player 1 will be the human player
     * in multiplayer mode, player1 will be the player has at the bottom of the screen
     *
     * @return if player1 has won
     */
    boolean hasPlayer1Won();

    /**
     * @return The GameScreen which for the battle (the main game screen)
     */
    GameScreen getCardDemoScreen();
}
