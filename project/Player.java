package project;

import java.util.ArrayList;
import java.util.List;

public class Player {
    String name;
    Player teammate;
    int score = 0;
    List<Card> handCards = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void setTeammate(Player teammate) {
        this.teammate = teammate;
        teammate.teammate = this;
    }
    
    public void arrangeHandCards(){
          for(int i=0; i<handCards.size(); i++){
              for(int j=0; j<handCards.size()-i-1; j++){
                  if(handCards.get(j).compareTo(handCards.get(j+1))>0){
                      Card temp = handCards.get(j);
                      handCards.set(j, handCards.get(j+1));
                      handCards.set(j+1, temp);
                  }
              }
          }
    }
    
    public void showHandCards(){
        System.out.print(name+"'s Hand Cards: ");
        for(int i=0; i<handCards.size(); i++){
            System.out.print(handCards.get(i).color+" "+handCards.get(i).rank);
            if(i!=handCards.size()-1){
                System.out.print(" | ");
            }
        }
        System.out.println();
    }
}
