package Layouts;

import Player.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Win extends VBox {

    private Label displayCongratulations;
    private Label displayNumberOfClicks;
    private Label displayUserName;
    private BackgroundImage congratulationsImage = new BackgroundImage(new Image("/Images/Win.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

    public Win(Player player, int numberOfClicks) {
        displayCongratulations = new Label("Congratulations");
        displayCongratulations.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        displayUserName = new Label(player.getName() + "!");
        displayUserName.setFont(Font.font("Courier New", FontWeight.BOLD, 22));

        displayNumberOfClicks = new Label("\nYou won the game with " + numberOfClicks + " clicks!");
        displayNumberOfClicks.setFont(Font.font("Courier New", FontWeight.BOLD, 18));

        this.getChildren().addAll(
                displayCongratulations,
                displayUserName,
                displayNumberOfClicks
        );

        this.setAlignment(Pos.CENTER);

        this.setBackground(new Background(congratulationsImage));
    }

}
