package uk.ac.qub.eeecs.game.cardDemo;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Created by nshah on 27/11/2017.
 */

public class Hero extends Sprite {

    private final int MAX_HEALTH = 30;
    private final int MAX_MANGA = 10;

    private int currentHealth;
    private int currentManga;
    private int mangaLimit;

    public Hero(GameScreen gameScreen){
        super(gameScreen);
        currentHealth = 30;
        mangaLimit = 1;
        currentManga = mangaLimit;
    }

    public void IncrementMangaLimit(){
        if (mangaLimit < MAX_MANGA){
            mangaLimit++;
        }
    }

    public void IncrementManga(){
        if(currentManga < mangaLimit){
            currentManga++;
        }
    }

    public void TakeDamage(int DamageDealt){
        currentHealth -= DamageDealt;
        if(currentHealth < 0){
            currentHealth = 0;
        }
    }
}
