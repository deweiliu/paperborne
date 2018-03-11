package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.VerticalSlider;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;


/**
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */


public class CardDemoScreen extends GameScreen {

    private Hero player, opponent;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private GameObject BoardBackground;
    private PushButton mEndTurnButton;
    private final float LEVEL_WIDTH = 1000.0f;
    private final float LEVEL_HEIGHT = 1000.0f;
    private boolean playerTurn;
    private Handler turnHandler;
    private Runnable endTurn;
    long startTime, turnTime;

    final private float SLIDER_WIDTH = 175f;
    final private float SLIDER_HEIGHT = 450f;
    private VerticalSlider manaSlider;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public CardDemoScreen(Game game, List<LevelCard> opponentDeck, List<LevelCard> playerDeck) {
        super("CardScreen", game);

        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

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
        assetManager.loadAndAddBitmap("Board", "img/Paper Board.JPG");
        assetManager.loadAndAddBitmap("Hero", "img/Hero Knight.JPG");
        assetManager.loadAndAddBitmap("Enemy", "img/Hero Dragon.JPG");
        assetManager.loadAndAddBitmap("EndTurn", "img/End_Turn.png");

        BoardBackground = new GameObject(mLayerViewport.getWidth() / 2f, mLayerViewport.getHeight() / 2f,
                mLayerViewport.getWidth(),
                mLayerViewport.getHeight(),
                getGame()
                        .getAssetManager().getBitmap("Board"), this);

        mEndTurnButton = new PushButton(
                mLayerViewport.getWidth() * 3.5f, mLayerViewport.getHeight() * 2, 200, 100, "EndTurn", this);

        player = new Hero(mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() / 6f - 2f, assetManager.getBitmap("Hero"), this, mGame);
        opponent = new Hero(mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() - mLayerViewport.getHeight() / 5f, assetManager.getBitmap("Enemy"), this, mGame);
        if (opponentDeck.isEmpty()) {
            // If the supplied opponent deck is empty
            // Set up the opponent with default deck
            opponent = new Hero(mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() - mLayerViewport.getHeight() / 5f,
                    assetManager.getBitmap("Enemy"),
                    this,
                    mGame
            );
        } else {
            // If an opponent deck has been supplied
            // Set up the opponent with the deck supplied
            opponent = new Hero(mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() - mLayerViewport.getHeight() / 5f,
                    assetManager.getBitmap("Enemy"),
                    this,
                    mGame,
                    opponentDeck
            );
        }
        if (playerDeck.isEmpty()) {
            player = new Hero(
                    mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() / 6f - 2f,
                    assetManager.getBitmap("Hero"),
                    this,
                    mGame
            );
        } else {
            player = new Hero(
                    mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() / 6f - 2f,
                    assetManager.getBitmap("Hero"),
                    this,
                    mGame,
                    playerDeck
            );
        }

        for (Card card : player.getHand().getCards()) {
            card.setPosition(mLayerViewport.getWidth() / 2f, mLayerViewport.getHeight() / 4f);
        }


        arrangeCards();

        playerTurn = true;

        endTurn = new Runnable() {
            @Override
            public void run() {

                player.clearDeadCards();
                opponent.clearDeadCards();
                for (Card card : player.getActiveCards())
                    card.setFinishedMove(false);
                player.refillMana();
                opponent.refillMana();

                if (!playerTurn) { //last turn was opponents and we haven't changed it to the player's - it's player turn to draw
                    if (!player.getDeck().isDeckEmpty() && player.getHand().getCards().size() < player.getHand().getMaxHandSize())
                        player.getHand().getCards().add(player.getDeck().drawCard());
                    else
                        player.takeDamage(1);
                } else { //it's end of player's turn and opponent's turn to draw
                    if (!opponent.getDeck().isDeckEmpty() && opponent.getHand().getCards().size() < opponent.getHand().getMaxHandSize())
                        opponent.getHand().getCards().add(opponent.getDeck().drawCard());
                    else
                        opponent.takeDamage(1);
                }

                arrangeCards();
                playerTurn = !playerTurn;
                turnHandler.postDelayed(this, 30000);
                startTime = System.currentTimeMillis();
            }
        };

        turnHandler = new Handler(Looper.getMainLooper());
        turnHandler.postDelayed(endTurn, 30000);

        startTime = System.currentTimeMillis();


        assetManager.loadAndAddBitmap("SliderBase", "img/SliderBase.png");
        assetManager.loadAndAddBitmap("SliderFill", "img/SliderFill.png");
        assetManager.loadAndAddBitmap("VerticalSliderFill", "img/VerticalSliderFill.png");

        // Set up text painter with styles
        Paint sliderPainter = new Paint();
        sliderPainter.setTextSize(60);
        sliderPainter.setColor(Color.BLACK);
        sliderPainter.setTextAlign(Paint.Align.CENTER);

        manaSlider = new VerticalSlider(0, 10, player.getCurrentMana(), sliderPainter,
                game.getScreenWidth() - 240f, game.getScreenHeight() - 230f, SLIDER_WIDTH, SLIDER_HEIGHT,
                "SliderBase", "VerticalSliderFill", this, false);
        /////////////////////////////
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

        turnTime = ((startTime + 30000) - System.currentTimeMillis()) / 1000;

        player.update(elapsedTime);
        opponent.update(elapsedTime);
        manaSlider.update(elapsedTime);


                        // Check for a touch down event, if one is found deselect all the cards
                        //only allow interaction if it's player's turn

                        if (!playerTurn) return;

                        // Check for touchdown event
                        boolean touchDown = false;
                        Input input = mGame.getInput();
                        for (TouchEvent touch : input.getTouchEvents()) {
                            if (touch.type == TouchEvent.TOUCH_DOWN) {
                                touchDown = true;
                            }
                        }

                        // If there has been a touch down
                        if (touchDown) {
                            mEndTurnButton.update(elapsedTime);
                            if (mEndTurnButton.isPushTriggered()) {
                                turnHandler.removeCallbacks(endTurn);
                                endTurn.run();
                                return;
                            }
                            // Go through each opponent card on the board
                            for (Card opponentCard : opponent.getActiveCards()) {
                                opponentCard.update(elapsedTime, mScreenViewport, mLayerViewport, opponent);

                                // If the opponent card has been tapped
                                if (opponentCard.isCardIsActive()) {
                                    // Check for any selected player cards on the board
                                    for (Card playerCard : player.getActiveCards()) {
                                        if (playerCard.isCardIsActive() && !playerCard.isFinishedMove()) {
                                            // If there is a selected player card that hasn't finished it's move
                                            // attack the tapped opponent card
                                            opponentCard.takeDamage(playerCard.getAttackValue());
                                            // Deselect player card and mark it as finished its move
                                            playerCard.setCardIsActive(false);
                                            playerCard.setFinishedMove(true);
                                            // Mark the opponent card as no longer tapped
                                            opponentCard.setCardIsActive(false);
                                        }
                                    }
                                }
                            }
                            // If there has been a touchdown event, mark each player card on the board as inactive
                            for (Card card : player.getActiveCards()) {
                                // Mark each player card on the board as deselected
                                card.setCardIsActive(false);
                            }
                        }
                        if (!player.getActiveCards().isEmpty()) {
                            // If the player has played cards
                            for (Card card : player.getActiveCards()) {
                                card.update(elapsedTime, mScreenViewport, mLayerViewport, player);
                            }
                        }
                        if (!player.getHand().getCards().isEmpty()) {
                            for (int i = 0; i < player.getHand().getCards().size(); i++) {
                                player.getHand().getCards().get(i).update(elapsedTime, mScreenViewport, mLayerViewport, player);
                            }

            /*TODO - breaks because we can't remove a card from hand and iterate over foreach, which happens when we play a card
            for (Card card : player.getHand().getCards()) {
                card.update(elapsedTime, mScreenViewport, mLayerViewport, player);
            }*/
                        }
                        if (!opponent.getActiveCards().isEmpty()) {
                            // If the opponent has played cards
                            for (Card card : opponent.getActiveCards()) {
                                card.update(elapsedTime, mScreenViewport, mLayerViewport, opponent);
                            }
                        }
                        if (!opponent.getHand().getCards().isEmpty()) {
                            for (Card card : opponent.getHand().getCards()) {
                                card.update(elapsedTime, mScreenViewport, mLayerViewport, opponent);
                            }
                        }
                        input.getTouchEvents().clear();
                    }

