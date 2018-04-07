package uk.ac.qub.eeecs.game.endGameLogic.interfaces_superclass_forScreens;

import android.graphics.Color;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.endGameLogic.EndGameController;

public abstract class EndGameScreenSuperClass implements EndGameScreen {
    protected boolean isFinished;
    protected EndGameController mController;
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    private GameObject background;
    private GameObject title;

    public EndGameScreenSuperClass(EndGameController controller) {
        this.mController = controller;
        mScreenViewport = mController.getScreenViewport();
        mLayerViewport = mController.getLayerViewport();
        isFinished = false;

        //Set up background
        final String BACKGROUND_NAME = "All End Game Screens Background";
        getGame().getAssetManager().loadAndAddBitmap(BACKGROUND_NAME, "img/Lined-Paper.png");
        background = new GameObject(mLayerViewport.x, mLayerViewport.y, mLayerViewport.getWidth(),
                mLayerViewport.getHeight(), getGame().getAssetManager().getBitmap(BACKGROUND_NAME), mController);

        //Set up title
        final String TITLE_NAME = "End Game Logic Title";
        getGame().getAssetManager().loadAndAddBitmap(TITLE_NAME, "img/End Game Logic/Title.png");
        title = new GameObject(mLayerViewport.x, mLayerViewport.getTop() - mLayerViewport.getHeight() / 10,
                mLayerViewport.getWidth(), mLayerViewport.getHeight() / 5
                , getGame().getAssetManager().getBitmap(TITLE_NAME), mController);

    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        background.update(elapsedTime);
        title.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        background.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        title.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public GameScreen getGameScreen() {
        return mController;
    }

    @Override
    public AssetStore getAssetManager() {
        return this.getGame().getAssetManager();
    }

    @Override
    public Game getGame() {
        return mController.getGame();
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

    public boolean isSinglePlayer() {
        return mController.isSinglePlayer();
    }

    public boolean hasPlayer1Won() {
        return mController.hasPlayer1Won();
    }

    public GameScreen getBattleScreen() {
        return mController.getBattleScreen();
    }

}
