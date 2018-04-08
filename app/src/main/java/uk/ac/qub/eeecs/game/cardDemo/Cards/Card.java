package uk.ac.qub.eeecs.game.cardDemo.Cards;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.ai.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.InputHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by user on 14/11/2017.
 */


public class Card extends Sprite {

    // Card stats text size
    private final static float STATS_TEXT_SIZE = 40.0f;
    private final static float STATS_MARGIN = 15.0f;

    // Card dimensions
    private final static float CARD_WIDTH = 40.0f;
    private final static float CARD_HEIGHT = 70.0f;

    // Card movement towards anchor values
    private final static float MAX_ACCELERATION = 100.0f;
    private final static float MAX_VELOCITY = 100.0f;

    // Distance card from anchor to just snap to position
    private final static float STOP_RADIUS = 15.0f;

    /**
     * CardState class for specifying where the card is in the game, either in the deck, hand or
     * board
     */
    public static class CardState
    {
        public static final int CARD_IN_DECK = 0;
        public static final int CARD_IN_HAND = 1;
        public static final int CARD_ON_BOARD = 2;
    }

    //Holds ID for a card
    private int cardID;

    //Holds the cards name
    private String cardName;

    //Anchor for the card to return to after being dragged
    private Vector2 anchor = new Vector2();

    //Centre of the card game object
    private Vector2 cardCentre = new Vector2();

    //Holds position where the card was first touched
    private Vector2 lastPosition = new Vector2();

    //Checks if the card is active
    private boolean cardIsActive;

    //Checks if the card is pressed down
    private  boolean cardPressedDown;

    //Checks if the card is finished moving
    private boolean finishedMove;

    //Holds card mana cost
    private int manaCost;

    //Holds card attack value
    private int attackValue;

    //Holds card health value
    private int healthValue;

    //Checks if card is dead
    private boolean cardIsDead;

    // The current state the card is in in the game, either on the board, in the hand or the deck
    private int cardState;

    // The card stats drawing text style
    private Paint textStyle;


