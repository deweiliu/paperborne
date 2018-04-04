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


    GameScreen getGameScreen();

    Game getGame();

    int getScreenWidth();

    int getScreenHeight();

    LayerViewport getLayerViewport();

    ScreenViewport getScreenViewPort();

    AssetStore getAssetManager();

    boolean isSinglePlayer();

    boolean hasPlayer1Won();

    GameScreen getBattleScreen();
}
