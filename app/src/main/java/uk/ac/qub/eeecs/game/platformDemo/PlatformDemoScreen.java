package uk.ac.qub.eeecs.game.platformDemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * A simple platform-style demo that generates a number of platforms and
 * provides a player controlled entity that can move about the images.
 * <p>
 * Illustrates both button based user input and collision handling.
 *
 * @version 1.0
 */
public class PlatformDemoScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the width and height of the game world
     */
    private final float LEVEL_WIDTH = 2000.0f;
    private final float LEVEL_HEIGHT = 320.0f;

    /**
     * Define the layer and screen view ports
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;

    /**
     * Create three simple touch controls for player input
     */
    private PushButton moveLeft, moveRight, jumpUp;
    private List<PushButton> mControls;

    /**
     * Define an array of sprites to populate the game world
     */
    private ArrayList<Platform> mPlatforms;

    /**
     * Define the player
     */
    private PlayerSphere mPlayer;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple platform game level
     *
     * @param game Game to which this screen belongs
     */
    public PlatformDemoScreen(Game game) {
        super("PlatformDemoScreen", game);

        // Create the view ports
        mLayerViewport = new LayerViewport(240, 160, 240, 160);
        mScreenViewport = new ScreenViewport();
        GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);

        // Load in the assets used by this layer
        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Platform1", "img/Platform1.png");
        assetManager.loadAndAddBitmap("Platform2", "img/Platform2.png");
        assetManager.loadAndAddBitmap("Ground", "img/Ground.png");
        assetManager.loadAndAddBitmap("Ball", "img/Ball.png");
        assetManager.loadAndAddBitmap("RightArrow", "img/RightArrow.png");
        assetManager.loadAndAddBitmap("LeftArrow", "img/LeftArrow.png");
        assetManager.loadAndAddBitmap("UpArrow", "img/UpArrow.png");
        assetManager.loadAndAddMusic("PlatformBackgroundMusic","music/background_music_pokemon.mp3");
        assetManager.loadAndAddSound("Bounce", "sound/bounce.mp3");

        //Plays the background music
        this.getGame().getAssetManager().getMusic("PlatformBackgroundMusic").setLopping(true);

        this.getGame().getAssetManager().getMusic("PlatformBackgroundMusic").play();

        // Determine the screen size to correctly position the touch buttons
        int screenWidth = game.getScreenWidth();
        int screenHeight = game.getScreenHeight();

        // Create and position the touch buttons
        mControls = new ArrayList<PushButton>();
        moveLeft = new PushButton(100.0f, (screenHeight - 100.0f),
                100.0f, 100.0f, "LeftArrow", this);
        mControls.add(moveLeft);
        moveRight = new PushButton(225.0f, (screenHeight - 100.0f),
                100.0f, 100.0f, "RightArrow", this);
        mControls.add(moveRight);
        jumpUp = new PushButton((screenWidth - 125.0f),
                (screenHeight - 100.0f), 100.0f, 100.0f, "UpArrow", this);
        mControls.add(jumpUp);

        // Create the player
        mPlayer = new PlayerSphere(100.0f, 100.0f, this);

        // Create the platforms
        mPlatforms = new ArrayList<Platform>();

        // Add a wide platform for the ground tile
        int groundTileWidth = 64, groundTileHeight = 35, groundTiles = 50;
        mPlatforms.add(
                new Platform(groundTileWidth * groundTiles / 2, groundTileHeight / 2,
                        groundTileWidth * groundTiles, groundTileHeight,
                        "Ground", groundTiles, 1, this));

        // Add a number of randomly positioned platforms. They are not added in
        // the first 300 units of the level to avoid overlap with the player.
        Random random = new Random();
        String platform;

        /*
        User Story 21 (2/2): Platform Variety
        Dewei
        Add "Platform2" and make them appeared randomly.
         */
        int platformWidth = 70, platformHeight = 70, nNumRandomPlatforms = 30;
        int platform2Width = 120, platform2Height = 70;
        int platform3Width = 70, platform3Height = 120;
        for (int idx = 0; idx < nNumRandomPlatforms; idx++) {
            if (idx % 3 == 0){
                mPlatforms.add(new Platform(
                        300.0f + (random.nextFloat() * LEVEL_WIDTH),
                        (random.nextFloat() * (LEVEL_HEIGHT - platformHeight)),
                        platformWidth, platformHeight,
                        "Platform", this));
            } else if(idx % 3 == 1) {
                mPlatforms.add(new Platform(
                        300.0f + (random.nextFloat() * LEVEL_WIDTH),
                        (random.nextFloat() * (LEVEL_HEIGHT - platform2Height)),
                        platform2Width, platform2Height,
                        "Platform2", this));
            } else if (idx % 3 == 2){ mPlatforms.add(new Platform(
                        300.0f + (random.nextFloat() * LEVEL_WIDTH),
                        (random.nextFloat() * (LEVEL_HEIGHT - platform3Height)),
                        platform3Width, platform3Height,
                        "Platform2", this));
            }
        }

    }


    // /////////////////////////////////////////////////////////////////////////
    // Update and Draw
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the platform demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Update the touch buttons checking for player input
        for (PushButton control : mControls)
            control.update(elapsedTime, mLayerViewport, mScreenViewport);

        // Update the player
        mPlayer.update(elapsedTime, moveLeft.isPushed(),
                moveRight.isPushed(), jumpUp.isPushed(), mPlatforms);

        //Plays the Bounce sound if jumpUp is triggered
        if(jumpUp.isPushTriggered()){
            this.getGame().getAssetManager().getSound("Bounce").play();
        }


        // Ensure the player cannot leave the confines of the world
        BoundingBox playerBound = mPlayer.getBound();
        if (playerBound.getLeft() < 0)
            mPlayer.position.x -= playerBound.getLeft();
        else if (playerBound.getRight() > LEVEL_WIDTH)
            mPlayer.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

        if (playerBound.getBottom() < 0)
            mPlayer.position.y -= playerBound.getBottom();
        else if (playerBound.getTop() > LEVEL_HEIGHT)
            mPlayer.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);

        // Focus the layer viewport on the player's x location
        mLayerViewport.x = mPlayer.position.x;

        // Ensure the viewport cannot leave the confines of the world
        if (mLayerViewport.getLeft() < 0)
            mLayerViewport.x -= mLayerViewport.getLeft();
        else if (mLayerViewport.getRight() > LEVEL_WIDTH)
            mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

        if (mLayerViewport.getBottom() < 0)
            mLayerViewport.y -= mLayerViewport.getBottom();
        else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
            mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);
    }

    /**
     * Draw the platform demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.WHITE);

        // Draw the player
        mPlayer.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Draw each of the platforms
        for (Platform platform : mPlatforms)
            platform.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        // Draw the controls last of all
        for (PushButton control : mControls)
            control.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }
}
