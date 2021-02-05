package finalProject;

public class Bot extends Player {

    public Bot(String name) {
        super("Bot " + name);
    }

    public int decideCard(Card[] playedCards, Donald donald) {
        int max = -1;
        String targetColor = "";
        if (playedCards[0] != null && hasColor(playedCards[0].getColor())) {
            targetColor = playedCards[0].getColor();
        } else {
            if (donald.isActivated() && hasColor(donald.getColor())) {
                targetColor = donald.getColor();
            } else {
                for (String color : Card.getColors()) {
                    if (hasColor(color)) {
                        targetColor = color;
                        for (int i = 0; i < getHandCards().length; i++) {
                            if (getHandCards()[i].getColor().equals(targetColor)) {
                                if (max == -1) {
                                    max = i;
                                } else {
                                    if (getHandCards()[i].compareTo(getHandCards()[max]) < 0) {
                                        max = i;
                                    }
                                }
                            }
                        }
                        return max;
                    }
                }
            }
        }
        for (int i = 0; i < getHandCards().length; i++) {
            if (getHandCards()[i].getColor().equals(targetColor)) {
                if (max == -1) {
                    max = i;
                } else {
                    if (getHandCards()[i].compareTo(getHandCards()[max]) > 0) {
                        max = i;
                    }
                }
            }
        }
        return max;
    }
}
