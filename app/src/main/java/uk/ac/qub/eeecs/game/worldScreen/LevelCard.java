package uk.ac.qub.eeecs.game.worldScreen;

/**
 * Created by Jamie T on 08/03/2018.
 * Class to represent a card in the level definition, reduced down to essential information that
 * can be used to construct a deck for a particular level
 */

public class LevelCard
{
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
	 * @param name card name
	 * @param bitmap card bitmap
	 * @param manaCost card mana cost
	 * @param healthValue card health
	 * @param attackValue card attack
	 */
	public LevelCard(String name, String bitmap, int manaCost, int healthValue, int attackValue)
	{
		this.name = name;
		this.bitmap = bitmap;
		this.manaCost = manaCost;
		this.healthValue = healthValue;
		this.attackValue = attackValue;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getBitmap()
	{
		return bitmap;
	}
	
	public void setBitmap(String bitmap)
	{
		this.bitmap = bitmap;
	}
	
	public int getManaCost()
	{
		return manaCost;
	}
	
	public void setManaCost(int manaCost)
	{
		this.manaCost = manaCost;
	}
	
	public int getHealthValue()
	{
		return healthValue;
	}
	
	public void setHealthValue(int healthValue)
	{
		this.healthValue = healthValue;
	}
	
	public int getAttackValue()
	{
		return attackValue;
	}
	
	public void setAttackValue(int attackValue)
	{
		this.attackValue = attackValue;
	}
}
