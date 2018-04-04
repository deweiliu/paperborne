package uk.ac.qub.eeecs.game.endGameLogic;

import java.util.List;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.SaveManager;
import uk.ac.qub.eeecs.game.ui.Rotating;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.GameOverScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.*;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.GetNameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.ShowRecordsScreen;
import uk.ac.qub.eeecs.game.worldScreen.SaveGame;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 * <p>
 * To use the package of endGameLogic
 * simply create an instance of EndGameController with parameters of current screen.
 * If it is single player mode, player1 is the human player
 * If it is multiple player mode, player1 is the player who at the bottom of screen, and player2 is the at the top of screen.
 */

public class EndGameController extends GameScreen {

    //Record the state
    private int state;
    private static final int GAME_OVER_SCREEN = 0;
    private static final int GET_NAME_SCREEN = 1;
    private static final int SHOW_RECORDS_SCREEN = 2;

    /***************************************************************************************************/

    private boolean isSinglePlayer;
    private boolean hasPlayer1Won;
    private EndGameScreen mEndGameScreen ;
    private GameScreen mBattleScreen;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private Rotating loading;

    //Constructor for testing
    public EndGameController(GameScreen cardDemoScreen, boolean isSinglePlayer, boolean hasPlayer1Won) {
        this(cardDemoScreen, isSinglePlayer, hasPlayer1Won, null);
        //this(cardDemoScreen, isSinglePlayer, hasPlayer1Won, "level_one");
    }


    public EndGameController(GameScreen cardDemoScreen, boolean isSinglePlayer, boolean hasPlayer1Won, String levelID) {
        super("EndGameController", cardDemoScreen.getGame());

        //If this is not a test (I let levelID be null when I test EndGameController)
        if (levelID != null) {

            //If the player was playing against computer
            if (isSinglePlayer == true) {

                //If the player has won
                if (hasPlayer1Won == true) {

                    //Record it and unlock new level
                    this.setCompletedLevelRecords(levelID);
                }
            }
        }
        /***************************************************************************************************/

        //Set of variables
        this.mBattleScreen = cardDemoScreen;
        this.isSinglePlayer = isSinglePlayer;
        this.hasPlayer1Won = hasPlayer1Won;

        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);


        //Set up loading animation
        final String LOADING_NAME = "loadingToMainMenuFromEndGameLogic";
        mGame.getAssetManager().loadAndAddBitmap(LOADING_NAME, "img/End Game Logic/loading.png");
        loading = new Rotating(2000, mLayerViewport.getHeight() / 4,
                mGame.getAssetManager().getBitmap(LOADING_NAME), this, 5000);

        //Start to run End game screen in the game
        this.mEndGameScreen = new GameOverScreen(this);
        this.state = GAME_OVER_SCREEN;
        mGame.getScreenManager().removeScreen(mBattleScreen.getName());
        mGame.getScreenManager().addScreen(this);
        mGame.getScreenManager().setAsCurrentScreen(this.getName());
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (mEndGameScreen.isFinished()) {
            if (loading.isFinished()) {
                changeToNextScreen();
            } else {
                loading.update(elapsedTime);
            }
        } else {
            mEndGameScreen.update(elapsedTime);
        }
    }
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        mEndGameScreen.draw(elapsedTime, graphics2D);
        if (mEndGameScreen.isFinished()) {
            loading.draw(elapsedTime, graphics2D);
        }
    }

    private void changeToNextScreen() {
        loading.reset();
        switch (this.state) {
            //From game over animations to get player name
            case GAME_OVER_SCREEN:
                this.mEndGameScreen = new GetNameScreen(this);
                this.state = GET_NAME_SCREEN;
                break;

            //From get player name to records screen
            case GET_NAME_SCREEN:
                User user = ((GetNameScreen) mEndGameScreen).getUserName();

                this.mEndGameScreen = new ShowRecordsScreen(this, user);
                this.state = SHOW_RECORDS_SCREEN;
                break;

            //All eng game screens have finished,
            case SHOW_RECORDS_SCREEN:

                // Go back to the main menu.
                MenuScreen menuScreen = new MenuScreen(super.mGame);
                mGame.getScreenManager().removeScreen(this.getName());
                mGame.getScreenManager().addScreen(menuScreen);
                mGame.getScreenManager().setAsCurrentScreen(menuScreen.getName());
                break;

            //The current screen is null. Create a game over animations
            default:
                this.mEndGameScreen = new GameOverScreen(this);
                this.state = GAME_OVER_SCREEN;
                break;
        }

    }


    private void setCompletedLevelRecords(String levelID) {
        try {
            SaveGame save = SaveManager.loadSavedGame(SaveManager.DEFAULT_SAVE_SLOT, mGame);
            List<String> completedLevel = save.getCompleted();
            completedLevel.add(levelID);
            save.setCompleted(completedLevel);
            SaveManager.writeSaveFile(save, mGame);
        } catch (Exception e) {
            // throw new Exception("Cannot save record correctly.");
        }
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public boolean hasPlayer1Won() {
        return hasPlayer1Won;
    }

    public GameScreen getBattleScreen() {
        return mBattleScreen;
    }

    public ScreenViewport getScreenViewport() {
        return mScreenViewport;
    }

    public LayerViewport getLayerViewport() {
        return mLayerViewport;
    }


}

