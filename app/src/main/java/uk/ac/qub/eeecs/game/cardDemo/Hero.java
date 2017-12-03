package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.cardDemo.Card;

/**
 * Created by nshah on 27/11/2017.
 */

public class Hero extends Sprite {

    private final int MAX_HEALTH = 30;
    private final int MAX_MANA = 10;

    private int currentHealth;
    private int currentMana;
    private int manaLimit;

    public Hero(float x, float y, Bitmap bitmap, GameScreen gameScreen){
        super(x, y, bitmap, gameScreen);
        currentHealth = 30;
        manaLimit = 1;
        currentMana = manaLimit;
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
}
