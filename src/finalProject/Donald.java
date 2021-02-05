package finalProject;

public class Donald {

    private Player caller;
    private String color;
    private int number;
    private boolean activated = false;

    public Donald(Player caller, String color, int number) {
        this.caller = caller;
        this.color = color;
        this.number = number;
    }

    public static boolean isValid(String color, int number) {
        if (number < 1 || number > 7) {
            return false;
        }
        for (String color1 : Card.getColors()) {
            if (color.equalsIgnoreCase(color1) || color.equalsIgnoreCase("NO")) {
                return true;
            }
        }
        return false;
    }

    public static int determineDonald(int[] numsCalled, String[] colorsCalled) {
        //compare number first then color
        int highestNum = numsCalled[0];
        int index = 0;
        if (numsCalled[0] == 0 && numsCalled[1] == 0 && numsCalled[2] == 0 && numsCalled[3] == 0) { //no one call for donald
            index = -1;
            return index;
        }
        for (int i = 1; i < numsCalled.length; i++) {
            if (numsCalled[i] == 0) {
                continue;
            }
            if (numsCalled[i] > highestNum) {
                highestNum = numsCalled[i];
                index = i;
            }
            if (numsCalled[i] == highestNum) {
                if (!compareDonaldColor(colorsCalled[index], colorsCalled[i])) {
                    index = i;
                }
            }
        }
        return index;
    }

    private static boolean compareDonaldColor(String color1, String color2) { //true if color1 is larger, else false
        //this method just help to determineDonald
        String[] donaldColors = {"GREEN", "BLUE", "YELLOW", "RED", "NO"};
        int index1 = -1;
        int index2 = -1;
        for (int i = donaldColors.length - 1; i >= 0; i--) {
            if (color1.equals(donaldColors[i])) {
                index1 = i;
            }
            if (color2.equals(donaldColors[i])) {
                index2 = i;
            }
        }
        return index1 >= index2;
    }

    public Player getCaller() {
        return caller;
    }

    public void setCaller(Player caller) {
        this.caller = caller;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
