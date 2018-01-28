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
	// Level ID
	private String id;
	// Level name
	private String name;
	// Level button representation
	private PushButton button;
	// Prerequisite levels required to be completed before this level is playable
	private List<GameLevel> prerequisites;
	// TODO: Implement a level deck to be used in this level's battle
	// TODO: Implement a level boss/hero for the player to play against
	
	/**
	 * Constructor for creating a level with no prerequisites
	 * @param id unique level ID
	 * @param name level name
	 * @param button level button representation
	 */
	public GameLevel(String id, String name, PushButton button)
	{
		this.id = id;
		this.name = name;
		this.button = button;
		this.prerequisites = new ArrayList<>();
	}
	
	/**
	 * Constructor for creating a level with required prerequisites
	 * @param id unique level ID
	 * @param name level name
	 * @param button level button representation
	 * @param prerequisites list of levels that are required to be completed before this level
	 */
	public GameLevel(String id, String name, PushButton button, List<GameLevel> prerequisites)
	{
		this.id = id;
		this.name = name;
		this.button = button;
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
	
	public List<GameLevel> getPrerequisites()
	{
		return prerequisites;
	}
	
	public void setPrerequisites(List<GameLevel> prerequisites)
	{
		this.prerequisites = prerequisites;
	}
}
