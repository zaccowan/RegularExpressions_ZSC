import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Program to take in a novel in plain text form and a regular expressions pattern text file
 * and output at file of all the pattern match counts for given novel.
 * @author Zachary Cowan
 * @version 1.0
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 */
public class NovelProcessor {

    public static void main(String [] args) {

        String novelName = "";
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
            if(!novelPath.endsWith(".txt")) {
                novelName = novelPath;
                novelPath += ".txt";
            }
            else {
                novelName = novelPath.substring(0, novelPath.length()-4);
            }
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

        try {
            File outputFile = new File( "./" +novelName + "_wc.txt");

            if (outputFile.createNewFile()) {
                System.out.println("File created: " + outputFile.getName());
                FileWriter myWriter = new FileWriter(novelName + "_wc.txt");
                myWriter.write(novelName + " is lame.");
                myWriter.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    } // end main
} // end class
