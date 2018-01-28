package uk.ac.qub.eeecs.game.cardDemo.endGameLogic.getPlayerNameScreen;

/**
 * Created by 40216004 on 24/01/2018.
 */

class User {
   private String name;
   private boolean win;
   public User(String name, boolean win) {
      this.name = name;
      this.win = win;
   }

   public String getName() {
      return name;
   }

   public boolean isWin() {
      return win;
   }
}
