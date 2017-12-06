package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.cardDemo.Cards.*;


/**
 * Created by nshah on 27/11/2017.
 */

public class Hero extends Sprite {

    private final int MAX_HEALTH = 30;
    private final int MAX_MANA = 10;

    private int currentHealth;
    private int currentMana;
    private int manaLimit;
    private ArrayList<Card> activeCards;
    private Deck deck;
    private Hand hand;

    public Hero(float x, float y, Bitmap bitmap, GameScreen gameScreen, Game game){
        super(x, y, bitmap, gameScreen);
        currentHealth = MAX_HEALTH;
        manaLimit = 1;
        currentMana = manaLimit;
        deck = new Deck(gameScreen, game);
        hand = new Hand();
    }

    public void IncrementMangaLimit(){
        if (manaLimit < MAX_MANA){
            manaLimit++;
        }
    }

    public void IncrementManga(){
        if(currentMana < manaLimit){
            currentMana++;
        }
    }

    public void TakeDamage(int DamageDealt){
        currentHealth -= DamageDealt;
        if(currentHealth < 0){
            currentHealth = 0;
        }
    }

    public int getCurrentHealth(){
        return this.currentHealth;
    }

    public int getCurrentMana(){
        return this.currentMana;
    }

    public ArrayList<Card> getActiveCards() {
        return activeCards;
    }

    public void playCard(Card cardToPlay){
        if (this.activeCards.size() < 7 && currentMana >= cardToPlay.getManaCost()){
            this.activeCards.add(cardToPlay);
            this.currentMana -= cardToPlay.getManaCost();
        } else {
            //Warning message to be displayed
            //Hero can only have a maximum of 7 cards active at any time
        }
    }
}
