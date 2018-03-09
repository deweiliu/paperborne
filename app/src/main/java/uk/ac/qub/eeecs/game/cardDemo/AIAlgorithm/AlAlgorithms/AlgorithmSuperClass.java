package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.AlAlgorithms;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Cards.Card;
import uk.ac.qub.eeecs.game.cardDemo.Hero;

/**
 * Created by 40216004 Dewei Liu on 08/03/2018.
 * <p>
 * This super class is to restrict the stuff that AI can access to prevent computer from cheating the game.
 */

public abstract class AlgorithmSuperClass {
    private Hero human;
    private Hero ai;

    //Make the algorithm work randomly
    protected Random random;
    protected boolean isValid = false;

    public AlgorithmSuperClass(Board mBoard) {
        this.ai = mBoard.getAIHero();
        this.human = mBoard.getUserHero();
        random = new Random();
        AIAlgorithm();
    }

    //Decide if this action of this class will be done/ is valid.
    public boolean isValid() {
        return isValid;
    }

    abstract public int actionNumber();

    abstract protected void AIAlgorithm();

    protected void checkValid_ThrowException() {
        if (this.isValid() == false) {
            throw new IllegalStateException("The method which calls this method cannot be called, unless isValid == true.");
        }
    }

    //AI player stuff accessible by computer
    protected int getMyMana() {
        return ai.getCurrentMana();
    }

    protected int getMyHealth() {
        return ai.getCurrentHealth();
    }

    protected ArrayList<Card> getMyHandCards() {
        return ai.getHand().getCards();
    }

    protected ArrayList<Card> getMyBoardCards() {
        return ai.getActiveCards();
    }

    protected int getMyDeckCardsNumber() {
        return ai.getDeck().getCardsInDeck().size();
    }

    //human player stuff accessible by computer
    protected int getPlayerHealth() {
        return human.getCurrentHealth();
    }

    protected ArrayList<Card> getPlayerBoardCards() {
        return human.getActiveCards();
    }

    protected int getPlayerHandCardNumber() {
        return human.getHand().getCards().size();
    }

    protected boolean isPlayerDeckEmpty() {
        return human.getDeck().isDeckEmpty();
    }


}
