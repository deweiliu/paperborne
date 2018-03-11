package uk.ac.qub.eeecs.game.cardDemo.AIAlgorithm.algorithms;

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
    private Hero humanPlayer;
    private Hero AIPlayer;

    //Make the algorithm work randomly
    protected Random random;
    protected boolean isValid = false;

    public AlgorithmSuperClass(Board mBoard) {
        this.AIPlayer = mBoard.getAIHero();
        this.humanPlayer = mBoard.getUserHero();
        random = new Random();
        algorithm();
    }

    //Decide if this Action of this class will be done/ is valid.
    public boolean isValid() {
        return isValid;
    }

    abstract public int actionNumber();

    abstract protected void algorithm();

    protected void checkValid_ThrowException() {
        if (this.isValid() == false) {
            throw new IllegalStateException("The method which calls this method cannot be called, unless isValid == true.");
        }
    }

    //AI player stuff accessible by computer
    protected int getMyMana() {
        return AIPlayer.getCurrentMana();
    }

    protected int getMyHealth() {
        return AIPlayer.getCurrentHealth();
    }

    protected ArrayList<Card> getMyHandCards() {
        return AIPlayer.getHand().getCards();
    }

    protected ArrayList<Card> getMyBoardCards() {
        return AIPlayer.getActiveCards();
    }

    protected int getMyDeckCardsNumber() {
        return AIPlayer.getDeck().getCardsInDeck().size();
    }

    protected int getMyMaxActiveCard(){
        return AIPlayer.getMaxActiveCards();
    }

    //humanPlayer player stuff accessible by computer
    protected int getPlayerHealth() {
        return humanPlayer.getCurrentHealth();
    }

    protected ArrayList<Card> getPlayerBoardCards() {
        return humanPlayer.getActiveCards();
    }

    protected int getPlayerHandCardNumber() {
        return humanPlayer.getHand().getCards().size();
    }

    protected boolean isPlayerDeckEmpty() {
        return humanPlayer.getDeck().isDeckEmpty();
    }


}
