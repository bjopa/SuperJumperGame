public class Player {

    private int xPos;
    private int yPos;
    private int gameCycle;
    private boolean doInitialize;

    public Player(int xPos, int yPos, int gameCycle, boolean doInitialize) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.gameCycle = gameCycle;
        this.doInitialize=doInitialize;
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
