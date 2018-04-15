package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.VerticalSlider;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIController;
import uk.ac.qub.eeecs.game.cardDemo.ai_Algorithm.AIDecision;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.options.OptionsManager;
import uk.ac.qub.eeecs.game.worldScreen.Level;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;


/**
 * Starter class for Card game stories in the 2nd sprint
 *
 * @version 1.0
 */


public class CardDemoScreen extends GameScreen {

    private static final int DEFAULT_MUSIC_VOLUME = 10;

    private Hero player;
    private Hero opponent;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private GameObject BoardBackground;
    private VerticalSlider manaSlider;

    private AIController aiOpponent;
    private boolean startedThinking;
    private TurnController turnController;
    private Runnable endTurn;

    private Level level;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for creating game screen without level details, mainly for testing
     *
     * @param game Game to which this screen belongs
     */
    public CardDemoScreen(Game game) {
        this(game, new ArrayList<LevelCard>(), new ArrayList<LevelCard>(), null);
    }

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public CardDemoScreen(Game game, List<LevelCard> opponentDeck, List<LevelCard> playerDeck, Level level) {
        super("CardScreen", game);
        OptionsManager mOptionsManager = new OptionsManager(game.getContext());
        this.level = level;

        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(),
                game.getScreenHeight());

        if (mScreenViewport.width > mScreenViewport.height) {
            mLayerViewport = new LayerViewport(240.0f, 240.0f
                    * mScreenViewport.height / mScreenViewport.width, 240.0f,
                    240.0f * mScreenViewport.height / mScreenViewport.width);
        } else {
            mLayerViewport = new LayerViewport(240.0f * mScreenViewport.width
                    / mScreenViewport.height, 240.0f, 240.0f
                    * mScreenViewport.width / mScreenViewport.height, 240.0f);
        }


        AssetStore assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Card", "img/Hearthstone_Card_Template.png");
        assetManager.loadAndAddBitmap("Back", "img/Cards/Card Back.jpg");
        assetManager.loadAndAddBitmap("Board", "img/Board/Paper Board.jpg");
        assetManager.loadAndAddBitmap("Hero", "img/Hero/Knight Hero.JPG");
        assetManager.loadAndAddBitmap("Enemy", "img/Hero/Dragon Hero.JPG");

        BoardBackground = new GameObject(mLayerViewport.getWidth() / 2f, mLayerViewport.getHeight() / 2f,
                mLayerViewport.getWidth(),
                mLayerViewport.getHeight(),
                getGame().getAssetManager().getBitmap("Board"), this);

