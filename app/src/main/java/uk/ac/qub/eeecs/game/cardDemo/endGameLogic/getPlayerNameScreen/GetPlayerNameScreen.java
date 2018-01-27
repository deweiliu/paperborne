package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameLogic;
import uk.ac.qub.eeecs.game.cardDemo.endGameLogic.EndGameScreen;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public class GetPlayerNameScreen implements EndGameScreen {


    private boolean isFinished = false;
    private User winner, loser;
    private int nameReady;
    private EndGameLogic endGameLogic;
    private boolean isSinglePlayer;
    private GetNameInterface getName;
    private ScreenViewport mScreenViewport;
    private LayerViewport mLayerViewport;
    private boolean stopDraw = false;
    private GameObject backGround;
private boolean turningToNext=false;
    public GetPlayerNameScreen(EndGameLogic endGameLogic) {
        super();
        this.endGameLogic = endGameLogic;
        mScreenViewport = new ScreenViewport(0, 0, getGame().getScreenWidth(), getGame().getScreenHeight());
        mLayerViewport = new LayerViewport(0, 0, getGame().getScreenWidth() / 2, getGame().getScreenHeight() / 2);
        this.isSinglePlayer = endGameLogic.isSinglePlayer();
        nameReady = 0;
        getName = null;
        backGround = new GameObject(getLayerViewport().x, getLayerViewport().y,
                getLayerViewport().getWidth(), getLayerViewport().getHeight(),
                endGameLogic.getBackGround(), endGameLogic);

        final float LOADING_SIZE = mLayerViewport.getHeight() / 4;

    }

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }

    private void updateForWinner(ElapsedTime elapsedTime) {
        if (getName == null|| getName instanceof GetLoserName) {
            getName = new GetWinnerName(this);
        } else {
            getName.update(elapsedTime);
            if (getName.isFinished()) {
                winner = getName.getUserInfo();
                this.nameReady++;
            }
        }
    }

    private void updateForLoser(ElapsedTime elapsedTime) {
        if (getName == null||getName instanceof GetWinnerName) {
            getName = new GetLoserName(this);
        } else {
            getName.update(elapsedTime);
            if (getName.isFinished()) {
                loser = getName.getUserInfo();
                this.nameReady++;
            }
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if(this.turningToNext){

        }else{
        backGround.update(elapsedTime);

        //Update get name object
        if (isSinglePlayer) {
            if (endGameLogic.isPlayer1Wins()) {
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
        }}
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (this.stopDraw) {
            this.stopDraw = false;
        } else {
            backGround.draw(elapsedTime, graphics2D, getLayerViewport(), getScreenViewPort());
            getName.draw(elapsedTime, graphics2D);
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public GameScreen getGameScreen() {
        return endGameLogic;
    }

    @Override
    public Game getGame() {
        return endGameLogic.getGame();
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
