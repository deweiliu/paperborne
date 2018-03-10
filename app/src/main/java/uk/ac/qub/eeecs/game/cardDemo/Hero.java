package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Deck;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Hand;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;


/**
 * Created by nshah on 27/11/2017.
 */

public class Hero extends Sprite {

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
    
    /**
     * Constructor for the Hero without a supplied deck, using the default card setup
     * @param x horizontal position of the hero
     * @param y vertical position of the hero
     * @param bitmap hero bitmap
     * @param gameScreen gameScreen the hero exists on
     * @param game game the hero exists in
     */
    public Hero(float x, float y, Bitmap bitmap, GameScreen gameScreen, Game game){
        super(x, y, 70.0f, 105.0f, bitmap, gameScreen);
        currentHealth = MAX_HEALTH;
        manaLimit = 5;
        currentMana = manaLimit;
        deck = new Deck(gameScreen, game);
        hand = new Hand(deck);
        activeCards = new ArrayList<>();
        heroIsDead = false;
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
        deck = new Deck(gameScreen, game, levelDeck);
        hand = new Hand(deck);
        activeCards = new ArrayList<>();
        heroIsDead = false;
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

    //Getters
    public int getCurrentHealth(){
        return this.currentHealth;
    }

    public int getCurrentMana(){
        return this.currentMana;
    }

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
}