        player = new Hero(mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() / 6f - 2f, assetManager.getBitmap("Hero"), this, mGame);
        opponent = new Hero(mLayerViewport.getWidth() / 8f - 8.0f, mLayerViewport.getHeight() - mLayerViewport.getHeight() / 5f, assetManager.getBitmap("Enemy"), this, mGame);
        if (opponentDeck.isEmpty()) {
            // If the supplied opponent deck is empty
            // Set up the opponent with default deck
            opponent = new Hero(mLayerViewport.getWidth() / 8f - 25f, mLayerViewport.getHeight() - mLayerViewport.getHeight() / 5f,
                    assetManager.getBitmap("Enemy"),
                    this,
                    mGame
            );
        } else {
            // If an opponent deck has been supplied
            // Set up the opponent with the deck supplied
            opponent = new Hero(mLayerViewport.getWidth() / 8f - 25f, mLayerViewport.getHeight() - mLayerViewport.getHeight() / 5f,
                    assetManager.getBitmap("Enemy"),
                    this,
                    mGame,
                    opponentDeck
            );
        }
        if (playerDeck.isEmpty()) {
            player = new Hero(
                    mLayerViewport.getWidth() / 8f - 25f, mLayerViewport.getHeight() / 6f - 2f,
                    assetManager.getBitmap("Hero"),
                    this,
                    mGame
            );
        } else {
            player = new Hero(
                    mLayerViewport.getWidth() / 8f - 25f, mLayerViewport.getHeight() / 6f - 2f,
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

        endTurn = new Runnable() {
            @Override
            public void run() {

                player.clearDeadCards();
                opponent.clearDeadCards();
                for (Card card : player.getActiveCards()) { //reset card movement states and snap to anchor positions
                    card.setFinishedMove(false);
                    card.setCardIsActive(false);
                    card.acceleration.set(Vector2.Zero);
                    card.velocity.set(Vector2.Zero);
                    card.position.set(card.getAnchor());
                }
                for (Card card : opponent.getActiveCards()) {
                    card.setFinishedMove(false);
                    card.setCardIsActive(false);
                    card.acceleration.set(Vector2.Zero);
                    card.velocity.set(Vector2.Zero);
                    card.position.set(card.getAnchor());
                }
                for (Card card : player.getHand().getCards()) { //do it for hand too - if player tries to play a card and it is denied, snap back to hand
                    card.acceleration.set(Vector2.Zero);
                    card.velocity.set(Vector2.Zero);
                    card.position.set(card.getAnchor());
                }


                if (!turnController.isPlayerTurn()) { //last turn was opponents and we haven't changed it to the player's - it's player turn to draw

                    //When it's the start of the player's turn, Increment mana by 1, refill mana, and change mana slider to that value
                    player.incrementManaLimit();
                    player.refillMana();
                    manaSlider.setVal(player.getCurrentMana());


                    if (!player.getDeck().isDeckEmpty() && player.getHand().getCards().size() < player.getHand().getMaxHandSize()) {
                        player.getHand().getCards().add(player.getDeck().drawCard());
                    } else {
                        player.takeDamage(1);
                    }
                } else { //it's end of player's turn and opponent's turn to draw

                    //increments opponents mana and refill opponents mana on the start of their turn
                    opponent.incrementManaLimit();
                    opponent.refillMana();

                    if (!opponent.getDeck().isDeckEmpty() && opponent.getHand().getCards().size() < opponent.getHand().getMaxHandSize()) {
                        opponent.getHand().getCards().add(opponent.getDeck().drawCard());
                    } else {
                        opponent.takeDamage(1);
                    }
                }

                arrangeCards();
                turnController.switchTurn();
            }
        };

        turnController = new TurnController(this);

        assetManager.loadAndAddBitmap("SliderBase", "img/SliderBase.png");
        assetManager.loadAndAddBitmap("SliderFill", "img/SliderFill.png");
        assetManager.loadAndAddBitmap("VerticalSliderFill", "img/Board/VerticalSliderFill.png");
        assetManager.loadAndAddMusic("BattleMusic", "music/BattleMusic.mp3");

        Music battleMusic = assetManager.getMusic("BattleMusic");
        battleMusic.setLopping(true);

        battleMusic.setVolume(mOptionsManager.getIntOption(OptionsManager.MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME) / (float) 10);
        if (!mOptionsManager.getBoolOption(OptionsManager.MUSIC_MUTED, false)) {
            battleMusic.play();
        }

        // Set up text painter with styles
        Paint sliderPainter = new Paint();
        sliderPainter.setTextSize(mGame.getScreenWidth() / 36);
        sliderPainter.setColor(Color.BLACK);
        sliderPainter.setTextAlign(Paint.Align.CENTER);

        //creates new vertical slider for the players mana
        float SLIDER_WIDTH = game.getScreenWidth() / 16;
        float SLIDER_HEIGHT = game.getScreenHeight() / 3.2f;
        manaSlider = new VerticalSlider(0, 10, player.getCurrentMana(), sliderPainter,
                game.getScreenWidth() - 135f, game.getScreenHeight() - 230f, SLIDER_WIDTH, SLIDER_HEIGHT,
                "SliderBase", "VerticalSliderFill", this, false);
        /////////////////////////////
        aiOpponent = new AIController(player, opponent);
        startedThinking = false;
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
        //If some one has died, end the game
        if (player.getHeroIsDead()) {
            new EndGameController(this, true, false, level);
        } else {
            if (opponent.getHeroIsDead()) {
                new EndGameController(this, true, true, level);
            }
        }
        //Else continue to play the game

        player.update(elapsedTime);
        opponent.update(elapsedTime);

        //Updates mana slider each time a card is played
        manaSlider.setVal(player.getCurrentMana());
        manaSlider.update(elapsedTime);

        // Check for touchdown event if it's the player's turn
        boolean touchDown = false;
        Input input = mGame.getInput();
        if (turnController.isPlayerTurn()) {
            for (TouchEvent touch : input.getTouchEvents()) {
                if (touch.type == TouchEvent.TOUCH_DOWN) {
                    touchDown = true;
                }
            }
        }
        turnController.update(elapsedTime);
        if (turnController.isEndTurnButtonPushed()) {
            return;
        }
        // If there has been a touch down
        if (touchDown) {
            // Go through each opponent card on the board
            for (Card opponentCard : opponent.getActiveCards()) {
                opponentCard.update(elapsedTime, mScreenViewport, mLayerViewport, opponent);

                // If the opponent card has been tapped
                if (opponentCard.isCardIsActive()) {
                    // Check for any selected player cards on the board
                    for (Card playerCard : player.getActiveCards()) {
                        if (playerCard.isCardIsActive() && !playerCard.isFinishedMove() && !opponentCard.getCardIsDead()) {
                            // If there is a selected player card that hasn't finished it's move and and isn't dead
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
            opponent.update(elapsedTime, mScreenViewport, mLayerViewport);
            if (opponent.getHeroTouched()) {
                for (Card playerCard : player.getActiveCards()) {
                    if (playerCard.isCardIsActive() && !playerCard.isFinishedMove()) {
                        //If there is a selected card that hasn't finished their move
                        // attack the hero
                        opponent.takeDamage(playerCard.getAttackValue());
                        //Deselect the player card and mark it as finished its move
                        playerCard.setCardIsActive(false);
                        playerCard.setFinishedMove(true);
                        //mark the opponent hero as no longer touched
                        opponent.setHeroTouched(false);
                    }
                }
            }

            // If there has been a touchdown event, mark each player card on the board as inactive
            for (Card card : player.getActiveCards()) {
                // Mark each player card on the board as deselected
                card.setCardIsActive(false);
            }
        }

        if (turnController.isPlayerTurn()) { //don't allow interaction if it's not their turn
            if (!player.getActiveCards().isEmpty()) {
                // If the player has played cards
                for (Card card : player.getActiveCards()) {
                    card.update(elapsedTime, mScreenViewport, mLayerViewport, player);
                }
            }
            if (!player.getHand().getCards().isEmpty()) {
                for (int i = 0; i < player.getHand().getCards().size(); i++) { //not great since it'll check the size every loop, but required to avoid concurrent access error
                    player.getHand().getCards().get(i).update(elapsedTime, mScreenViewport, mLayerViewport, player);
                }
            }
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

        //only allow interaction if it's player's turn
        if (turnController.isPlayerTurn()) {
            if (startedThinking) {
                // If the AI has been told to start thinking and it is now the player's turn
                if (!aiOpponent.isFinished()) {
                    // Inform the AI to stop thinking as it has run out of time
                    aiOpponent.notifyOverTime();
                    startedThinking = false;
                }
            }
            return;
        }
        // If it's the opponent turn
        if (startedThinking) {
            // If the AI has been told to started thinking
            if (aiOpponent.isFinished()) {
                // If the AI has finished thinking
                startedThinking = false;
                // Get the AI action
                AIDecision action = aiOpponent.getDecision();
                // Flag to check if the decision is to end turn
                boolean ended = false;
                // Decide what action to implement
                switch (action.getAction()) {
                    case AIDecision.END_TURN: {
                        // End turn
                        turnController.doEndTurn();
                        // Decision is to end turn so update flag
                        ended = true;
                        break;
                    }
                    case AIDecision.ATTACK_HERO: {
                        // Attack hero
                        player.takeDamage(action.getAttackerCard().getAttackValue());
                        action.getAttackerCard().setFinishedMove(true);
                        break;
                    }
                    case AIDecision.ATTACK_ACTIVE_CARD: {
                        // Attack active card
                        action.getTargetCard().takeDamage(action.getSourceCard().getAttackValue());
                        action.getSourceCard().setFinishedMove(true);
                        break;
                    }
                    case AIDecision.PLAY_CARD: {
                        // Play Card
                        Card card = action.getCardPlayed();
                        card.position.set(mLayerViewport.halfWidth, mLayerViewport.getTop());
                        opponent.playCard(card);
                        break;
                    }
                }
                if (!ended) {
                    // If the action wasn't end turn, start thinking again
                    aiOpponent.startThinking(TurnController.getTurnTime() / 9);
                    startedThinking = true;
                }
            }
        } else {
            // If the AI hasn't been told to start thinking, ask it to start thinking
            aiOpponent.startThinking(TurnController.getTurnTime() / 9);
            startedThinking = true;
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

        BoardBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        player.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        manaSlider.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

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
                if (card.getCardIsDead()) {
                    paint.setColorFilter(new LightingColorFilter(Color.RED, 0));
                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                } else if (card.isCardIsActive()) {
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

        if (opponent.getActiveCards() != null) {
            for (Card card : opponent.getActiveCards()) {
                if (card.getCardIsDead()) {
                    Paint paint = new Paint();
                    paint.setColorFilter(new LightingColorFilter(Color.RED, 0));
                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);
                } else {
                    card.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
                }
            }
        }
        turnController.draw(elapsedTime, graphics2D);
    }

    public void arrangeCards() {
        float len = player.getHand().getCards().size();
        float widthSteps = (mLayerViewport.getWidth() / (len + 1)) / 1.4f, heightSteps = mLayerViewport.getHeight() / 30;
        for (int i = 0; i < len; i++) {
            Card activeCard = player.getHand().getCards().get(i);
            Vector2 handPosition = new Vector2((widthSteps * (i + 1)) + 70f, heightSteps * 6);
            activeCard.setAnchor(handPosition.x, handPosition.y);
        }

        len = player.getActiveCards().size();
        widthSteps = (mLayerViewport.getWidth() / 1.5f) / (len + 1);
        float offset = mLayerViewport.getWidth() / 6;
        for (int i = 0; i < len; i++) {
            Card activeCard = player.getActiveCards().get(i);
            Vector2 handPosition = new Vector2((widthSteps * (i + 1)) + offset, mLayerViewport.getHeight() / 2);
            activeCard.setAnchor(handPosition.x, handPosition.y);
        }

        len = opponent.getActiveCards().size();
        widthSteps = (mLayerViewport.getWidth() / 1.5f) / (len + 1);
        offset = mLayerViewport.getWidth() / 6;
        for (int i = 0; i < len; i++) {
            Card activeCard = opponent.getActiveCards().get(i);
            Vector2 handPosition = new Vector2((widthSteps * (i + 1)) + offset, (mLayerViewport.getHeight() / 5) * 4);
            activeCard.setAnchor(handPosition.x, handPosition.y);
        }
    }

    public Hero getPlayer() {
        return player;
    }

    public Hero getOpponent() {
        return opponent;
    }

    public Runnable getEndTurn() {
        return endTurn;
    }

    public TurnController getTurnController() {
        return turnController;
    }
}