package uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetLoserName extends GetNameSuperClass {


    public GetLoserName(EndGameScreen gameScreen,User.UserName name) {
        super(gameScreen, name);
    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawText("Sorry for losing the game,", paint.getTextSize(), mScreen.getScreenHeight()/5+ paint.getTextSize(), super.paint);
        graphics2D.drawText("but we are happy to record your name :)", paint.getTextSize(), mScreen.getScreenHeight()/5+ 2*paint.getTextSize(), super.paint);
        super.draw(elapsedTime, graphics2D);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}
