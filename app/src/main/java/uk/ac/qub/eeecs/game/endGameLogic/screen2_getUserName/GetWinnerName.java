package uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.interface_superclass.GetNameSuperclass;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetWinnerName extends GetNameSuperclass {

    public GetWinnerName(EndGameScreen gameScreen, User.UserName name) {
        super(gameScreen, name);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);
        graphics2D.drawText("Congratulations for winning the game!",
                paint.getTextSize(), mScreen.getScreenHeight() / 5 + paint.getTextSize(), super.paint);
        graphics2D.drawText("Please leave your name below :)",
                paint.getTextSize(), mScreen.getScreenHeight() / 5 + 2 * paint.getTextSize(), super.paint);
    }

}
