import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
    public static void main(String [] args) throws IOException {
        HashMap<String, Integer> wcHashMap = new HashMap<String, Integer>();

        File rootDir = new File("./");
        FileFilter fileFilter = file -> file.getPath().endsWith("_wc.txt");

        Pattern pattern = Pattern.compile("[a-zA-z]*|[0-9]+");

        File[] wcFileArr = rootDir.listFiles(fileFilter);
        for(int i = 0; i < Objects.requireNonNull(wcFileArr).length ; i++ ) {
            File currFile = wcFileArr[i];
            System.out.println("File Processed: " + currFile.getName());
            BufferedReader br = new BufferedReader(new FileReader(currFile.getPath()));
            String line = br.readLine();
            while( line != null) {
//                System.out.println(line);

                // Since Count follows the last '|' in a line, the count is only in indices after the last '|'
                // The following finds this index.
                int countStartIndex = 0 ;
                for( int j = 0 ; j < line.length() ; j++) if ( line.charAt(j) == '|' ) countStartIndex = j;

                int currWordCount = Integer.parseInt(line.substring(countStartIndex+1));
                String currPattern = line.substring(0 , countStartIndex);

                if( !wcHashMap.containsKey(currPattern) ) {
                    wcHashMap.put(currPattern, currWordCount); // Adds pattern and count to string for first time
                } else {
                    int oldCount = wcHashMap.get(currPattern);
                    wcHashMap.replace(currPattern, oldCount+currWordCount);
                }

                line = br.readLine(); // Get next line
            }
        } //closes for that iterates of lines of file

        System.out.println("-------------------------------------------------------------------------------------------------------" +
                "\nThe following is final count of each pattern for all processed novels (as indicated above).\n" +
                "-------------------------------------------------------------------------------------------------------");
        int wcTotal = 0;
        for( String key : wcHashMap.keySet()) {
            System.out.println("\n"+key);
            System.out.println("Final Count: " + wcHashMap.get(key));
            wcTotal += wcHashMap.get(key);
        }


        System.out.println("------------------------------------"+
                "\nTotal Word Count:  " + wcTotal +
                "\n------------------------------------");

    } // closes main
} //closes class
