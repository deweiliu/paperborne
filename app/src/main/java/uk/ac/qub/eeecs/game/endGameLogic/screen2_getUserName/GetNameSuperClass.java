package uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName;

import android.graphics.Color;
import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameScreen;
import uk.ac.qub.eeecs.game.endGameLogic.interfaces.EndGameStuff;

/**
 * Created by 40216004 Dewei Liu on 22/01/2018.
 */

public abstract class GetNameSuperClass implements EndGameStuff {
    private boolean isFinished;

    //This is for derive class
    protected Paint paint;

    //This is for this class
    private Paint mPaint;
    protected EndGameScreen mScreen;
    private LayerViewport mLayer;
    private PushButton nextAlphabet, previousAlphabet, enterButton;
    private NameOnScreen name;
    private boolean isWinner;
    private PushButton confirm;
    private User.UserName userName;

    public GetNameSuperClass(EndGameScreen gameScreen, User.UserName name) {
        this.paint = new Paint();
        paint.setTextSize(90);
        paint.setColor(Color.BLACK);
        this.mScreen = gameScreen;
        this.userName = name;
        isFinished = false;
        mPaint = new Paint();
        mPaint.setTextSize(150);
        mPaint.setColor(Color.BLACK);
        mLayer = gameScreen.getLayerViewport();
        AssetStore asset = mScreen.getAssetManager();
        final float buttonSize = mLayer.getHeight() / 4;
        this.name = new NameOnScreen(mScreen);

/***********************************************************************************************/
        final String DIR = "img/End Game Logic/";
        final String PUSHED = " pushed";
        final String EXTENSION = ".png";


        //Set up the push button of next alphabet
        final String NEXT_ALPHABET_BUTTON_NAME = "next alphabet";
        asset.loadAndAddBitmap(NEXT_ALPHABET_BUTTON_NAME, DIR + NEXT_ALPHABET_BUTTON_NAME + EXTENSION);
        asset.loadAndAddBitmap(NEXT_ALPHABET_BUTTON_NAME + PUSHED, DIR + NEXT_ALPHABET_BUTTON_NAME + PUSHED + EXTENSION);
        nextAlphabet = new PushButton(mLayer.getRight() - buttonSize / 2, mLayer.getBottom() + buttonSize / 2,
                buttonSize, buttonSize, NEXT_ALPHABET_BUTTON_NAME, NEXT_ALPHABET_BUTTON_NAME + PUSHED, mScreen.getGameScreen());
        nextAlphabet.processInLayerSpace(true);

        //Set up the push button of previous alphabet
        final String PREVIOUS_ALPHABET_BUTTON_NAME = "previous alphabet";
        asset.loadAndAddBitmap(PREVIOUS_ALPHABET_BUTTON_NAME, DIR + PREVIOUS_ALPHABET_BUTTON_NAME + EXTENSION);
        asset.loadAndAddBitmap(PREVIOUS_ALPHABET_BUTTON_NAME + PUSHED, DIR + PREVIOUS_ALPHABET_BUTTON_NAME + PUSHED + EXTENSION);
        previousAlphabet = new PushButton(mLayer.getLeft() + buttonSize / 2, mLayer.getBottom() + buttonSize / 2,
                buttonSize, buttonSize, PREVIOUS_ALPHABET_BUTTON_NAME, PREVIOUS_ALPHABET_BUTTON_NAME + PUSHED, mScreen.getGameScreen());
        previousAlphabet.processInLayerSpace(true);


        //Set up the enter button
        final String ENTER_BUTTON_NAME = "enter button";
        asset.loadAndAddBitmap(ENTER_BUTTON_NAME, DIR + ENTER_BUTTON_NAME + EXTENSION);
        enterButton = new PushButton(mLayer.x, mLayer.getBottom() + buttonSize / 2,
                buttonSize, buttonSize, ENTER_BUTTON_NAME, mScreen.getGameScreen());
        enterButton.processInLayerSpace(true);

        //Set up the confirm button
        final String CONFIRM_BUTTON_NAME = "Confirm Button";
        asset.loadAndAddBitmap(CONFIRM_BUTTON_NAME, DIR + CONFIRM_BUTTON_NAME + EXTENSION);
        confirm = new PushButton(mLayer.getRight() - buttonSize, mLayer.y + mLayer.getHeight() / 4.5f, buttonSize * 1.8f, buttonSize / 1.2f,
                CONFIRM_BUTTON_NAME, mScreen.getGameScreen());
        confirm.processInLayerSpace(true);

    }

