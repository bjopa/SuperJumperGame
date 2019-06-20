import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        //Set up Terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false); //size 80x24


        //Set up Player 1
        Player theHero = new Player('\u263B', (terminal.getTerminalSize().getColumns() / 2) - 1, terminal.getTerminalSize().getRows() - 2, 1, true);
        int oldX = theHero.getxPos(), oldY = theHero.getyPos();
        int jumpHeight = 7;
        int direction = 1;

//        //Set up Enemy
//        Enemy enemy1 = new Enemy(terminal.getTerminalSize().getColumns()-2, 22, 1);
//        //paint enemy
//        terminal.setBackgroundColor(TextColor.ANSI.RED);
//        terminal.setCursorPosition(enemy1.getxPos(),enemy1.getyPos());
//        terminal.putCharacter('O');

        //Set up levels, allocate initial lvl, set up signs
        Level level1 = new Level("Level1.txt");
        Level level2 = new Level("Level2.txt");
        int[][] playGround = level1.getLevelDesign();
        Message[] messages = new Message[4];
        Message gameOverSign = new Message("GameOverSign.txt");
        Message deathNoteLava = new Message("DeathNoteLava.txt");
        Message deathNoteSpikes = new Message("DeathNoteSpikes.txt");
        Message stageClear = new Message("StageClear.txt");
        messages[0] = gameOverSign;
        messages[1] = deathNoteLava;
        messages[2] = deathNoteSpikes;
        messages[3] = stageClear;

        //Set up KeyStroke variables
        KeyStroke keyStroke;
        KeyType type;
        boolean continueReadingInput = true;

        //running it...
        while (continueReadingInput) {

            //STARTUP PHASE!
            if (theHero.getDoInitialize()) {
                terminal.clearScreen();
                switch (theHero.getGameCycle()) {
                    case 1:
                        playGround = level1.getLevelDesign();
                        break;
                    case 2:
                        playGround = level2.getLevelDesign();
                        break;
                }
                //paint level
                for (int i = 0; i < playGround.length; i++) {
                    for (int j = 0; j < playGround[i].length; j++) {
                        char blockType;
                        switch (playGround[i][j]) {
                            case 1:
                                terminal.setForegroundColor(TextColor.ANSI.DEFAULT);
                                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                                blockType = '\u2588';
                                break;
                            case 2:
                                terminal.setForegroundColor(TextColor.ANSI.YELLOW);
                                terminal.setBackgroundColor(TextColor.ANSI.RED);
                                blockType = '\u25B2';
                                break;
                            case 3:
                                terminal.setForegroundColor(TextColor.ANSI.WHITE);
                                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                                blockType = '\u25B2';
                                break;
                            case 9:
                                terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
                                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                                blockType = '\u2638';
                                break;
                            default:
                                continue;
                        }
                        terminal.setCursorPosition(i, j);
                        terminal.putCharacter(blockType);
                    }
                }
                //initial status bar print
                terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
                terminal.setBackgroundColor(TextColor.ANSI.BLUE);
                String statusBar = "SUPER JUMPER  - LEVEL:" + theHero.getGameCycle() + " - LIVES LEFT:" + theHero.getLives();
                for (int i = 0; i < statusBar.length(); i++) {
                    terminal.setCursorPosition(i + 5, 0);
                    terminal.putCharacter(statusBar.charAt(i));
                }
                terminal.flush();
                //end of startup/nextlvl

                terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setCursorPosition(theHero.getxPos(), theHero.getyPos());
                terminal.putCharacter(theHero.getFace());
                terminal.flush();
                theHero.setDoInitialize(false);
            }

            //Main game input checker/ticker
            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();

//                //move enemy at interval
//                if (enemy1.getTimer()%80==0) {
//                    enemy1.moveEnemy(terminal, theHero);
//                    enemy1.setTimer(1);
//                }
//                enemy1.setTimer(enemy1.getTimer()+1);
//
//                //check collision
//                collisionChecker(theHero,enemy1,terminal);

            } while (keyStroke == null);
            type = keyStroke.getKeyType();

            oldX = theHero.getxPos();
            oldY = theHero.getyPos();
            switch (type) {
                case Escape: {
                    continueReadingInput = false;
                    terminal.close();
                    break;
                }
                case Tab: {
                    jump(theHero, terminal, jumpHeight, direction, playGround, messages);
                    break;
                }
                case ArrowRight: {
                    direction = 1;
                    //check wall hit
                    if (playGround[theHero.getxPos() + direction][theHero.getyPos()] == 1) {
                        break;
                    }
                    theHero.setxPos(theHero.getxPos() + direction);
                    move(theHero, oldX, oldY, terminal);
                    //check what's under player
                    if (playGround[theHero.getxPos()][theHero.getyPos() + 1] != 1) {
                        checkFloor(theHero, playGround, oldX, oldY, direction, terminal, messages);
                        break;
                    }
                    break;
                }
                case ArrowLeft: {
                    direction = -1;
                    //check wall hit
                    if (playGround[theHero.getxPos() + direction][theHero.getyPos()] == 1) {
                        break;
                    }
                    theHero.setxPos(theHero.getxPos() + direction);
                    move(theHero, oldX, oldY, terminal);
                    //check what's under player
                    if (playGround[theHero.getxPos()][theHero.getyPos() + 1] != 1) {
                        checkFloor(theHero, playGround, oldX, oldY, direction, terminal, messages);
                        break;
                    }
                    break;
                }
            } //end switch
            //check if player reached the goal
            if (playGround[theHero.getxPos()][theHero.getyPos()] == 9) levelClear(theHero, terminal, messages);

        } //end while

    } //end main method

    public static void jump(Player p, Terminal terminal, int jumpHeight, int direction, int[][] playGround, Message[] messages) throws Exception {
        int oldX;
        int oldY;
        for (int i = 0; i < jumpHeight; i++) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            //check hit ceiling on jump
            if (playGround[p.getxPos()][p.getyPos() - 1] != 0) {
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
            terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter(p.getFace());
            terminal.flush();
            Thread.sleep(12 + i * 2);
        }

        int currentHeight = (terminal.getTerminalSize().getRows() - p.getyPos());
        for (int i = currentHeight; i > 0; i--) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            //check hit floor or lava on falling from jump
            if (playGround[p.getxPos()][p.getyPos() + 1] != 1) {
                checkFloor(p, playGround, oldX, oldY, direction, terminal, messages);
                break;
            }
            //TODO remove commented section below if nothing weird happens
//            } else if (playGround[p.getxPos()][p.getyPos() + 1] >= 1) {
//                terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
//                terminal.setCursorPosition(oldX, oldY);
//                terminal.putCharacter(' ');
//                terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
//                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
//                terminal.setCursorPosition(p.getxPos(), p.getyPos());
//                terminal.putCharacter(p.getFace());
//                terminal.flush();
//                switch (playGround[p.getxPos()][p.getyPos() + 1]) {
//                    case 2:
//                        p.setyPos(p.getyPos() + 1);
//                        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
//                        terminal.setCursorPosition(oldX, oldY);
//                        terminal.putCharacter(' ');
//                        terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
//                        terminal.setBackgroundColor(TextColor.ANSI.RED);
//                        terminal.setCursorPosition(p.getxPos(), p.getyPos());
//                        terminal.putCharacter(p.getFace());
//                        terminal.flush();
//                        death("LAVA", p, terminal);
//                        break;
//                    case 3:
//                        death("SPIKES", p, terminal);
//                        break;
//                }
//                break;
//            }

            p.setyPos(p.getyPos() + 1);
            //check hit wall on jump
            if (i % 3 == 0)
                if (playGround[p.getxPos() + direction][p.getyPos()] == 0)
                    p.setxPos(p.getxPos() + direction);

            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            terminal.setCursorPosition(oldX, oldY);
            terminal.putCharacter(' ');
            terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter(p.getFace());
            terminal.flush();
            Thread.sleep(12 + (int) (i / 4));
        }
    }

    public static void move(Player p, int oldX, int oldY, Terminal terminal) throws Exception {
        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
        terminal.setCursorPosition(oldX, oldY);
        terminal.putCharacter(' ');
        terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
        terminal.setBackgroundColor(TextColor.ANSI.BLACK);
        terminal.setCursorPosition(p.getxPos(), p.getyPos());
        terminal.putCharacter(p.getFace());
        terminal.flush();
    }

    public static void fall(Player p, int[][] playGround, int direction, Terminal terminal, Message[] messages) throws Exception {
        int currentHeight = (terminal.getTerminalSize().getRows() - p.getyPos());
        int oldX;
        int oldY;

        for (int i = currentHeight; i > 0; i--) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos() + 1] == 1) {
                break;
            }
            if (playGround[p.getxPos()][p.getyPos() + 1] > 1 && playGround[p.getxPos()][p.getyPos() + 1] !=9) {
                checkFloor(p, playGround, oldX, oldY, direction, terminal, messages);
                break;
            }

            p.setyPos(p.getyPos() + 1);
            if (i % 3 == 0)
                if (playGround[p.getxPos() + direction][p.getyPos()] == 0)
                    p.setxPos(p.getxPos() + direction);

            terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
            terminal.setCursorPosition(oldX, oldY);
            terminal.putCharacter(' ');
            terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
            terminal.setCursorPosition(p.getxPos(), p.getyPos());
            terminal.putCharacter(p.getFace());
            terminal.flush();
            Thread.sleep(12 + (int) (i / 4));
        }
    }

    public static void checkFloor(Player p1, int[][] playGround, int oldX, int oldY, int direction, Terminal terminal, Message[] messages) throws Exception {
        int floorType = playGround[p1.getxPos()][p1.getyPos() + 1];
        switch (floorType) {
            case 0:
            case 9:
                fall(p1, playGround, direction, terminal, messages);
                break;
            case 2: //lava
                oldY = p1.getyPos();
                oldX = p1.getxPos();
                p1.setyPos(p1.getyPos() + 1);
                terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
                terminal.setCursorPosition(oldX, oldY);
                terminal.putCharacter(' ');
                terminal.setBackgroundColor(TextColor.ANSI.RED);
                terminal.setCursorPosition(p1.getxPos(), p1.getyPos());
                terminal.putCharacter(p1.face);
                terminal.flush();
                displayMessage(messages[1].getSignDesign(), terminal,0);
                death(p1, terminal, messages);
                break;
            case 3: //spikes
                terminal.setBackgroundColor(TextColor.ANSI.RED);
                displayMessage(messages[2].getSignDesign(), terminal,0);
                death(p1, terminal, messages);
            default:
                break;
        }
    }

    public static void levelClear(Player p, Terminal terminal, Message[] messages) throws Exception {
        displayMessage(messages[3].getSignDesign(), terminal,0);
        p.setxPos((terminal.getTerminalSize().getColumns() / 2) - 1);
        p.setyPos((terminal.getTerminalSize().getRows()) - 2);
        p.setGameCycle(p.getGameCycle() + 1);
        p.setDoInitialize(true);
    }

    public static void death(Player p, Terminal terminal, Message[] messages) throws Exception {
        p.setLives(p.getLives() - 1);
        if (p.getLives() < 0) {
            //GAMEOVERSIGN
            displayMessage(messages[0].getSignDesign(), terminal, 1);
            //restart yes or no
            boolean hasResponded = false;
            KeyStroke keyStroke;
            char c;
            do {
                do {
                    Thread.sleep(5);
                    keyStroke = terminal.pollInput();
                } while (keyStroke == null);
                switch (keyStroke.getKeyType()) {
                    case Character:
                        if (keyStroke.getCharacter() == 'y' || keyStroke.getCharacter() == 'n') hasResponded = true;
                        break;
                    default:
                        break;
                }
            } while (!hasResponded);
            c = keyStroke.getCharacter();

            if (c == 'y') {
                p.setxPos((terminal.getTerminalSize().getColumns() / 2) - 1);
                p.setyPos((terminal.getTerminalSize().getRows()) - 2);
                p.setGameCycle(1);
                p.setLives(2);
                p.setDoInitialize(true);
                System.out.println("Restarting... Switch to terminal!");
                int oldX = p.getxPos(), oldY = p.getyPos();
            } else {
                terminal.close();
                System.exit(0);
            }

        } else {
            p.setxPos((terminal.getTerminalSize().getColumns() / 2) - 1);
            p.setyPos((terminal.getTerminalSize().getRows()) - 2);
            p.setDoInitialize(true);
        }
    }

    public static void displayMessage(char[][] signC, Terminal terminal, int endGame) throws Exception {
        for (int i = 0; i < signC.length; i++) {
            for (int j = 0; j < signC[i].length; j++) {
                char blockType;
                switch (signC[i][j]) {
                    case '0':
                        terminal.setBackgroundColor(TextColor.ANSI.WHITE);
                        blockType = ' ';
                        break;
                    case '1':
                        terminal.setForegroundColor(TextColor.ANSI.BLUE);
                        blockType = '\u2588';
                        break;
                    case '2':
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        blockType = '\u2588';
                        break;
                    default:
                        terminal.setForegroundColor(TextColor.ANSI.BLACK);
                        blockType = signC[i][j];
                        break;
                }
                terminal.setCursorPosition(28 + i, 5 + j);
                terminal.putCharacter(blockType);
            }
        }
        terminal.flush();
        if (endGame != 1) {
            boolean hasPressedC = false;
            KeyStroke keyStroke;
            char c;
            do {
                do {
                    Thread.sleep(5);
                    keyStroke = terminal.pollInput();
                } while (keyStroke == null);
                switch (keyStroke.getKeyType()) {
                    case Character:
                        if (keyStroke.getCharacter() == 'c') hasPressedC = true;
                        break;
                    default:
                        break;
                }
            } while (!hasPressedC);
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