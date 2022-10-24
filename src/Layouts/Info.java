package Layouts;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Info extends VBox {

    private Label displayInfo = new Label("WELCOME TO OUR MINESWEEPER GAME\n" +
            "\n" +
            "RULES\n" +
            "Minesweeper rules are very simple. The board is divided into cells, with mines randomly distributed.\n" +
            "To win, you need to open all the cells. The number on a cell shows the number of mines adjacent to it.\n" +
            "Using this information, you can determine cells that are safe, and cells that contain mines.\n" +
            "Cells suspected of being mines can be marked with a flag using the right mouse button.\n" +
            "If you click on a mine the game is over.\n" +
            "\n" +
            "START THE GAME\n" +
            "Enter your name and click on start.\n" +
            "Left click: Select a tile\n" +
            "Right click: Mark a tile with a flag\n" +
            "Smiley/Dead face: Restarts the game\n" +
            "\n" +
            "DEVELOPED BY\n" +
            "Gergő Holló\n" +
            "Filippos Mantzios\n" +
            "Konstantina Aggelopoulou\n" +
            "\n" +
            "SPECIAL THANKS TO\n" +
            "Mr. Helder Pereira");

    public Info() {
        this.getChildren().add(displayInfo);
    }

}
