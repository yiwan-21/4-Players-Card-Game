package finalProject;

import java.util.Random;
import java.util.ArrayList;

public class Card {
    //color > rank

    private static String[] colors = {"GREEN", "BLUE", "YELLOW", "RED"}; //RED > GREEN
    private static String[] ranks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "B", "C"}; //C > 1
    private static ArrayList<Card> cardDeck = new ArrayList<Card>();
    private String color;
    private String rank;
    private String imageAddress;

    //Start
    public Card(String color, String rank) {
        this.color = color;
        this.rank = rank;
    }
    //End
    
    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public Card(String color, String rank, String imageAddress) {
        this.color = color;
        this.rank = rank;
        this.imageAddress = imageAddress;
    }

    public static void generateCardDeck() {
        for (String color1 : colors) {
            for (String rank1 : ranks) {
                String addressTemp = "src\\Graphics\\" + color1 + " " + rank1 + ".png";
                cardDeck.add(new Card(color1, rank1, addressTemp));
            }
        }
    }

    public static void assignCard(Player[] players) {
        Random r = new Random();
        int cardCount = 52;
        int rand;
        Card[] holdCards = new Card[13];
        for (Player player : players) {
            for (int j = 0; j < holdCards.length; j++) {
                rand = r.nextInt(cardCount--);
                holdCards[j] = cardDeck.get(rand);
                cardDeck.remove(rand);
            }
            player.setHandCards(holdCards.clone());
        }
    }

    public int compareTo(Card card) { //Compare Rank only
        int temp1 = -1;
        int temp2 = -1;
        for (int i = 0; i < ranks.length; i++) {
            if (rank.equals(ranks[i])) {
                temp1 = i;
            }
            if (card.rank.equals(ranks[i])) {
                temp2 = i;
            }
        }
        if (temp1 > temp2) {
            return 1;
        } else if (temp1 < temp2) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int highestRank(Card[] playedCards, Donald donald) {
        String winningColor;
        if (donald.isActivated() && !donald.getColor().equals("NO")) {
            winningColor = donald.getColor();
        } else {
            winningColor = playedCards[0].color;
        }

        int winningIndex = -1;
        int win = 0;
        boolean hasWinningColor = false;
        for (int i = 0; i < playedCards.length; i++) {
            int temp = 0;
            if (i == playedCards.length - 1 && !playedCards[i].color.equals(winningColor) && hasWinningColor == false) {
                winningColor = playedCards[0].color;
                i = 0;
            }
            if (!playedCards[i].color.equals(winningColor)) {
                continue;
            }
            hasWinningColor = true;
            for (Card playedCard : playedCards) {
                if(!playedCard.color.equals(winningColor)){
                    continue;
                }
                temp += playedCards[i].compareTo(playedCard);
            }
            if (temp >= win) {
                win = temp;
                winningIndex = i;
            }
        }
        return winningIndex;
    }

    public static boolean isValid(String rank, String color) {
        boolean valid = true;
        outer:
        {
            for (String rank1 : ranks) {
                if (rank.equalsIgnoreCase(rank1)) {
                    for (String color1 : colors) {
                        if (color.equalsIgnoreCase(color1)) {
                            break outer;
                        }
                    }
                }
            }
            valid = false;
        }
        return valid;
    }

    @Override
    public String toString() {
        return color + " " + rank;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public static String[] getColors() {
        return colors;
    }

    public static void setColors(String[] colors) {
        Card.colors = colors;
    }

    public static String[] getRanks() {
        return ranks;
    }

    public static void setRanks(String[] ranks) {
        Card.ranks = ranks;
    }

}
