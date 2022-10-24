package Layouts;

import Player.Player;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class GameBoard extends Group {

    public interface winHandler {
        void onWin(Player player, int numberOfClicks);
    }

    private winHandler handler = null;
    private GridPane header = new GridPane();
    private Label displayUserName = new Label();
    private ImageView displayLittleMan = new ImageView(new Image("/Images/HappyFace.png"));
    //private Label displayLittleMan = new Label();
    private Label displayNumberOfMines = new Label();
    private Label displayNumberOfUndiscoveredTiles = new Label();
    private GridPane board = new GridPane();
    private int remainingMines;
    private int remainingTiles;
    private int numberOfClicks;
    private Player currentPlayer;
    private int[][] boardWithNumbers;
    private int amountOfMines;
    private int amountOfRows;
    private int amountOfColumns;

    Media soundOfClick = new Media(new File("./src/Voices/click.mp3").toURI().toString());
    Media soundOfExplosion = new Media(new File("./src/Voices/explosion.wav").toURI().toString());
    Media soundOfWin = new Media(new File("./src/Voices/win.wav").toURI().toString());
    MediaPlayer playSounds;


    public GameBoard(Player player) {
        currentPlayer = player;

        start();
    }

    public double getGridWidth() {
        return board.getPrefWidth();
    }

    public double getGridHeight() {
        return board.getPrefHeight();
    }

    public void setResultHandler(winHandler handler) {
        this.handler = handler;
    }

    private void start() {
        numberOfClicks = 0;

        displayLittleMan.setFitWidth(30);
        displayLittleMan.setFitHeight(30);

        ColumnConstraints constraintForName = new ColumnConstraints();
        ColumnConstraints constraintForLittleMan = new ColumnConstraints();
        ColumnConstraints constraintForMineCounter = new ColumnConstraints();
        ColumnConstraints constraintForTilesCounter = new ColumnConstraints();

        header.getColumnConstraints().addAll(constraintForName, constraintForLittleMan, constraintForMineCounter, constraintForTilesCounter);

        setBoardSizeAndNumberOfMines();

        setColumnsOfTheHeader();

        header.add(displayUserName, 0, 0);
        header.add(displayLittleMan, 1, 0);
        header.add(displayNumberOfMines, 2, 0);
        header.add(displayNumberOfUndiscoveredTiles, 3, 0);

        displayUserName.setText(currentPlayer.getName());

        displayNumberOfMines.setText("Mines: " + amountOfMines);
        remainingMines = amountOfMines;

        displayNumberOfUndiscoveredTiles.setText("Tiles: " + amountOfRows * amountOfColumns + "");
        remainingTiles = amountOfRows * amountOfColumns;

        for (int i = 0; i < header.getChildren().size(); i++) {
            if (header.getChildren().get(i) instanceof Label) {
                ((Label) header.getChildren().get(i)).setFont(Font.font("Courier New", FontWeight.BOLD, 18));
            }
        }

        displayLittleMan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                restart();
            }
        });

        boardWithNumbers = new int[amountOfRows][amountOfColumns];
        placeMines();
        addNumberOfTheMinesAround();

        fillUpTheBoard();

        board.setPadding(new Insets(30, 0, 0, 10));

        this.getChildren().addAll(
                board,
                header
        );
    }

    private void restart() {
        this.getChildren().clear();
        board.getChildren().clear();
        header.getChildren().clear();

        board.setDisable(false);

        displayLittleMan.setImage(new Image("/Images/HappyFace.png"));

        start();
    }

    private void fillUpTheBoard() {
        for (int i = 0; i < amountOfRows; i++) {
            for (int j = 0; j < amountOfColumns; j++) {
                Rectangle tile = new Rectangle(20, 20);

                tile.setFill(Color.LIGHTGRAY);
                tile.setStroke(Color.BLACK);

                board.add(tile, j, i);

                tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (tile.getFill() == Color.LIGHTGRAY) {
                                selectPosition(board.getChildren().indexOf(tile) / amountOfColumns, board.getChildren().indexOf(tile) % amountOfColumns);
                            }
                        }

                        if (event.getButton() == MouseButton.SECONDARY) {
                            if (tile.getFill() == Color.LIGHTGRAY) {
                                tile.setFill(new ImagePattern(new Image("/Images/Mark.png")));

                                remainingMines--;
                                displayNumberOfMines.setText("Mines: " + remainingMines);
                            } else {
                                tile.setFill(Color.LIGHTGRAY);

                                remainingMines++;
                                displayNumberOfMines.setText("Mines: " + remainingMines);
                            }
                        }

                        if (event.getButton() == MouseButton.MIDDLE) {
                            if (tile.getFill() == Color.LIGHTGRAY) {
                                selectPosition(board.getChildren().indexOf(tile) / amountOfColumns, board.getChildren().indexOf(tile) % amountOfColumns);
                            }
                        }

                        playSounds = new MediaPlayer(soundOfClick);
                        playSounds.play();

                        numberOfClicks++;

                        displayNumberOfUndiscoveredTiles.setText("Tiles: " + remainingTiles);

                        doWin();
                    }
                });

                tile.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        displayLittleMan.setImage(new Image("/Images/NervousFace.png"));
                    }
                });

                tile.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        displayLittleMan.setImage(new Image("/Images/HappyFace.png"));
                    }
                });
            }
        }
    }

    private void setBoardSizeAndNumberOfMines() {
        if (currentPlayer.getDifficulty().equals("Beginner")) {
            amountOfRows = 20;
            amountOfColumns = 20;
            amountOfMines = 5;
        } else if (currentPlayer.getDifficulty().equals("Intermediate")) {
            amountOfRows = 20;
            amountOfColumns = 40;
            amountOfMines = 100;
        } else if (currentPlayer.getDifficulty().equals("Expert")) {
            amountOfRows = 40;
            amountOfColumns = 40;
            amountOfMines = 350;
        }
    }

    private void setColumnsOfTheHeader() {
        for (int i = 0; i < header.getColumnConstraints().size(); i++) {
            if (i != 1) {
                header.getColumnConstraints().get(i).setPrefWidth(amountOfColumns * 20 / 3.0);
                header.getColumnConstraints().get(i).setHalignment(HPos.CENTER);
            }
        }
    }

    private int randomGenerator(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }

    private void placeMines() {
        int counter = 0;

        while (counter < amountOfMines) {
            int row = randomGenerator(amountOfRows);
            int column = randomGenerator(amountOfColumns);

            if (boardWithNumbers[row][column] != 10) {
                boardWithNumbers[row][column] = 10;

                counter++;
            }
        }
    }

    private void addNumberOfTheMinesAround() {
        for (int i = 0; i < boardWithNumbers.length; i++) {
            for (int j = 0; j < boardWithNumbers[i].length; j++) {
                if (boardWithNumbers[i][j] == 10) {
                    checkAround(i, j);
                }
            }
        }
    }

    private void checkAround(int specificRow, int specificColumn) {
        int row = specificRow;
        int column = specificColumn - 1;

        if (specificColumn > 0 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow - 1;
        column = specificColumn - 1;

        if (specificRow > 0 && specificColumn > 0 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow - 1;
        column = specificColumn;

        if (specificRow > 0 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow - 1;
        column = specificColumn + 1;

        if (specificRow > 0 && specificColumn < boardWithNumbers[specificRow].length - 1 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow;
        column = specificColumn + 1;

        if (specificColumn < boardWithNumbers[specificRow].length - 1 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow + 1;
        column = specificColumn + 1;

        if (specificRow < boardWithNumbers.length - 1 && specificColumn < boardWithNumbers[specificRow].length - 1 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow + 1;
        column = specificColumn;

        if (specificRow < boardWithNumbers.length - 1 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }

        row = specificRow + 1;
        column = specificColumn - 1;

        if (specificRow < boardWithNumbers.length - 1 && specificColumn > 0 && boardWithNumbers[row][column] != 10) {
            boardWithNumbers[row][column] += 1;
        }
    }

    private void selectPosition(int specificRow, int specificColumn) {
        if (boardWithNumbers[specificRow][specificColumn] == 10) {
            displayLittleMan.setImage(new Image("/Images/SadFace.png"));

            playSounds = new MediaPlayer(soundOfExplosion);
            playSounds.play();

            for (int i = 0; i < amountOfRows; i++) {
                for (int j = 0; j < amountOfColumns; j++) {
                    if (boardWithNumbers[i][j] == 10 && this.board.getChildren().get(i * amountOfColumns + j) instanceof Rectangle) {
                        ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/Mine.png")));
                    }
                }
            }

            this.board.setDisable(true);
        } else if (boardWithNumbers[specificRow][specificColumn] == 0) {
            if (this.board.getChildren().get(specificRow * amountOfColumns + specificColumn) instanceof Rectangle) {
                if (((Rectangle) this.board.getChildren().get(specificRow * amountOfColumns + specificColumn)).getFill() == Color.LIGHTGRAY) {
                    selectedPositionIsBlank(specificRow, specificColumn);
                }
            }
        } else {
            setImageOfNumber(specificRow, specificColumn);

            remainingTiles--;
        }
    }

    private void selectedPositionIsBlank(int specificRow, int specificColumn) {
        if (this.board.getChildren().get(specificRow * amountOfColumns + specificColumn) instanceof Rectangle) {
            ((Rectangle) this.board.getChildren().get(specificRow * amountOfColumns + specificColumn)).setFill(Color.WHITE);
            this.board.getChildren().get(specificRow * amountOfColumns + specificColumn).setDisable(true);
        }

        remainingTiles--;

        boardWithNumbers[specificRow][specificColumn] = -1;

        int row = specificRow;
        int column = specificColumn - 1;

        if (specificColumn > 0) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow - 1;
        column = specificColumn - 1;

        if (specificRow > 0 && specificColumn > 0) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow - 1;
        column = specificColumn;

        if (specificRow > 0) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow - 1;
        column = specificColumn + 1;

        if (specificRow > 0 && specificColumn < boardWithNumbers[specificRow].length - 1) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow;
        column = specificColumn + 1;

        if (specificColumn < boardWithNumbers[specificRow].length - 1) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow + 1;
        column = specificColumn + 1;

        if (specificRow < boardWithNumbers.length - 1 && specificColumn < boardWithNumbers[specificRow].length - 1) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow + 1;
        column = specificColumn;

        if (specificRow < boardWithNumbers.length - 1) {
            checkIfAroundPositionIsBlank(row, column);
        }

        row = specificRow + 1;
        column = specificColumn - 1;

        if (specificRow < boardWithNumbers.length - 1 && specificColumn > 0) {
            checkIfAroundPositionIsBlank(row, column);
        }
    }

    private void checkIfAroundPositionIsBlank(int specificRow, int specificColumn) {
        if (this.board.getChildren().get(specificRow * amountOfColumns + specificColumn) instanceof Rectangle) {
            if (((Rectangle) this.board.getChildren().get(specificRow * amountOfColumns + specificColumn)).getFill() == Color.LIGHTGRAY) {
                if (boardWithNumbers[specificRow][specificColumn] != 0) {
                    remainingTiles--;

                    setImageOfNumber(specificRow, specificColumn);
                } else {
                    selectedPositionIsBlank(specificRow, specificColumn);
                }
            }
        }
    }

    private void setImageOfNumber(int i, int j) {
        if (this.board.getChildren().get(i * amountOfColumns + j) instanceof Rectangle) {
            if (boardWithNumbers[i][j] == 1) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberOne.png")));
            } else if (boardWithNumbers[i][j] == 2) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberTwo.png")));
            } else if (boardWithNumbers[i][j] == 3) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberThree.png")));
            } else if (boardWithNumbers[i][j] == 4) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberFour.png")));
            } else if (boardWithNumbers[i][j] == 5) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberFive.png")));
            } else if (boardWithNumbers[i][j] == 6) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberSix.png")));
            } else if (boardWithNumbers[i][j] == 7) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberSeven.png")));
            } else if (boardWithNumbers[i][j] == 8) {
                ((Rectangle) this.board.getChildren().get(i * amountOfColumns + j)).setFill(new ImagePattern(new Image("/Images/NumberEight.png")));
            }

            this.board.getChildren().get(i * amountOfColumns + j).setDisable(true);
        }
    }

    private void doWin() {
        if (remainingTiles == amountOfMines) {
            playSounds = new MediaPlayer(soundOfWin);
            playSounds.play();

            if (handler != null) {
                handler.onWin(currentPlayer, numberOfClicks);
            }

            board.setDisable(true);
        }
    }

}
