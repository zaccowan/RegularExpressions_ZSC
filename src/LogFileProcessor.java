import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * LogFileProcessor takes into two arguments via command line and uses Regular Expression to store the number of instances of
 * IP address and users. IP address and users are stored in two respective HashMaps where:
 *      Key - The IP address or user name of type String
 *      Value - The count of the given key.
 *          For example, if a user is referenced 3 times in the .log, the value for the user key (count) would be 3.
 *          The same structure is followed for IP addresses.
 *
 * The nature of the HashMap does not allow for duplicate key's so, when a already present key is encountered again,
 * a new value is calculated using the previous state of said value.
 *      For example, if the count of user "bob" was 3, and the pattern detects "bob" again, the new count is 3+1 = 4.
 *
 * The HashMap's lack of duplicate keys also allows easy counting of unique users and IP addresses.
 * The number of unique IPs and users is the number of rows in the HashMap. This can be accessed via the keySet().size() call.
 * </pre>
 * @author Zachary Cowan
 * @version 1.0
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 */
public class LogFileProcessor {

    public static void main(String[] args) throws IOException {
        new LogFileProcessor(args);
    }

    private final HashMap<String, Integer> ipHashMap;
    private final HashMap<String, Integer> userHashMap;

    private String fileName;

    private int numLinesProcessed;
    private int numIPv4References;
    private int numUserReferences;

    LogFileProcessor(String [] args) throws IOException {
        // Gets first argument from command line call.
        // By Design the user needs to present the name of the file they wish to process.
        //
        fileName = args[0];

        // Conditional allows user to neglect the .log ending when entering the file name.
        // if it is absent from user input, append it to the end of the string.
        if(!fileName.endsWith(".log")) {
            fileName = fileName.concat(".log");
        }

        // Hash maps used to store the pattern matches
        // IP address and usernames are the keys of their respective maps
        // Count is the value of both maps
        ipHashMap = new HashMap<>();
        userHashMap = new HashMap<>();

        // Create a file reference to the .log file specified by user
        File logFile = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(fileName)); // user to read the file

        // Pattern uses to detect IPv4 Address
        // Such address come in the very general form:   w.x.y.z
        // Where w, x, y, and z take the place of a number with anywhere from 1 to 3 digits
        Pattern ipv4Pattern = Pattern.compile("([0-9]{1,3}\\.){3}([0-9]{1,3})");

        // Pattern used to detect users referenced in the log file
        // All user references follow the term "user "
        // The following pattern matches the entirety of "user <user_name>"
        // The prefix "user " will have to be stripped before storing.
        Pattern userPattern = Pattern.compile("user [a-zA-Z]+");

        // Counter for number of lines processed
        numLinesProcessed = 0 ;

        // Extra counts to find the number of references of both IP address and users
        // These are not the same as the unique counts because duplicates are present
        numIPv4References = 0;
        numUserReferences = 0;

        String line = br.readLine();
        while( line != null) {
            numLinesProcessed++;
            Matcher matcher = ipv4Pattern.matcher(line);

            // Find all IPv4 matches in line
            while ( matcher.find()) {
                String ipInstance = matcher.group();

                // Add instance to hash map with respect to already existing entries
                if( ipHashMap.containsKey(ipInstance)) ipHashMap.computeIfPresent(ipInstance, (key, value) -> value + 1); // recalculate value for key
                else ipHashMap.put(ipInstance, 1);
                numIPv4References++;
            }

            matcher = userPattern.matcher(line);

            // Find all user matches in line
            while ( matcher.find()) {
                String userInstance = matcher.group();
                String userName = userInstance.substring(5); // to strip prefix "user " from pattern match
                if( userName.equals("unknown")) continue; // do not add user "unknown"
                // "user unknown" does not reference a user in the system but the lack of one

                // Add instance to hash map with respect to already existing entries
                if( userHashMap.containsKey(userName)) userHashMap.computeIfPresent(userName, (key, value) -> value + 1); // recalculate value for key
                else userHashMap.put(userName, 1);

                numUserReferences++;
            }

            line = br.readLine();
        }

        /*
         * Design of command line arguments allows the user to use one of three output modes. If argument two is:
         *      0 - default output with general information about the file and its counts are printed.
         *      1 - unique IP addresses and their respective counts are printed
         *      2 - unique users and their respective counts are printed
         */

        if( args[1].equals("1")) printIPHashMap();
        else if( args[1].equals("2")) printUserHashMap();
        else printHashMapsOverview();

    }

    public String getFileName() {
        return fileName;
    }

    public int getIPHashSize() {
        return ipHashMap.keySet().size();
    }
    public int getUserHashSize() {
        return userHashMap.keySet().size();
    }

    public int getNumLinesProcessed() {
        return numLinesProcessed;
    }
    public int getNumIPv4References() {
        return numIPv4References;
    }
    public int getNumUserReferences() {
        return numUserReferences;
    }


    public void printIPHashMap() {
        for( String key : ipHashMap.keySet()) {
            System.out.println(key +": " + ipHashMap.get(key));
        }
    }

    public void printUserHashMap() {
        for( String key : userHashMap.keySet()) {
            System.out.println(key +": " + userHashMap.get(key));
        }
    }

    public void printHashMapsOverview() {
        System.out.println(getNumLinesProcessed() + " lines in the log file (\"" + getFileName() + "\") were parsed.\n"+
                "Found " + getNumIPv4References() + " total ipv4 address references.\n" +
                "Found " + getNumUserReferences() + " total user references.\n" +

                "There are " + getIPHashSize() + " unique IPV4 addresses in the log.\n" +
                "There are " + getUserHashSize() + " unique users in the log.");
    }


}
