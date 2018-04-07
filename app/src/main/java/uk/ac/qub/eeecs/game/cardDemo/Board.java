package uk.ac.qub.eeecs.game.cardDemo;

import java.util.ArrayList;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.cardDemo.Hero;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;

/**
 * Created by nshah on 03/12/2017.
 */

public class Board  {

    private Hero userHero;
    private Hero opponentHero;

    public Board( Hero userHero, Hero opponentHero) {
        this.userHero = userHero;
        this.opponentHero = opponentHero;
    }

    public Hero getUserHero() {
        return userHero;
    }

    public Hero getOpponentHero() {
        return opponentHero;
    }

    public Hero getAIHero() {
        return this.getOpponentHero();
    }
}
