package uk.ac.qub.eeecs.game.gameHelp;

import android.util.Log;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.CardDescription;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HelpScreenSuperClass;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.HeroDescription;
import uk.ac.qub.eeecs.game.gameHelp.helpScreens.TextDescription;
import uk.ac.qub.eeecs.game.ui.PopUp;

/**
 * Created by 40216004 Dewei Liu  on 28/03/2018.
 */

public class HelpScreenController {

    private Game mGame;
    private HelpScreenSuperClass textDescription, heroDescription, cardDescription;

    private HelpScreenSuperClass currentScreen;
    private ArrayList<HelpScreenSuperClass> screens;

    public HelpScreenController(Game game) {
        mGame = game;
        /***************************************************************************************/
        //set up all help game screens
        screens = new ArrayList<HelpScreenSuperClass>();

        textDescription = new TextDescription(mGame, this);
        screens.add(textDescription);

        heroDescription = new HeroDescription(mGame, this);
        screens.add(heroDescription);

        cardDescription = new CardDescription(mGame, this);
        screens.add(cardDescription);
        /***************************************************************************************/
     //The first page to display
          setUpNewScreen(textDescription);

    }

    public void previousScreen() {
        int index = screens.indexOf(currentScreen);
        if (index <= 0) {
            currentScreen.setErrorMessage("Hey, this is the first page.", 2);
        } else {
            setUpNewScreen(screens.get(index - 1));
        }
    }

    public void nextScreen() {
        int index = screens.indexOf(currentScreen);
        if (index >= screens.size() - 1) {
            currentScreen.setErrorMessage("Hey, this is the last page." , 2);
        } else {
            setUpNewScreen(screens.get(index +1));
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
