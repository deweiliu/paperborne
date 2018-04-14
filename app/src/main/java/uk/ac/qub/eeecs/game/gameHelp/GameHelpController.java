package uk.ac.qub.eeecs.game.gameHelp;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.CardDescription;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.Copyright;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.ForTestingEndGameLogic;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HelpScreenSuperClass;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HeroDescription;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.TextDescription;

/**
 * To use the Game Help
 * just simply create a object of this class, and it will handle everything for you
 * <p>
 * Created by 40216004 Dewei Liu  on 28/03/2018.
 */

public class GameHelpController {

    private Game mGame;
    private HelpScreenSuperClass currentScreen;
    private ArrayList<HelpScreenSuperClass> screens;

    /**
     * Default Game Help Screens
     *
     * @param game the game on the screen
     */
    public GameHelpController(Game game) {
        mGame = game;

        /***************************************************************************************/
        //set up all help game screens
        screens = new ArrayList<>();
        screens.add(new TextDescription(mGame, this));
        screens.add(new HeroDescription(mGame, this));
        screens.add( new CardDescription(mGame, this));
        screens.add(new Copyright(mGame, this));
        screens.add(new ForTestingEndGameLogic(mGame, this));

        /***************************************************************************************/
        //The first page to display
        setUpNewScreen(screens.get(0));
    }

    /**
     * Customised Game help screens
     *
     * @param game        the game on the screen
     * @param helpScreens all screens you want to display for game help
     */
    public GameHelpController(Game game, ArrayList<HelpScreenSuperClass> helpScreens) {
        mGame = game;
        /***************************************************************************************/
        //set up all help game screens
        screens = helpScreens;
        /***************************************************************************************/
        //The first page to display
        setUpNewScreen(helpScreens.get(0));
    }

    /**
     * Turn to the previous game help screen
     *
     * @return true if it has been successfully turn to the previous screen
     */
    public boolean previousScreen() {
        int index = screens.indexOf(currentScreen);
        if (index <= 0) {
            return false;
        } else {
            setUpNewScreen(screens.get(index - 1));
            return true;
        }
    }

    /**
     * Turn to the next game help screen
     *
     * @return true if it has been successfully turn to the next screen
     */
    public boolean nextScreen() {
        int index = screens.indexOf(currentScreen);
        if (index >= screens.size() - 1) {
            return false;
        } else {
            setUpNewScreen(screens.get(index + 1));
            return true;
        }
    }

    public void setUpNewScreen(GameScreen newScreen) {
        mGame.getScreenManager().removeScreen(mGame.getScreenManager().getCurrentScreen().getName());
        mGame.getScreenManager().addScreen(newScreen);
        mGame.getScreenManager().setAsCurrentScreen(newScreen.getName());
        if (newScreen instanceof HelpScreenSuperClass) {
            this.currentScreen = (HelpScreenSuperClass) newScreen;
            this.currentScreen.reset();
        }
    }

    public HelpScreenSuperClass getCurrentScreen() {
        return currentScreen;
    }
}
