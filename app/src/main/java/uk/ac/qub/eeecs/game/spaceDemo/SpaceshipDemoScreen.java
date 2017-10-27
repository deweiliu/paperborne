package uk.ac.qub.eeecs.game.spaceDemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.DemoGame;

/**
 * Simple steering game world
 *
 * @version 1.0
 */
public class SpaceshipDemoScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Width and height of the level
     */
    private final float LEVEL_WIDTH = 1000.0f;
    private final float LEVEL_HEIGHT = 1000.0f;

    /**
     * Define viewports for this layer and the associated screen projection
     */
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;

    /**
     * Define the background star scape
     */
    private GameObject mSpaceBackground;

    /**
     * Define the player's spaceship
     */
    private PlayerSpaceship mPlayerSpaceship;

    /**
     * Define asteroids in the game world
     */
    private final int NUM_ASTEROIDS = 20;
    private List<Asteroid> mAsteroids;

    /**
     * Define AI controlled ships in the game world
     */
    private final int NUM_SEEKERS = 5;
    private final int NUM_TURRETS = 5;
    private List<AISpaceship> mAISpaceships;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple steering game world
     *
     * @param game Game to which this screen belongs
     */
    public SpaceshipDemoScreen(Game game) {
        super("SpaceshipDemoScreen", game);

        // Create the screen viewport
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        // Create the layer viewport, taking into account the orientation
        // and aspect ratio of the screen.
        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.height
                    / mScreenViewport.width, 240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240);

        // Load in the assets used by the steering demo
        AssetStore assetManager = mGame.getAssetManager();

        assetManager.loadAndAddBitmap("SpaceBackground", "img/SpaceBackground.png");
        assetManager.loadAndAddBitmap("Asteroid1", "img/Asteroid1.png");
        assetManager.loadAndAddBitmap("Asteroid2", "img/Asteroid2.png");
        assetManager.loadAndAddBitmap("Spaceship1", "img/Spaceship1.png");
        assetManager.loadAndAddBitmap("Spaceship2", "img/Spaceship2.png");
        assetManager.loadAndAddBitmap("Spaceship3", "img/Spaceship3.png");
        assetManager.loadAndAddBitmap("Spaceship4", "img/User Story 5 Ship Image.png");
        assetManager.loadAndAddBitmap("Turret1", "img/Turret.png");
        assetManager.loadAndAddBitmap("Turret2", "img/User Story 5 Turret Image.png");

        assetManager.loadAndAddMusic("SpaceBackgroundMusic", "music/Halo.mp3");
        assetManager.getMusic("SpaceBackgroundMusic").play();
        assetManager.getMusic("SpaceBackgroundMusic").setLopping(true);

        // Create the space background
        mSpaceBackground = new GameObject(LEVEL_WIDTH / 2.0f,
                LEVEL_HEIGHT / 2.0f, LEVEL_WIDTH, LEVEL_HEIGHT, getGame()
                .getAssetManager().getBitmap("SpaceBackground"), this);

        // Create the player spaceship
        mPlayerSpaceship = new PlayerSpaceship(100, 100, this);

        // Create a number of randomly positioned asteroids
        Random random = new Random();
        mAsteroids = new ArrayList<Asteroid>(NUM_ASTEROIDS);
        for (int idx = 0; idx < NUM_ASTEROIDS; idx++)
            mAsteroids.add(new Asteroid(random.nextFloat() * LEVEL_WIDTH,
                    random.nextFloat() * LEVEL_HEIGHT, this));

        // Create a number of randomly positioned AI controlled ships
        mAISpaceships = new ArrayList<AISpaceship>(NUM_SEEKERS + NUM_TURRETS);
        for (int idx = 0; idx < NUM_SEEKERS; idx++)
            mAISpaceships.add(new AISpaceship(random.nextFloat() * LEVEL_WIDTH,
                    random.nextFloat() * LEVEL_HEIGHT,
                    AISpaceship.ShipBehaviour.Seeker, this));
        for (int idx = 0; idx < NUM_TURRETS; idx++)
            mAISpaceships.add(new AISpaceship(random.nextFloat() * LEVEL_WIDTH,
                    random.nextFloat() * LEVEL_HEIGHT,
                    AISpaceship.ShipBehaviour.Turret, this));
    }

    // /////////////////////////////////////////////////////////////////////////
    // Support methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Return the player spaceship
     *
     * @return Player spaceship
     */
    public PlayerSpaceship getPlayerSpaceship() {
        return mPlayerSpaceship;
    }

    /**
     * Return a list of the AI spaceships in the level
     *
     * @return List of AI controlled spaceships
     */
    public List<AISpaceship> getAISpaceships() {
        return mAISpaceships;
    }

    /**
     * Return a list of asteroids in the the level
     *
     * @return List of asteroids in the level
     */
    public List<Asteroid> getAsteroids() {
        return mAsteroids;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Update and Draw methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the spaceship demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Update the player spaceship
        mPlayerSpaceship.update(elapsedTime);

        // Ensure the player cannot leave the confines of the world
        BoundingBox playerBound = mPlayerSpaceship.getBound();
        if (playerBound.getLeft() < 0)
            mPlayerSpaceship.position.x -= playerBound.getLeft();
        else if (playerBound.getRight() > LEVEL_WIDTH)
            mPlayerSpaceship.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

        if (playerBound.getBottom() < 0)
            mPlayerSpaceship.position.y -= playerBound.getBottom();
        else if (playerBound.getTop() > LEVEL_HEIGHT)
            mPlayerSpaceship.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);

        //User Story 9
        // Ensure the AI cannot leave the confines of the world
        for (int i = 0; i < mAISpaceships.size(); i++) {
            BoundingBox aiBound = mAISpaceships.get(i).getBound();
            if (aiBound.getLeft() < 0)
                mAISpaceships.get(i).position.x -= aiBound.getLeft();
            else if (aiBound.getRight() > LEVEL_WIDTH)
                mAISpaceships.get(i).position.x -= (aiBound.getRight() - LEVEL_WIDTH);

            if (aiBound.getBottom() < 0)
                mAISpaceships.get(i).position.y -= aiBound.getBottom();
            else if (aiBound.getTop() > LEVEL_HEIGHT)
                mAISpaceships.get(i).position.y -= (aiBound.getTop() - LEVEL_HEIGHT);
        }

        //User Story 9
        //Collison between Usership and AI ships
        for (int i = 0; i < mAISpaceships.size(); i++) {
            //Prevent overlapping of playerShip and AIShips
            CollisionDetector.determineAndResolveCollision(mPlayerSpaceship, mAISpaceships.get(i));

        }
        //Collison between Usership and Astroids
        for (int i = 0; i < mAsteroids.size(); i++) {
            //Prevent overlapping of playerShip and Astroids
            CollisionDetector.determineAndResolveCollision(mPlayerSpaceship, mAsteroids.get(i));

        }

        //User Story 10
        //Collison between AI Ships and Astroids
        for (int i = 0; i < mAISpaceships.size(); i++) {
            for (int j = 0; j < mAsteroids.size(); j++) {
                //Prevent overlapping of AIShip and Astroids
                CollisionDetector.determineAndResolveCollision(mAISpaceships.get(i), mAsteroids.get(j));
            }
        }
        //Collison between AI Ships and AI ships
        for (int i = 0; i < mAISpaceships.size(); i++) {
            for (int j = 0; j < mAISpaceships.size(); j++) {
                //Prevent overlapping of AIShip and AIShips
                if (i != j) {
                    CollisionDetector.determineAndResolveCollision(mAISpaceships.get(i), mAISpaceships.get(j));
                }
            }
        }
        //Collison between Astroids and Astroids
        for (int i = 0; i < mAsteroids.size(); i++) {
            for (int j = 0; j < mAsteroids.size(); j++) {
                //Prevent overlapping of Astroids and Astroids
                if (i != j) {
                    CollisionDetector.determineAndResolveCollision(mAsteroids.get(i), mAsteroids.get(j));
                }
            }
        }


        // Focus the layer viewport on the player
        mLayerViewport.x = mPlayerSpaceship.position.x;
        mLayerViewport.y = mPlayerSpaceship.position.y;

        // Ensure the viewport cannot leave the confines of the world
        if (mLayerViewport.getLeft() < 0)
            mLayerViewport.x -= mLayerViewport.getLeft();
        else if (mLayerViewport.getRight() > LEVEL_WIDTH)
            mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

        if (mLayerViewport.getBottom() < 0)
            mLayerViewport.y -= mLayerViewport.getBottom();
        else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
            mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);

        // Update each of the AI controlled spaceships
        for (AISpaceship aiSpaceship : mAISpaceships)
            aiSpaceship.update(elapsedTime);

        // Update each of the asteroids
        for (Asteroid asteroid : mAsteroids)
            asteroid.update(elapsedTime);
    }

    /**
     * Draw the space ship demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Create the screen to black and define a clip based on the viewport
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        // Draw the background first of all
        mSpaceBackground.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);

        // Draw each of the asteroids
        for (Asteroid asteroid : mAsteroids)
            asteroid.draw(elapsedTime, graphics2D, mLayerViewport,
                    mScreenViewport);

        // Draw each of the AI controlled spaceships
        for (AISpaceship aiSpaceship : mAISpaceships)
            aiSpaceship.draw(elapsedTime, graphics2D, mLayerViewport,
                    mScreenViewport);

        // Draw the player
        mPlayerSpaceship.draw(elapsedTime, graphics2D, mLayerViewport,
                mScreenViewport);
    }
}