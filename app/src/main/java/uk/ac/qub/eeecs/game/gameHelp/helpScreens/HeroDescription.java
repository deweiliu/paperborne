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

public class HeroDescription extends HelpScreenSuperClass {

    private ArrayList<HeroPicture> heros = new ArrayList<>();

    private Vector<String> description;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param game       Game instance to which the game screen belongs
     * @param controller
     */
    public HeroDescription(Game game, HelpScreenController controller) {
        super("HeroDescription", game, controller);

        String[] heroNames = {"Dragon", "Jester", "Knight", "Queen"};
        for (int i = 0; i < heroNames.length; i++) {
            float x = mLayerViewport.getLeft() + mLayerViewport.getWidth() / heroNames.length / 2 + i * (mLayerViewport.getWidth() / heroNames.length);
            float y = mLayerViewport.y + mLayerViewport.getHeight() / 4;
            float width = mLayerViewport.getWidth() / (heroNames.length + 2);
            float height = mLayerViewport.halfHeight;
            heros.add(new HeroPicture(assetManager, x, y, width, height, heroNames[i], this));
        }

        description = new Vector<String>();
        description.add("You will get a random hero above with 30 health.");
        description.add("If your hero's health is lower than or equal to 0, you lost.");
        description.add("If the opponent hero's health is lower than or equal to 0, you win.");

        ;

    }


    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);
        for (HeroPicture each : heros) {
            each.update(elapsedTime);
        }
    }


    @Override
    public void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        for (HeroPicture each : heros) {
            each.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
        for (int i = 0; i < description.size(); i++) {
            float x = mPaint.getTextSize();
            float y = mGame.getScreenHeight() / 5 + mLayerViewport.halfHeight + ((i + 1) * mPaint.getTextSize() * 1.2f);
            graphics2D.drawText(description.get(i), x, y, mPaint);
        }

    }

    private class HeroPicture {
        private GameObject hero;
        private String name;
        private Paint paint = new Paint();
        private float width;

        public HeroPicture(AssetStore assetManager, float x, float y, float width, float height, String bitmapName, GameScreen gameScreen) {
            this.width = width;
            String assetName = "Hero " + bitmapName;
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

