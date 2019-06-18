public class Level {

    int[][] playGround = new int[80][24];

}

//    int[][] playGround = new int[80][24];
//        for (int col = 0; col < playGround.length; col++) {
//        for (int row = 0; row < playGround[col].length; row++) {
//        if (col == 0 || col == playGround.length - 1) playGround[col][row] = 1;
//        else {
//        if (row == 0 || row == playGround[col].length - 1) playGround[col][row] = 1;
//        else if (col > 40 && row == 19) playGround[col][row] = 1;
//        else if (col > 36 && col < 40 && row == 15) playGround[col][row] = 1;
//        else if (col == 34 && row > 12 && row < 17) playGround[col][row] = 1;
//        else if (col < 35 && row == 17) playGround[col][row] = 1;
//        else playGround[col][row] = 0;
//        }
//        }
//        }