                    /**
                     * Draw the card demo screen
                     *
                     * @param elapsedTime Elapsed time information
                     * @param graphics2D  Graphics instance
                     */
                    @Override
                    public void draw (ElapsedTime elapsedTime, IGraphics2D graphics2D){
        /*Paint paint = new Paint(Color.BLACK);
        graphics2D.clear(Color.WHITE);*/
                        BoardBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

                        player.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                        manaSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                        //highly inconsistent for some unknown reason


                        for (Card card : player.getHand().getCards()) {
                            if (card.isCardIsActive()) {
                                //Highlight the card if active
                                Paint paint = new Paint();
                                paint.setColorFilter(new LightingColorFilter(Color.GREEN, 0));
                                card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                            } else {
                                card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                            }
                        }

                        if (player.getActiveCards() != null) {
                            // If there are cards on the board played by the player
                            for (Card card : player.getActiveCards()) {
                                // If any card is active
                                Paint paint = new Paint();
                                if (card.isCardIsActive()) {
                                    //Highlight the card if active on the board with a blue highlight
                                    paint.setColorFilter(new LightingColorFilter(Color.BLUE, 0));
                                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                                } else if (card.isFinishedMove()) {
                                    //Highlight the card with a gray tint if the car has been played
                                    paint.setColorFilter(new LightingColorFilter(Color.GRAY, 0));
                                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                                } else {
                                    // If the card isn't active, just draw it normally
                                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                                }
                            }
                        }
                        opponent.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

                        for (Card card : opponent.getHand().getCards())
                            card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                        if (opponent.getActiveCards() != null)
                            for (Card card : opponent.getActiveCards()) {
                                if (card.getCardIsDead()) {
                                    Paint paint = new Paint();
                                    paint.setColorFilter(new LightingColorFilter(Color.RED, 0));
                                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                                } else
                                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                            }

                        mEndTurnButton.draw(elapsedTime, graphics2D);

                        String turnRemaining = turnTime + " seconds left in current turn";
                        String whoseTurn = "Player turn: " + playerTurn;
                        String canInteract = "Player " + (playerTurn ? "can" : "cannot") + " interact";
                        Paint paint = new Paint();
                        paint.setTextSize(48);
                        paint.setARGB(255, 255, 255, 255);
                        paint.setShadowLayer(5.0f, 2.0f, 2.0f, Color.BLACK);
                        graphics2D.drawText(turnRemaining, mLayerViewport.getWidth() / 8, (mLayerViewport.getHeight() / 2) - 100, paint);
                        graphics2D.drawText(whoseTurn, mLayerViewport.getWidth() / 8, (mLayerViewport.getHeight() / 2) - 50, paint);
                        graphics2D.drawText(canInteract, mLayerViewport.getWidth() / 8, mLayerViewport.getHeight() / 2, paint);
                    }

