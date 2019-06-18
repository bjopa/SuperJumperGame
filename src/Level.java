import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Level {

    int[][] levelDesign = new int[80][24];
    String levelString;

    public Level(String filename) throws Exception{
        this.levelString=readLevelFile(filename);
        stringToArray(this.levelString);
    }

    public String readLevelFile(String filename) throws Exception{
        BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public String getLevelString() {
        return levelString;
    }

    public void stringToArray(String levelString) {
        String[] tempString = levelString.split("\n",0);
        System.out.println(tempString[16]);
        //TODO Add string splitter on &
    }

}