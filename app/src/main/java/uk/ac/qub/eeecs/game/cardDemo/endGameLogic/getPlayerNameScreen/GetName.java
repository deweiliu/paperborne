package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen;

import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public abstract class GetName implements GetNameInterface {
    protected User user;
    private long startTime;
    private long elapsedTime;
    private final static long PERIOD = 5000;
    private boolean isFinished;
    protected Paint mPaint;
    protected EndGameScreen mEndGameScreen;

    public GetName(EndGameScreen gameScreen, boolean isWinner) {
        this.mEndGameScreen = gameScreen;
        startTime = System.currentTimeMillis();
        isFinished = false;
        mPaint = new Paint();
        mPaint.setTextSize(100);
        mPaint.setColor(Color.WHITE);

        //Set default name
        user = new User("Hello Jamie", isWinner);
    }
@Override
    public User getUserInfo() {
        return this.user;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if ((this.elapsedTime = System.currentTimeMillis() - startTime) > PERIOD) {
            this.isFinished = true;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        String text = String.format("Hello, we are getting the name...%d", (int) ((PERIOD) - (this.elapsedTime)) / 1000);
        graphics2D.drawText(text, 0, 100, mPaint);
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }


}
