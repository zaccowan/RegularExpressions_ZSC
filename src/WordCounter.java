import java.io.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
    public static void main(String [] args) throws IOException {
        File rootDir = new File("./");
        FileFilter fileFilter = file -> file.getPath().endsWith("_wc.txt");

        File[] wcFileArr = rootDir.listFiles(fileFilter);
        for(int i = 0; i < Objects.requireNonNull(wcFileArr).length ; i++ ) {
            File currFile = wcFileArr[i];
            BufferedReader br = new BufferedReader(new FileReader(currFile.getPath()));
            String line = br.readLine();
            while( line != null) {

                System.out.println(line);

                line = br.readLine(); // Get next line
            }
        }
    }
}
