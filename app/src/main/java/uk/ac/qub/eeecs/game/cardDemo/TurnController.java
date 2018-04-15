package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created on 13 Apr
 * For improving code quality of card demo screen
 * created this class to put those things together
 */
public class TurnController {

    private static long TURN_TIME = 30000; //milliseconds

    private boolean endTurnButtonPushed;
    private Paint mPaint;
    private PushButton mEndTurnButton;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private boolean playerTurn;
    private long startTime;
    private Runnable endTurnTask;
    private Handler turnHandler;
    private CardDemoScreen gameScreen;


    public TurnController(CardDemoScreen cardDemoScreen) {
        //Set up variables
        playerTurn = true;
        gameScreen = cardDemoScreen;
        Game game = gameScreen.getGame();

        /*******************************************************************/

        //Set up end turn button
        game.getAssetManager().loadAndAddBitmap("EndTurn", "img/Board/End_Turn.png");
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, mScreenViewport.width / 2, mScreenViewport.height / 2);

        final float BUTTON_SIZE = mLayerViewport.halfWidth / 7;
        mEndTurnButton = new PushButton(mLayerViewport.getWidth() / 10.0f, mLayerViewport.getHeight() / 2.0f,
                BUTTON_SIZE * 2, BUTTON_SIZE, "EndTurn", gameScreen);

        /*******************************************************************/

        //paint for status
        mPaint = new Paint();
        mPaint.setTextSize(cardDemoScreen.getGame().getScreenWidth() / 48);
        mPaint.setARGB(255, 255, 255, 255);
        mPaint.setShadowLayer(5.0f, 2.0f, 2.0f, Color.BLACK);

        //get the runnable
        endTurnTask = cardDemoScreen.getEndTurn();
        turnHandler = new Handler(Looper.getMainLooper());
        turnHandler.postDelayed(this.endTurnTask, TURN_TIME);

        //record the start time for first turn
        startTime = System.currentTimeMillis();
    }

    public void switchTurn() {
        playerTurn = !playerTurn;
        turnHandler.postDelayed(this.endTurnTask, TURN_TIME);
        startTime = System.currentTimeMillis();
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public static long getTurnTime() {
        return TURN_TIME;
    }


    /**
     * @return if the end turn button is pushed IN THIS UPDATE
     */
    public boolean isEndTurnButtonPushed() {
        return endTurnButtonPushed;
    }

    public void update(ElapsedTime elapsedTime) {
        if (playerTurn)
            mEndTurnButton.update(elapsedTime, mLayerViewport, mScreenViewport);
        if (endTurnButtonPushed = mEndTurnButton.isPushTriggered()) {
            this.doEndTurn();
        }
    }

    public void doEndTurn() {
        turnHandler.removeCallbacks(endTurnTask);
        endTurnTask.run();
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        String turnRemaining = (((startTime + TURN_TIME) - System.currentTimeMillis()) / 1000) + " seconds left in current turn";
        String whoseTurn = "Player turn: " + playerTurn;
        String canInteract = "Player " + (playerTurn ? "can" : "cannot") + " interact";

        int i = 0;
        final float x = gameScreen.getGame().getScreenWidth() / 40;
        graphics2D.drawText(turnRemaining, x, mPaint.getTextSize() * (++i), mPaint);
        graphics2D.drawText(whoseTurn, x, mPaint.getTextSize() * (++i), mPaint);
        graphics2D.drawText(canInteract, x, mPaint.getTextSize() * (++i), mPaint);
        mEndTurnButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

    }
}
