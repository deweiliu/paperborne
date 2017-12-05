package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;


/**
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */


public class CardDemoScreen extends GameScreen {

    private Card mCards;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;



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
        assetManager.loadAndAddBitmap("Back", "img/Hearthstone_Card_Back.png");

        //Sets cards x,y position to the centre of the screen
        mCards = new Card(1, "Test Card", mScreenViewport.centerX(),mScreenViewport.centerY(),
                mGame.getAssetManager().getBitmap("Card"), this, 1, 1, 1);






    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Update the card
        mCards.update(elapsedTime);

        //Sets the layer vire port to the centre of the screen
        mLayerViewport.x=mScreenViewport.centerX();
        mLayerViewport.y=mScreenViewport.centerY();
    }




    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        Paint paint = new Paint(Color.BLACK);
        graphics2D.clear(Color.WHITE);
        mCards.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //Code for bounding the card within the players viewpoint
        /*
        BoundingBox cardBound = mCards.getBound();
        if (cardBound.getLeft() < 0)
            mCards.position.x -= cardBound.getLeft();
        else if (cardBound.getRight() > mScreenViewport.width)
            mCards.position.x -= (cardBound.getRight() - mScreenViewport.width);

        if (cardBound.getBottom() < 0)
            mCards.position.y -= cardBound.getBottom();
        else if (cardBound.getTop() > mScreenViewport.height)
            mCards.position.y -= (cardBound.getTop() - mScreenViewport.height);*/

    }


}
