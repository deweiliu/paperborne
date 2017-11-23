package uk.ac.qub.eeecs.game.cardDemo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.CanvasGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.GestureHandler;

/**
 * Created by user on 14/11/2017.
 */


public class Card extends Sprite {

    private  boolean cardPressedDown = false;
    private boolean finishedMove;
    private Vector2 cardCentre = new Vector2();
    //private Vector2 screenDimensions = new Vector2();


    public Card (float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, 140.0f, 210.0f, gameScreen.getGame().getAssetManager().getBitmap("Card"), gameScreen);
        cardCentre.x = 140.0f/2;
        cardCentre.y = 210.0f/2;

    }



    public void update(ElapsedTime elapsedTime) {
        Input input = mGameScreen.getGame().getInput();
        for(TouchEvent touch: input.getTouchEvents() ) {
            //Checks if the touch event happens on the card
            if (touch.type == TouchEvent.TOUCH_DOWN && ((input.getTouchX(touch.pointer) > position.x - cardCentre.x)
                    && (input.getTouchX(touch.pointer) < position.x + cardCentre.x)
                    && (input.getTouchY(touch.pointer) > (screenDimensions.y)- position.y - cardCentre.y)
                    && (input.getTouchY(touch.pointer) < (screenDimensions.y) - position.y + cardCentre.y))) {
                cardPressedDown = true;
                finishedMove = false;

                //Checks what image is held on card, if clicked, swaps it with the alternative
                if(touch.type != TouchEvent.TOUCH_DRAGGED) {
                    if (mBitmap == mGameScreen.getGame().getAssetManager().getBitmap("Card")) {
                        mBitmap = mGameScreen.getGame().getAssetManager().getBitmap("Back");
                    } else {
                        mBitmap = mGameScreen.getGame().getAssetManager().getBitmap("Card");
                    }
                }

            }
            //Checks if card is released
            if (touch.type == TouchEvent.TOUCH_UP) {
                cardPressedDown = false;
                finishedMove = true;
                //Checks if card is dragged
            } else if (touch.type == TouchEvent.TOUCH_DRAGGED && cardPressedDown) {
                if (!Float.isNaN(input.getTouchX(touch.pointer))) {
                    position.x = input.getTouchX(touch.pointer);
                    //screenDimensions used to invert Y values
                    position.y = screenDimensions.y - input.getTouchY(touch.pointer);
                }
            }
            super.update(elapsedTime);
        }
    }

}


