package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetWinnerName extends GetName {

    public GetWinnerName(EndGameScreen gameScreen) {
        super(gameScreen, true);
    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawText("Here is get winner name screen", 0, super.mEndGameScreen.getLayerViewport().getTop() - 200, super.mPaint);
        super.draw(elapsedTime, graphics2D);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}
