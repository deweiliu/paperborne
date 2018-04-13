package uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens.EndGameScreenSuperclass;
import uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName.User;
import uk.ac.qub.eeecs.game.endGameLogic.screen3_showRecords.interface_superclass.RecordsInterface;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class ShowRecordsScreen extends EndGameScreenSuperclass {
    private RecordsManager manager;
    private RecordsInterface showRecords;
    private User playerName;


    public ShowRecordsScreen(EndGameController controller, User playerName) {
        super(controller);

        this.playerName = playerName;

        manager = new RecordsManager(getGame().getContext());
        if (playerName.isSinglePlayer()) {
            if (playerName.isWinnerPlayer1()) {
                manager.addWinner(playerName.getWinner());
            } else {
                manager.addLoser(playerName.getLoser());
            }
        } else {
            manager.addLoser(playerName.getLoser());
            manager.addWinner(playerName.getWinner());
        }
        showRecords = new CurrentPlayerRecord(this, playerName, manager);

    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);

        showRecords.update(elapsedTime);

        if (showRecords.isFinished()) {

            //Switch between two record screens
            if (showRecords instanceof CurrentPlayerRecord) {
                showRecords = new HistoricalPlayersRecords(this, playerName, manager);
            } else if (showRecords instanceof HistoricalPlayersRecords) {
                showRecords = new CurrentPlayerRecord(this, playerName, manager);
            } else {
                //Log.d("Some thing goes wrong.", "");
            }
        }
    }
    public void homeButtonPushed(){
        isFinished = true;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);
        showRecords.draw(elapsedTime, graphics2D);


    }

}
