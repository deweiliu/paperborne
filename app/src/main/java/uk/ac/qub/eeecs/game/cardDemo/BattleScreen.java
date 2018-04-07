package uk.ac.qub.eeecs.game.cardDemo;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.Game;

/**
 * Created by nshah on 06/12/2017.
 */

public class BattleScreen extends GameScreen {

    private Board board;

    public BattleScreen(Game game){
        super("BattleScreen", game);
       // board = new Board();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }
}
