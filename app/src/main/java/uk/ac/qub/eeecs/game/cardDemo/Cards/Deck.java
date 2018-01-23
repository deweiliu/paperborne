package uk.ac.qub.eeecs.game.cardDemo.Cards;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.Random;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Created by user on 05/12/2017.
 */

public class Deck {
    private final int MAX_DECK_SIZE = 15;
    private ArrayList<Card> cardsInDeck = new ArrayList<>();

    public Deck(GameScreen gameScreen, Game game) {


        game.getAssetManager().loadAndAddBitmap("Card", "img/Hearthstone_Card_Template.png");

        //Creates a card with certain values
        Card oneCostCard = new Card(1, "Weak Man",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("Card"), gameScreen, 1, 1, 1);

        Card twoCostCard = new Card(2, "Dog",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("Card"), gameScreen, 2, 2, 2);

        Card threeCostCard = new Card(3, "Fat Man",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("Card"), gameScreen, 3, 3, 2);

        Card fourCostCard = new Card(4, "Sword",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("Card"), gameScreen, 4, 6, 1);

        Card fiveCostCard = new Card(5, "Dragon",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("Card"), gameScreen, 5, 5, 7);

        //Adds 5 1-cost minions, 4 2-cost minions, 3 3-cost minions, 2 4-cost minions and a 5-cost minion into the deck
        for (int i = 1; i < 6; i++) {
            cardsInDeck.add(i, oneCostCard);
        }
        for (int i = 6; i < 10; i++) {
            cardsInDeck.add(i, twoCostCard);
        }
        for (int i = 10; i < 13; i++) {
            cardsInDeck.add(i, threeCostCard);
        }
        for (int i = 13; i < 15; i++) {
            cardsInDeck.add(i, fourCostCard);
        }
        cardsInDeck.add(15, fiveCostCard);

    }

    //Generates a random number between 0 and 14, gets the index of the card
    public int drawCard(){
        int drawnCard;
        Random randomCard = new Random();
        int low = 0;
        int high = 14;
        int result = randomCard.nextInt(high - low) + low;
        drawnCard = cardsInDeck.indexOf(result);

        return drawnCard;
    }

    //Draws 3 random cards and adds then to your first hand
    public ArrayList<Integer> firstHand() {
        ArrayList<Integer> firstHand = new ArrayList<>();
        ArrayList<Integer> tempArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int card = drawCard();
            firstHand.add(card);
            cardsInDeck.remove(card);
        }
        return firstHand;
    }

    //Checks of the Deck is empty
    public boolean isDeckEmpty(){
        if(cardsInDeck.isEmpty()){
            return true;
        }
        return false;
    }

    //Returns a random card from the deck
    public Card addCardToHand(){
        int drawnCard = this.drawCard();
        return cardsInDeck.get(drawnCard);
    }
}
