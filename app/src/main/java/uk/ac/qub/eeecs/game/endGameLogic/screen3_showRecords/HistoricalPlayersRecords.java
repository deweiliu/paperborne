package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;

import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Comparator;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.interface_superclass.RecordsSuperclass;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class HistoricalPlayersRecords extends RecordsSuperclass {
    private ArrayList<UserRecord> users;
    private final static int NUMBER_OF_PLAYER = 3;

    HistoricalPlayersRecords(EndGameScreen mEndGameScreen, User user, RecordsManager manager) {
        super(mEndGameScreen, user, manager);

        //Load all uers information
        users = new ArrayList<>();
        for (String name : manager.getNames()) {
            int wins = manager.getTimesOfWin(name);
            int loses = manager.getTimesOfLose(name);
            users.add(new UserRecord(name, wins, loses));
        }

        //Pick 3 players with highest win rate
        users.sort(new MyComparator());
        while (users.size() > NUMBER_OF_PLAYER) {
            users.remove(NUMBER_OF_PLAYER);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);
        float width = mScreen.getScreenWidth();
        float height = mScreen.getScreenHeight();
        float gap = width / NUMBER_OF_PLAYER / 4;
        int index = 1;
        for (UserRecord each : users) {
            each.draw(elapsedTime, graphics2D, index++ * width / 4 + gap, height, mPaint);
        }

        //print fields name
        index = 1;
        graphics2D.drawText("Name:", 0, ((height / 5) * ++index) - mPaint.getTextSize(), mPaint);
        graphics2D.drawText("Win:", 0, ((height / 5) * ++index) - mPaint.getTextSize(), mPaint);
        graphics2D.drawText("Lose:", 0, ((height / 5) * ++index) - mPaint.getTextSize(), mPaint);
        graphics2D.drawText("Win rate:", 0, ((height / 5) * ++index) - mPaint.getTextSize(), mPaint);
        graphics2D.drawText("Showing the " + NUMBER_OF_PLAYER + " players with highest win rate historically", 0, height, mPaint);
    }


    private class MyComparator implements Comparator<UserRecord> {

        @Override
        public int compare(UserRecord o1, UserRecord o2) {
            if (o1.winRate() > o2.winRate()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private class UserRecord {
        private int timesOfWin;
        private int timesOfLose;
        private String name;

        public double winRate() {
            return (double) timesOfWin / (timesOfLose + timesOfWin);
        }

        UserRecord(String name, int timesOfWin, int timesOfLose) {
            this.timesOfWin = timesOfWin;
            this.timesOfLose = timesOfLose;
            this.name = name;
        }

        public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, float x, float screenHeight, Paint paint) {
            graphics2D.drawText(name, x, screenHeight / 5 * 2 - paint.getTextSize(), paint);
            graphics2D.drawText("" + timesOfWin, x, screenHeight / 5 * 3 - paint.getTextSize(), paint);
            graphics2D.drawText("" + timesOfLose, x, screenHeight / 5 * 4 - paint.getTextSize(), paint);
            graphics2D.drawText(String.format("%.0f%%", this.winRate() * 100), x, screenHeight / 5 * 5 - paint.getTextSize(), paint);
        }
    }
}
