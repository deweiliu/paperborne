package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
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
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Deck;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Hand;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;


/**
 * Created by nshah on 27/11/2017.
 */

public class Hero extends Sprite {

    // Hero stats text size
    private final static float STATS_TEXT_SIZE = 70.0f;

    private final int MAX_HEALTH = 30;
    private final int MAX_MANA = 10;
    private final int MAX_ACTIVE_CARDS = 7;

    private int currentHealth;
    private int currentMana;
    private int manaLimit;
    private ArrayList<Card> activeCards;
    private Deck deck;
    private Hand hand;
    private boolean heroIsDead;
    private Paint textStyle;
    private boolean heroTouched;
    private Vector2 heroCentre = new Vector2();

    /**
     * Constructor for the Hero without a supplied deck, using the default card setup
     * @param x horizontal position of the hero
     * @param y vertical position of the hero
     * @param bitmap hero bitmap
     * @param gameScreen gameScreen the hero exists on
     * @param game game the hero exists in
     */
    public Hero(float x, float y, Bitmap bitmap, GameScreen gameScreen, Game game){
        this(x, y, bitmap, gameScreen, game, null);
    }
    
    /**
     * Constructor for the Hero with a supplied deck by parameter
     * @param x horizontal position of the hero
     * @param y vertical position of the hero
     * @param bitmap hero bitmap
     * @param gameScreen gameScreen the hero exists on
     * @param game game the hero exists in
     * @param levelDeck list of cards to build the deck from
     */
    public Hero(float x, float y, Bitmap bitmap, GameScreen gameScreen, Game game, List<LevelCard> levelDeck)
    {
        super(x, y, 70.0f, 105.0f, bitmap, gameScreen);
        currentHealth = MAX_HEALTH;
        manaLimit = 5;
        currentMana = manaLimit;
        heroTouched = false;
        heroCentre.x = 70.0f / 2;
        heroCentre.y = 105.0f / 2;
        if(levelDeck != null)
        {
            // If there is a level deck provided, use that for the deck
            deck = new Deck(gameScreen, game, levelDeck);
        }
        else
        {
            // If no level deck provided, use the default deck
            deck = new Deck(gameScreen, game);
        }
        hand = new Hand(deck);
        activeCards = new ArrayList<>();
        heroIsDead = false;
        // Text style for drawing the hero health
        textStyle = new Paint();
        textStyle.setTextSize(STATS_TEXT_SIZE);
        textStyle.setTextAlign(Paint.Align.CENTER);
        // Go through each card in the deck and set it's position to the cards position, so
        // when the card is dealt and visible, it comes from the hero
        for(Card card : deck.getCardsInDeck())
        {
            card.setPosition(position);
        }
    }

    //Increases the mana limit, at the end of every turn, up to a max value
    public void incrementManaLimit(){
        if (manaLimit < MAX_MANA){
            manaLimit++;
        }
    }

    //Refills the hero's mana at the start of every move
    public void refillMana(){
        currentMana = manaLimit;
    }

    public void takeDamage(int DamageDealt){
        currentHealth -= DamageDealt;
        if(currentHealth < 0){
            currentHealth = 0;
        }

        //Checks the currentHealth to determine if the Hero is dead
        if(currentHealth == 0){
            heroIsDead = true;
        }
    }


    public void playCard(Card cardToPlay){
        if (this.activeCards.size() < 7 && currentMana >= cardToPlay.getManaCost()){
            this.activeCards.add(cardToPlay);
            this.currentMana -= cardToPlay.getManaCost();
            cardToPlay.setCardState(Card.CardState.CARD_ON_BOARD);
            this.hand.getCards().remove(cardToPlay);
            cardToPlay.setFinishedMove(true);
            // After the card is been played, rearrange all the card positions on the board
            ((CardDemoScreen)mGameScreen).arrangeCards();
        }
    }

    public void clearDeadCards() {
        Iterator<Card> i = activeCards.iterator();
        while(i.hasNext()) {
            Card current = i.next();
            if(current.getCardIsDead())
                i.remove();
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint)
    {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport, paint);
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport,
                screenViewport, drawSourceRect, drawScreenRect)) {
            textStyle.setColor(Color.GREEN);
            graphics2D.drawText(String.valueOf(currentHealth), drawScreenRect.centerX(), drawScreenRect.bottom, textStyle);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport)
    {
        this.draw(elapsedTime, graphics2D, layerViewport, screenViewport, new Paint());
    }


    public void update(ElapsedTime elapsedTime, ScreenViewport screenViewport, LayerViewport layerViewport){
            Input input = mGameScreen.getGame().getInput();
            for (TouchEvent touch : input.getTouchEvents()){
                Vector2 pos = new Vector2();
                InputHelper.convertScreenPosIntoLayer(screenViewport, new Vector2(touch.x, touch.y),
                        layerViewport, pos);

                //Check if the touch event occurs on the hero
                if (touch.type == TouchEvent.TOUCH_DOWN && (pos.x > position.x - heroCentre.x)
                        && (pos.x < position.x + heroCentre.x)
                        && (pos.y > position.y - heroCentre.y)
                        && (pos.y < position.y + heroCentre.y)){
                    heroTouched = true;
                } else {
                    heroTouched = false;
                }
            }
    }
    //Getters
    public int getCurrentHealth(){
        return this.currentHealth;
    }

    public boolean isAlive(){
        if(this.getCurrentHealth()>0){
            return true;
        }else{
            return false;
        }
    }

    public int getCurrentMana(){
        return this.currentMana;
    }

    public int getManaLimit() { return this.manaLimit; }

    public ArrayList<Card> getActiveCards() {
        return this.activeCards;
    }

    public Hand getHand(){
        return this.hand;
    }

    public Deck getDeck(){
        return this.deck;
    }

    public boolean getHeroIsDead(){
        return this.heroIsDead;
    }

    public int getMaxActiveCards() {
        return this.MAX_ACTIVE_CARDS;
    }

    public boolean getHeroTouched() {return this.heroTouched; }

    public void setHeroTouched(boolean touched) {this.heroTouched = touched; }

}
