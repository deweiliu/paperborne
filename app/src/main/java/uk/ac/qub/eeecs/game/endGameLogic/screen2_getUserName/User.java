package uk.ac.qub.eeecs.game.endGameLogic.screen2_getUserName;

/**
 * Created by 40216004 on 24/01/2018.
 */

public class User {
    private boolean isSinglePlayer;
    private boolean isWinnerPlayer1;
    private UserName winner;
    private UserName loser;


    public User(boolean isSinglePlayer, boolean isPlayer1Win) {
        this.isWinnerPlayer1 = isPlayer1Win;
        this.isSinglePlayer = isSinglePlayer;
    }


    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public boolean isWinnerPlayer1() {
        return isWinnerPlayer1;
    }

    public void setWinner(UserName winner) {
        this.winner = winner;
    }

    public void setLoser(UserName loser) {
        this.loser = loser;
    }

    public String getWinner() {
        if(winner==null){
            return null;
        }else{
        return winner.name;
    }}

    public String getLoser() {if(loser==null){
        return null;
    }else{
        return loser.name;
    }}

    public class UserName {
        private String name;

        public UserName() {
        }

        public void setName(String name)

        {
            this.name = name;
        }
    }

}
