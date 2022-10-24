import Player.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Layouts.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage greetingStage) throws Exception {
        Menu menu = new Menu();

        greetingStage.setTitle("Menu");
        greetingStage.setScene(new Scene(menu, 320, 350));
        greetingStage.setResizable(false);

        greetingStage.show();

        menu.setResultHandler(new Menu.ResultHandler() {
            @Override
            public void onSubmit(Player player) {
                Stage gameStage = new Stage();
                gameStage.setTitle("Game");
                gameStage.initOwner(greetingStage);
                gameStage.initModality(Modality.WINDOW_MODAL);

                GameBoard gameLayout = new GameBoard(player);

                gameStage.setScene(new Scene(gameLayout, gameLayout.getGridWidth(), gameLayout.getGridHeight()));
                gameStage.setResizable(false);

                gameStage.show();

                gameLayout.setResultHandler(new GameBoard.winHandler() {
                    @Override
                    public void onWin(Player player, int numberOfClicks) {
                        Stage winStage = new Stage();
                        winStage.setTitle("Win");
                        winStage.initOwner(gameStage);
                        winStage.initModality(Modality.WINDOW_MODAL);

                        winStage.setScene(new Scene(new Win(player, numberOfClicks), 400, 200));
                        winStage.setResizable(false);

                        winStage.showAndWait();
                    }
                });
            }

            @Override
            public void onImage() {
                Stage infoStage = new Stage();
                infoStage.setTitle("Info");
                infoStage.initOwner(greetingStage);
                infoStage.initModality(Modality.WINDOW_MODAL);

                infoStage.setScene(new Scene(new Info(), 550, 380));
                infoStage.setResizable(false);

                infoStage.showAndWait();
            }
        });
    }

}

//TODO: We should add comments to the methods, what do they for