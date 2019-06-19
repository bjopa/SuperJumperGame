public class Player {

    private int xPos;
    private int yPos;
    private int newGame;

    public Player(int xPos, int yPos, int newGame) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.newGame = newGame;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getNewGame() {
        return newGame;
    }

    public void setNewGame(int newGame) {
        this.newGame = newGame;
    }
}
