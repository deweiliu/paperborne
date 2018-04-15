package uk.ac.qub.eeecs.game.gameHelp.helpScreens;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

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
import uk.ac.qub.eeecs.game.gameHelp.GameHelpController;
import uk.ac.qub.eeecs.game.ui.PopUp;
import uk.ac.qub.eeecs.game.worldScreen.WorldScreen;

/**
 * Created by 40216004 Dewei Liu on 28/03/2018.
 */

public abstract class HelpScreenSuperClass extends GameScreen {
    /*********************************************************************************************/
    //Fields for derived classes

    //Viewports for derived class
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Paint mPaint;
    protected AssetStore assetManager;

    /*********************************************************************************************/
    //Fields for superclass

    //Viewports for super class
    private ScreenViewport mScreen;
    private LayerViewport mLayer;
    private GameObject mBackground;
    private PushButton mBackIcon;
    private PushButton mNextIcon;
    private GameObject mHeader;
    private GameHelpController mController;
    private PushButton startPlay;
    private PushButton home;
    private ArrayList<PushButton> icons;
    private PopUp popUpMessage;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param name       the name of screen
     * @param game       Game instance to which the game screen belongs
     * @param controller the controller of help screen
     */
    public HelpScreenSuperClass(String name, Game game, GameHelpController controller) {
        super("Game help - " + name, game);
        this.mController = controller;
        //Set up viewports
        mScreen = new ScreenViewport(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());
        mLayer = new LayerViewport(0, 0, mGame.getScreenWidth() / 2, mGame.getScreenHeight() / 2);
        final float HEADER_HEIGHT = mLayer.getHeight() / 5;
        mScreenViewport = new ScreenViewport(0, (int) HEADER_HEIGHT, mGame.getScreenWidth(), mGame.getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, mScreenViewport.width / 2, mScreenViewport.height / 2);

        //Set up paint
        mPaint = new Paint();
        mPaint.setTextSize(mGame.getScreenWidth() / 45);
        mPaint.setColor(Color.WHITE);

        assetManager = mGame.getAssetManager();
        final String DIR = "img/Game Help/";
        // Create help screen background
        final String BACKGROUND_NAME = "Help Screen Background";
        assetManager.loadAndAddBitmap(BACKGROUND_NAME, DIR + "HelpBackground.png");
        mBackground = new GameObject(mLayer.x, mLayer.y, mLayer.getWidth(), mLayer.getHeight(),
                assetManager.getBitmap(BACKGROUND_NAME), this);

        // Create the header
        final String TITLE_NAME = "Help Screen Title";
        assetManager.loadAndAddBitmap(TITLE_NAME, DIR + "Title.png");
        mHeader = new GameObject(mLayer.x, mLayer.getTop() - HEADER_HEIGHT / 2, mLayer.getWidth(), HEADER_HEIGHT,
                assetManager.getBitmap(TITLE_NAME), this);

        assetManager.loadAndAddBitmap(PopUp.POPUP_BITMAP_ID, PopUp.POPUP_BITMAP_PATH);

        /*******************************************************************************************************/
        //set up all push buttons
        icons = new ArrayList<>();
        final float ICON_HEIGHT = mLayer.getHeight() / 5;
        final float ICON_WIDTH = mLayer.getWidth() / 10;

        //Create the home button
        assetManager.loadAndAddBitmap("HelpScreenHome", DIR + "Home button.png");
        home = new PushButton(mLayer.getLeft() + ICON_WIDTH / 2, mLayer.getTop() - ICON_HEIGHT / 2,
                ICON_WIDTH, ICON_HEIGHT, "HelpScreenHome", this);
        icons.add(home);

        //Create the start play button
        assetManager.loadAndAddBitmap("HelpScreenStartPlay", DIR + "Start play.png");
        startPlay = new PushButton(mLayer.getRight() - ICON_WIDTH / 2, mLayer.getTop() - ICON_HEIGHT / 2,
                ICON_WIDTH, ICON_WIDTH, "HelpScreenStartPlay", this);
        icons.add(startPlay);

        // Create the back button
        assetManager.loadAndAddBitmap("BackIcon", DIR + "LeftArrow.png");
        mBackIcon = new PushButton(mLayer.getLeft() + ICON_WIDTH / 2, mLayer.getBottom() + ICON_HEIGHT / 2,
                ICON_WIDTH, ICON_HEIGHT, "BackIcon", this);
        icons.add(mBackIcon);

        // Create the next button
        assetManager.loadAndAddBitmap("NextIcon", DIR + "Right arrow.png");
        mNextIcon = new PushButton(mLayer.getRight() - ICON_WIDTH / 2, mLayer.getBottom() + ICON_HEIGHT / 2,
                ICON_WIDTH, ICON_HEIGHT, "NextIcon", this);
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
        mBackground.update(elapsedTime);
        mHeader.update(elapsedTime);

        if (mBackIcon.isPushTriggered()) {
            if (!mController.previousScreen()) {
                this.setPopUpMessage("Hey, this is the first page.", 2);
            }
        } else if (mNextIcon.isPushTriggered()) {
            if (!mController.nextScreen()) {
                this.setPopUpMessage("Hey, this is the last page.", 2);
            }
        } else if (home.isPushTriggered()) {
            mController.setUpNewScreen(new MenuScreen(mGame));
        } else if (startPlay.isPushTriggered()) {
            mController.setUpNewScreen(new WorldScreen(mGame));
        }

        if (popUpMessage != null) {
            popUpMessage.update(elapsedTime);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Draw in the back
        graphics2D.clear(Color.WHITE);
        mBackground.draw(elapsedTime, graphics2D, mLayer, mScreen);

        //Draw in the middle
        this.drawGameHelp(elapsedTime, graphics2D);
        this.drawInFront(elapsedTime, graphics2D);
    }

    private void drawInFront(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        mHeader.draw(elapsedTime, graphics2D, mLayer, mScreen);
        for (PushButton each : icons) {
            each.draw(elapsedTime, graphics2D, mLayer, mScreen);
        }
        if (popUpMessage != null) {
            if (popUpMessage.isVisible()) {
                popUpMessage.draw(elapsedTime, graphics2D);
            }
        }
    }

    abstract protected void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    /**
     * Use default bitmap for pop up message
     *
     * @param message  the message you want to display
     * @param duration the period you the pop up message
     */
    public void setPopUpMessage(String message, long duration) {
        this.setPopUpMessage(message, duration, mGame.getAssetManager().getBitmap(PopUp.POPUP_BITMAP_ID));
    }

    /**
     * Use personalise bitmap for pop up message
     *
     * @param message  the message you want to display
     * @param duration the period you the pop up message
     * @param bitmap   the bitmap you want to use
     */
    public void setPopUpMessage(String message, long duration, Bitmap bitmap) {
        this.popUpMessage = new PopUp(message, duration, mGame.getScreenWidth() / 40, bitmap, this);
        // Display the popup
        popUpMessage.show();
    }

    public PopUp getPopUpMessage() {
        return popUpMessage;
    }

    public void reset() {
    }
}
