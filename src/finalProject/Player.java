package finalProject;

import java.util.Arrays;

public class Player {

    private String name;
    private int score;
    private Card[] handCards = new Card[13];
    private Team team;

    public Player(String name) {
        this.name = name;
        score = 0;
    }

    public void arrangeHandCards() {
        Card[] temp = handCards.clone();
        int counter = 0;
        //sort color
        for (String color : Card.getColors()) {
            //GREEN, BLUE, YELLOW, RED
            for (Card temp1 : temp) {
                if (temp1.getColor().equals(color)) {
                    handCards[counter++] = temp1;
                }
            }
        }
        //bubble sort rank
        Card hold;
        int index1 = -1;
        int index2 = -1;
        for (int i = 0; i < handCards.length - 1; i++) {
            for (int j = 0; j < handCards.length - 1; j++) {
                if (handCards[j].getColor().equals(handCards[j + 1].getColor())) {
                    for (int k = 0; k < Card.getRanks().length; k++) {
                        if (handCards[j].getRank().equals(Card.getRanks()[k])) {
                            index1 = k;
                        }
                        if (handCards[j + 1].getRank().equals(Card.getRanks()[k])) {
                            index2 = k;
                        }
                    }
                    if (index1 > index2) {
                        hold = handCards[j];
                        handCards[j] = handCards[j + 1];
                        handCards[j + 1] = hold;
                    }
                }
            }
        }
    }

    public void showHandCards() {
        System.out.println(Arrays.toString(handCards));
    }

    public boolean hasColor(String color) {
        for (Card handCard : handCards) {
            if (handCard.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCard(String color, String rank) {
        for (Card handCard : handCards) {
            if (handCard.getColor().equals(color) && handCard.getRank().equals(rank)) {
                return true;
            }
        }
        return false;
    }

    public Card playCard(int index) {
        Card[] temp = new Card[handCards.length - 1];
        Card Return = handCards[index];
        int counter = 0;
        for (int i = 0; i < handCards.length; i++) {
            if (i != index) {
                temp[counter++] = handCards[i];
            }
        }
        handCards = temp;
        return Return;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Card[] getHandCards() {
        return handCards;
    }

    public void setHandCards(Card[] handCards) {
        this.handCards = handCards;
    }
}