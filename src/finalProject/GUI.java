package finalProject;

import java.io.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class GUI extends Application{

    //For game
    private static Player[] players = new Player[4];
    private static int playerNum;
    private static Donald donald;
    private static int currentPlayer;
    private static int roundCounter;
    private static int[] numsCalled;
    private static String[] colorsCalled;
    private static Card[] playedCards = new Card[4];
    
    //For GUI
    private static ObservableList list;
    private static TextInputDialog td;
    private static final Button[][] cardButtons = new Button[4][13];
    private static final Text[] nameTexts = new Text[4];
    private static ImageView[] playedImages = new ImageView[4];
    private static Text[] playedTexts = new Text[4];
    private static Pane pane;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage){
        startGame(stage);
    }
    
    public static void startGame(Stage stage){
        Image icon = null;
        try {
            icon = new Image(new FileInputStream(".\\src\\Graphics\\icon.png"));
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        td = new TextInputDialog();
        Stage dialog = (Stage) td.getDialogPane().getScene().getWindow();
        dialog.getIcons().add(icon);
        td.setGraphic(new ImageView(icon));
        
        td.setHeaderText("Please Enter Number Of Player (1->4)");
        td.setTitle("Insert Player");
        td.setContentText("Number: ");
        boolean valid;
        do{
            valid = true;
            td.showAndWait();
            try{
                playerNum = Integer.parseInt(td.getEditor().getText());
                if(playerNum<=0 || playerNum >=5){
                    valid = false;
                }
            } catch(NumberFormatException e){
                valid = false;
                td.setHeaderText("Please Enter Valid Number Of Player(1->4)");
            }
            td.getEditor().setText("");
        }while(!valid);
        
        donald = null;
        roundCounter = 1;
        currentPlayer = 0;
        colorsCalled = new String[4];
        numsCalled = new int[4];
        pane = new Pane();
        
        for(int i=0; i<players.length; i++){
            if(i<playerNum){
                td.setHeaderText("Please Enter Player "+(i+1)+"'s Name");
                td.setContentText("Name: ");
                td.showAndWait();
                String name = td.getEditor().getText();
                players[i] = new Player(name);
                td.getEditor().setText("");
            }
            else{
                players[i] = new Bot((i+1)+"");
            }
        }
        
        Card.generateCardDeck();
        Card.assignCard(players);
        
        Group root = new Group();
        list = root.getChildren();
        
        Scene scene = new Scene(root, 1260, 705, Color.GREEN);
        
        Button arrangeButton = new Button("Arrange Card");
        arrangeButton.setFont(new Font(20));
        arrangeButton.minWidth(100);
        arrangeButton.minHeight(100);
        arrangeButton.setTranslateX(1000-335);
        arrangeButton.setTranslateY(700-60);
        arrangeButton.setOnAction((ActionEvent t) -> {
            players[currentPlayer].arrangeHandCards();
            updateCurrent();
        });
        list.add(arrangeButton);
        
        pane.prefHeight(1500);
        pane.setStyle("-fx-background: #8bc34a;");
        
        Text paneTitle = new Text("Records: ");
        paneTitle.setFont(new Font(25));
        paneTitle.setX(10);
        paneTitle.setY(25);
        pane.getChildren().add(paneTitle);
        
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setStyle("-fx-background: #8bc34a;");
        scrollPane.setPrefSize(330, 705);
        scrollPane.setTranslateX(930);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        list.add(scrollPane);
        
        inputDonald(0);
        
        stage.setTitle("Donald Game");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
    
    public static String[] donaldWindow(String header){
        String[] called = new String[2];
        for(int i=0; i<2; i++){
            if(i==0){
                td.setHeaderText(header+"\n (1 -> 7)");
                td.setContentText("Number: ");
            }
            else{
                td.setHeaderText(header+"\n(GREEN < BLUE < YELLOW < RED < NO)");
                td.setContentText("Color: ");
            }
            td.showAndWait();
            called[i] = td.getEditor().getText();
            td.getEditor().setText("");
        }
        return called;
    }
    
    public static void promptDonaldInput(int index){
        if(!(players[index] instanceof Bot)){
            td.setTitle("Decide Donald");
            td.setHeaderText("Do you want to call for Donald?");
            td.setContentText("[YES/NO]: ");
            td.showAndWait();
            String choice = td.getEditor().getText();
            td.getEditor().setText("");

            while(!choice.equals("YES") && !choice.equals("NO")){
                td.setHeaderText("[YES/NO] Only");
                td.showAndWait();
                choice = td.getEditor().getText();
                td.getEditor().setText("");
            }

            if(choice.equals("YES")){
                boolean valid = true;
                String colorCalled = null;
                int numCalled = 0;
                String display = "Please Enter Donald";
                do{
                    if(!valid){
                        display = "Please Enter Valid Donald";
                    }
                    valid = true;
                    String[] called = donaldWindow(display);
                    try{
                        numCalled = Integer.parseInt(called[0]);
                        colorCalled = called[1];
                    } catch(NumberFormatException e){
                        valid = false;
                    }
                    if(valid){
                        valid = Donald.isValid(colorCalled, numCalled);
                    }
                } while(!valid);
                numsCalled[index] = numCalled;
                colorsCalled[index] = colorCalled;
            }
        }
        if(index<3){
            inputDonald(index+1);
        }
        else{
            int choosen = Donald.determineDonald(numsCalled, colorsCalled);
            if(choosen!=-1){
                setFirstPlayer(players[choosen]);
            }
            else{
                setFirstPlayer(players[0]);
            }
            updateScene();
            
            Button cover;
            if(choosen!=-1){
                donald = new Donald(players[currentPlayer], colorsCalled[choosen], numsCalled[choosen]);
                cover = coverScreen(players[currentPlayer].getName()+" will be the Donald Player!\n\n"+
                                    "Round: "+roundCounter+"\n"+
                                    players[currentPlayer].getName());
            }
            else{
                donald = new Donald(players[0], "NO", 0);
                cover = coverScreen("Nobody called for Donald\n\n"+"Round: "+roundCounter+"\n"+players[0].getName()+" will be the Donald Player!\n"+
                                    players[0].getName());
            }
            cover.setOnAction((t) -> {
                list.remove(cover);
                askForTeam(choosen);
            });
        }
    }
    
    public static void inputDonald(int index){
        currentPlayer = index;
        if(!(players[currentPlayer] instanceof Bot)){
            updateScene();
            Button cover = coverScreen(players[index].getName());
            cover.setOnAction((t) -> {
                list.remove(cover);
                promptDonaldInput(index);
            }); 
        }
        else{
            promptDonaldInput(index);
        }
    }
    
    public static void askForTeam(int choosen){
        boolean valid = true;
        int wrongType = 0;
        String[] colorRank = new String[2];
        td.setTitle("Decide Teams");
        td.setContentText("Color Rank: ");
        do{
            if(!valid){
                if(wrongType == 0){
                    td.setHeaderText("Please Enter Valid \"COLOR RANK\"");
                }
                else{
                    td.setHeaderText("Please Enter Someone Else's Card");
                }
            }
            else{
                td.setHeaderText("Please Choose Your Teammate\n(By entering \"COLOR RANK\" of a card)");
            }
            valid = true;
            td.showAndWait();
            colorRank = td.getEditor().getText().split(" ");
            try{
                if(donald.getCaller().hasCard(colorRank[0], colorRank[1])){
                    valid = false;
                    wrongType = 1;
                }
                else if(!Card.isValid(colorRank[1], colorRank[0])){
                    valid = false;
                    wrongType = 0;
                }
            } catch(ArrayIndexOutOfBoundsException e){
                valid = false;
                wrongType = 0;
            }
        } while(!valid);
        
        if(choosen==-1){
            numsCalled[0] = 0;
            choosen = 0;
        }
        for (Player player : players) {
            if (player.hasCard(colorRank[0], colorRank[1])) {
                Team team1 = new Team(donald.getCaller(), player, 6+numsCalled[choosen]);
                donald.getCaller().setTeam(team1);
                player.setTeam(team1);
                int index = 0;
                Player[] unTeamPlayers = new Player[2];
                
                for (Player otherPlayer : players) {
                    if (otherPlayer.getTeam() == null) {
                        unTeamPlayers[index] = otherPlayer;
                        index++;
                    }
                }
                Team team2 = new Team(unTeamPlayers[0], unTeamPlayers[1], 8-numsCalled[choosen]);
                unTeamPlayers[0].setTeam(team2);
                unTeamPlayers[1].setTeam(team2);
                break;
            }
        }
        updateScene();
    }
    
    public static void displayNameTexts(){
        int index = 0;
        list.removeAll(nameTexts[0], nameTexts[1], nameTexts[2], nameTexts[3]);
        for (Player player : players) {
            if (!player.equals(players[currentPlayer])) {
                nameTexts[index] = new Text(player.getName());
                nameTexts[index].setFont(new Font(20));
                list.add(nameTexts[index]);
                index++;
            } else {
                if(donald!=null){
                    Team currentTeam = players[currentPlayer].getTeam();
                    nameTexts[3] = new Text("Donald caller     : "+donald.getCaller().getName()+"\n"+
                                            "Donald is played: "+donald.isActivated()+"\n"+
                                            "Donald number  : "+donald.getNumber()+"\n"+
                                            "Donald color      : "+donald.getColor()+"\n"+
                                            "Your Team          : "+currentTeam.getMembers()[0].getName()+" and "+currentTeam.getMembers()[1].getName()+"\n"+
                                            "Your score          : "+players[currentPlayer].getScore());
                    nameTexts[3].setFont(new Font(20));
                    nameTexts[3].setX(1000-390);
                    nameTexts[3].setY(700-205);
                    list.add(nameTexts[3]);
                }
            }
        }
                
        nameTexts[0].setX(25);
        nameTexts[0].setY(25);
        
        nameTexts[1].setX(355);
        nameTexts[1].setY(130);
        
        nameTexts[2].setX(1000-195);
        nameTexts[2].setY(25);
    }
    
    public static void updateCurrent(){
        for(int i=0; i<players.length; i++){
            if(i==currentPlayer){
                if(cardButtons[i].length>players[currentPlayer].getHandCards().length){
                    list.remove(cardButtons[i][players[currentPlayer].getHandCards().length]);
                }
                for(int j=0; j<players[currentPlayer].getHandCards().length; j++){
                    Image image = null;
                    try{
                        image = new Image(new FileInputStream(players[currentPlayer].getHandCards()[j].getImageAddress()));
                    } catch(FileNotFoundException e){
                        System.out.println(e);
                    }
                    ImageView cardImage = new ImageView(image);
                    cardImage.setPreserveRatio(true);
                    cardImage.setFitWidth(80);
                    
                    list.remove(cardButtons[i][j]);
                    
                    cardButtons[i][j].setGraphic(cardImage);
                    
                    list.add(cardButtons[i][j]);
                }
                return;
            }
        }
    }
    
    public static void updateScene(){
        displayNameTexts();
        int index = 0;
        for(int i=0; i<4; i++){
            for(int j=0; j<players[i].getHandCards().length; j++){
                if(i!=currentPlayer){
                    try {
                        Image image;
                        image = switch (index) {
                            case 0 -> new Image(new FileInputStream(".\\src\\Graphics\\back left.png"));
                            case 1 -> new Image(new FileInputStream(".\\src\\Graphics\\back up.png"));
                            default -> new Image(new FileInputStream(".\\src\\Graphics\\back right.png"));
                        };
                        ImageView cardImage = new ImageView(image);
                        cardImage.setPreserveRatio(true);
                        
                        list.remove(cardButtons[i][j]);
                        
                        cardButtons[i][j] = new Button();
                        cardButtons[i][j].setStyle("-fx-focus-color: white;");
                        
                        switch(index){
                            case 0 -> {
                                cardImage.setFitHeight(60);
                                cardButtons[i][j].setTranslateX(20);
                                cardButtons[i][j].setTranslateY(20*j+30);
                            }
                            case 1 -> {
                                cardImage.setFitWidth(60);
                                cardButtons[i][j].setTranslateX(20*j+330);
                                cardButtons[i][j].setTranslateY(10);
                            }
                            default -> {
                                cardImage.setFitHeight(60);
                                cardButtons[i][j].setTranslateX(1000-200);
                                cardButtons[i][j].setTranslateY(20*j+30);
                            }
                        }
                        cardButtons[i][j].setGraphic(cardImage);
                        
                        list.add(cardButtons[i][j]);
                    } catch (FileNotFoundException ex) {
                        System.out.println(ex);
                    }
                }
                else{
                    Image image = null;
                    try {
                        image = new Image(new FileInputStream(players[currentPlayer].getHandCards()[j].getImageAddress()));
                    } catch (FileNotFoundException ex) {
                        System.out.println(ex);
                    }
                    
                    ImageView cardImage= new ImageView(image);
                    cardImage.setPreserveRatio(true);
                    cardImage.setFitWidth(80);
                    
                    list.remove(cardButtons[i][j]);
                    
                    cardButtons[i][j] = new Button();
                    cardButtons[i][j].setStyle("-fx-focus-color: white;");
                    if(j<=6){
                        cardButtons[i][j].setTranslateX(80*j+20);
                        cardButtons[i][j].setTranslateY(700-230);
                    }
                    else{
                        cardButtons[i][j].setTranslateX(80*(j-7)+20);
                        cardButtons[i][j].setTranslateY(700-130);
                    }
                    cardButtons[i][j].setGraphic(cardImage);
                    cardButtons[i][j].setOnAction((ActionEvent t) -> {
                        for(int x=0; x<players.length; x++){
                            for(int y=0; y<players[x].getHandCards().length; y++){
                                if(cardButtons[x][y].equals(t.getSource())){
                                    pickCard(y);
                                    return;
                                }
                            }
                        }
                    });
                    
                    list.add(cardButtons[i][j]);
                }
            }
            index += i==currentPlayer?0:1;
        }
    }
    
    public static void pickCard(int index){
        Card pickedCard = players[currentPlayer].getHandCards()[index];
        if(currentPlayer!=0 && players[currentPlayer].hasColor(playedCards[0].getColor()) && !pickedCard.getColor().equals(playedCards[0].getColor())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Image icon = null;
            try {
                icon = new Image(new FileInputStream(".\\src\\Graphics\\icon.png"));
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(icon);
            alert.setGraphic(new ImageView(icon));
            
            alert.setTitle("Invalid Card Pick");
            alert.setHeaderText("You have "+playedCards[0].getColor()+" color Card!\n"+
                                "("+playedCards[0].getColor()+" Card is played by First Player)");
            alert.setContentText("Please choose any from your "+playedCards[0].getColor()+" color Card");
            
            alert.show();
        }
        else{
            pickedCard = players[currentPlayer].playCard(index);
            if(!donald.isActivated() && players[currentPlayer].equals(donald.getCaller()) && pickedCard.getColor().equals(donald.getColor())){
                donald.setActivated(true);
            }
            
            try {
                playedImages[currentPlayer] = new ImageView(new Image(new FileInputStream(pickedCard.getImageAddress())));
            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
            playedImages[currentPlayer].setX((currentPlayer-1)*130+330);
            playedImages[currentPlayer].setY(220);
            playedImages[currentPlayer].setPreserveRatio(true);
            playedImages[currentPlayer].setFitWidth(100);

            playedTexts[currentPlayer] = new Text(players[currentPlayer].getName());
            playedTexts[currentPlayer].setFont(new Font(20));
            playedTexts[currentPlayer].setFill(Color.WHITE);
            playedTexts[currentPlayer].setX((currentPlayer-1)*130+335);
            playedTexts[currentPlayer].setY(215);

            playedCards[currentPlayer] = pickedCard;
            list.addAll(playedImages[currentPlayer], playedTexts[currentPlayer]);
            
            updateCurrent();
            if(currentPlayer!=3){
                currentPlayer = currentPlayer+1;
            }
            else{
                currentPlayer = Card.highestRank(playedCards, donald);
                setFirstPlayer(players[currentPlayer]);
                currentPlayer = 0;

                players[currentPlayer].setScore(players[currentPlayer].getScore()+1);
                players[currentPlayer].getTeam().setScore(players[currentPlayer].getTeam().getScore()+1);

                for(int i=0; i<playedImages.length; i++){
                   list.removeAll(playedImages[i], playedTexts[i]);
                }
                
                Text roundRecord = new Text("Round "+roundCounter+" winner: "+players[currentPlayer].getName());
                roundRecord.setX(10);
                roundRecord.setY((roundCounter-1)*150+50);
                roundRecord.setFont(new Font(15));
                for(int i=0; i<playedImages.length; i++){
                    playedImages[i].setFitWidth(80);
                    playedImages[i].setX(10+90*i);
                    playedImages[i].setY((roundCounter-1)*150+55);
                    pane.getChildren().add(playedImages[i]);
                }
                pane.getChildren().add(roundRecord);
                
                playedImages = new ImageView[4];
                playedTexts = new Text[4];
                playedCards = new Card[4];
                roundCounter++;
            }
            
            if(players[currentPlayer].getTeam().getScore()>=players[currentPlayer].getTeam().getWinScore()){
                td.setTitle("Game Over");
                td.getEditor().setText("");
                String member1 = players[currentPlayer].getTeam().getMembers()[0].getName();
                String member2 = players[currentPlayer].getTeam().getMembers()[1].getName();
                td.setHeaderText("Congratulation! "+member1+" and "+member2+" win!\n"+
                                 "Play Again?");
                td.setContentText("[YES/NO]");
                td.showAndWait();
                String restart = td.getEditor().getText();
                boolean valid = restart.equals("YES") || restart.equals("NO");
                while(!valid){
                    td.getEditor().setText("");
                    td.setHeaderText("YES/NO only");
                    td.showAndWait();
                    restart = td.getEditor().getText();
                    valid = restart.equals("YES") || restart.equals("NO");
                }
                
                Stage stage = (Stage)(cardButtons[0][0].getScene().getWindow());
                stage.close();
                if(restart.equals("YES")){
                    startGame(stage);
                }
            }
            else{
                if(!(players[currentPlayer] instanceof Bot)){
                    updateScene();
                    coverScreen("Round: "+roundCounter+"\n"+players[currentPlayer].getName());
                }
                else{
                    int autoPick = ((Bot)players[currentPlayer]).decideCard(playedCards, donald);
                    pickCard(autoPick);
                }
            }
        }
    }
    
    public static Button coverScreen(String playerName){
        Button cover = new Button();
        
        cover.setStyle("-fx-background-color: #beebee; ");
        cover.setPrefSize(1300, 720);
        cover.setTranslateX(0);
        cover.setTranslateY(0);
        cover.setOnAction((t) -> {
            list.remove(cover);
        });
        
        cover.setFont(new Font(30));
        cover.setText(playerName+"'s Turn\n\nClick if Ready");
        list.add(cover);
        return cover;
    }
    
    public static void setFirstPlayer(Player p){
        currentPlayer = 0;
        for(int i=1; i<players.length; i++){
            if(players[i].equals(p)){
                Player temp = players[0];
                players[0] = players[i];
                players[i] = temp;
            }
        }
    }
}
