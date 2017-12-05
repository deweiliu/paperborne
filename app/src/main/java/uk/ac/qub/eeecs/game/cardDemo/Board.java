package uk.ac.qub.eeecs.game.cardDemo;

import java.util.ArrayList;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.cardDemo.Hero;


/**
 * Created by nshah on 03/12/2017.
 */

public class Board extends Sprite {

    private Hero userHero;
    private Hero oponentHero;
    private ArrayList<Card> ActiveCards;


    public Board(float x, float y, Bitmap bitmap, GameScreen gameScreen, Hero userHero, Hero oponentHero) {
        super(x, y, bitmap, gameScreen);
        this.userHero = userHero;
        this.oponentHero = oponentHero;
    }

}
