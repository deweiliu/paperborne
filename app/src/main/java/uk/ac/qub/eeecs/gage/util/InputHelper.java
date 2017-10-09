package uk.ac.qub.eeecs.gage.util;

import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

public final class InputHelper {

    // /////////////////////////////////////////////////////////////////////////
    // Screen and Layer Mappings
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Convert the specified screen position from the screen viewport into
     * the coordinate space defined for the layer viewport, storing the result
     * in the specified layer Vector2 position.
     *
     * @param screenViewport Screen viewport enclosing the screen position
     * @param screenPosition Screen position
     * @param layerViewport  Layer viewport
     * @param layerPosition  Calculated layer position
     */
    public static final void convertScreenPosIntoLayer(
            ScreenViewport screenViewport, Vector2 screenPosition,
            LayerViewport layerViewport, Vector2 layerPosition) {

        // Convert screen coordinate into [-0.5, 0.5] range
        float screenXRatio = (float) (screenPosition.x - screenViewport.left)
                / (float) (screenViewport.right - screenViewport.left) - 0.5f;
        float screenYRatio = (float) (screenPosition.y - screenViewport.bottom)
                / (float) (screenViewport.top - screenViewport.bottom) - 0.5f;

        // Determine layer coordinate
        layerPosition.x = layerViewport.x + 2.0f * screenXRatio * layerViewport.halfWidth;
        layerPosition.y = layerViewport.y + 2.0f * screenYRatio * layerViewport.halfHeight;
    }

    /**
     * Convert the specified screen position from the screen viewport into
     * the coordinate space defined for the layer viewport, storing the result
     * in the specified layer Vector2 position.
     *
     * @param screenViewport Screen viewport enclosing the screen position
     * @param screenX        Screen x position
     * @param screenY        Screen y position
     * @param layerViewport  Layer viewport
     * @param layerPosition  Calculated layer position
     */
    public static final void convertScreenPosIntoLayer(
            ScreenViewport screenViewport, float screenX, float screenY,
            LayerViewport layerViewport, Vector2 layerPosition) {

        // Convert screen coordinate into [-0.5, 0.5] range
        float screenXRatio = (float) (screenX - screenViewport.left)
                / (float) (screenViewport.right - screenViewport.left) - 0.5f;
        float screenYRatio = (float) (screenY - screenViewport.bottom)
                / (float) (screenViewport.top - screenViewport.bottom) - 0.5f;

        // Determine layer coordinate
        layerPosition.x = layerViewport.x + 2.0f * screenXRatio * layerViewport.halfWidth;
        layerPosition.y = layerViewport.y + 2.0f * screenYRatio * layerViewport.halfHeight;
    }
}
