import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Level {

    int[][] levelDesign = new int[80][24];
    String levelString;

    public Level(String filename) throws Exception{
        this.levelString=readLevelFile(filename);
    }

    public String readLevelFile(String filename) throws Exception{
        BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
        return (readAllLines(reader));
    }

    public String readAllLines(BufferedReader reader) throws Exception {
        StringBuilder content = new StringBuilder();
        String line;
        while ((line=reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }
        return content.toString();
    }

    public String getLevelString() {
        return levelString;
    }

}