                public void arrangeCards () {
                    float len = player.getHand().getCards().size();
                    float widthSteps = (mLayerViewport.getWidth()/(len+1)) / 1.4f, heightSteps = mLayerViewport.getHeight()/30;
                    for(int i = 0; i < len; i++) {
                        Card activeCard = player.getHand().getCards().get(i);
                        Vector2 handPosition = new Vector2((widthSteps*(i+1)), heightSteps*6);
                        activeCard.setAnchor(handPosition.x + 70f, handPosition.y);
                        activeCard.setPosition(handPosition);
                    }

                    len = player.getActiveCards().size();
                    widthSteps = (mLayerViewport.getWidth() / 2) / (len + 1);
                    float offset = mLayerViewport.getWidth() / 4;
                    for (int i = 0; i < len; i++) {
                        Card activeCard = player.getActiveCards().get(i);
                        Vector2 handPosition = new Vector2((widthSteps * (i + 1)) + offset, mLayerViewport.getHeight() / 2);
                        activeCard.setAnchor(handPosition.x, handPosition.y);
                        activeCard.setPosition(handPosition);
                    }
                }
            }

/*  float len = player.getHand().getCards().size();
        float widthSteps = (mLayerViewport.getWidth()/(len+1)) / 1.4f, heightSteps = mLayerViewport.getHeight()/30;
        for(int i = 0; i < len; i++) {
            Card activeCard = player.getHand().getCards().get(i);
            Vector2 handPosition = new Vector2((widthSteps*(i+1)), heightSteps*6);
            activeCard.setAnchor(handPosition.x + 70f, handPosition.y);
            activeCard.setPosition(handPosition);
        }
        */