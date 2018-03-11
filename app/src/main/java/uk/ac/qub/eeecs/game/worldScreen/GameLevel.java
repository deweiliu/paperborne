package uk.ac.qub.eeecs.game.worldScreen;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.ui.PushButton;

/**
 * Created by Jamie T on 26/01/2018.
 * Class to represent a Game Level on the level selection World Screen
 */

public class GameLevel
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
	// TODO: Implement a level boss/hero for the player to play against
	
	/**
	 * Constructor for creating a level with no prerequisites
	 * @param id unique level ID
	 * @param name level name
	 * @param deck the cards that make up a deck that the opponent will play for this level
	 * @param button level button representation
	 */
	public GameLevel(String id, String name, PushButton button, List<LevelCard> deck)
	{
		this.id = id;
		this.name = name;
		this.button = button;
		this.deck = deck;
		this.prerequisites = new ArrayList<>();
	}
	
	/**
	 * Constructor for creating a level with required prerequisites
	 * @param id unique level ID
	 * @param name level name
	 * @param button level button representation
	 * @param deck the cards that make up a deck that the opponent will play for this level
	 * @param prerequisites list of levels that are required to be completed before this level
	 */
	public GameLevel(String id, String name, PushButton button, List<LevelCard> deck, List<String> prerequisites)
	{
		this.id = id;
		this.name = name;
		this.button = button;
		this.deck = deck;
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
}
