package uk.ac.qub.eeecs.game.gameHelp.helpScreens;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.game.gameHelp.GameHelpController;

/**
 * Created by 40216004 Dewei Liu on 13/04/2018.
 */

public class Copyright extends HelpScreenSuperClass {

    private ArrayList<Developer> developers;
    private Paint mPaint;
    private ArrayList<String> descriptions;

    /**
     * Create a new game screen associated with the specified game instance
     *
     * @param game       Game instance to which the game screen belongs
     * @param controller the controller of help screen
     */
    public Copyright(Game game, GameHelpController controller) {
        super("Copyright", game, controller);
        /*****************************************************************************************************/
        //Set descriptions
        descriptions = new ArrayList<>();
        descriptions.add("This project is developed for CSC2040.");
        descriptions.add("2017 - 2018 All rights reserved.");

        /*****************************************************************************************************/
        //Set developers
        developers = new ArrayList<>();
        developers.add(new Developer("Team", "115", "5 students"));
        developers.add(new Developer("Cameron", "Stevenson", "40177234"));
        developers.add(new Developer("Dewei", "Liu", "40216004"));
        developers.add(new Developer("Jamie", "Caldwell", "40121687"));
        developers.add(new Developer("Jamie", "Thompson", "40178456"));
        developers.add(new Developer("Nameer", "Shahzad", "40172761"));
        this.reset();

        /*****************************************************************************************************/
        //Set paint
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize((mGame.getScreenHeight() / 2) / developers.size() / (4 / 3));


    }

    @Override
    public void reset() {
        super.reset();
        developers.sort(new MyComparator());
    }

    @Override
    protected void drawGameHelp(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //Show developers
        final int SIZE = developers.size();
        for (int i = 0; i < SIZE; i++) {
            int y = mGame.getScreenHeight() / 5 + (i + 1) * ((mGame.getScreenHeight() / 2) / SIZE);
            developers.get(i).draw(mGame.getScreenWidth(), y, graphics2D, mPaint);
        }

        /*****************************************************************************************************/
        //Show descriptions
        final int x = mGame.getScreenWidth() / 10;
        final int startY = mGame.getScreenHeight() / 5 + (mGame.getScreenHeight() / 2);
        final int spaceAvailable = mGame.getScreenHeight() - startY;
        for (int i = 0; i < descriptions.size(); i++) {
            graphics2D.drawText(descriptions.get(i), x, startY + spaceAvailable / (descriptions.size() + 1) * (i + 1), mPaint);
        }
    }

    private class MyComparator implements Comparator<Developer> {
        private Random random;

        public MyComparator() {
            super();
            this.random = new Random();
        }

        @Override
        public int compare(Developer o1, Developer o2) {
            if (o1.getFirstName().equals("Team")) {
                return -1;
            } else if (o2.getFirstName().equals("Team")) {
                return 1;
            } else {
                if (random.nextBoolean()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }

    private class Developer {
        private String firstName;
        private String lastName;
        private String studentNumber;

        public String getFirstName() {
            return firstName;
        }

        public Developer(String firstName, String lastName, String studentNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.studentNumber = studentNumber;
        }

        public void draw(float screenWidth, int y, IGraphics2D graphics2D, Paint paint) {
            int index = 0;
            graphics2D.drawText(firstName, screenWidth / 3 * index++, y, paint);
            graphics2D.drawText(lastName, screenWidth / 3 * index++, y, paint);
            graphics2D.drawText(studentNumber, screenWidth / 3 * index++, y, paint);
        }
    }
}
