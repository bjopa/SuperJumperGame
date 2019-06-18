import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        //Set up Terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false); //size 80x24

        //initial status bar print
        String statusBar = "SUPER JUMPER  - STATUS: ALIVE";
        for (int i = 0; i<statusBar.length(); i++) {
            terminal.setCursorPosition(i+5,1);
            terminal.putCharacter(statusBar.charAt(i));
        }
        terminal.flush();

        //Set up Player 1
        Player p1 = new Player((terminal.getTerminalSize().getColumns() / 2) - 1, terminal.getTerminalSize().getRows()-2);
        int oldX = p1.getxPos(), oldY = p1.getyPos();
        int jumpHeight = 7;
        int direction = 1;

        //Set up Obstacle
//        Obstacle obs1 = new Obstacle(terminal.getTerminalSize().getColumns(), 18, 1);
//        int oldXO = obs1.getxPos(), oldYO=obs1.getyPos();

        //Set up Playfield
//        terminal.setForegroundColor(TextColor.Indexed.fromRGB(170,85,0));
//        for(int i = 0 ; i < terminal.getTerminalSize().getColumns() ; i++) {
//            terminal.setCursorPosition(i, 19);
//            terminal.putCharacter('\u2588');
//        }

        int[][] playGround = new int[80][24];
        for (int i=0; i<playGround.length ; i++) {
            for (int j=0 ; j<playGround[i].length; j++) {
                if (i==0 || i == playGround.length-1) playGround[i][j]=1;
                else {
                    if (j==0 || j==playGround[i].length-1) {
                        playGround[i][j]=1;
                    }
                    else if (i>40 && j == 19) {
                        playGround[i][j]=1;
                    }
                    else if (i<35 && j == 17) {
                        playGround[i][j]=1;
                    }
                    else playGround[i][j]=0;
                }
            }
        }

        for (int i = 0 ; i<playGround.length ; i++) {
            for (int j = 0 ; j < playGround[i].length  ;j++) {
                if (playGround[i][j]==1) {
                    terminal.setCursorPosition(i,j);
                    terminal.putCharacter('\u2588');
                }
            }
        }
        terminal.flush();

        terminal.setForegroundColor(TextColor.ANSI.DEFAULT);
        //terminal.setBackgroundColor(TextColor.Indexed.fromRGB(200,0,0));
        terminal.setBackgroundColor(TextColor.ANSI.GREEN);
        terminal.setCursorPosition(p1.getxPos(), p1.getyPos());
        terminal.putCharacter('O');
//        terminal.setBackgroundColor(TextColor.ANSI.RED);
//        terminal.setCursorPosition(obs1.getxPos(),obs1.getyPos());
//        terminal.putCharacter('O');
        terminal.flush();

        //Set up KeyStroke variables
        KeyStroke keyStroke;
        KeyType type;
        boolean continueReadingInput = true;

        //running it...
        while (continueReadingInput) {
            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
                //move obstacle at interval
//                if (obs1.getTimer()%80==0) {
//                    obs1.moveObstacle(terminal, p1);
//                    obs1.setTimer(1);
//                }
//                obs1.setTimer(obs1.getTimer()+1);
//
//                //check collision
//                collisionChecker(p1,obs1,terminal);

            } while (keyStroke == null);
            type = keyStroke.getKeyType();

            oldX = p1.getxPos();
            oldY = p1.getyPos();
            switch (type) {
                case Escape: {
                    continueReadingInput = false;
                    terminal.close();
                    break;
                }
                case Tab: {
                    jump(p1, oldX, oldY, terminal, jumpHeight, direction, playGround);
                    break;
                }
                case ArrowRight: {
                    direction = 1;
                    p1.setxPos(p1.getxPos() + direction);
                    move(p1, oldX, oldY, terminal/*, obs1*/);
                    break;
                }
                case ArrowLeft: {
                    direction = -1;
                    p1.setxPos(p1.getxPos() + direction);
                    move(p1, oldX, oldY, terminal/*, obs1*/);
                    break;
                }
            } //end switch

        } //end while

    } //end main method

    public static void jump(Player p, int oldX, int oldY, Terminal terminal, int jumpHeight, int direction, int[][] playGround) throws Exception {
        for (int i = 0; i < jumpHeight; i++) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos()-1]==1) {
                System.out.println("hit floor");
                break;
            }

            p.setyPos(p.getyPos() - 1);
            if (i % 3 == 0) p.setxPos(p.getxPos() + direction);

            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            terminal.setCursorPosition(oldX, oldY);
            terminal.putCharacter(' ');
            terminal.setBackgroundColor(TextColor.ANSI.GREEN);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter('O');
            terminal.flush();
            Thread.sleep(12+i*2);
        }

        int currentHeight = p.getyPos();

        for (int i = currentHeight; i > 0; i--) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos()+1]==1) {
                System.out.println("hit floor");
                break;
            }

            p.setyPos(p.getyPos() + 1);
            if (i % 3 == 0) p.setxPos(p.getxPos() + direction);

            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            terminal.setCursorPosition(oldX, oldY);
            terminal.putCharacter(' ');
            terminal.setBackgroundColor(TextColor.ANSI.GREEN);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter('O');
            terminal.flush();
            Thread.sleep(12+(int)(i/4));
        }
    }

    public static void move(Player p, int oldX, int oldY, Terminal terminal/*, Obstacle obs1*/) throws Exception {
        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
        terminal.setCursorPosition(oldX, oldY);
        terminal.putCharacter(' ');
        terminal.setBackgroundColor(TextColor.ANSI.GREEN);
        terminal.setCursorPosition(p.getxPos(), p.getyPos());
        terminal.putCharacter('O');
        terminal.flush();
    }

    public static void collisionChecker(Player p1, Obstacle obs1, Terminal terminal) throws Exception {
        if (obs1.getxPos()==p1.getxPos() && obs1.getyPos()==p1.getyPos()) {
            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            String deathMess = "-DEAD-";
            for (int i = 0; i<deathMess.length(); i++) {
                terminal.setCursorPosition(i+29,1);
                terminal.putCharacter(deathMess.charAt(i));
            }
            terminal.flush();
        }
    }

} //end main class