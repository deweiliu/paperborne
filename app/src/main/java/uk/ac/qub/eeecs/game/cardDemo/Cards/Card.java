package uk.ac.qub.eeecs.game.cardDemo.Cards;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Created by user on 14/11/2017.
 */


public class Card extends Sprite {
    
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


    public Card (int cardID, String cardName, float startX, float startY, Bitmap bitmap, GameScreen gameScreen, int manaCost, int attackValue, int healthValue) {
        super(startX, startY, 70.0f, 105.0f, bitmap, gameScreen);
        cardCentre.x = 70.0f/2;
        cardCentre.y = 105.0f/2;
        anchor = new Vector2(position.x,position.y);

        this.cardID = cardID;
        this.cardName = cardName;
        this.manaCost = manaCost;
        this.attackValue = attackValue;
        this.healthValue = healthValue;
        cardState = 0;

        /* Calculates the centre of the card
        this.cardCentre.x = cardDimensions.x / 2;
        this.cardCentre.y = cardDimensions.y / 2;
        */

        cardPressedDown = false;
        cardIsActive = false;
        finishedMove = false;
        cardIsDead = false;

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


    public void update(ElapsedTime elapsedTime) {
        Input input = mGameScreen.getGame().getInput();
        for(TouchEvent touch: input.getTouchEvents() ) {
            //Checks if the touch event happens on the card
            if (touch.type == TouchEvent.TOUCH_DOWN && ((input.getTouchX(touch.pointer) > position.x - cardCentre.x)
                    && (input.getTouchX(touch.pointer) < position.x + cardCentre.x)
                    && (input.getTouchY(touch.pointer) > position.y - cardCentre.y)
                    && (input.getTouchY(touch.pointer) < position.y + cardCentre.y))) {
                cardPressedDown = true;
                cardIsActive = true;
                finishedMove = false;
                this.setLastPosition(this.position);


                //Checks what image is held on card, if clicked, swaps it with the alternative
                if (mBitmap == mGameScreen.getGame().getAssetManager().getBitmap("Card")) {
                    mBitmap = mGameScreen.getGame().getAssetManager().getBitmap("Back");
                } else {
                    mBitmap = mGameScreen.getGame().getAssetManager().getBitmap("Card");
                }

            }
            //Checks if card is released
            if (touch.type == TouchEvent.TOUCH_UP) {
                if(cardState != CardState.CARD_ON_BOARD)
                {
                    cardIsActive = false;
                    finishedMove = true;
                }
                cardPressedDown = false;
                position = new Vector2(anchor.x, anchor.y);
                //Checks if card is dragged
            } else if (touch.type == TouchEvent.TOUCH_DRAGGED && cardPressedDown) {
                if (!Float.isNaN(input.getTouchX(touch.pointer))) {
                    if(cardState != CardState.CARD_ON_BOARD)
                    {
                        position.x = input.getTouchX(touch.pointer);
                        //screenDimensions used to invert Y values
                        position.y = screenDimensions.y - input.getTouchY(touch.pointer);
                    }
                }
            }

            super.update(elapsedTime);
        }
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
    }
}


