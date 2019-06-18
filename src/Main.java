import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Main {

    public static void main(String[] args) throws Exception {

        //Set up Terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false); //size 80x24

        //initial status bar print
        String statusBar = "SUPER JUMPER  - STATUS: ALIVE";
        for (int i = 0; i < statusBar.length(); i++) {
            terminal.setCursorPosition(i + 5, 1);
            terminal.putCharacter(statusBar.charAt(i));
        }
        terminal.flush();

        //Set up Player 1
        Player p1 = new Player((terminal.getTerminalSize().getColumns() / 2) - 1, terminal.getTerminalSize().getRows() - 2);
        int oldX = p1.getxPos(), oldY = p1.getyPos();
        int jumpHeight = 7;
        int direction = 1;

        //Set up Enemy
//        Enemy obs1 = new Enemy(terminal.getTerminalSize().getColumns(), 18, 1);
//        int oldXO = obs1.getxPos(), oldYO=obs1.getyPos();

        //Set up Playfield
//        terminal.setForegroundColor(TextColor.Indexed.fromRGB(170,85,0));
//        for(int i = 0 ; i < terminal.getTerminalSize().getColumns() ; i++) {
//            terminal.setCursorPosition(i, 19);
//            terminal.putCharacter('\u2588');
//        }

        int[][] playGround = new int[80][24];
        for (int col = 0; col < playGround.length; col++) {
            for (int row = 0; row < playGround[col].length; row++) {
                if (col == 0 || col == playGround.length - 1) playGround[col][row] = 1;
                else {
                    if (row == 0 || row == playGround[col].length - 1) playGround[col][row] = 1;
                    else if (col > 40 && row == 19) playGround[col][row] = 1;
                    else if (col > 36 && col < 40 && row == 15) playGround[col][row] = 1;
                    else if (col == 34 && row > 12 && row < 17) playGround[col][row] = 1;
                    else if (col < 35 && row == 17) playGround[col][row] = 1;
                    else playGround[col][row] = 0;
                }
            }
        }

        for (int i = 0; i < playGround.length; i++) {
            for (int j = 0; j < playGround[i].length; j++) {
                if (playGround[i][j] == 1) {
                    terminal.setCursorPosition(i, j);
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
//                    obs1.moveEnemy(terminal, p1);
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
                    if (p1.getxPos() + direction == terminal.getTerminalSize().getColumns() - 1) {
                        System.out.println("hit wall");
                        break;
                    }
                    p1.setxPos(p1.getxPos() + direction);
                    move(p1, oldX, oldY, terminal/*, obs1*/);
                    if (playGround[p1.getxPos()][p1.getyPos() + 1] == 0 && p1.getyPos() != terminal.getTerminalSize().getRows() - 2) {
                        fall(p1, oldX, oldY, playGround, direction, terminal);
                    }
                    break;
                }
                case ArrowLeft: {
                    direction = -1;
                    if (p1.getxPos() + direction == 0) {
                        System.out.println("hit wall");
                        break;
                    }
                    p1.setxPos(p1.getxPos() + direction);
                    move(p1, oldX, oldY, terminal/*, obs1*/);
                    if (playGround[p1.getxPos()][p1.getyPos() + 1] == 0 && p1.getyPos() != terminal.getTerminalSize().getRows() - 2) {
                        fall(p1, oldX, oldY, playGround, direction, terminal);
                    }
                    break;
                }
            } //end switch

        } //end while

    } //end main method

    public static void jump(Player p, int oldX, int oldY, Terminal terminal, int jumpHeight, int direction, int[][] playGround) throws Exception {
        for (int i = 0; i < jumpHeight; i++) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos() - 1] == 1) {
                System.out.println("hit ceiling");
                break;
            }

            p.setyPos(p.getyPos() - 1);

            //check hit wall on jump
            if (i % 3 == 0)
                if (playGround[p.getxPos() + direction][p.getyPos()] == 0)
                    p.setxPos(p.getxPos() + direction);

            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            terminal.setCursorPosition(oldX, oldY);
            terminal.putCharacter(' ');
            terminal.setBackgroundColor(TextColor.ANSI.GREEN);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter('O');
            terminal.flush();
            Thread.sleep(12 + i * 2);
        }

        int currentHeight = (terminal.getTerminalSize().getRows() - p.getyPos());
        for (int i = currentHeight; i > 0; i--) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos() + 1] == 1) {
                System.out.println("hit floor");
                break;
            }

            p.setyPos(p.getyPos() + 1);
            //check hit wall on jump
            if (i % 3 == 0)
                if (playGround[p.getxPos() + direction][p.getyPos()] == 0)
                    p.setxPos(p.getxPos() + direction);

            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            terminal.setCursorPosition(oldX, oldY);
            terminal.putCharacter(' ');
            terminal.setBackgroundColor(TextColor.ANSI.GREEN);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter('O');
            terminal.flush();
            Thread.sleep(12 + (int) (i / 4));
        }
    }

    public static void move(Player p, int oldX, int oldY, Terminal terminal/*, Enemy obs1*/) throws Exception {
        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
        terminal.setCursorPosition(oldX, oldY);
        terminal.putCharacter(' ');
        terminal.setBackgroundColor(TextColor.ANSI.GREEN);
        terminal.setCursorPosition(p.getxPos(), p.getyPos());
        terminal.putCharacter('O');
        terminal.flush();
    }

    public static void fall(Player p, int oldX, int oldY, int[][] playGround, int direction, Terminal terminal) throws Exception {
        int currentHeight = (terminal.getTerminalSize().getRows() - p.getyPos());

        for (int i = currentHeight; i > 0; i--) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos() + 1] == 1) {
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
            Thread.sleep(12 + (int) (i / 4));
        }
    }

    public static void collisionChecker(Player p1, Enemy obs1, Terminal terminal) throws Exception {
        if (obs1.getxPos() == p1.getxPos() && obs1.getyPos() == p1.getyPos()) {
            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            String deathMess = "-DEAD-";
            for (int i = 0; i < deathMess.length(); i++) {
                terminal.setCursorPosition(i + 29, 1);
                terminal.putCharacter(deathMess.charAt(i));
            }
            terminal.flush();
        }
    }

} //end main class