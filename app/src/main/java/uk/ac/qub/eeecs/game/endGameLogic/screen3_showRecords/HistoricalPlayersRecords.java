package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Comparator;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class HistoricalPlayersRecords extends RecordsSuperClass {
private   ArrayList<UserRecord> users;
private final static int NUMBER_OF_PLAYER=3;
    public HistoricalPlayersRecords(EndGameScreen mEndGameScreen, User user, RecordsManager manager) {
        super(mEndGameScreen, user, manager);
         users = new ArrayList<>();

        for (String name : manager.getNames()) {
            int wins, loses;
            wins = manager.getTimesOfWin(name);
            loses = manager.getTimesOfLose(name);
            users.add(new UserRecord(name, wins, loses));
        }

        //Pick 3 players with highest win rate
        users.sort(new MyComparator());
        while(users.size()>NUMBER_OF_PLAYER){
            users.remove(NUMBER_OF_PLAYER);
        }



    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);
        float width=mScreen.getScreenWidth();
        float height=mScreen.getScreenHeight();
        float gap=width/NUMBER_OF_PLAYER/4;
        int index=1;
        for(UserRecord each:users){
            each.draw(elapsedTime,graphics2D,index*width/4+gap,height,mPaint);
            index++;
        }

        graphics2D.drawText("Name:",0,height/5*2-mPaint.getTextSize(),mPaint);
        graphics2D.drawText("Win:",0,height/5*3-mPaint.getTextSize(),mPaint);
        graphics2D.drawText("Lose:",0,height/5*4-mPaint.getTextSize(),mPaint);
        graphics2D.drawText("Win rate:",0,height/5*5-mPaint.getTextSize(),mPaint);
        graphics2D.drawText("Showing the 3 players with heghest win rate historically",0,height,mPaint);
  }

    @Override
    public boolean isFinished() {
        return super.isFinished;
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

        public int getTimesOfWin() {
            return timesOfWin;
        }

        public int getTimesOfLose() {
            return timesOfLose;
        }

        public double winRate() {
            return (double) timesOfWin / (timesOfLose + timesOfWin);
        }

        public String getName() {
            return name;
        }

        public UserRecord(String name, int timesOfWin, int timesOfLose) {
            this.timesOfWin = timesOfWin;
            this.timesOfLose = timesOfLose;
            this.name = name;
        }
        public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, float x, float screenHeight, Paint paint) {
            graphics2D.drawText(name,x,screenHeight/5*2-paint.getTextSize(),paint);
            graphics2D.drawText(""+timesOfWin,x,screenHeight/5*3-paint.getTextSize(),paint);
            graphics2D.drawText(""+timesOfLose,x,screenHeight/5*4-paint.getTextSize(),paint);
            graphics2D.drawText(""+this.winRate(),x,screenHeight/5*5-paint.getTextSize(),paint);
        }
    }
}
