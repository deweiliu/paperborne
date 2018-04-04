package uk.ac.qub.eeecs.game.worldScreen;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.ui.PushButton;

/**
 * Created by Jamie T on 26/01/2018.
 * Class to represent a Game Level on the level selection World Screen
 */

public class Level
{
	// JSON String identifiers for levels
	public static final String LEVEL_ID = "id";
	public static final String LEVEL_NAME = "name";
	public static final String LEVEL_BITMAP = "bitmap";
	public static final String LEVEL_BITMAP_PATH = "bitmapPath";
	public static final String LEVEL_X_PERCENT = "xPercent";
	public static final String LEVEL_Y_PERCENT = "yPercent";
	public static final String LEVEL_WIDTH = "width";
	public static final String LEVEL_HEIGHT = "height";
	public static final String LEVEL_DECK = "deck";
	public static final String LEVEL_PREREQUISITES = "prerequisites";
	
	// Level ID
	private String id;
	// Level name
	private String name;
	// Level button representation
	private PushButton button;
	// Cards that make up a deck the opponent will play for this level
	private List<LevelCard> deck;
	// Prerequisite levels required to be completed before this level is playable represented by ID
	private List<String> prerequisites;
	// Level bitmap path
	private String bitmapPath;
	// Level bitmap ID
	private String bitmapID;
	// The percentage X position the level is to be placed at on the screen
	// 0% = complete left, 100% = complete right, 50% = centre
	private double xPercent;
	// The percentage Y position the level is to be placed at on the screen
	// 0% = complete top, 100% = complete bottom, 50% = centre
	private double yPercent;
	// The level's bitmap width
	private double width;
	// The level's bitmap height
	private double height;
	// TODO: Implement a level boss/hero for the player to play against
	
	/**
	 * Constructor for creating a level with no prerequisites
	 *
	 * @param id         unique level ID
	 * @param name       level name
	 * @param xPercent   the percentage x position th level is placed on the screen from the left
	 * @param yPercent   the percentage y position the level is placed on the screen from the top
	 * @param width      the level bitmap width
	 * @param height     the level bitmap height
	 * @param deck       the cards that make up a deck that the opponent will play for this level
	 * @param bitmapPath the level bitmap path
	 * @param bitmapID   the level bitmap ID
	 */
	public Level(String id, String name, double xPercent, double yPercent, double width,
				 double height, List<LevelCard> deck, String bitmapPath, String bitmapID)
	{
		this.id = id;
		this.name = name;
		this.xPercent = xPercent;
		this.yPercent = yPercent;
		this.width = width;
		this.height = height;
		this.deck = deck;
		this.bitmapPath = bitmapPath;
		this.bitmapID = bitmapID;
		this.prerequisites = new ArrayList<>();
		// Button must be initialised after constructor with setButton method
		this.button = null;
	}
	
	/**
	 * Constructor for creating a level with required prerequisites
	 *
	 * @param id            unique level ID
	 * @param name          level name
	 * @param xPercent      the percentage x position th level is placed on the screen from the left
	 * @param yPercent      the percentage y position the level is placed on the screen from the top
	 * @param width         the level bitmap width
	 * @param height        the level bitmap height
	 * @param deck          the cards that make up a deck that the opponent will play for this level
	 * @param bitmapPath    the level bitmap path
	 * @param bitmapID      the level bitmap ID
	 * @param prerequisites list of levels that are required to be completed before this level
	 */
	public Level(String id, String name, double xPercent, double yPercent, double width,
				 double height, List<LevelCard> deck, String bitmapPath, String bitmapID,
				 List<String> prerequisites)
	{
		this(id, name, xPercent, yPercent, width, height, deck, bitmapPath, bitmapID);
		this.prerequisites = prerequisites;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public PushButton getButton()
	{
		return button;
	}
	
	public void setButton(PushButton button)
	{
		this.button = button;
	}
	
	public List<LevelCard> getDeck()
	{
		return deck;
	}
	
	public void setDeck(List<LevelCard> deck)
	{
		this.deck = deck;
	}
	
	public List<String> getPrerequisites()
	{
		return prerequisites;
	}
	
	public void setPrerequisites(List<String> prerequisites)
	{
		this.prerequisites = prerequisites;
	}
	
	public String getBitmapPath()
	{
		return bitmapPath;
	}
	
	public void setBitmapPath(String bitmapPath)
	{
		this.bitmapPath = bitmapPath;
	}
	
	public String getBitmapID()
	{
		return bitmapID;
	}
	
	public void setBitmapID(String bitmapID)
	{
		this.bitmapID = bitmapID;
	}
	
	public double getxPercent()
	{
		return xPercent;
	}
	
	public void setxPercent(double xPercent)
	{
		this.xPercent = xPercent;
	}
	
	public double getyPercent()
	{
		return yPercent;
	}
	
	public void setyPercent(double yPercent)
	{
		this.yPercent = yPercent;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public void setWidth(double width)
	{
		this.width = width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public void setHeight(double height)
	{
		this.height = height;
	}
}
