package uk.ac.qub.eeecs.game.gameHelp.helpScreens;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Vector;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.gameHelp.HelpScreenController;

/**
 * Created by 40216004 Dewei Liu on 28/03/2018.
 */

public class CardDescription extends HelpScreenSuperClass {
    private ArrayList<CardPicture> cards = new ArrayList<>();
    private Paint mPaint;
    private Vector<String> description;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param game       Game instance to which the game screen belongs
     * @param controller
     */
    public CardDescription(Game game, HelpScreenController controller) {
        super("HeroDescription", game, controller);

        String[] cardNames = {"Dragon", "Dog", "Fat man", "Weak man", "Sword"};
        for (int i = 0; i < cardNames.length; i++) {
            float x = mLayerViewport.getLeft() + mLayerViewport.getWidth() / cardNames.length / 2 + i * (mLayerViewport.getWidth() / cardNames.length);
            float y = mLayerViewport.y + mLayerViewport.getHeight() / 4;
            float width = mLayerViewport.getWidth() / (cardNames.length + 2);
            float height = mLayerViewport.halfHeight;
            cards.add(new CardPicture(assetManager, x, y, width, height, cardNames[i], this));
        }

        description = new Vector<String>();
        description.add("The cards above will be showing attack value (left right),");
        description.add("health value (right bottom) and mana-cost (left top).");
        description.add("The card it not attackable only in the turn you play it,");
        description.add("The card with health lower than or equaling to 0 will be destroyed.");

        mPaint = new Paint();
        mPaint.setTextSize(60);
        mPaint.setColor(Color.WHITE);
        ;

    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        for (CardPicture each : cards) {
            each.update(elapsedTime);
        }
    }


    @Override
    public void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        for (CardPicture each : cards) {
            each.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
        for (int i = 0; i < description.size(); i++) {
            float x = mPaint.getTextSize();
            float y = mGame.getScreenHeight() / 5 + mLayerViewport.halfHeight + ((i + 1) * mPaint.getTextSize() * 1.2f);
            graphics2D.drawText(description.get(i), x, y, mPaint);
        }

    }

    private class CardPicture {
        private GameObject hero;
        private String name;
        private Paint paint = new Paint();
        private float width;

        public CardPicture(AssetStore assetManager, float x, float y, float width, float height, String bitmapName, GameScreen gameScreen) {
            this.width = width;
            String assetName = "Card " + bitmapName;
            assetManager.loadAndAddBitmap("my" + assetName, "img/Game Help/" + assetName + ".JPG");
            Bitmap bitmap = assetManager.getBitmap("my" + assetName);
            hero = new GameObject(x, y, width, height, bitmap, gameScreen);
            name = bitmapName;
            paint.setTextSize(80);
            paint.setColor(Color.BLUE);
        }

        public void update(ElapsedTime elapsedTime) {
            hero.update(elapsedTime);
        }

        public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
            hero.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            graphics2D.drawText(name, hero.position.x + mLayerViewport.halfWidth - width / 2, hero.position.y + mLayerViewport.halfHeight, paint);
        }
    }

}