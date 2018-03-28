package uk.ac.qub.eeecs.game.gameHelp.helpScreens;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.gameHelp.HelpScreenController;
import uk.ac.qub.eeecs.game.ui.PopUp;
import uk.ac.qub.eeecs.game.worldScreen.WorldScreen;

/**
 * Created by 40216004 Dewei Liu on 28/03/2018.
 */

public abstract class HelpScreenSuperClass extends GameScreen {
 //Viewports for super class
    private ScreenViewport mScreen;
    private LayerViewport mLayer;

    //Viewports for derive class
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;

    protected   AssetStore assetManager;

    private GameObject mBackground;
    private PushButton mBackIcon;
    private PushButton mNextIcon;
    private GameObject mHelpScreenHeader;
    private HelpScreenController mController;

    private PushButton startPlay;
    private PushButton home;
    private ArrayList<PushButton> icons;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param name
     * @param game Game instance to which the game screen belongs
     */
    public HelpScreenSuperClass(String name, Game game, HelpScreenController controller) {
        super(name, game);
        this.mController = controller;
        mScreen = new ScreenViewport(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());
        mLayer = new LayerViewport(0, 0, mGame.getScreenWidth() / 2, mGame.getScreenHeight() / 2);


        // Load in the assets used by the steering demo
         assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("HelpBackground", "img/Game Help/HelpBackground.png");
        // Create help screen background
        mBackground = new GameObject(mLayer.x, mLayer.y, mLayer.getWidth(), mLayer.getHeight(),
                getGame().getAssetManager().getBitmap("HelpBackground"), this);

        // Create the header
        assetManager.loadAndAddBitmap("Title", "img/Title.png");
        Bitmap title = mGame.getAssetManager().getBitmap("Title");
        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        float headerHeight = mLayer.getHeight() / 5;
        mHelpScreenHeader = new GameObject(mLayer.x, mLayer.getTop() - headerHeight / 2, mLayer.getWidth(), headerHeight,
                Bitmap.createBitmap(title, 0, 0, title.getWidth(), title.getHeight(), matrix, true), this);

        assetManager.loadAndAddBitmap(PopUp.POPUP_BITMAP_ID, PopUp.POPUP_BITMAP_PATH);


        mScreenViewport = new ScreenViewport(0, (int)headerHeight, mGame.getScreenWidth(), mGame.getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, mScreenViewport.width/2, mScreenViewport.height/2);
/*******************************************************************************************************/
//set up all icons
        icons = new ArrayList<>();
        final float iconHeight = mLayer.getHeight() / 5;
        final float iconWidth = mLayer.getWidth() / 10;

        //Create the home button
        assetManager.loadAndAddBitmap("HelpScreenHome", "img/Game Help/Home button.png");
        home = new PushButton(mLayer.getLeft() + iconWidth / 2, mLayer.getTop() - iconHeight / 2, iconWidth, iconHeight, "HelpScreenHome", this);
        icons.add(home);

        //Create the start play button
        assetManager.loadAndAddBitmap("HelpScreenStartPlay", "img/Game Help/Start play.png");
        startPlay = new PushButton(mLayer.getRight() - iconWidth / 2, mLayer.getTop() - iconHeight / 2, iconWidth, iconWidth, "HelpScreenStartPlay", this);
        icons.add(startPlay);

        // Create the back button
        assetManager.loadAndAddBitmap("BackIcon", "img/Game Help/LeftArrow.png");
        mBackIcon = new PushButton(mLayer.getLeft() + iconWidth / 2, mLayer.getBottom() + iconHeight / 2, iconWidth, iconHeight, "BackIcon", this);
        icons.add(mBackIcon);

        // Create the next button
        assetManager.loadAndAddBitmap("NextIcon", "img/Game Help/Right arrow.png");
        mNextIcon = new PushButton(mLayer.getRight() - iconWidth / 2, mLayer.getBottom() + iconHeight / 2, iconWidth, iconHeight, "NextIcon", this);
        icons.add(mNextIcon);

        for (PushButton each : icons) {
            each.processInLayerSpace(true);
        }


    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        for (PushButton each : icons) {
            each.update(elapsedTime, mLayer, mScreen);
        }

        if (mBackIcon.isPushTriggered()) {
            mController.previousScreen();
        }


        if (mNextIcon.isPushTriggered()) {
            mController.nextScreen();
        }
        if (home.isPushTriggered()) {
            mController.setUpNewScreen(new MenuScreen(mGame));
        }
        if (startPlay.isPushTriggered()) {
            mController.setUpNewScreen(new WorldScreen(mGame));
        }


        if (errorMessage != null) {
            errorMessage.update(elapsedTime);

        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the help screen background first of all
        mBackground.draw(elapsedTime, graphics2D, mLayer, mScreen);
        this.drawGameHelp(elapsedTime, graphics2D);
        this.drawInFront(elapsedTime, graphics2D);


    }

    private void drawInFront(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //Draw the help screen header
        mHelpScreenHeader.draw(elapsedTime, graphics2D, mLayer, mScreen);


        for (PushButton each : icons) {
            each.draw(elapsedTime, graphics2D, mLayer, mScreen);
        }


        if (errorMessage != null) {
            if (errorMessage.isVisible()) {
                errorMessage.draw(elapsedTime, graphics2D);
            }
        }
    }

    abstract void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    private PopUp errorMessage;

    public void setErrorMessage(String message, long duration) {
        this.errorMessage = new PopUp(message, duration, 72, mGame.getAssetManager().getBitmap(PopUp.POPUP_BITMAP_ID), this);
        // Display the popup
        errorMessage.show();

    }
}
