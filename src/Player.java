public class Player {

    char face;
    private int xPos;
    private int yPos;
    private int gameCycle;
    private boolean doInitialize;

    public Player(char face, int xPos, int yPos, int gameCycle, boolean doInitialize) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.gameCycle = gameCycle;
        this.doInitialize=doInitialize;
        this.face = face;
    }

    public char getFace() {
        return face;
    }

    public void setFace(char face) {
        this.face = face;
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

    public int getGameCycle() {
        return gameCycle;
    }

    public void setGameCycle(int gameCycle) {
        this.gameCycle = gameCycle;
    }

    public boolean getDoInitialize() {
        return doInitialize;
    }

    public void setDoInitialize(boolean doInitialize) {
        this.doInitialize = doInitialize;
    }


}
