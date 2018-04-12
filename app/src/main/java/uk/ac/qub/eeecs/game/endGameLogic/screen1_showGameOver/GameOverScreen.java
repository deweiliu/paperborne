package uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreenSuperclass;
import uk.ac.qub.eeecs.game.endGameLogic.screen1_showGameOver.interface_superclass.GameOverInterface;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GameOverScreen extends EndGameScreenSuperclass {

    //In millisecond
    private final static long ANIMATION_PERIOD = 3000;
    private Paint mPaint;
    private GameOverInterface mAnimation;
    private final static int TEXT_SIZE = 100;

    public GameOverScreen(EndGameController controller) {
        super(controller);

        //Set up the paint
        mPaint = new Paint();
        this.mPaint.setColor(Color.GREEN);
        this.mPaint.setTextSize(TEXT_SIZE);


        final String WIN_ANIMATION_NAME = "WinAnimation";
        getAssetManager().loadAndAddBitmap(WIN_ANIMATION_NAME, "img/End Game Logic/you-win.png");
        Bitmap winAnimation = getAssetManager().getBitmap(WIN_ANIMATION_NAME);

        final String LOSE_ANIMATION_NAME = "LoseAnimation";
        getAssetManager().loadAndAddBitmap(LOSE_ANIMATION_NAME, "img/End Game Logic/you-lose.png");
        Bitmap loseAnimation = getAssetManager().getBitmap(LOSE_ANIMATION_NAME);

        if (isSinglePlayer()) {
            if (hasPlayer1Won()) {
                mAnimation = new SinglePlayerGameOver(this, winAnimation);
            } else {
                mAnimation = new SinglePlayerGameOver(this, loseAnimation);
            }
        } else {
            if (hasPlayer1Won()) {
                mAnimation = new TwoPlayerGameOver(this, winAnimation, loseAnimation);
            } else {
                mAnimation = new TwoPlayerGameOver(this, loseAnimation, winAnimation);

            }

        }
        mAnimation.start(ANIMATION_PERIOD);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        mAnimation.update(elapsedTime);
        if (mAnimation.isFinished()) {
            if (getGame().getInput().existsTouch(0)) {
                isFinished = true;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);
        super.getCardDemoScreen().draw(elapsedTime, graphics2D);
        mAnimation.draw(elapsedTime, graphics2D);
        if (mAnimation.isFinished()) {

            final float textPositionX = getGame().getScreenWidth() / 5;
            final String text = "Touch Screen to Continue...";

            if (isSinglePlayer()) {
                graphics2D.drawText(text, textPositionX, getGame().getScreenHeight() * 4 / 5, this.mPaint);
            } else {
                graphics2D.drawText(text, textPositionX, (getGame().getScreenHeight() - TEXT_SIZE) / 2, this.mPaint);
            }
        }
    }


}


