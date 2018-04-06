package uk.ac.qub.eeecs.game.cardDemo.Cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.worldScreen.LevelCard;

/**
 * Created by user on 05/12/2017.
 */

public class Deck {
    private final int MAX_DECK_SIZE = 15;
    private ArrayList<Card> cardsInDeck;
    
    /**
     * Constructor for deck that uses default card setup
     * @param gameScreen gameScreen the deck exists in
     * @param game game the deck exists in
     */
    public Deck(GameScreen gameScreen, Game game) {
        game.getAssetManager().loadAndAddBitmap("Card", "img/Hearthstone_Card_Template.png");
        game.getAssetManager().loadAndAddBitmap("1 cost", "img/Cards/Weak man.JPG");
        game.getAssetManager().loadAndAddBitmap("2 cost", "img/Cards/Dog.JPG");
        game.getAssetManager().loadAndAddBitmap("3 cost", "img/Cards/Fat man.JPG");
        game.getAssetManager().loadAndAddBitmap("4 cost", "img/Cards/Sword.JPG");
        game.getAssetManager().loadAndAddBitmap("5 cost", "img/Cards/Card Dragon.JPG");
        cardsInDeck = new ArrayList<Card>();

        //Creates a card with certain values
        Card oneCostCard = new Card(1, "Weak Man",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("1 cost"), gameScreen, 1, 1, 1);

        Card twoCostCard = new Card(2, "Dog",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("2 cost"), gameScreen, 2, 2, 2);

        Card threeCostCard = new Card(3, "Fat Man",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("3 cost"), gameScreen, 3, 3, 2);

        Card fourCostCard = new Card(4, "Sword",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("4 cost"), gameScreen, 4, 6, 1);

        Card fiveCostCard = new Card(5, "Dragon",
                game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetManager().getBitmap("5 cost"), gameScreen, 5, 5, 7);

        //Adds 5 1-cost minions, 4 2-cost minions, 3 3-cost minions, 2 4-cost minions and a 5-cost minion into the deck
        for (int i = 0; i < 5; i++) {
            cardsInDeck.add(new Card(oneCostCard));
        }
        for (int i = 5; i < 9; i++) {
            cardsInDeck.add(new Card(twoCostCard));
        }
        for (int i = 9; i < 12; i++) {
            cardsInDeck.add(new Card(threeCostCard));
        }
        for (int i = 12; i < 14; i++) {
            cardsInDeck.add(new Card(fourCostCard));
        }
        cardsInDeck.add(new Card(fiveCostCard));

    }
    
    /**
     * Constructor for deck that uses a card set up supplied by parameter
     * @param gameScreen gameScreen the deck exists in
     * @param game game the deck exists in
     * @param deck supplied cards to create the deck with
     */
    public Deck(GameScreen gameScreen, Game game, List<LevelCard> deck) {
    
        game.getAssetManager().loadAndAddBitmap("Card", "img/Cards/Card Back.jpg");
        game.getAssetManager().loadAndAddBitmap("1 cost", "img/Cards/Weakman.JPG");
        game.getAssetManager().loadAndAddBitmap("2 cost", "img/Cards/Dog.JPG");
        game.getAssetManager().loadAndAddBitmap("3 cost", "img/Cards/Fatman.JPG");
        game.getAssetManager().loadAndAddBitmap("4 cost", "img/Cards/Sword.JPG");
        game.getAssetManager().loadAndAddBitmap("5 cost", "img/Cards/Dragon.JPG");
        cardsInDeck = new ArrayList<>();
        for(int i = 0; i < deck.size(); i++)
        {
            LevelCard card = deck.get(i);
            cardsInDeck.add(new Card(
                    i,
                    card.getName(),
                    game.getScreenWidth(),
                    game.getScreenHeight(),
                    game.getAssetManager().getBitmap(card.getBitmap()),
                    gameScreen,
                    card.getManaCost(),
                    card.getAttackValue(),
                    card.getHealthValue()
            ));
        }
        
    }

    //draws a random card
    public Card drawCard(){
        Random randomCard = new Random();
        int maximum;
        if(cardsInDeck.size() > 1) {
            maximum = cardsInDeck.size() - 1;
        } else {
            maximum = 1;
        }
        int result = randomCard.nextInt(maximum);
        Card temp = new Card(cardsInDeck.get(result));
        cardsInDeck.remove(result);
        temp.setCardState(Card.CardState.CARD_IN_HAND);
        return temp;
    }

    //broke this with drawcard changes but it's not used anyway, can fix it if we need it - jc
/*    //Draws 3 random cards and adds then to your first hand
    public ArrayList<Integer> firstHand() {
        ArrayList<Integer> firstHand = new ArrayList<>();
        ArrayList<Integer> tempArray = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int card = drawCard();
            firstHand.add(drawCard());
            cardsInDeck.remove(card);
        }
        return firstHand;
    }*/

    //Checks of the Deck is empty
    public boolean isDeckEmpty(){
        if(cardsInDeck.isEmpty()){
            return true;
        }
        return false;
    }


    public ArrayList<Card> getCardsInDeck() {
        return cardsInDeck;
    }
}
