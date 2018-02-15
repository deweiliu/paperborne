package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
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

    private Hero player, opponent;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private final float LEVEL_WIDTH = 500.0f;
    private final float LEVEL_HEIGHT = 1000.0f;
    private GameObject mCardDemoScreen;

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
        float widthSteps = ((mGame.getScreenWidth())/24), heightSteps = (mGame.getScreenHeight())/16;
        float len = player.getHand().getCards().size();
        for(int i = 0; i < len; i++) {
            Card activeCard = player.getHand().getCards().get(i);
            Vector2 initPos = player.getHand().getCards().get(i).getPosition();

            if(i<len/2) {
                int modifier = 5 - i;
                Vector2 handPosition = new Vector2((initPos.x - widthSteps*modifier), heightSteps*6);
                activeCard.setAnchor(handPosition.x, handPosition.y);
                activeCard.setPosition(handPosition);
            }
            else {
                int modifier = i - 4;
                Vector2 handPosition = new Vector2((initPos.x + widthSteps*modifier), heightSteps*6);
                activeCard.setAnchor(handPosition.x, handPosition.y);
                activeCard.setPosition(handPosition);
            }
        }
        /*
         * HARDCODING TESTING
         * Plays 3 random cards from the player's deck
         */
        for(int i = 0; i < 3; i++)
        {
            player.refillMana();
            player.playCard(player.getDeck().drawCard());
        }
        // Put one card in the middle of the screen for testing
        Card playerCard = player.getActiveCards().get(0);
        Vector2 playerPosition = new Vector2(mScreenViewport.centerX(), mScreenViewport.centerY());
        playerCard.setPosition(playerPosition);
        playerCard.setAnchor(playerPosition.x, playerPosition.y);
        
        // Plays 1 random cards from the opponent's deck
        for(int i = 0; i < 1; i++)
        {
            opponent.refillMana();
            opponent.playCard(opponent.getDeck().drawCard());
        }
        // Put one card above the player's card
        Card opponentCard = opponent.getActiveCards().get(0);
        Vector2 opponentPosition = new Vector2(mScreenViewport.centerX(), mScreenViewport.centerY() + opponentCard.getBound().getHeight());
        opponentCard.setPosition(opponentPosition);
        opponentCard.setAnchor(opponentPosition.x, opponentPosition.y);
        /*
         * END HARDCODED TESTING
         */
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
        //Sets the layer viewport to the position of the background
        mLayerViewport.x=mScreenViewport.centerX();
        mLayerViewport.y=mScreenViewport.centerY();

        player.update(elapsedTime);
        opponent.update(elapsedTime);
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
        // Check for touchdown event
        boolean touchDown = false;
        for(TouchEvent touch : mGame.getInput().getTouchEvents())
        {
            if(touch.type == TouchEvent.TOUCH_DOWN)
            {
                touchDown = true;
            }
        }
        
        if(touchDown)
        {
            for (Card opponentCard : opponent.getActiveCards())
            {
                if(opponentCard.isCardIsActive())
                {
                    for (Card playerCard : player.getActiveCards())
                    {
                        if(playerCard.isCardIsActive())
                        {
                            opponentCard.takeDamage(playerCard.getAttackValue());
                            playerCard.setCardIsActive(false);
                            playerCard.setFinishedMove(true);
                            opponentCard.setCardIsActive(false);
                        }
                    }
                }
            }
            // If there has been a touchdown event, mark each player card on the board as inactive
            for (Card card : player.getActiveCards())
            {
                card.setCardIsActive(false);
                card.setFinishedMove(true);
            }
        }
        if(player.getActiveCards() != null)
        {
            // If the player has played cards
            for (Card card : player.getActiveCards())
            {
                card.update(elapsedTime);
            }
        }
        if(opponent.getActiveCards() != null)
        {
            // If the opponent has played cards
            for (Card card : opponent.getActiveCards())
            {
                card.update(elapsedTime);
            }
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
        
        if(player.getActiveCards() != null)
        {
            // If there are cards on the board played by the player
            for (Card card : player.getActiveCards())
            {
                // If any card is active
                if (card.isCardIsActive()) {
                    //Highlight the card if active on the board with a blue highlight
                    Paint paint = new Paint();
                    paint.setColorFilter(new LightingColorFilter(Color.BLUE, 0));
                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                } else {
                    // If the card isn't active, just draw it normally
                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                }
            }
        }
        opponent.draw(elapsedTime, graphics2D ,mLayerViewport, mScreenViewport);
        for(Card card : opponent.getHand().getCards()) card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        if(opponent.getActiveCards() != null) for(Card card : opponent.getActiveCards()) card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }


}
