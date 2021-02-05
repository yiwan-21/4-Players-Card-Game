package finalProject;

public class Team {

    private Player[] members;
    private int score = 0;
    private int winScore;

    public Team(Player a, Player b, int winScore) {
        members = new Player[2];
        members[0] = a;
        members[1] = b;
        this.winScore = winScore;
    }

    public Player[] getMembers() {
        return members;
    }

    public void setMembers(Player[] members) {
        this.members = members;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWinScore() {
        return winScore;
    }

    public void setWinScore(int winScore) {
        this.winScore = winScore;
    }
}