package uk.ac.qub.eeecs.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.qub.eeecs.gage.Game;

/**
 * Sample demo game that is create within the MainActivity class
 *
 * @version 1.0
 */
public class DemoGame extends Game {

    /**
     * Create a new demo game
     */
    public DemoGame() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.ac.qub.eeecs.gage.Game#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go with a default 30 UPS/FPS
        setTargetFramesPerSecond(30);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call the Game's onCreateView to get the view to be returned.
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create and add a stub game screen to the screen manager. We don't
        // want to do this within the onCreate method as the menu screen
        // will layout the buttons based on the size of the view.
        //MenuScreen stubMenuScreen = new MenuScreen(this);
        //mScreenManager.addScreen(stubMenuScreen);
        SplashScreen splashScreen = new SplashScreen(this);
        mScreenManager.addScreen(splashScreen);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if (mScreenManager.getCurrentScreen().getName().equals("MenuScreen"))
            return false;

        getScreenManager().removeScreen(mScreenManager.getCurrentScreen().getName());
        MenuScreen menuScreen = new MenuScreen(this);
        getScreenManager().addScreen(menuScreen);

        return true;
    }

}