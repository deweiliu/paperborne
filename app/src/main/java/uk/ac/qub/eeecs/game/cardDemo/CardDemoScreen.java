package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.gage.ai.SteeringBehaviours;


/**
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */


public class CardDemoScreen extends GameScreen {

    private Hero player, opponent;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private final float LEVEL_WIDTH = 500.0f;
    private final float LEVEL_HEIGHT = 1000.0f;
    private GameObject mCardDemoScreen;
    private boolean playerTurn;
    private Timer timer;
    long startTime, turnTime;

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

        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());
        GraphicsHelper.create3To2AspectRatioScreenViewport(game, mScreenViewport);

        // Create the layer viewport, taking into account the orientation
        // and aspect ratio of the screen.
        if (mScreenViewport.width > mScreenViewport.height)
            mLayerViewport = new LayerViewport(480.0f, 480.0f
                    * mScreenViewport.height / mScreenViewport.width, 480,
                    480.0f * mScreenViewport.height / mScreenViewport.width);
        else
            mLayerViewport = new LayerViewport(480.0f * mScreenViewport.height
                    / mScreenViewport.width, 480.0f, 480.0f
                    * mScreenViewport.height / mScreenViewport.width, 480);


        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Card", "img/Hearthstone_Card_Template.png");
        assetManager.loadAndAddBitmap("Back", "img/Hearthstone_Card_Back.png");
        assetManager.loadAndAddBitmap("Board", "img/Board.png");
        assetManager.loadAndAddBitmap("Hero", "img/Hero_Base.png");
        assetManager.loadAndAddBitmap("Enemy", "img/Enemy_Base.png");


        mCardDemoScreen = new GameObject(mScreenViewport.centerX(),mScreenViewport.centerY(), LEVEL_WIDTH, LEVEL_HEIGHT/2.5f, getGame()
               .getAssetManager().getBitmap("Board"), this);


        player = new Hero(mGame.getScreenWidth()/2, (mGame.getScreenHeight()/16)*6, assetManager.getBitmap("Hero"), this, mGame);
        opponent = new Hero(mGame.getScreenWidth()/2, (mGame.getScreenHeight()/16)*10, assetManager.getBitmap("Enemy"), this, mGame);
        for(Card card : player.getHand().getCards()) {
            card.setPosition(mGame.getScreenWidth()/2, (mGame.getScreenHeight()/16)*7);
        }
        float len = player.getHand().getCards().size();
        float widthSteps = mGame.getScreenWidth()/(len+1), heightSteps = (mGame.getScreenHeight())/16;
        for(int i = 0; i < len; i++) {
            Card activeCard = player.getHand().getCards().get(i);
            activeCard.setPosition((widthSteps*(i+1)), heightSteps*7);
        }

        playerTurn = true;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                           @Override
                           public void run() {
                               playerTurn = !playerTurn;
                               startTime = System.currentTimeMillis();
                           }
                       }, 0, 30000);
        startTime = System.currentTimeMillis();


    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////
    //
    //
    //
    // ////////////////////////////////////////////////
    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        //Sets the layer viewport to the position of the background
        mLayerViewport.x=mScreenViewport.centerX();
        mLayerViewport.y=mScreenViewport.centerY();

        player.update(elapsedTime);
        opponent.update(elapsedTime);

        if(playerTurn) {
            //if(player.getActiveCards() != null) for(Card card : player.getActiveCards()) card.update(elapsedTime);
            for(Card card : player.getHand().getCards()) {
                card.update(elapsedTime);
                //place card in correct position, either on the board or back into the hand
                if (card.isFinishedMove()) {
                    if (card.position.x == 100 && card.position.y == 100) {

                    } else {
                        //SteeringBehaviours.seek(card, card.getLastPosition(), card.acceleration);
                    }
                }
            }
        } else {
            for(Card card : opponent.getHand().getCards()) card.update(elapsedTime);
        }

        turnTime = ((startTime + 30000) - System.currentTimeMillis())/1000;
        if(turnTime==0) {
            
        }

    }


    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        /*Paint paint = new Paint(Color.BLACK);
        graphics2D.clear(Color.WHITE);*/
        mCardDemoScreen.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        player.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //highly inconsistent for some unknown reason
        for(Card card : player.getHand().getCards()) {
            if (card.isCardIsActive()) {
                //Highlight the card if active
                Paint paint = new Paint();
                paint.setColorFilter(new LightingColorFilter(Color.GREEN, 0));
                card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
            } else {
                card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }
        if(player.getActiveCards() != null) for(Card card : player.getActiveCards()) card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        opponent.draw(elapsedTime, graphics2D ,mLayerViewport, mScreenViewport);
        for(Card card : opponent.getHand().getCards()) card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        if(opponent.getActiveCards() != null) for(Card card : opponent.getActiveCards()) card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        String turnRemaining = turnTime + " seconds left in current turn";
        String whoseTurn =  "Player turn: " + playerTurn;
        Paint paint = new Paint();
        paint.setTextSize(48);
        graphics2D.drawText(turnRemaining, getGame().getScreenWidth()/2, getGame().getScreenHeight()/2, paint);
        graphics2D.drawText(whoseTurn, getGame().getScreenWidth()/2, (getGame().getScreenHeight()/2)-50, paint);

    }


}
