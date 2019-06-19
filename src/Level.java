import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Level {

    private int[][] levelDesign = new int[24][80];
    private String[][] levelAsString = new String[24][80];
    private String levelString;

    Level(String filename) throws Exception{
        this.levelString=readLevelFile(filename);
        stringToArray(this.levelString);
    }

    private String readLevelFile(String filename) throws Exception{
        BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public String getLevelString() {
        return levelString;
    }

    private void stringToArray(String levelString) {

        String[] splitString = levelString.split("\r\n",0);

        for (int i = 0 ; i < splitString.length; i++)
            levelAsString[i] = splitString[i].split(";", 0);

        for (int i = 0; i < levelAsString.length ; i++)
            for (int j = 0; j < levelAsString[i].length; j++)
                levelDesign[i][j] = Integer.parseInt(levelAsString[i][j]);

    }

}