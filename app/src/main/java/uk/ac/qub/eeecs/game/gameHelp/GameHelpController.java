package uk.ac.qub.eeecs.game.gameHelp;

import java.util.ArrayList;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.CardDescription;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.ForTestingEndGameLogic;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HelpScreenSuperClass;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HeroDescription;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.TextDescription;

/**
 * Created by 40216004 Dewei Liu  on 28/03/2018.
 */

public class GameHelpController {

    private Game mGame;
    private HelpScreenSuperClass textDescription;
    private HelpScreenSuperClass heroDescription;
    private HelpScreenSuperClass cardDescription;
    private HelpScreenSuperClass forDeveloperTesting;

    private HelpScreenSuperClass currentScreen;
    private ArrayList<HelpScreenSuperClass> screens;

    public GameHelpController(Game game) {
        mGame = game;

        /***************************************************************************************/
        //set up all help game screens
        textDescription = new TextDescription(mGame, this);
        heroDescription = new HeroDescription(mGame, this);
        cardDescription = new CardDescription(mGame, this);
        forDeveloperTesting = new ForTestingEndGameLogic(mGame, this);

        screens = new ArrayList<>();
        screens.add(textDescription);
        screens.add(heroDescription);
        screens.add(cardDescription);
        screens.add(forDeveloperTesting);

        /***************************************************************************************/
        //The first page to display
        setUpNewScreen(textDescription);
    }

    public void previousScreen() {
        int index = screens.indexOf(currentScreen);
        if (index <= 0) {
            currentScreen.setPopUpMessage("Hey, this is the first page.", 2);
        } else {
            setUpNewScreen(screens.get(index - 1));
        }
    }

    public void nextScreen() {
        int index = screens.indexOf(currentScreen);
        if (index >= screens.size() - 1) {
            currentScreen.setPopUpMessage("Hey, this is the last page.", 2);
        } else {
            setUpNewScreen(screens.get(index + 1));
        }
    }

    public void setUpNewScreen(GameScreen newScreen) {
        mGame.getScreenManager().removeScreen(mGame.getScreenManager().getCurrentScreen().getName());
        mGame.getScreenManager().addScreen(newScreen);
        mGame.getScreenManager().setAsCurrentScreen(newScreen.getName());
        if (newScreen instanceof HelpScreenSuperClass) {
            this.currentScreen = (HelpScreenSuperClass) newScreen;
        }
    }


}
