package Player;

public class Player {

    private String name;
    private String difficulty;

    public Player(String name, String difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

}