    public Card (int cardID, String cardName, float startX, float startY, Bitmap bitmap, GameScreen gameScreen, int manaCost, int attackValue, int healthValue) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, bitmap, gameScreen);

        //Dimensions of the card from the super
        this.cardCentre.x = CARD_WIDTH/2f;
        this.cardCentre.y = CARD_HEIGHT/2f;

        // Set anchor as start position
        anchor.set(position);

        // Assign card constructor values
        this.cardID = cardID;
        this.cardName = cardName;
        this.manaCost = manaCost;
        this.attackValue = attackValue;
        this.healthValue = healthValue;

        // Assign card constant values
        this.maxAcceleration = MAX_ACCELERATION;
        this.maxVelocity = MAX_VELOCITY;

        // Starting card state is in the deck
        cardState = CardState.CARD_IN_DECK;

        // Initialise card flags
        cardPressedDown = false;
        cardIsActive = false;
        finishedMove = false;
        cardIsDead = false;

        // Set up card stats drawing text style
        textStyle = new Paint();
        textStyle.setTextSize(STATS_TEXT_SIZE);
        textStyle.setTextAlign(Paint.Align.CENTER);

    }

    //Copy constructor for the Card game object to allow mulitple cards to be created
    public Card(Card blankCard){
        this(blankCard.getCardID(), blankCard.getCardName(), blankCard.position.x, blankCard.position.y,
                blankCard.getBitmap(), blankCard.mGameScreen, blankCard.getManaCost(), blankCard.getAttackValue(), blankCard.getHealthValue() );
    }

    //Method for card to take damage
    public void takeDamage(int damageDealt){
        healthValue -= damageDealt;
        if(healthValue < 0){
            healthValue = 0;
        }

        //Checks the healthValue to determine if the Card is dead
        if(healthValue == 0){
            cardIsDead = true;
        }
    }

    public void update(ElapsedTime elapsedTime, ScreenViewport screenViewport, LayerViewport layerViewport, Hero hero) {
        Input input = mGameScreen.getGame().getInput();
        for (TouchEvent touch : input.getTouchEvents()) {
            // Convert touch coordinates into screen coordinates
            Vector2 layerPos = new Vector2();
            InputHelper.convertScreenPosIntoLayer(
                    screenViewport,
                    new Vector2(touch.x, touch.y),
                    layerViewport, layerPos);
            //Checks if the touch event happens on the card
            if (touch.type == TouchEvent.TOUCH_DOWN && (layerPos.x > position.x - cardCentre.x)
                    && (layerPos.x < position.x + cardCentre.x)
                    && (layerPos.y > position.y - cardCentre.y)
                    && (layerPos.y < position.y + cardCentre.y) && this.finishedMove == false) {
                cardPressedDown = true;
                cardIsActive = true;
                //finishedMove = false;

                /*
                //Checks what image is held on card, if clicked, swaps it with the alternative
                if (mBitmap == mGameScreen.getGame().getAssetManager().getBitmap("Card")) {
                    mBitmap = mGameScreen.getGame().getAssetManager().getBitmap("Back");
                } else {
                    mBitmap = mGameScreen.getGame().getAssetManager().getBitmap("Card");
                }
                */
            }
            if (touch.type == TouchEvent.TOUCH_UP) {
                
                if(cardState != CardState.CARD_ON_BOARD)
                {
                    // If the card is not on the board mark it as deselected
                    cardIsActive = false;
                }


                // If there is a touch up event, return the pressed card back to anchor position
                if(cardPressedDown && cardState == CardState.CARD_IN_HAND) {
                    //are we allowed to play a card?
                    if(hero.getActiveCards().size() < hero.getMaxActiveCards()) {
                        //Checks if the hero has enough mana to play the card
                        if (hero.getCurrentMana() >= this.getManaCost()) {
                            //If the card is dropped onto the top half of the screen, place it onto the board
                            if (this.position.y > layerViewport.getHeight() / 2) {
//                                //Place the cards accordingly
//                                if (this.position.x > layerViewport.getWidth() / 2) {
//                                    this.anchor.x += 20;
//                                } else {
//                                    this.anchor.x -= 20;
//                                }
                                //Moves the card from the players hand to their active cards
                                hero.playCard(this);
                                this.finishedMove = true;
                            }
                        }
                    }
                    //position = new Vector2(this.anchor.x, this.anchor.y);
                }
                cardPressedDown = false;
                //Checks if card is dragged
            } else if (touch.type == TouchEvent.TOUCH_DRAGGED && cardPressedDown) {
                if (!Float.isNaN(layerPos.x)) {
                    if (!Float.isNaN(layerPos.x)) {
                        if (cardState != CardState.CARD_ON_BOARD) {
                            this.position.x = layerPos.x;
                            //screenDimensions used to invert Y values
                            this.position.y = layerPos.y;
                        }
                    }
                }
            }
        }
        if(!cardPressedDown) {
            // If the card hasn't been pressed down on - i.e not being dragged
            if (position.x != anchor.x || position.y != anchor.y) {
                // If the card is not at it's anchor position
                // Calculate distance between the position and the anchor
                Vector2 anchorDistance = new Vector2();
                anchorDistance.set(anchor.x - position.x, anchor.y - position.y);
                if (anchorDistance.length() > STOP_RADIUS) {
                    // If the distance between the position and the anchor is more than the stop
                    // radius
                    // Accelerate towards the anchor
                    SteeringBehaviours.seek(this,
                            anchor,
                            acceleration);
                } else {
                    // If the distance between the position and the anchor is less than the stop
                    // distance
                    // Snap the card to the anchor, reset velocity and acceleration
                    acceleration.set(Vector2.Zero);
                    velocity.set(Vector2.Zero);
                    position.set(anchor);
                }
            }
        }
        super.update(elapsedTime);
    }
    
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint)
    {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport, paint);
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport,
                screenViewport, drawSourceRect, drawScreenRect)) {
            // Draw mana in blue
            textStyle.setColor(Color.BLUE);
            graphics2D.drawText(String.valueOf(manaCost), drawScreenRect.left + STATS_MARGIN, drawScreenRect.top + STATS_MARGIN, textStyle);
            // Draw health in green
            textStyle.setColor(Color.GREEN);
            graphics2D.drawText(String.valueOf(healthValue), drawScreenRect.left + STATS_MARGIN, drawScreenRect.bottom - STATS_MARGIN, textStyle);
            // Draw attack value in red
            textStyle.setColor(Color.RED);
            graphics2D.drawText(String.valueOf(attackValue), drawScreenRect.right - STATS_MARGIN, drawScreenRect.bottom - STATS_MARGIN, textStyle);
        }
    }
    
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport)
    {
        this.draw(elapsedTime, graphics2D, layerViewport, screenViewport, new Paint());
    }
    
    //Getters and Setters
    public Vector2 getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(){
        setPosition(this.lastPosition.x,this.lastPosition.y);
        finishedMove = false;
        cardIsActive = false;
    }

    public void setLastPosition(Vector2 lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Vector2 getPosition() { return position; }

    public void setPosition(Vector2 position) { this.position = position; }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public boolean isCardIsActive() {
        return cardIsActive;
    }

    public void setCardIsActive(boolean cardIsActive) {
        this.cardIsActive = cardIsActive;
    }

    public boolean isCardPressedDown() {
        return cardPressedDown;
    }

    public void setCardPressedDown(boolean cardPressedDown) {this.cardPressedDown = cardPressedDown;}

    public boolean isFinishedMove() {
        return finishedMove;
    }

    public void setFinishedMove(boolean finishedMove) {
        this.finishedMove = finishedMove;
    }

    public String getCardName() {return cardName;}

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardID() { return cardID;}

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getHealthValue() {
        return healthValue;
    }

    public void setHealthValue(int healthValue) {
        this.healthValue = healthValue;
    }

    public boolean getCardIsDead(){
        return this.cardIsDead;
    }

    public int getCardState()
    {
        return cardState;
    }

    public void setCardState(int cardState)
    {
        this.cardState = cardState;
    }

    public Vector2 getAnchor()
    {
        return anchor;
    }

    public void setAnchor(float x, float y)
    {
        this.anchor.x = x;
        this.anchor.y = y;
        this.acceleration.set(Vector2.Zero);
        this.velocity.set(Vector2.Zero);
    }
}


