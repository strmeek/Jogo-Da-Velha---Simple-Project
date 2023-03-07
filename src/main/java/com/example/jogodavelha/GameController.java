package com.example.jogodavelha;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private Label LabelTurn;

    @FXML
    private Button buttonClose;

    @FXML
    private Button buttonDarkMode;

    @FXML
    private Button button_0_0;

    @FXML
    private Button button_0_1;

    @FXML
    private Button button_0_2;

    @FXML
    private Button button_1_0;

    @FXML
    private Button button_1_1;

    @FXML
    private Button button_1_2;

    @FXML
    private Button button_2_0;

    @FXML
    private Button button_2_1;

    @FXML
    private Button button_2_2;

    @FXML
    private GridPane gridGame;

    @FXML
    private ImageView imgViewClose;

    @FXML
    private ImageView imgViewDarkMode;

    @FXML
    private ImageView imgViewTurn;

    @FXML
    private ImageView img_0_0;

    @FXML
    private ImageView img_0_1;

    @FXML
    private ImageView img_0_2;

    @FXML
    private ImageView img_1_0;

    @FXML
    private ImageView img_1_1;

    @FXML
    private ImageView img_1_2;

    @FXML
    private ImageView img_2_0;

    @FXML
    private ImageView img_2_1;

    @FXML
    private ImageView img_2_2;

    @FXML
    private VBox vboxGame;

    HashMap buttonIcons = new HashMap();

    Object[][] matriz = new Object[3][3];

    private boolean darkMode = false;
    private boolean turn = false;
    private int countPlays = 0;

    Image currentTurnImage;
    Image lightCircle = new Image("C:\\Java Projects\\JogoDaVelha\\src\\main\\resources\\com\\example\\jogodavelha\\icons\\Circle.png");
    Image lightX = new Image("C:\\Java Projects\\JogoDaVelha\\src\\main\\resources\\com\\example\\jogodavelha\\icons\\Xicon.png");
    Image blackCircle = new Image("C:\\Java Projects\\JogoDaVelha\\src\\main\\resources\\com\\example\\jogodavelha\\icons\\BlackCircle.png");
    Image blackX = new Image("C:\\Java Projects\\JogoDaVelha\\src\\main\\resources\\com\\example\\jogodavelha\\icons\\BlackX.png");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshGrid();
        checkTurn();
        standardStyle();

        buttonDarkMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeMode();
                playAgain();
            }
        });
        buttonClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }


    public void handleClicks(ActionEvent event){
        //Recognize the position Clicked
        Object buttonClicked = event.getSource();
        //set the image for user
        for(Node node : gridGame.getChildren()){
            if (Objects.equals(node, buttonClicked)) {
                ImageView icon = (ImageView) buttonIcons.get(node);
                if(icon.getImage() == null){
                    //sets the image
                    icon.setImage(currentTurnImage);
                    //saves on the matrix
                    int row = GridPane.getRowIndex(node);
                    int col = GridPane.getColumnIndex(node);

                    matriz[row][col] = icon.getImage().getUrl();
                    //check if it's a win
                    if(checkVictory()){
                        playAgain();
                        break;
                    }
                    //if it's not, changes the turn
                    swapTurn();
                    //sets the image for the next turn and count the moves
                    checkTurn();
                } else {
                    Alert cannotPlay = new Alert(Alert.AlertType.INFORMATION);
                    cannotPlay.setContentText("Alguém já selecionou essa casa, por favor escolha uma vazia.");
                    cannotPlay.show();
                }
            }
        }
    }
    public boolean checkVictory(){

        //Row winning possibilities
        for (int i = 0; i < 3; i++) {
            //if any element on the column 0 is not null
            if(matriz[i][0] != null &&
                    //and the next element is equal to the previous
                    matriz[i][0].equals(matriz[i][1]) &&
                    //and also the next one
                    matriz[i][0].equals(matriz[i][2])){

                //player wins
                setWinningCombinations(i, 0, i, 1, i, 2);
                winningMessage();
                return true;
            }
        }
        //Colum winning possibilities
        for(int i = 0; i < 3; i++) {
            //if any element on the row 0 is not null
            if (matriz[0][i] != null &&
                    //and the next down element is equal to the previous
                    matriz[0][i].equals(matriz[1][i])  &&
                    //and also the next one
                    matriz[0][i].equals(matriz[2][i])) {

                //player wins
                setWinningCombinations(0, i, 1, i, 2, i);
                winningMessage();
                return true;
            }
        }
        //diagonals possibilities
        if(matriz[0][0] != null &&
                matriz[0][0].equals(matriz[1][1]) &&
                matriz[0][0].equals(matriz[2][2])){
            //player wins
            setWinningCombinations(0, 0, 1, 1, 2, 2);
            winningMessage();
            return true;
        }

        if(matriz[0][2] != null &&
                matriz[0][2].equals(matriz[1][1]) &&
                matriz[0][2].equals(matriz[2][0])){
            //Player wins!
            setWinningCombinations(0, 2, 1, 1, 2, 0);
            winningMessage();
            return true;
        }

        // Check for tie
        if(countPlays == 9){
            Alert tie = new Alert(Alert.AlertType.INFORMATION);
            tie.setContentText("Jogo empatado.");
            tie.show();
            return true;
        }
        return false;
    }
    public void setWinningCombinations(int row1,
                                       int col1,
                                       int row2,
                                       int col2,
                                       int row3,
                                       int col3){
        Node win1 = getNodeFromGridPane(gridGame,row1,col1);
        Node win2 = getNodeFromGridPane(gridGame,row2,col2);
        Node win3 = getNodeFromGridPane(gridGame,row3,col3);
        win1.setStyle("-fx-background-color: -GREEN;");
        win2.setStyle("-fx-background-color: -GREEN;");
        win3.setStyle("-fx-background-color: -GREEN;");
    }

    public Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    public void refreshGrid(){
        gridGame.getChildren().clear();

        gridGame.add(button_0_0,0,0);
        gridGame.add(button_0_1,0,1);
        gridGame.add(button_0_2,0,2);
        gridGame.add(button_1_0,1,0);
        gridGame.add(button_1_1,1,1);
        gridGame.add(button_1_2,1,2);
        gridGame.add(button_2_0,2,0);
        gridGame.add(button_2_1,2,1);
        gridGame.add(button_2_2,2,2);

        linkButtonsToIcons();
    }
    public void checkTurn(){
        if(turn == false && darkMode == false){
            currentTurnImage = new Image(blackX.getUrl());
            imgViewTurn.setImage(currentTurnImage);
        } else if(turn == false && darkMode == true) {
            currentTurnImage = new Image(lightX.getUrl());
            imgViewTurn.setImage(currentTurnImage);
        } else if(turn == true && darkMode == false) {
            currentTurnImage = new Image(blackCircle.getUrl());
            imgViewTurn.setImage(currentTurnImage);
        } else {
            currentTurnImage = new Image(lightCircle.getUrl());
            imgViewTurn.setImage(currentTurnImage);
        }
        countPlays += 1;
    }
    public boolean swapTurn(){
        return turn = !turn;
    }
    public void winningMessage(){
        if (turn == false){
            Alert xWin = new Alert(Alert.AlertType.INFORMATION);
            xWin.setContentText("X VENCEU");
            xWin.show();
        } else {
            Alert oWin = new Alert(Alert.AlertType.INFORMATION);
            oWin.setContentText("CIRCLE VENCEU");
            oWin.show();
        }
    }
    public Object[] iconArray(){
        Object[] icons = new Object[9];

        icons[0] = img_0_0;
        icons[1] = img_1_0;
        icons[2] = img_2_0;
        icons[3] = img_0_1;
        icons[4] = img_1_1;
        icons[5] = img_2_1;
        icons[6] = img_0_2;
        icons[7] = img_1_2;
        icons[8] = img_2_2;

        return icons;
    }
    public void linkButtonsToIcons(){
        buttonIcons.clear();

        buttonIcons.put(button_0_0, img_0_0);
        buttonIcons.put(button_0_1, img_0_1);
        buttonIcons.put(button_0_2, img_0_2);
        buttonIcons.put(button_1_0, img_1_0);
        buttonIcons.put(button_2_0, img_2_0);
        buttonIcons.put(button_1_1, img_1_1);
        buttonIcons.put(button_1_2, img_1_2);
        buttonIcons.put(button_2_1, img_2_1);
        buttonIcons.put(button_2_2, img_2_2);
    }
    public void changeMode(){
        darkMode = !darkMode;
        if(darkMode == false){
            standardStyle();
        } else {
            darkMode();
        }
    }
    public void standardStyle(){
        Image image = new Image("C:\\Java Projects\\JogoDaVelha\\src\\main\\resources\\com\\example\\jogodavelha\\icons\\lightmode.png");
        imgViewDarkMode.setImage(image);
        imgViewClose.setStyle("-fx-effect: innershadow(gaussian, #000, 10, 0, 255px, 255px);");
        imgViewDarkMode.setStyle("-fx-effect: innershadow(gaussian, #000, 10, 0, 255px, 255px);");
        vboxGame.setStyle("-fx-background-color: -MATTE-WHITE");
        LabelTurn.setStyle("-fx-text-fill: -BLACK");
        button_0_0.setStyle("-GRID-LINES: -BLACK");
        button_0_1.setStyle("-GRID-LINES: -BLACK");
        button_0_2.setStyle("-GRID-LINES: -BLACK");
        button_1_0.setStyle("-GRID-LINES: -BLACK");
        button_1_1.setStyle("-GRID-LINES: -BLACK");
        button_1_2.setStyle("-GRID-LINES: -BLACK");
        button_2_0.setStyle("-GRID-LINES: -BLACK");
        button_2_1.setStyle("-GRID-LINES: -BLACK");
        button_2_2.setStyle("-GRID-LINES: -BLACK");
        buttonDarkMode.setStyle("-fx-border-color: -BLACK");
        buttonClose.setStyle("-fx-border-color: -BLACK");

        //checks who was the turn and change the image
        if(currentTurnImage.equals(lightX)){
            imgViewTurn.setImage(blackX);
            currentTurnImage = new Image(blackX.getUrl());
        } else if(currentTurnImage.equals(lightCircle)){
            imgViewTurn.setImage(blackCircle);
            currentTurnImage = new Image(blackCircle.getUrl());
        } else {
            checkTurn();
        }
    }
    public void darkMode(){
        Image moonImage = new Image("C:\\Java Projects\\JogoDaVelha\\src\\main\\resources\\com\\example\\jogodavelha\\icons\\nightmode.png");

        imgViewDarkMode.setImage(moonImage);
        imgViewClose.setStyle("-fx-effect: innershadow(gaussian, #000, 0, 0, 0px, 0px);");
        imgViewDarkMode.setStyle("-fx-effect: innershadow(gaussian, #000, 0, 0, 0px, 0px);");
        vboxGame.setStyle("-fx-background-color: -BLACK;");
        button_0_0.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_0_1.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_0_2.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_1_0.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_1_1.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_1_2.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_2_0.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_2_1.setStyle("-GRID-LINES: -MATTE-WHITE;");
        button_2_2.setStyle("-GRID-LINES: -MATTE-WHITE;");
        LabelTurn.setStyle("-fx-text-fill: -MATTE-WHITE;");
        buttonDarkMode.setStyle("-fx-border-color: -MATTE-WHITE;");
        buttonClose.setStyle("-fx-border-color: -MATTE-WHITE;");

        //checks who was the turn and change the image
        if(currentTurnImage.equals(blackX)){
            imgViewTurn.setImage(lightX);
            currentTurnImage = new Image(lightX.getUrl());
        } else if (currentTurnImage.equals(blackCircle)){
            imgViewTurn.setImage(lightCircle);
            currentTurnImage = new Image(lightCircle.getUrl());
        } else {
            checkTurn();
        }
    }
    public void playAgain(){
        //clears matrix
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matriz[i][j] = null;
            }
        }
        //clears icons
        Object[] arr = iconArray();
        for (int i = 0; i < arr.length; i++) {
            ImageView icon = (ImageView) arr[i];
            icon.setImage(null);
            refreshGrid();
            countPlays = 0;
        }
        //erase green blocks
        if(darkMode == false){
            standardStyle();
        } else {
            darkMode();
        }
    }
}