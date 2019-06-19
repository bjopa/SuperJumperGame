import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Scanner;

public class Main {
    //TODO Fixa färger!! Klarare gul tex
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

        //Set up levels, allocate initial lvl
        Level level1 = new Level("Level1.txt");
        Level level2 = new Level("Level2.txt");
        int[][] playGround = level1.getLevelDesign();

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
                String statusBar = "SUPER JUMPER  - LEVEL:" + theHero.getGameCycle();
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
                    jump(theHero, oldX, oldY, terminal, jumpHeight, direction, playGround);
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
                        checkFloor(theHero, playGround, oldX, oldY, direction, terminal);
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
                        checkFloor(theHero, playGround, oldX, oldY, direction, terminal);
                        break;
                    }
                    break;
                }
            } //end switch
            if (playGround[theHero.getxPos()][theHero.getyPos()] == 9) levelClear(theHero, terminal);

        } //end while

    } //end main method

    //TODO remove oldX/Y from method parameters below if possible
    public static void jump(Player p, int oldX, int oldY, Terminal terminal, int jumpHeight, int direction, int[][] playGround) throws Exception {
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
            //TODO ersätt med metod?
            if (playGround[p.getxPos()][p.getyPos() + 1] == 1) {
                break;
            } else if (playGround[p.getxPos()][p.getyPos() + 1] >= 1) {
                terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
                terminal.setCursorPosition(oldX, oldY);
                terminal.putCharacter(' ');
                terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setCursorPosition(p.getxPos(), p.getyPos());
                terminal.putCharacter(p.getFace());
                terminal.flush();
                switch (playGround[p.getxPos()][p.getyPos() + 1]) {
                    case 2:
                        p.setyPos(p.getyPos() + 1);
                        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
                        terminal.setCursorPosition(oldX, oldY);
                        terminal.putCharacter(' ');
                        terminal.setForegroundColor(TextColor.Indexed.fromRGB(255, 255, 0));
                        terminal.setBackgroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(p.getxPos(), p.getyPos());
                        terminal.putCharacter(p.getFace());
                        terminal.flush();
                        death("LAVA", p, terminal);
                        break;
                    case 3:
                        death("SPIKES", p, terminal);
                        break;
                }
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

    public static void fall(Player p, int oldX, int oldY, int[][] playGround, int direction, Terminal terminal) throws Exception {
        int currentHeight = (terminal.getTerminalSize().getRows() - p.getyPos());

        for (int i = currentHeight; i > 0; i--) {
            oldX = p.getxPos();
            oldY = p.getyPos();

            if (playGround[p.getxPos()][p.getyPos() + 1] == 1) {
                break;
            }
            if (playGround[p.getxPos()][p.getyPos() + 1] > 1) {
                checkFloor(p, playGround, oldX, oldY, direction, terminal);
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

    public static void checkFloor(Player p1, int[][] playGround, int oldX, int oldY, int direction, Terminal terminal) throws Exception {
        int floorType = playGround[p1.getxPos()][p1.getyPos() + 1];
        switch (floorType) {
            case 0:
                fall(p1, oldX, oldY, playGround, direction, terminal);
                break;
            case 2:
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
                death("LAVA!", p1, terminal);
                break;
            case 3:
                death("SPIKES!", p1, terminal);
            default:
                break;
        }
    }

    public static void levelClear(Player p, Terminal terminal) throws Exception {
        System.out.println("WELL DONE - GO AHEAD!");
        p.setxPos((terminal.getTerminalSize().getColumns() / 2) - 1);
        p.setyPos((terminal.getTerminalSize().getRows()) - 2);
        p.setGameCycle(p.getGameCycle() + 1);
        p.setDoInitialize(true);
    }

    public static void death(String reason, Player p, Terminal terminal) throws Exception {
        Scanner sc = new Scanner(System.in);
        //TODO listen to keys
        //TODO introduce lives
        System.out.println("You died from hitting " + reason);
        System.out.print("Would you like to play again (y/n)?: ");
        String choice = sc.nextLine();
        switch (choice) {
            case "y":
            case "Y":
                p.setxPos((terminal.getTerminalSize().getColumns() / 2) - 1);
                p.setyPos((terminal.getTerminalSize().getRows()) - 2);
                p.setGameCycle(1);
                p.setDoInitialize(true);
                System.out.println("Restarting... Switch to terminal!");
                int oldX = p.getxPos(), oldY = p.getyPos();
                break;
            default:
                terminal.close();
                System.exit(0);
                break;

        }
    }

} //end main class