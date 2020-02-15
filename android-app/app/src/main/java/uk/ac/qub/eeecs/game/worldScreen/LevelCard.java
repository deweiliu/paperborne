package uk.ac.qub.eeecs.game.worldScreen;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jamie T on 08/03/2018.
 * Class to represent a card in the level definition, reduced down to essential information that
 * can be used to construct a deck for a particular level
 */

public class LevelCard {
    // JSON String identifiers for cards
    public static final String CARD_NAME = "name";
    public static final String CARD_ATTACK = "attackValue";
    public static final String CARD_HEALTH = "healthValue";
    public static final String CARD_BITMAP = "bitmap";
    public static final String CARD_MANA_COST = "manaCost";

    // Card name
    private String name;
    // Card bitmap
    private String bitmap;
    // Card mana cost
    private int manaCost;
    // Card health
    private int healthValue;
    // Card attack
    private int attackValue;

    /**
     * Constructor for level card, reduced representation down to essential information about card
     * stats
     *
     * @param name        card name
     * @param bitmap      card bitmap
     * @param manaCost    card mana cost
     * @param healthValue card health
     * @param attackValue card attack
     */
    public LevelCard(String name, String bitmap, int manaCost, int healthValue, int attackValue) {
        this.name = name;
        this.bitmap = bitmap;
        this.manaCost = manaCost;
        this.healthValue = healthValue;
        this.attackValue = attackValue;
    }

    /**
     * Copy constructor for level card
     *
     * @param copyCard the card to copy
     */
    public LevelCard(LevelCard copyCard) {
        this(copyCard.name, copyCard.bitmap, copyCard.manaCost, copyCard.healthValue, copyCard.attackValue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getHealthValue() {
        return healthValue;
    }

    public void setHealthValue(int healthValue) {
        this.healthValue = healthValue;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    /**
     * Converts the card to a JSON string form for writing to a file
     *
     * @return JSON String form of this card
     */
    public String toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CARD_NAME, name);
            jsonObject.put(CARD_BITMAP, bitmap);
            jsonObject.put(CARD_HEALTH, healthValue);
            jsonObject.put(CARD_MANA_COST, manaCost);
            jsonObject.put(CARD_ATTACK, attackValue);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
