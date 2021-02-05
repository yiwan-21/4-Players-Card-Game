package project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    Scanner sc = new Scanner(System.in);
    List<String> donaldColors = new ArrayList<>(Arrays.asList("GREEN", "BLUE", "YELLOW", "RED", "NO"));
    String donaldColor;
    int donaldRank = 0;
    Player donaldCaller;
    int teamMarks[] = new int[]{0, 0};
    Player players[] = new Player[4];
    boolean donaldActivated = false;
    
    public void test() {
        checkAndDecideDonald();
    }
    
    public void start(){
        setPlayers();
        generateCards();
        
        shuffleCards();
        assignCards();
        checkAndDecideDonald();
        System.out.println();
        
        int winingScores[] = new int[]{6+donaldRank, 8-donaldRank};
        int teamScores[] = new int[]{0, 0};
        Card choosenCards[] = new Card[4];
        int roundCounter = 1;
        while(teamScores[0]<winingScores[0] && teamScores[1]<winingScores[1]){
            System.out.println("--------------Round "+roundCounter+"-------------------");
            if(!donaldColor.equals("NO")){
                System.out.println("Donald is played: "+donaldActivated);
                System.out.println("Donald number is: "+donaldRank);
                System.out.println("Donald color is: "+donaldColor);
                System.out.println("Donald player is: "+donaldCaller.name);
            }
            else{
                System.out.println("No Donald");
                System.out.println("Donald player is: "+donaldCaller.name);
            }
            for(int i=0; i<players.length; i++){
                int choice = 0;
                System.out.println();
                System.out.println(players[i].name+"'s turn: \n"+
                                   "Please enter your command: \n"+
                                   "1: Arrange Card \n"+
                                   "2: Play Card \n"+
                                   "3: Show score(Excluding teammate's score) \n"+
                                   "4: Show hand card ");
                choice = sc.nextInt();
                
                while(choice<=0 || choice>=5){
                    System.out.println("\nPlease enter valid command:");
                    choice = sc.nextInt();
                }
                while(choice!=2){
                    if(choice>=1 && choice<=4){
                        System.out.println();
                        switch(choice){
                            case 1:
                                players[i].arrangeHandCards();
                                System.out.println("Completed!");
                                break;
                            case 3:
                                System.out.println("Your Score: "+players[i].score);
                                break;
                            case 4:
                                players[i].showHandCards();
                                break;
                        }
                        System.out.println("\nEnter your next command: ");
                        choice = sc.nextInt();
                    }
                    else{
                        System.out.println("\nPlease enter valid command:");
                        choice = sc.nextInt();
                    }
                }
                
                System.out.println("\nWhich card do you want to play?(Starting from 0)");
                choice = sc.nextInt();
                
                Card choosenCard = players[i].handCards.get(choice);
                System.out.println(players[i].name+" has played "+choosenCard.color+" "+choosenCard.rank);
                if(players[i].equals(donaldCaller)){
                    if(donaldColor!=null && choosenCard.color.equals(donaldColor)){
                        donaldActivated = true;
                    }
                }
                choosenCards[i] = choosenCard;
                players[i].handCards.remove(choosenCard);
            }
            int max = 0;
            for(int i=1; i<choosenCards.length; i++){
                if(donaldActivated){
                    if(choosenCards[i].color.equals(donaldColor) && !choosenCards[max].color.equals(donaldColor)){
                        max = i;
                    }
                    else if(choosenCards[i].compareTo(choosenCards[max])>0){
                        max = i;
                    }
                }
                else if(choosenCards[i].compareTo(choosenCards[max])>0){
                    max = i;
                }
            }
            System.out.println("\n"+players[max].name+" wins this round!");
            players[max].score++;
            if(players[max].equals(donaldCaller)|| players[max].equals(donaldCaller.teammate)){
                teamScores[0]++;
            }
            else{
                teamScores[1]++;
            }
            setFirstPlayer(players[max]);
            roundCounter++;
        }
        if(teamScores[0]==winingScores[0]){
            System.out.println("Team 1 won!! Congratulation!! ");
        }
        else{
            System.out.println("Team 2 won!! Congratulation!!");
        }
    }
    
    public void setFirstPlayer(Player p){
        for(int i=0; i<players.length; i++){
            if(p.equals(players[i])){
                Player temp = players[0];
                players[0] = players[i];
                players[i] = temp;
                break;
            }
        }
    }
    
    public void checkAndDecideDonald(){
        for(int i=0; i<players.length; i++){
            System.out.println(players[i].name+" want to check your card?[YES/NO]");
            String call = sc.nextLine();
            System.out.println();
            if(call.equals("YES")){
                players[i].showHandCards();
                System.out.println();
            }
            
            System.out.println(players[i].name+" call for donald?[YES/NO]");
            call = sc.nextLine();
            if(call.equals("YES")){
                System.out.println("\nEnter the number of donald:[0-7]");
                int tempRank = sc.nextInt();
                
                System.out.println("\nEnter the donald:[GREEN,BLUE,YELLOW,RED,NO]");
                sc.nextLine();
                String tempColor = sc.nextLine();
                
                if(donaldColor==null || donaldColors.indexOf(tempColor)>donaldColors.indexOf(donaldColor)){
                    donaldCaller = players[i];
                    donaldColor = tempColor;
                    donaldRank = tempRank;
                }
                else if(donaldColor.equals(tempColor)){
                    if(donaldRank<tempRank){
                        donaldCaller = players[i];
                        donaldRank = tempRank;
                        donaldColor = tempColor;
                    }
                }
            }
            System.out.println();
        }
        if(donaldCaller!=null){
            System.out.println(donaldCaller.name+" would be the donald.");
            System.out.println(donaldCaller.name+" choose your teammate by entering \"Color Rank\" of someone's card:");
            String[] colorRank = sc.nextLine().split(" ");
            System.out.println();
            
            List<Player> singles = new ArrayList<Player>(Arrays.asList(players));
            outer:
            for(int i=0; i<players.length; i++){
                for(int j=0; j<players[i].handCards.size(); j++){
                    Card temp = players[i].handCards.get(j);
                    if(temp.color.equals(colorRank[0]) && temp.rank.equals(colorRank[1])){
                        donaldCaller.setTeammate(players[i]);
                        System.out.println("Team 1: "+donaldCaller.name+" and "+players[i].name);
                        singles.remove(donaldCaller);
                        singles.remove(players[i]);
                        singles.get(0).setTeammate(singles.get(1));
                        System.out.println("Team 2: "+singles.get(0).name+" and "+singles.get(1).name);
                        break outer;
                    }
                }
            }
        }
        else{
            System.out.println("This game has no donald");
        }
    }
    
    public void assignCards(){
        System.out.println();
        System.out.println("Assigning the cards to player...");
        int index = 0;
        for(Player player: players){
            for(int i=0; i<13; i++){
                player.handCards.add(Card.cardDeck[index]);
                index++;
            }
        }
        System.out.println("Assigned!\n");
    }
    
    public void generateCards(){
        System.out.println("Constructing the cards in the deck...");
        int index = 0;
        for(String color: Card.colors){
            for(int i=1; i<=13; i++){
                if(i<=10){
                    Card.cardDeck[index] = new Card(color, i+"");
                }
                else if(i==11){
                    Card.cardDeck[index] = new Card(color, "A");
                }
                else if(i==12){
                    Card.cardDeck[index] = new Card(color, "B");
                }
                else{
                    Card.cardDeck[index] = new Card(color, "C");
                }
                index++;
            }
        }
        System.out.println("52 cards have been constructed");
    }
    
    public void shuffleCards(){
        Random r = new Random();
        
        System.out.println("Shuffling the card deck");
        for(int i=0; i<Card.cardDeck.length; i++){
            int first = r.nextInt(Card.cardDeck.length);
            int second = r.nextInt(Card.cardDeck.length);
            if(first!=second){
                Card temp = Card.cardDeck[first];
                Card.cardDeck[first] = Card.cardDeck[second];
                Card.cardDeck[second] = temp;
            }
        }
        System.out.println("Completed");
    }
    
    public void setPlayers(){
        for(int i=0; i<players.length; i++){
            System.out.print("Player "+(i+1)+", please enter your name: ");
            String name = sc.nextLine();
            players[i] = new Player(name);
        }
    }
}
