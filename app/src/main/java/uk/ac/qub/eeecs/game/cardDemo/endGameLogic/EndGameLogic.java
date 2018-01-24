package uk.ac.qub.eeecs.game.cardDemo.endGameLogic;

import android.graphics.Color;
import android.util.Log;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.playerName.GetPlayerNameScreen;

import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.playerName.PlayerName;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.gameOverAnimations.GameOverScreen;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.recordsScreen.RecordsScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class EndGameLogic extends GameScreen {
    private static final int GAME_OVER_ANIMATIONS = 0;
    private static final int PLAYERS_NAME = 1;
    private static final int RECORDS_SCREEN = 2;

    private int statue = -1;
    private boolean isSinglePlayer;
    private boolean isPlayer1Wins;
    private EndGameScreen mEndGameScreen = null;
    private GameScreen mBattleScreen;
    private PlayerName mPlayerName;
    private boolean stopDraw = false;

    public EndGameLogic(GameScreen battleScreen, boolean isSinglePlayer, boolean isPlayer1Wins) {
        super("EndGameLogic", battleScreen.getGame());
        this.mBattleScreen = battleScreen;
        this.isSinglePlayer = isSinglePlayer;
        this.isPlayer1Wins = isPlayer1Wins;


        this.mEndGameScreen = new GameOverScreen(this);
        this.statue = GAME_OVER_ANIMATIONS;


        mGame.getScreenManager().removeScreen(mBattleScreen.getName());
        mGame.getScreenManager().addScreen(this);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        mEndGameScreen.update(elapsedTime);
        if (mEndGameScreen.isFinished()) {
            nextScreen();
            Log.d("End Game Logic ", ".update(ElapsedTime) called.");
            this.stopDraw = true;
        }
    }

    private void nextScreen() {
        switch (this.statue) {
            //From game over animations to get player name
            case GAME_OVER_ANIMATIONS:
                this.mEndGameScreen = new GetPlayerNameScreen(this);
                this.statue = PLAYERS_NAME;
                break;

            //From get player name to records screen
            case PLAYERS_NAME:
                this.mEndGameScreen = new RecordsScreen(this, this.mPlayerName);
                this.statue = RECORDS_SCREEN;
                break;

            //From records screen to the main menu.
            case RECORDS_SCREEN:
                mGame.getScreenManager().removeScreen(this.getName());
                mGame.getScreenManager().addScreen(new MenuScreen(super.mGame));
                break;

            //The current screen is null. Create a game over animations
            default:
                this.mEndGameScreen = new GameOverScreen(this);
                this.statue = GAME_OVER_ANIMATIONS;
                break;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (stopDraw) {
            stopDraw = false;
        } else {
            graphics2D.clear(Color.WHITE);
            mEndGameScreen.draw(elapsedTime, graphics2D);
        }
    }

    public void setmPlayerName(PlayerName mPlayerName) {
        this.mPlayerName = mPlayerName;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public boolean isPlayer1Wins() {
        return isPlayer1Wins;
    }

    public GameScreen getmBattleScreen() {
        return mBattleScreen;
    }
}
