package uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetNameScreen implements EndGameScreen {


    private boolean isFinished = false;
    private int nameReady;
    private EndGameController controller;
    private boolean isSinglePlayer;
    private GetNameSuperClass getName;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private boolean stopDraw = false;
    private boolean turningToNext = false;
private User user;
    public GetNameScreen(EndGameController endGameController) {
        super();
        this.controller = endGameController;

        user=new User(controller.isSinglePlayer(),controller.isPlayer1Wins());
        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);
        this.isSinglePlayer = endGameController.isSinglePlayer();
        nameReady = 0;
        getName = null;

        final float LOADING_SIZE = mLayerViewport.getHeight() / 4;


    }

      private void updateForWinner(ElapsedTime elapsedTime) {
        if (getName == null || getName instanceof GetLoserName) {
            User.UserName winner= user.new UserName();
user.setWinner(winner);
            getName = new GetWinnerName(this,winner);
        } else {
            getName.update(elapsedTime);
            if (getName.isFinished()) {
                this.nameReady++;
            }
        }
    }

    private void updateForLoser(ElapsedTime elapsedTime) {
        if (getName == null || getName instanceof GetWinnerName) {
            User.UserName loser=user.new UserName();
            user.setLoser(loser);
            getName = new GetLoserName(this,loser);
        } else {
            getName.update(elapsedTime);
            if (getName.isFinished()) {
                this.nameReady++;
            }
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (this.turningToNext) {

        } else {

            //Update get UserName object
            if (isSinglePlayer) {
                if (controller.isPlayer1Wins()) {
                    updateForWinner(elapsedTime);
                } else {
                    updateForLoser(elapsedTime);
                }
                if (nameReady == 1) {
                    isFinished = true;
                }
            } else {
                switch (nameReady) {
                    case 0:
                        updateForWinner(elapsedTime);
                        break;
                    case 1:
                        updateForLoser(elapsedTime);
                        break;
                    case 2:
                        isFinished = true;
                        break;
                    default:
                        break;
                }

            }
            if (getName == null) {
                stopDraw = true;
                ;
            }
        }
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (this.stopDraw) {
            this.stopDraw = false;
        } else {
            getName.draw(elapsedTime, graphics2D);
        }
    }
public User getUserName(){
        return this.user;
}
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public GameScreen getGameScreen() {
        return controller;
    }

    @Override
    public Game getGame() {
        return controller.getGame();
    }

    @Override
    public int getScreenWidth() {
        return getGame().getScreenWidth();
    }

    @Override
    public int getScreenHeight() {
        return getGame().getScreenHeight();
    }

    @Override
    public LayerViewport getLayerViewport() {
        return mLayerViewport;
    }

    @Override
    public ScreenViewport getScreenViewPort() {
        return mScreenViewport;
    }

    @Override
    public AssetStore getAssetManager() {
        return getGame().getAssetManager();
    }
}
