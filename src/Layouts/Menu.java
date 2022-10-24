package Layouts;

import Player.Player;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Menu extends VBox {

    public interface ResultHandler {
        void onSubmit(Player player);

        void onImage();
    }

    private ResultHandler resultHandler = null;
    private TextField userName = new TextField();
    private ComboBox difficulties = new ComboBox();
    private Button start = new Button("Start");
    private ImageView welcomeImage = new ImageView(new Image("/Images/MinesweeperWelcome.png"));
    private Label welcomeMessage = new Label("Welcome to Minesweeper");
    private Label name = new Label("Enter your name");

    public Menu() {
        setSpacing(10);
        setAlignment(Pos.TOP_CENTER);

        welcomeMessage.setFont(Font.font("Courier New", FontWeight.BOLD, 22));

        name.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        setComboBox();

        userName.setMaxWidth(200);

        start.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (resultHandler != null) {
                    Player player = new Player(userName.getText(), difficulties.getValue().toString());
                    resultHandler.onSubmit(player);
                }
            }
        });

        this.getChildren().addAll(
                welcomeMessage,
                welcomeImage,
                name,
                userName,
                difficulties,
                start
        );

        welcomeImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (resultHandler != null) {
                    resultHandler.onImage();
                }
            }
        });
    }

    private void setComboBox() {
        difficulties.getItems().addAll(
                "Beginner",
                "Intermediate",
                "Expert"
        );

        difficulties.getSelectionModel().select(1);
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

}
