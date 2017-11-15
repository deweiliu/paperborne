package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */


public class CardDemoScreen extends GameScreen {

    private Card mCards;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private final float LEVEL_WIDTH = 600.0f;
    private final float LEVEL_HEIGHT = 600.0f;
    private Vector2 playerTouchAcceleration = new Vector2();
    private Vector2 screenCentre = new Vector2();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public CardDemoScreen(Game game) {
        super("CardScreen", game);

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


        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Card", "img/Hearthstone_Card_Template.png");
        mCards = new Card(250,150,this);






    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    public Card getCards() {
        return mCards;
    }
    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Update the card
        mCards.update(elapsedTime);

        // Ensure the card cannot leave the confines of the world
        BoundingBox playerBound = mCards.getBound();
        if (playerBound.getLeft() < 0)
            mCards.position.x -= playerBound.getLeft();
        else if (playerBound.getRight() > LEVEL_WIDTH)
            mCards.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

        if (playerBound.getBottom() < 0)
            mCards.position.y -= playerBound.getBottom();
        else if (playerBound.getTop() > LEVEL_HEIGHT)
            mCards.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);
        

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
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        mCards.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);


    }

}
