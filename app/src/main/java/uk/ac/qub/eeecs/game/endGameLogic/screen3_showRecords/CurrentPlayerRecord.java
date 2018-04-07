package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.interface_superclass.RecordsSuperclass;
import uk.ac.qub.eeecs.game.ui.Moving;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class CurrentPlayerRecord extends RecordsSuperclass {
    private String name;
    private int timesOfWin, timesOfLose;
    private Moving stamp;

    public CurrentPlayerRecord(EndGameScreen mEndGameScreen, User user, RecordsManager manager) {
        super(mEndGameScreen, user, manager);
        String stampName;
        /*****************************************************************************/
        //Set up the player name to display
        if (this.user.isSinglePlayer()) {
            if (this.user.isWinnerPlayer1()) {
                name = this.user.getWinner();
                stampName = "you-win.png";
            } else {
                name = this.user.getLoser();
                stampName = "you-lose.png";

            }
        } else {
            name = this.user.getWinner();
            stampName = "you-win.png";

        }
        /*****************************************************************************/

        //Get historical information about this player
        timesOfLose = manager.getTimesOfLose(name);
        timesOfWin = manager.getTimesOfWin(name);

        /*****************************************************************************/
        //Set up the stamp
        mScreen.getAssetManager().loadAndAddBitmap("Stamp", "img/End Game Logic/" + stampName);
        stamp = new Moving(mScreen.getLayerViewport().getWidth() / 2, mScreen.getLayerViewport().getBottom(), mScreen.getScreenWidth() / 4,
                mScreen.getScreenWidth() / 5, mScreen.getAssetManager().getBitmap("Stamp"), mScreen.getGameScreen());
        stamp.setDestination(mScreen.getLayerViewport().halfWidth / 2, (int) (mScreen.getLayerViewport().halfHeight / 5 * 3));
        stamp.start(2000);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        stamp.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);

        //Display name
        graphics2D.drawText("Name: ", 100, mScreen.getScreenHeight() / 5 * 3 - mPaint.getTextSize(), mPaint);
        graphics2D.drawText(name, mScreen.getScreenWidth() / 5 * 3, mScreen.getScreenHeight() / 5 * 3 - mPaint.getTextSize(), mPaint);

        //Display times of win
        graphics2D.drawText("Times of win", 100, mScreen.getScreenHeight() / 5 * 4 - mPaint.getTextSize(), mPaint);
        graphics2D.drawText("" + timesOfWin, mScreen.getScreenWidth() / 5 * 3, mScreen.getScreenHeight() / 5 * 4 - mPaint.getTextSize(), mPaint);

        //Display times of lose
        graphics2D.drawText("Times of lose", 100, mScreen.getScreenHeight() / 5 * 5 - mPaint.getTextSize(), mPaint);
        graphics2D.drawText("" + timesOfLose, mScreen.getScreenWidth() / 5 * 3, mScreen.getScreenHeight() / 5 * 5 - mPaint.getTextSize(), mPaint);

        stamp.draw(elapsedTime, graphics2D, mScreen.getLayerViewport(), mScreen.getScreenViewPort());
    }
}