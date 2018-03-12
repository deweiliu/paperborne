package uk.ac.qub.eeecs.game.cardDemo.Cards;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

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
    
    private static final Vector2 MANA_TRANSLATION = new Vector2(-7.5f, -25f);

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
    
    private Paint textStyle;


    public Card (int cardID, String cardName, float startX, float startY, Bitmap bitmap, GameScreen gameScreen, int manaCost, int attackValue, int healthValue) {
        super(startX, startY, 40f, 70f, bitmap, gameScreen);

        //Dimensions of the card from the super
        this.cardCentre.x = 30f/2f;
        this.cardCentre.y = 70f/2f;
        anchor = new Vector2(position.x,position.y);


        this.cardID = cardID;
        this.cardName = cardName;
        this.manaCost = manaCost;
        this.attackValue = attackValue;
        this.healthValue = healthValue;
        cardState = CardState.CARD_IN_DECK;

        cardPressedDown = false;
        cardIsActive = false;

        finishedMove = false;
        cardIsDead = false;
        
        textStyle = new Paint();
        textStyle.setTextSize(40f);
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
                                this.anchor.y = layerViewport.getHeight() / 2;
                                //Moves the card from the players hand to their active cards
                                hero.playCard(this);
                                //Place the cards accordingly
                                if (this.position.x > layerViewport.getWidth() / 2) {
                                    this.anchor.x += 20;
                                } else {
                                    this.anchor.x -= 20;
                                }
                                this.finishedMove = true;
                            }
                        }
                    }
                    position = new Vector2(this.anchor.x, this.anchor.y);
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
                super.update(elapsedTime);
            }
        }
    }
    
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint)
    {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport, paint);
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport,
                screenViewport, drawSourceRect, drawScreenRect)) {
            textStyle.setColor(Color.BLUE);
            graphics2D.drawText(String.valueOf(manaCost), drawScreenRect.left, drawScreenRect.top, textStyle);
            textStyle.setColor(Color.GREEN);
            graphics2D.drawText(String.valueOf(healthValue), drawScreenRect.left, drawScreenRect.bottom, textStyle);
            textStyle.setColor(Color.RED);
            graphics2D.drawText(String.valueOf(attackValue), drawScreenRect.right, drawScreenRect.bottom, textStyle);
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
    }
}


