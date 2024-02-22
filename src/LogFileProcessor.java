import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogFileProcessor {
    public static void main(String [] args) throws IOException {
        String fileName = args[0];
        if(!fileName.endsWith(".log")) {
            fileName = fileName.concat(".log");
        }

        HashMap<String, Integer> ipHashMap = new HashMap<>();
        HashMap<String, Integer> userHashMap = new HashMap<>();


        File logFile = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        Pattern ipv4Pattern = Pattern.compile("([0-9]{1,3}\\.){3}([0-9]{1,3})");
        Pattern userPattern = Pattern.compile("user [a-zA-Z]+");
        int numLinesProcessed = 0 ;
        int numIPv4References = 0;
        int numUserReferences = 0;

        String line = br.readLine();
        while( line != null) {
            numLinesProcessed++;
            Matcher matcher = ipv4Pattern.matcher(line);

            // Find all IPv4 matches in line
            while ( matcher.find()) {
                String ipInstance = matcher.group();
                if( ipHashMap.containsKey(ipInstance)) ipHashMap.computeIfPresent(ipInstance, (key, value) -> value + 1);
                else ipHashMap.put(ipInstance, 1);
                numIPv4References++;
            }

            matcher = userPattern.matcher(line);

            // Find all user matches in line
            while ( matcher.find()) {
                String userInstance = matcher.group();
                String userName = userInstance.substring(5); // to strip "user " from pattern match
                if( userName.equals("unknown")) continue; // do not add "unknown" because it is recognized by the pattern but is not referencing an actual user

                if( userHashMap.containsKey(userName)) userHashMap.computeIfPresent(userName, (key, value) -> value + 1);
                else userHashMap.put(userName, 1);

                numUserReferences++;
            }

            line = br.readLine();
        }

        if( args[1].equals("1")) {
            for( String key : ipHashMap.keySet()) {
                System.out.println(key +": " + ipHashMap.get(key));
            }
        } else if( args[1].equals("2")) {
            for( String key : userHashMap.keySet()) {
                System.out.println(key +": " + userHashMap.get(key));
            }
        } else {
            System.out.println(numLinesProcessed + " lines in the log file were parsed.\n"+
                    "Found " +numIPv4References + " total ipv4 address references.\n" +
                    "Found " +numUserReferences + " total user references.\n" +

                    "There are " + ipHashMap.keySet().size() + " unique IPV4 addresses in the log.\n" +
                    "There are " + userHashMap.keySet().size() + " unique users in the log.");
        }
    }



}