    public String getUserInfo() {
        return name.getName();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        nextAlphabet.update(elapsedTime, mLayer, mScreen.getScreenViewPort());
        previousAlphabet.update(elapsedTime, mLayer, mScreen.getScreenViewPort());
        enterButton.update(elapsedTime, mLayer, mScreen.getScreenViewPort());
        if (enterButton.isPushTriggered()) {
            name.update(NameOnScreen.ENTER_BUTTON, elapsedTime);
        } else if (nextAlphabet.isPushTriggered()) {
            name.update(NameOnScreen.NEXT_ALPHABET, elapsedTime);
        } else if (previousAlphabet.isPushTriggered()) {
            name.update(NameOnScreen.PREVIOUS_ALPHABET, elapsedTime);
        } else {
            name.update(-1, elapsedTime);
        }
        confirm.update(elapsedTime,mLayer,mScreen.getScreenViewPort());
        if (confirm.isPushTriggered()) {
            userName.setName(name.getName());
            this.isFinished = true;

        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        nextAlphabet.draw(elapsedTime, graphics2D, mLayer, mScreen.getScreenViewPort());
        previousAlphabet.draw(elapsedTime, graphics2D, mLayer, mScreen.getScreenViewPort());
        enterButton.draw(elapsedTime, graphics2D, mLayer, mScreen.getScreenViewPort());
        name.draw(elapsedTime, graphics2D);
        confirm.draw(elapsedTime, graphics2D, mLayer, mScreen.getScreenViewPort());
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    private class NameOnScreen {
        public final static int NEXT_ALPHABET = 0;
        public final static int PREVIOUS_ALPHABET = 1;
        public final static int ENTER_BUTTON = 2;
        private char[] name;
        private EndGameScreen mScreen;
        private long lastDrawTime;
        private final static int max = 8;
        int currentPosition;
        private GameObject nameBackground;
        private GameObject white;
        private boolean isWhiteVisable;

        public NameOnScreen(EndGameScreen mEndGameScreen) {
            mScreen = mEndGameScreen;
            currentPosition = 0;
            name = new char[max];
            for (int i = 0; i < name.length; i++) {
                name[i] = 'x';
            }
            name[0] = 'A';

            Game game = mScreen.getGame();
            //Set up UserName background
            final String BACKGROUND_NAME = "Name Background";
            mScreen.getAssetManager().loadAndAddBitmap(BACKGROUND_NAME, "img/End Game Logic/" + BACKGROUND_NAME + ".png");
            nameBackground = new GameObject(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth(), game.getScreenHeight() / 5,
                    mScreen.getAssetManager().getBitmap(BACKGROUND_NAME), mScreen.getGameScreen());

            final String WHITE_NAME = "white";
            mScreen.getAssetManager().loadAndAddBitmap(WHITE_NAME, "img/End Game Logic/" + WHITE_NAME + ".png");
            white = new GameObject(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / max, game.getScreenHeight() / 5,
                    mScreen.getAssetManager().getBitmap(WHITE_NAME), mScreen.getGameScreen());
            isWhiteVisable = true;
        }

        public void update(int input, ElapsedTime elapsedTime) {
            if (input == NEXT_ALPHABET) {
                if (name[currentPosition] < 'Z') {
                    name[currentPosition] += 1;
                }
            } else if (input == PREVIOUS_ALPHABET) {
                if (name[currentPosition] > 'A') {
                    name[currentPosition] -= 1;
                }
            } else if (input == ENTER_BUTTON) {

                if (currentPosition < max - 1) {


                    currentPosition += 1;
                    name[currentPosition] = 'A';

                }
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDrawTime > 700) {
                isWhiteVisable = !isWhiteVisable;
                lastDrawTime = currentTime;
            }

            nameBackground.update(elapsedTime);
            white.update(elapsedTime);
            white.setPosition((mScreen.getGame().getScreenWidth() / max) * ((float) 1 / 2 + currentPosition), white.position.y);

        }

        public String getName() {
            String n = new String(name);
            int x = n.indexOf("x");
            if (x == -1) {
                return n.substring(0, n.length());
            } else {
                return n.substring(0, x);
            }
        }

        public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
            nameBackground.draw(elapsedTime, graphics2D);
            if (isWhiteVisable == true) {
                white.draw(elapsedTime, graphics2D);
            }
            String n = this.getName();
            for (int i = 0; i < n.length(); i++) {
                graphics2D.drawText("" + n.charAt(i), (mScreen.getGame().getScreenWidth() / max) * ((float) 1 / 2 + i) - mPaint.getTextSize() / 2,
                        mScreen.getGame().getScreenHeight() / 2 + mPaint.getTextSize() / 2, mPaint);
            }
        }
    }
}
