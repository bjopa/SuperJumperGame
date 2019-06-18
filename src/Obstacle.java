import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

public class Obstacle {
    private int xPos;
    private int yPos;
    private int timer;

    public Obstacle(/*int[] body*/ int xPos, int yPos, int timer) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.timer = timer;
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

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void moveObstacle(Terminal terminal, Player p1) throws  Exception {

        int oldXO = xPos;
        int oldYO = yPos;
        xPos=xPos-1;

        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
        terminal.setCursorPosition(oldXO, oldYO);
        terminal.putCharacter(' ');
        terminal.setBackgroundColor(TextColor.ANSI.RED);
        terminal.setCursorPosition(xPos, yPos);
        terminal.putCharacter('O');
        terminal.flush();

    }
}
