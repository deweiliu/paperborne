package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.playerName;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class PlayerName implements EndGameStuff {
    private boolean isSinglePlayer;
    private String winnerName = null;
    private String loserName = null;

    public PlayerName(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }


    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getLoserName() {
        return loserName;
    }

    public void setLoserName(String loserName) {
        this.loserName = loserName;
    }
}
