package uk.ac.qub.eeecs.gage;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by Jamie on 12/04/2018.
 * Utility class to aid in creating unit tests
 */

public class TestUtil
{
	/**
	 * Convert the specified layer position from the layer viewport into
	 * the coordinate space defined for the screen viewport, storing the result
	 * in the specified screen Vector2 position.
	 *
	 * @param screenViewport Screen viewport
	 * @param screenPosition Calculated screen position
	 * @param layerViewport  Layer viewport
	 * @param layerPosition  Layer Position
	 */
	public static void convertLayerPosIntoScreen(ScreenViewport screenViewport, Vector2 screenPosition, LayerViewport layerViewport, Vector2 layerPosition)
	{
		// Calculate layer ratios
		float layerXRatio = (layerPosition.x - (int)layerViewport.getLeft()) / ((int)layerViewport.getRight() - (int)layerViewport.getLeft());
		float layerYRatio = (layerPosition.y - (int)layerViewport.getTop()) / ((int)layerViewport.getBottom() - (int)layerViewport.getTop());
		
		// Update screen positions
		screenPosition.x = (2.0f * layerXRatio) * ((float)screenViewport.width/2);
		screenPosition.y = (2.0f * layerYRatio) * ((float)screenViewport.height/2);
	}
	
	/**
	 * Creates touch events (Touch down and then up) on a game object's position, converts from
	 * the game object's layer coordinates into screen coordinates for touch
	 * @param object the object to touch
	 * @param layerViewport layer viewport the object is drawn with
	 * @param screenViewport screen viewport the object is drawn with
	 * @param iGraphics2D drawing interface
	 * @return list of touch events, first touch down then touch up on the objects position
	 */
	public static List<TouchEvent> touchObject(GameObject object, LayerViewport layerViewport, ScreenViewport screenViewport, IGraphics2D iGraphics2D)
	{
		object.draw(new ElapsedTime(), iGraphics2D, layerViewport, screenViewport);
		Vector2 touchPosition = new Vector2();
		convertLayerPosIntoScreen(screenViewport, touchPosition, layerViewport, object.position);
		return touchObject(touchPosition);
	}
	
	/**
	 * Creates touch events (Touch down and then up) on a game object's position
	 *
	 * @param object the object to touch
	 * @param iGraphics2D drawing interface
	 * @return list of touch events, first touch down then touch up on the objects position
	 */
	public static List<TouchEvent> touchObject(GameObject object, IGraphics2D iGraphics2D)
	{
		object.draw(new ElapsedTime(), iGraphics2D);
		return touchObject(object.position);
	}
	
	/**
	 * Creates touch events on the touch position provided
	 * @param touchPosition the position to create the touch events on
	 * @return list of touch events, first touch down then touch up on the touch position
	 */
	private static List<TouchEvent> touchObject(Vector2 touchPosition)
	{
		// Set up a touch down event on the button
		TouchEvent touchDown = new TouchEvent();
		touchDown.x = touchPosition.x;
		touchDown.y = touchPosition.y;
		touchDown.type = TouchEvent.TOUCH_DOWN;
		List<TouchEvent> touchEvents = new ArrayList<>();
		touchEvents.add(touchDown);
		// Set up a touch up event on the button
		TouchEvent touchUp = new TouchEvent();
		touchUp.x = touchPosition.x;
		touchUp.y = touchPosition.y;
		touchUp.type = TouchEvent.TOUCH_UP;
		touchEvents.add(touchUp);
		return touchEvents;
	}
}
