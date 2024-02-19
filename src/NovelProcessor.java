import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NovelProcessor {

    public static void main(String [] args) {

        String novelPath = "";
        String regexPath = "";

        // Handle Case where invalid number of arguments are entered
        if( args.length != 2) {
            System.err.println("You have not entered a valid number of arguments.\n" +
                    "Command should take the form:" +
                    "\n\n" +
                    "\tjava NovelProcessor <novel_name> <regex_pattern_file>" +
                    "\n");

        } else {
            novelPath = args[0];
            if(!novelPath.endsWith(".txt")) novelPath += ".txt";
            regexPath = args[1];
            if(!regexPath.endsWith(".txt")) regexPath += ".txt";
        }


        try {
            BufferedReader patternReader = new BufferedReader(new FileReader(new File(regexPath)));

            String patternLine = patternReader.readLine();
            while (patternLine != null) {
                BufferedReader novelReader = new BufferedReader(new FileReader(new File(novelPath)));

                Pattern pattern = Pattern.compile(patternLine);
                String novelLine = novelReader.readLine();
                int numMatches = 0;
                while( novelLine != null) {
                    Matcher matcher = pattern.matcher(novelLine);
                    while(matcher.find()) {
                        numMatches++;
                    }
                    novelLine = novelReader.readLine(); // Get next line
                }
                System.out.println(patternLine + "|" + numMatches);

                patternLine = patternReader.readLine();
            }

        } // end try
        catch (IOException e) {
            throw new RuntimeException(e);
        } // end catch
    } // end main
} // end class
