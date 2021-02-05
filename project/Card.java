package project;

public class Card implements Comparable<Card>{
    static Card cardDeck[] = new Card[52];
    static String colors[] = {"GREEN", "BLUE", "YELLOW", "RED"};
    static String ranks[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "B", "C"};
    String color;
    String rank;
    
    
    public Card(String color, String rank){
        this.color = color;
        this.rank = rank;
    }

    @Override
    public int compareTo(Card o) {
        if(!this.color.equals(o.color)){
            for(int i=colors.length-1; i>=0; i--){
                if(colors[i].equals(this.color)){
                    return 1;
                }
                if(colors[i].equals(o.color)){
                    return -1;
                }
            }
        }
        else{
            for(int i=ranks.length-1; i>=0; i--){
                if(ranks[i].equals(this.rank)){
                    return 1;
                }
                if(ranks[i].equals(o.rank)){
                    return -1;
                }
            }
        }
        return 0;
    }
}
