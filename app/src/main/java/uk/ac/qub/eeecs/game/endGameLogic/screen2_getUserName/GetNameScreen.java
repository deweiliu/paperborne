package uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreenSuperClass;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.interface_superclass.GetNameInterface;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetNameScreen extends EndGameScreenSuperClass {

    //hello this work
    private GetNameInterface getName;
    private User user;

    private boolean needWinnerName = true, needLoserName = true;

    public GetNameScreen(EndGameController controller) {
        super(controller);

        user = new User(isSinglePlayer(), hasPlayer1Won());
        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);
        getName = null;
        if (isSinglePlayer()) {
            if (hasPlayer1Won()) {
                needLoserName = false;
            } else {
                needWinnerName = false;
            }
        }
    }

    private void updateForWinner(ElapsedTime elapsedTime) {
        if (getName == null || getName instanceof GetLoserName) {
            User.UserName winner = user.new UserName();
            user.setWinner(winner);
            getName = new GetWinnerName(this, winner);
        } else {
            getName.update(elapsedTime);
            if (getName.isFinished()) {
                needWinnerName = false;
            }
        }
    }

    private void updateForLoser(ElapsedTime elapsedTime) {
        if (getName == null || getName instanceof GetWinnerName) {
            User.UserName loser = user.new UserName();
            user.setLoser(loser);
            getName = new GetLoserName(this, loser);
        } else {
            getName.update(elapsedTime);
            if (getName.isFinished()) {
                needLoserName = false;
            }
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        if (needWinnerName) {
            updateForWinner(elapsedTime);
        } else {
            if (needLoserName) {
                updateForLoser(elapsedTime);
            } else {
                isFinished = true;
            }
        }

    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (getName != null) {
            super.draw(elapsedTime, graphics2D);

            getName.draw(elapsedTime, graphics2D);
        }
    }

    public User getUserName() {
        return this.user;
    }

}
