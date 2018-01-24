package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.playerName;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetLoserName extends PlayerName {
    public GetLoserName(boolean isSinglePlayer) {
        super(isSinglePlayer);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
