package uk.ac.qub.eeecs.game.gameHelp.helpScreens;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.RecordsManager;
import uk.ac.qub.eeecs.game.gameHelp.GameHelpController;

/**
 * Created by 40216004 Dewei Liu on 01/04/2018.
 */

public class ForTestingEndGameLogic extends HelpScreenSuperClass {

    //Testing Buttons
    private ArrayList<PushButton> buttons;
    private PushButton youWin_SinglePlayer;
    private PushButton youLose_SinglePlayer;
    private PushButton youWin_MultiplePlayer;
    private PushButton youLose_MultiplePlayer;
    private PushButton cleanRecords;

    private ArrayList<String> text;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param game       Game instance to which the game screen belongs
     * @param controller
     */
    public ForTestingEndGameLogic(Game game, GameHelpController controller) {
        super("For Developer to Test End Game Logic", game, controller);

        //Set up buttons
        final String BUTTON_NAME = "button testing";
        assetManager.loadAndAddBitmap(BUTTON_NAME, "img/Game Help/" + BUTTON_NAME + ".png");
        float width = mGame.getScreenWidth();
        float height = mGame.getScreenHeight();
        youWin_SinglePlayer = new PushButton(width / 4, height / 8 * 3, width / 4, height / 4, BUTTON_NAME, this);
        youLose_SinglePlayer = new PushButton(width / 4 * 3, height / 8 * 3, width / 4, height / 4, BUTTON_NAME, this);
        youWin_MultiplePlayer = new PushButton(width / 4, height / 8 * 5, width / 4, height / 4, BUTTON_NAME, this);
        youLose_MultiplePlayer = new PushButton(width / 4 * 3, height / 8 * 5, width / 4, height / 4, BUTTON_NAME, this);
        cleanRecords = new PushButton(width / 4 * 3, height / 8 * 7, width / 4, height / 4, BUTTON_NAME, this);

        buttons = new ArrayList<>();
        buttons.add(youWin_SinglePlayer);
        buttons.add(youLose_SinglePlayer);
        buttons.add(youWin_MultiplePlayer);
        buttons.add(youLose_MultiplePlayer);
        buttons.add(cleanRecords);
        for (PushButton each : buttons) {
            each.processInLayerSpace(false);
        }

        //Set up texts
        text = new ArrayList<>();
        text.add("Wow, incredible that you found this land!");
        text.add("This is NOT a part of this game.");
        text.add("It is for developers to test the end game logic only.");
        text.add("So you don't need to play the game again and again to test the end game logic");
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        for (PushButton each : buttons) {
            each.update(elapsedTime);
        }

        if (youWin_SinglePlayer.isPushTriggered()) {
            new EndGameController(this, true, true);
        } else if (youLose_SinglePlayer.isPushTriggered()) {
            new EndGameController(this, true, false);
        } else if (youWin_MultiplePlayer.isPushTriggered()) {
            new EndGameController(this, false, true);
        } else if (youLose_MultiplePlayer.isPushTriggered()) {
            new EndGameController(this, false, false);
        } else if (cleanRecords.isPushTriggered()) {
            SharedPreferences.Editor editor = mGame.getContext().getSharedPreferences(RecordsManager.PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            super.setPopUpMessage("Hello, you have erased all records", 2);
        }


    }

    @Override
    protected void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //Draw buttons
        for (PushButton each : buttons) {
            each.draw(elapsedTime, graphics2D);
        }
        float width = mGame.getScreenWidth();
        float height = mGame.getScreenHeight();
        graphics2D.drawText("You win _ single Player", width / 4 - width / 8, height / 8 * 3, mPaint);
        graphics2D.drawText("You lose _ single Player", width / 4 * 3 - width / 8, height / 8 * 3, mPaint);
        graphics2D.drawText("You win _ multiple player", width / 4 - width / 8, height / 8 * 5, mPaint);
        graphics2D.drawText("You lose _ multiple player", width / 4 * 3 - width / 8, height / 8 * 5, mPaint);
        graphics2D.drawText("Erase all records", width / 4 * 3 - width / 8, height / 8 * 7, mPaint);

        /****************************************************************************************************************/
        //Draw prompt text
        int index = text.size() - 1;
        float x = mGame.getScreenWidth() / 8;
        for (String each : text) {
            graphics2D.drawText(each, x, mGame.getScreenHeight() - mPaint.getTextSize() * index--, mPaint);
        }
    }
}
